package com.wjj.application.service.index.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wjj.application.common.FastJsonUtils;
import com.wjj.application.context.ContextHolder;
import com.wjj.application.dto.AccountCustomerInfoDTO;
import com.wjj.application.dto.CustomerLoginCommonDTO;
import com.wjj.application.dto.advertising.AdvertisingManagerEntityDto;
import com.wjj.application.dto.index.AdAndGoodsHomeMall;
import com.wjj.application.dto.index.HealthHomeMall;
import com.wjj.application.dto.index.InfomationIndexDto;
import com.wjj.application.dto.index.MarketingActiveIndexDto;
import com.wjj.application.dto.infomation.HealthInformationDto;
import com.wjj.application.dto.marketing.MarketingActiveDto;
import com.wjj.application.dto.marketing.MarketingGoodsDto;
import com.wjj.application.dto.video.VideoManagerEntityDto;
import com.wjj.application.entity.advertising.AdvertisingManagerEntity;
import com.wjj.application.entity.information.HealthArticle;
import com.wjj.application.entity.information.HealthInfoParentTag;
import com.wjj.application.entity.information.HealthInfoTag;
import com.wjj.application.entity.information.HealthInfomation;
import com.wjj.application.entity.marketing.MarketingBanalPic;
import com.wjj.application.entity.marketing.MarketingGoods;
import com.wjj.application.entity.video.VideoArticleEntity;
import com.wjj.application.feign.AccountFeignClient;
import com.wjj.application.mapper.advertising.AdvertisingManagerMapper;
import com.wjj.application.mapper.infomation.HealthArticleMapper;
import com.wjj.application.mapper.infomation.HealthInfoTagMapper;
import com.wjj.application.mapper.infomation.HealthInfomationMapper;
import com.wjj.application.mapper.marketing.MarketingActiveMapper;
import com.wjj.application.mapper.marketing.MarketingBanalPicMapper;
import com.wjj.application.mapper.marketing.MarketingGoodsMapper;
import com.wjj.application.mapper.video.VideoArticleEntityMapper;
import com.wjj.application.mapper.video.VideoManagerMapper;
import com.wjj.application.response.Response;
import com.wjj.application.response.ReturnCode;
import com.wjj.application.security.AuthTokenDTO;
import com.wjj.application.security.PLAT_TYPE;
import com.wjj.application.service.index.AdAndGoodsHomeMallService;
import com.wjj.application.util.RedisClient;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdAndGoodsHomeMallServiceImpl implements AdAndGoodsHomeMallService{

	
	@Autowired
	private AdvertisingManagerMapper advertisingManagerMapper;
	
	@Autowired
	private MarketingActiveMapper marketingActiveMapper;
	
	@Autowired
	private VideoManagerMapper  videoManagerMapper;

	
	@Autowired
	private MarketingGoodsMapper marketingGoodsMapper;
	
	@Autowired
	private RedisClient redisClient;
	
	@Autowired
	private HealthInfomationMapper healthInfomationMapper;
	
	@Autowired
	private MarketingBanalPicMapper marketingBanalPicMapper;
	
	@Autowired
	private HealthArticleMapper healthArticleMapper;
	
	
	@Autowired
	private HealthInfoTagMapper healthInfoTagMapper;
	
	
	@Autowired
	private   AccountFeignClient accountFeignClient;
	
	@Autowired
	private VideoArticleEntityMapper videoArticleEntityMapper;
	

	
	
	/**
	 * 首次加载
	 */
	@Override
	public Response selectAdAndGoodsHomeMall() {
		AdAndGoodsHomeMall entity=new AdAndGoodsHomeMall();
		AdvertisingManagerEntityDto adBanaldto=new AdvertisingManagerEntityDto();
		adBanaldto.setType("2");
		adBanaldto.setUpdateStatus("2");
		List<AdvertisingManagerEntity> adBanal=advertisingManagerMapper.selectAdvertisingManagerList(adBanaldto);//类型 1:健康ban  2:商城bana   3:活动banna
		if(adBanal!=null&&adBanal.size()>0) {
			entity.setHeadAd(adBanal);
		}
		AdvertisingManagerEntityDto adMeddto=new AdvertisingManagerEntityDto();
		adMeddto.setType("3");
		adMeddto.setUpdateStatus("2");
		List<AdvertisingManagerEntity> adMed=advertisingManagerMapper.selectAdvertisingManagerList(adMeddto);
		if(adMed!=null&&adMed.size()>0) {
			entity.setMidAd(adMed);
		}
		
		//编号 1.新人专享  2.折扣专区  3.限量秒杀  4.今日爆款  5.国民优选 6.精选好货 7.为您推荐  8：热销
		MarketingActiveDto todatDto=new MarketingActiveDto();
		todatDto.setId(4L);
		MarketingActiveDto todayEntity= marketingActiveMapper.selectMarketingActiveDetail(todatDto);//今日爆款
		if(todatDto!=null) {
			entity.setTodayBursting(todayEntity);
		}
		
		MarketingActiveDto limitDto=new MarketingActiveDto();  
		limitDto.setId(3L);
		MarketingActiveDto limitEntity= marketingActiveMapper.selectMarketingActiveDetail(limitDto);//限量秒杀
		if(limitDto!=null) {
			entity.setLimitSecondKill(limitEntity);
		}
		
		MarketingActiveDto recommendedDto=new MarketingActiveDto();
		recommendedDto.setId(7L);
		MarketingActiveDto recommendedEntity= marketingActiveMapper.selectMarketingActiveDetail(recommendedDto);//限量秒杀
		if(recommendedDto!=null) {
			entity.setRecommended(recommendedEntity);
		}
		
		//视屏管理  类型1:健康首页  2：商城首页
		VideoManagerEntityDto video=new VideoManagerEntityDto();
		video.setType("2");
		video.setPageNo(0);
		video.setPageSize(2);
		List<VideoManagerEntityDto> vedioList = videoManagerMapper.selectVideoManagerListForIndex(video);
		if(vedioList!=null) {
			for(VideoManagerEntityDto item:vedioList) {
				long ids=item.getId();
				int actuanum=item.getActualNum();
				String videoClickNum="videoclicknum"+ids;
				try {
					String incNum = redisClient.get(videoClickNum);
					if(incNum!=null&&!incNum.equals("")) {
						item.setActualNum(actuanum+Integer.parseInt(incNum));
					}else {
						item.setActualNum(actuanum);
					}				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
			
			entity.setVideoHome(vedioList);
		}
		
		//热销专区
		MarketingGoodsDto hotDto =new MarketingGoodsDto();
		hotDto.setActiveId(8L);
		hotDto.setPageNo(0);
		hotDto.setPageSize(4);
		hotDto.setActiveType("8");
		List<MarketingGoodsDto> hotEntity = marketingGoodsMapper.selectMarketingGoodsForApp(hotDto);
		if(hotEntity!=null) {
			entity.setHotSale(hotEntity);
		}
		
		
		//国民优品
		MarketingGoodsDto gmDto =new MarketingGoodsDto();
		gmDto.setActiveId(5L);
		gmDto.setPageNo(0);
		gmDto.setPageSize(6);
		hotDto.setActiveType("5");
		List<MarketingGoodsDto> gmEntity = marketingGoodsMapper.selectMarketingGoodsForApp(gmDto);
		if(gmDto!=null) {
			entity.setSuperiorProducts(gmEntity);
		}
		return Response.ok(entity);
	}

	
	/**
	 * 根据活动类型编号查询对应的商品
	 */
	@Override
	public Map<String,Object> selectMarketGoodsList(MarketingGoodsDto dto) {
		Map<String,Object> map=new HashMap<String,Object>();
		Long activeId = dto.getActiveId();
		List<MarketingGoodsDto> list=null;
		Integer totals=null;
		
		
		if(activeId!=null&&activeId==1) {
			/*AuthTokenDTO authTokenDTO = ContextHolder.get();
			if (authTokenDTO != null &&authTokenDTO.getAccountNo() !=null) {//已经登录
				CustomerLoginCommonDTO request=new CustomerLoginCommonDTO();
	  			request.setAccountNo(authTokenDTO.getAccountNo());
	  			Response resonseAccount = accountFeignClient.getBaseMemberInfoByAccountNo(request);
	  			AccountCustomerInfoDTO acid = FastJsonUtils.toBean(FastJsonUtils.toJSONString(resonseAccount.getData()), AccountCustomerInfoDTO.class);
	  			String newOldFlag = acid.getNewOldFlag();//0新人，1老人
	  			if(newOldFlag!=null&&newOldFlag.equals("1")) {
	  				dto.setActiveType("0");
	  			}else {
	  				dto.setActiveType("1");
	  			}
			}else {
				dto.setActiveType("1");
			}*/
			
			list=marketingGoodsMapper.selectGoodsDetailBGoodsId(dto);
			totals=marketingGoodsMapper.selectGoodsDetailBGoodsIdCount(dto);
		}else {
			dto.setActiveType("5");
			list=marketingGoodsMapper.selectMarketingGoodsForApp(dto);
			totals=marketingGoodsMapper.selectMarketingGoodsForAppCount(dto);
		}
		
		 
		map.put("list",list);
		map.put("totals",totals);
		return map;
	}

	/**
	 * 查询视屏列表
	 */
	@Override
	public Map<String, Object> selectVedioGoodsList(VideoManagerEntityDto dto) {
		Map<String,Object> map=new HashMap<>();
		List<VideoManagerEntityDto>  list=videoManagerMapper.selectVideoManagerListForApp(dto);
		if(list!=null&&list.size()>0) {
			for(VideoManagerEntityDto item:list) {
				long ids=item.getId();
				int actuanum=item.getActualNum();
				String videoClickNum="videoclicknum"+ids;
				try {
					String incNum = redisClient.get(videoClickNum);
					if(incNum!=null&&!incNum.equals("")) {
						item.setActualNum(actuanum+Integer.parseInt(incNum));
					}else {
						item.setActualNum(actuanum);
					}				
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		
		Integer totals=videoManagerMapper.selectVideoManagerListCount(dto);
		map.put("list", list);
		map.put("totals", totals);
		return map;
	}


	//健康首页
	@Override
	public Response selectHealthHomeMallIndex() {
/*		AuthTokenDTO authTokenDTO = ContextHolder.get();
		MarketGoodsAndActiveTypeDto mgaatd=new MarketGoodsAndActiveTypeDto();
		mgaatd.setActiveType("1");
		if (authTokenDTO != null && PLAT_TYPE.PLATTPE_APP.getCode().equals(authTokenDTO.getPlatType())) {//已经登录
			CustomerLoginCommonDTO request=new CustomerLoginCommonDTO();
			request.setAccountNo(authTokenDTO.getAccountNo());
			Response resonseAccount = accountFeignClient.getBaseMemberInfoByAccountNo(request);
			AccountCustomerInfoDTO dto = FastJsonUtils.toBean(FastJsonUtils.toJSONString(resonseAccount.getData()), AccountCustomerInfoDTO.class);
			String newOldFlag = dto.getNewOldFlag();//0新人，1老人
			if(newOldFlag!=null&&newOldFlag.equals("1")) {
				mgaatd.setActiveType("0");
			}
		}*/
		
		//编号 1.新人专享  
		HealthHomeMall healthHomeMall=new HealthHomeMall();
		AdvertisingManagerEntityDto adBanaldto=new AdvertisingManagerEntityDto();
		adBanaldto.setType("1");
		adBanaldto.setUpdateStatus("2");
		List<AdvertisingManagerEntity> adBanal=advertisingManagerMapper.selectAdvertisingManagerList(adBanaldto);//类型 1:健康ban  2:商城bana   3:活动banna
		if(adBanaldto!=null) {
			healthHomeMall.setHealBananelList(adBanal);
		}
		
		
		MarketingGoodsDto hotDto =new MarketingGoodsDto();
		hotDto.setActiveId(1L);
		hotDto.setPageNo(0);
		hotDto.setPageSize(4);
		hotDto.setActiveType("1");
		/*AuthTokenDTO authTokenDTO = ContextHolder.get();
		if (authTokenDTO != null &&authTokenDTO.getAccountNo() !=null) {//已经登录
			CustomerLoginCommonDTO request=new CustomerLoginCommonDTO();
  			request.setAccountNo(authTokenDTO.getAccountNo());
  			Response resonseAccount = accountFeignClient.getBaseMemberInfoByAccountNo(request);
  			AccountCustomerInfoDTO acid = FastJsonUtils.toBean(FastJsonUtils.toJSONString(resonseAccount.getData()), AccountCustomerInfoDTO.class);
  			String newOldFlag = acid.getNewOldFlag();//0新人，1老人
  			if(newOldFlag!=null&&newOldFlag.equals("1")) {
  				hotDto.setActiveType("0");
  			}else {
  				hotDto.setActiveType("1");
  			}
		}else {
			hotDto.setActiveType("1");
		}*/
		List<MarketingGoodsDto> hotEntity = marketingGoodsMapper.selectGoodsDetailBGoodsId(hotDto);
		healthHomeMall.setNewHumanGoodsList(hotEntity);


		
		//编号 1.新人专享  2.折扣专区  3.限量秒杀  4.今日爆款  5.国民优选 6.精选好货 7.为您推荐  8：热销
		MarketingActiveDto discountN=new MarketingActiveDto();
		discountN.setId(2L);
		MarketingActiveDto discountActiveEntity= marketingActiveMapper.selectMarketingActiveDetail(discountN);//2.折扣专区
		if(discountActiveEntity!=null) {
			healthHomeMall.setDiscountActive(discountActiveEntity);
		}
		
		
		
		MarketingActiveDto choiceN=new MarketingActiveDto();//精选 choice
		choiceN.setId(6L);
		MarketingActiveDto choiceNEntity= marketingActiveMapper.selectMarketingActiveDetail(choiceN);//2.折扣专区
		if(choiceNEntity!=null) {
			healthHomeMall.setGoodChoiceActive(choiceNEntity);
		}
		

		//视屏管理  类型1:健康首页  2：商城首页
		VideoManagerEntityDto video=new VideoManagerEntityDto();
		video.setType("1");
		video.setPageNo(0);
		video.setPageSize(1);//selectVideoManagerListForIndex
		List<VideoManagerEntityDto> vedioList = videoManagerMapper.selectVideoManagerListForIndex(video);
		if(vedioList!=null&&vedioList.size()>0) {
			healthHomeMall.setVideoHealth(vedioList.get(0));
		}
	
		return Response.ok(healthHomeMall);
	}

	
	/**
	 * 健康资讯  健康首页
	 */
	@Override
	public Response selectHealthInfo(InfomationIndexDto dto) {
		Integer pageNo = dto.getPageNo();
		Integer pageSize = dto.getPageSize();
		int pageNos=pageNo;
		if(pageNo!=null&&pageSize!=null) {
			if(pageNo!=0) {
				pageNo=(pageNo-1)*pageSize;
			}
			dto.setPageNo(pageNo);
			dto.setPageSize(pageSize);
		}else {
			dto.setPageNo(0);
			dto.setPageSize(10);
		}
		String type = dto.getType();
		if(type!=null&&type.equals("1")) {
			Map<String,Object> recommendMap=new HashMap<>();
			List<HealthInformationDto> reList = healthInfomationMapper.selectRecondInfomation(dto);
			if(reList!=null&&reList.size()>0) {
				for(HealthInformationDto item:reList) {
					String infoRead="infomationreadclicknum"+item.getId();
	    			String infoLike="infomationlikeclicknum"+item.getId();
	    			try {
						String readNum = redisClient.get(infoRead);
						if(readNum!=null&&!readNum.equals("")) {
							Integer realRead = item.getRealRead();
							if(realRead!=null) {
								item.setRealRead(item.getRealRead()+Integer.parseInt(readNum));
							}else {
								item.setRealRead(Integer.parseInt(readNum));
							}
							
						}
						
						String likeNum = redisClient.get(infoLike);
						if(likeNum!=null&&!likeNum.equals("")) {
							Integer realLike = item.getRealLike();
							if(realLike!=null) {
								item.setRealLike(item.getRealLike()+Integer.parseInt(likeNum));
							}else {
								item.setRealLike(Integer.parseInt(likeNum));
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
			return Response.ok(reList);
			/*Integer reCount = healthInfomationMapper.selectRecondInfomationCount(dto);
			recommendMap.put("list", reList);
			recommendMap.put("total", reCount);
			recommendMap.put("pageNo", pageNos);
			recommendMap.put("pageSize", pageSize);
			dto.setInfomationMap(recommendMap);*/
		}else if(type!=null&&type.equals("2")) {
			Map<String,Object> hotMap=new HashMap<>();
			List<HealthInformationDto> hotList = healthInfomationMapper.selectHotInfomation(dto);
			if(hotList!=null&&hotList.size()>0) {
				for(HealthInformationDto item:hotList) {
					String infoRead="infomationreadclicknum"+item.getId();
	    			String infoLike="infomationlikeclicknum"+item.getId();
	    			try {
						String readNum = redisClient.get(infoRead);
						if(readNum!=null&&!readNum.equals("")) {
							Integer realRead = item.getRealRead();
							if(realRead!=null) {
								item.setRealRead(item.getRealRead()+Integer.parseInt(readNum));
							}else {
								item.setRealRead(Integer.parseInt(readNum));
							}
							
						}
						
						String likeNum = redisClient.get(infoLike);
						if(likeNum!=null&&!likeNum.equals("")) {
							Integer realLike = item.getRealLike();
							if(realLike!=null) {
								item.setRealLike(item.getRealLike()+Integer.parseInt(likeNum));
							}else {
								item.setRealLike(Integer.parseInt(likeNum));
							}
							
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			return Response.ok(hotList);
			/*Integer hotCount =healthInfomationMapper.selectHotInfomationCount(dto);
			hotMap.put("list", hotList);
			hotMap.put("total", hotCount);
			hotMap.put("pageNo", pageNos);
			hotMap.put("pageSize", pageSize);
			dto.setInfomationMap(hotMap);*/
		}
		return Response.ok();
	}

	
	/**
	 * 增加阅读量
	 * @throws Exception 
	 */
	@Override
	public Response insertHealthRead(HealthInfomation dto) throws Exception {
		String id = dto.getId();
		String videoClickNum="infomationreadclicknum"+id;
		redisClient.setIncAndTime(videoClickNum,172800);//2天=172800s
		return Response.returnCode(ReturnCode.SUCCESS.getCode(),ReturnCode.SUCCESS.getMsg());
	}

	
	/**
	 * 增加喜欢
	 */
	@Override
	public Response insertHealthLike(HealthInfomation dto) throws Exception  {
		String id = dto.getId();
		String videoClickNum="infomationlikeclicknum"+id;
		redisClient.setIncAndTime(videoClickNum,172800);//2天=172800s
		return Response.returnCode(ReturnCode.SUCCESS.getCode(),ReturnCode.SUCCESS.getMsg());
	}

	
	/**
	 * 取消喜欢
	 */
	@Override
	public Response cancelHealthLike(HealthInfomation dto) throws Exception  {
		String id = dto.getId();
		String videoClickNum="infomationlikeclicknum"+id;
		redisClient.setdelkeys(videoClickNum);//2天=172800s
		return Response.returnCode(ReturnCode.SUCCESS.getCode(),ReturnCode.SUCCESS.getMsg());
	}

	/**
	 * 带bannel的轮播图
	 */
	@Override
	public Response selectAdvertisingManagerAndBannelList(MarketingGoodsDto dto) {
		
		Long id = dto.getActiveId();
		MarketingActiveIndexDto adDto=new MarketingActiveIndexDto();
		dto.setActiveType(String.valueOf(id));
		List<MarketingBanalPic> picList = marketingBanalPicMapper.selectMarketingBanalPicByActive(id);
		

		if(id!=null&&id==1) {//新人专享
			/*AuthTokenDTO authTokenDTO = ContextHolder.get();
			if (authTokenDTO != null && PLAT_TYPE.PLATTPE_APP.getCode().equals(authTokenDTO.getPlatType())) {//已经登录
				CustomerLoginCommonDTO request=new CustomerLoginCommonDTO();
	  			request.setAccountNo(authTokenDTO.getAccountNo());
	  			Response resonseAccount = accountFeignClient.getBaseMemberInfoByAccountNo(request);
	  			AccountCustomerInfoDTO acid = FastJsonUtils.toBean(FastJsonUtils.toJSONString(resonseAccount.getData()), AccountCustomerInfoDTO.class);
	  			String newOldFlag = acid.getNewOldFlag();//0新人，1老人
	  			if(newOldFlag!=null&&newOldFlag.equals("1")) {
	  				dto.setActiveType("0");
	  			}else {
	  				dto.setActiveType("1");
	  			}
			}else {
				dto.setActiveType("1");
			}*/
			dto.setActiveType("1");
			List<MarketingGoodsDto> hotEntity = marketingGoodsMapper.selectGoodsDetailBGoodsId(dto);
			adDto.setMarketingGoodsList(hotEntity);
		}else {
			List<MarketingGoodsDto> goodList=marketingGoodsMapper.selectMarketingGoodsForApp(dto);
			adDto.setMarketingGoodsList(goodList);
		}
	
	
	
		
		
		
		
		adDto.setMarketingBanalPicList(picList);
		
		return Response.ok(adDto);
	}

	/**
	 * 查看文章详情
	 * 
	 */
	@Override
	public Response selectHealthInformationDto(HealthInfomation entity) {
		HealthInformationDto healthInformationDto = healthInfomationMapper.selectHealthInformationById(entity.getId());
		if(healthInformationDto!=null) {
			String groups = healthInformationDto.getGroups();
			if(groups!=null&&groups.equals("1")) {
				int intRealInt=0;
				int intRealReadInt=0;
				Integer realLike = healthInformationDto.getRealLike();
				if(realLike!=null) {
					intRealInt=intRealInt+realLike;
				}
				Integer realRead = healthInformationDto.getRealRead();
				if(realRead!=null) {
					intRealReadInt=intRealReadInt+realRead;
				}
				
				String infoLike="infomationlikeclicknum"+entity.getId();
				try {
					String likeNum = redisClient.get(infoLike);
					if(likeNum!=null&&!likeNum.equals("")) {
						intRealInt=intRealInt+Integer.valueOf(likeNum);
					}
					String videoClickNum="infomationreadclicknum"+entity.getId();
					String readNum = redisClient.get(videoClickNum);
					if(readNum!=null&&!readNum.equals("")) {
						intRealReadInt=intRealReadInt+Integer.valueOf(readNum);
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				healthInformationDto.setRealLike(intRealInt);
				healthInformationDto.setRealRead(intRealReadInt);
			
				
				
				
				
				HealthArticle ha = healthArticleMapper.selectHealthArticleById(entity.getId());
				healthInformationDto.setRemark(ha.getRemark());
				List<HealthInfoParentTag> parentTag = healthInfoTagMapper.selectHealthInfoTagParent(entity.getId());
				if(parentTag!=null&&parentTag.size()>0) {
					for(HealthInfoParentTag item:parentTag) {
						Long id2 = item.getId();
						List<HealthInfoTag> list2 = healthInfoTagMapper.selectHealthInfoByTagParent(id2);
						if(list2!=null&&list2.size()>0) {
							item.setHealthInfoTagList(list2);
						}
					}
					healthInformationDto.setHealthInfoParentTag(parentTag);
				}
			}else if(groups!=null&&groups.equals("2")) {
				List<VideoArticleEntity> actList = videoArticleEntityMapper.slectVideoArticleEntityByVideoNew();
				if(actList!=null&&actList.size()>0) {
					for(VideoArticleEntity item:actList) {
						int intRealInt=0;
						Integer realRead = item.getRealRead();
						if(realRead!=null) {
							intRealInt=intRealInt+realRead;
						}
						try {
							String videoClickNum="infomationreadclicknum"+item.getArticleId();
							String readNum = redisClient.get(videoClickNum);
							if(readNum!=null&&!readNum.equals("")) {
								intRealInt=intRealInt+Integer.valueOf(readNum);
							}	
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						item.setRealRead(intRealInt);
					}
				}
				healthInformationDto.setVideoArticleEntityList(actList);
				List<MarketingGoods> goodsList = marketingGoodsMapper.selectMarketingGoodsListNew();
				healthInformationDto.setMarketingGoodsList(goodsList);
				
			}
		}
		return Response.ok(healthInformationDto);
	}
	
	
	
	
	
	
	
	
	

}
