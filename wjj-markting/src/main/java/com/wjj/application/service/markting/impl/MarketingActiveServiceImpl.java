package com.wjj.application.service.markting.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wjj.application.dto.marketing.MarketingActiveDto;
import com.wjj.application.entity.GoodsMainInfo;
import com.wjj.application.entity.StandardDetailSupplement;
import com.wjj.application.entity.marketing.MarketingActive;
import com.wjj.application.entity.marketing.MarketingBanalPic;
import com.wjj.application.entity.marketing.MarketingGoods;
import com.wjj.application.feign.GoodsMainInfoFeignClient;
import com.wjj.application.json.JsonUtil;
import com.wjj.application.mapper.marketing.MarketingActiveMapper;
import com.wjj.application.mapper.marketing.MarketingBanalPicMapper;
import com.wjj.application.mapper.marketing.MarketingGoodsMapper;
import com.wjj.application.response.Response;
import com.wjj.application.response.ReturnCode;
import com.wjj.application.service.markting.MarketingActiveService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class MarketingActiveServiceImpl implements  MarketingActiveService{
	
	@Autowired
	private MarketingActiveMapper marketingActiveMapper;
	
	@Autowired
	private MarketingBanalPicMapper marketingBanalPicMapper;
	
	@Autowired
	private MarketingGoodsMapper marketingGoodsMapper;
	
	 @Autowired
	 private GoodsMainInfoFeignClient goodsMainInfoFeignClient;
	 
	 @Autowired
	 private TransportClient transportClient;
	 
		@Autowired
		private Environment env;
	
	
	//活动列表
	@Override
	public List<MarketingActive> selectMarketingActiveList() {
		return marketingActiveMapper.selectMarketingActiveList();
	}
	
	//活动列表编辑
	@Override
	public Integer updateMarketingActive(MarketingActive entity) {
		return marketingActiveMapper.updateMarketingActive(entity);
	}

	//活动列表查看相情
	@Override
	public MarketingActiveDto selectMarketingActiveDetail(MarketingActive entity) {
		return marketingActiveMapper.selectMarketingActiveDetail(entity);
	}

	//具体活动列表
	@Override
	public MarketingActiveDto selectMarketingActiveDetailByActive(MarketingActive entity) {
		MarketingActiveDto dto = marketingActiveMapper.selectMarketingActiveDetail(entity);
		Long activeId = entity.getId();
		List<MarketingBanalPic> picList = marketingBanalPicMapper.selectMarketingBanalPicByActive(activeId);
		List<MarketingGoods> goodslist = marketingGoodsMapper.selectMarketingGoodsList(activeId);
		if(picList!=null &&picList.size()>0) {
			dto.setMarketingBanalPicList(picList);
		}
		if(goodslist!=null&&goodslist.size()>0) {
			dto.setMarketingGoodsList(goodslist);
		}
		return dto;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor = {RuntimeException.class })
	public Response deleteMarketingActiveByActive(MarketingActiveDto dto) {
		List<MarketingGoods> goodsList = dto.getMarketingGoodsList();
		if(goodsList!=null&&goodsList.size()>0) {
			for(MarketingGoods item:goodsList) {
				Long id = item.getId();
				MarketingGoods items2 = marketingGoodsMapper.selectById(id);
				Long activeId = items2.getActiveId();
				if(activeId!=null&&activeId==1) {//新人专享
					StandardDetailSupplement sds=new StandardDetailSupplement();
					sds.setDetailId(items2.getDetailId());
					sds.setNewHumanPrice(new BigDecimal(0));
					goodsMainInfoFeignClient.updateStandardDetailSupplement(sds);	
				}
				marketingGoodsMapper.deleteById(id);
			}
		}
		return Response.returnCode(ReturnCode.SUCCESS.getCode(),ReturnCode.SUCCESS.getMsg());	
	}
	
	/**
	 * 更新
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor = {RuntimeException.class })
	public Response updateMarketingActiveByActive(MarketingActiveDto dto) {
		List<StandardDetailSupplement> list=new ArrayList<>();
		Long id2 = dto.getId();
		/*List<Long> detaiList=null;
		if(id2!=null&&id2==1) {
			detaiList = marketingGoodsMapper.selectDetaiListByActiveId(id2);
		}*/
		List<String> goodsIdList = marketingGoodsMapper.selectGoodsIdListByActiveId(id2);
		List<GoodsMainInfo> gmiList = goodsMainInfoFeignClient.selectGoodsMainListByList(goodsIdList);
		if(gmiList!=null&&gmiList.size()>0) {
			for(GoodsMainInfo item:gmiList) {
				this.updateEsActive(item.getGoodsId(), item.getSalePrice(), "0");
			}
		}
		marketingGoodsMapper.deleteMarketingGoodsByActiveId(id2);
		List<MarketingGoods> goodsList = dto.getMarketingGoodsList();
		List<String> goodsidList=new ArrayList<>();
		if(goodsList!=null&&goodsList.size()>0) {
			Set<String> hashSet=new HashSet<String>();
			for(MarketingGoods item:goodsList) {
				if(id2!=null&&(id2==1||id2==2||id2==3)) {
					StandardDetailSupplement sds=new StandardDetailSupplement();
					sds.setActiveType(String.valueOf(id2));
					sds.setDetailId(item.getDetailId());
					if(id2==1) {
						sds.setNewHumanPrice(item.getNewHumanPrice());
						hashSet.add(item.getGoodsId());	
					}else {
						goodsidList.add(item.getGoodsId());
						sds.setNewHumanPrice(item.getMinPrice());
						this.updateEsActive(item.getGoodsId(), item.getMinPrice(), String.valueOf(id2));
					}
					list.add(sds);
				}
				//Long id = item.getId();
				item.setActiveId(id2);
				String goodsTypeName = item.getGoodsTypeName();
				String classifyName = item.getClassifyName();
				if(goodsTypeName!=null&&!goodsTypeName.equals("")) {
					item.setClassifyName(goodsTypeName);
				}
				if(classifyName!=null&&!classifyName.equals("")) {
					item.setGoodsTypeName(classifyName);
				}
				marketingGoodsMapper.insert(item);
				/*if(id!=null) {
					marketingGoodsMapper.updateById(item);
				}else {
					marketingGoodsMapper.insert(item);
				}*/
			}
			
			if(id2==1) {
				List<MarketingGoods> mgList = marketingGoodsMapper.selectMInNewHUmanPriceAndGoodsId(id2);
				if(mgList!=null&&mgList.size()>0) {
					for(MarketingGoods item:mgList) {
						this.updateEsActive(item.getGoodsId(), item.getNewHumanPrice(), "1");
					}
				}
				//更新最小和最大的价格
				List<String> strlist = marketingGoodsMapper.selctMinAndMaxPriceGoodsIdByDetailId(1L);
				List<MarketingGoods> goodsListArr = goodsMainInfoFeignClient.selectMarketingGoodsList(strlist);
				//跟新最小和最大价格
				marketingGoodsMapper.updateMinAndMaxPriceByGoodsId(goodsListArr);
			}
		
			
			
			
			
		}
		
		marketingBanalPicMapper.deleteMarketingBanalPicByActive(id2);
		List<MarketingBanalPic> picList = dto.getMarketingBanalPicList();
		if(picList!=null&&picList.size()>0) {
			for(MarketingBanalPic item:picList) {
				item.setActiveId(id2);
				marketingBanalPicMapper.insert(item);
			}
		}
		dto.setUpdateTime(new Date());
		marketingActiveMapper.updateById(dto);
		
		//商品规格补充信息表  新增数据
		//if(list!=null&&list.size()>0) {
		if(id2!=null) {
			Map<String,Object> map=new HashMap<>();
			map.put("activeType", id2);
			String sowingListNewStr = JsonUtil.list2Json(list);
			map.put("list", sowingListNewStr);
			String goodsListStr = JsonUtil.list2Json(goodsidList);
			map.put("goodsList", goodsListStr);
			if(id2==1) {
				goodsMainInfoFeignClient.insertStandardDetailSupplementMapper(map);
			}
		}
		
		//}
		return Response.returnCode(ReturnCode.SUCCESS.getCode(),ReturnCode.SUCCESS.getMsg());	
	}
	
	public void updateEsActive(String goodsId,BigDecimal newHumanPrice,String  activeType) {
		String indexStr=env.getProperty("spring.elasticsearch.index");
		 String typeStr=env.getProperty("spring.elasticsearch.type");
		 float newHumanPriceFloat=0;
		 if(newHumanPrice!=null) {
			 newHumanPriceFloat = newHumanPrice.floatValue();
		 }
		UpdateRequest uRequest = new UpdateRequest();
        uRequest.index(indexStr);
        uRequest.type(typeStr);
        uRequest.id(goodsId);
        try {
			uRequest.doc(XContentFactory.jsonBuilder().startObject().
						field("activeType",activeType).
						field("newHumanPrice",newHumanPriceFloat).
					endObject());
			 transportClient.update(uRequest).get();
		} catch (Exception e) {
			e.printStackTrace();
		}   
		
	}

	//查询所有活动和一分抢的商品编号
	@Override
	public List<String> selectActiveGoodsIdListAndOnePenctByList(List<String> activeIdList) {
		if(activeIdList!=null&&activeIdList.size()>0) {
			int size = activeIdList.size();
			if(size==2) {
				return marketingGoodsMapper.selectActiveGoodsIdListAndOnePenctByList(activeIdList);
			}else {
				return marketingGoodsMapper.selectActiveGoodsIdListByList(activeIdList);
			}
		}
		
		return null;
	}

	//查询所有活动和一分抢的商品编号
	@Override
	public List<String> selectActiveGoodsIdListByList(List<String> activeIdList) {
		return marketingGoodsMapper.selectActiveGoodsIdListAndOnePenctByList(activeIdList);
	}


}
