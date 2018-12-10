package com.wjj.application.service.infomation.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wjj.application.dto.infomation.HealthPlanDto;
import com.wjj.application.entity.information.HealthInfoParentTag;
import com.wjj.application.entity.information.HealthParentTag;
import com.wjj.application.entity.information.HealthPlan;
import com.wjj.application.entity.information.HealthRelGoods;
import com.wjj.application.entity.information.HealthTag;
import com.wjj.application.json.JsonUtil;
import com.wjj.application.mapper.infomation.HealthPlanMapper;
import com.wjj.application.mapper.infomation.HealthRelGoodsMapper;
import com.wjj.application.mapper.infomation.HealthTagMapper;
import com.wjj.application.response.Response;
import com.wjj.application.response.ReturnCode;
import com.wjj.application.service.infomation.HealthPlanService;
import com.wjj.application.util.RedisClient;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public  class HealthPlanServiceImpl  extends ServiceImpl<HealthPlanMapper, HealthPlan> implements HealthPlanService{
	
	@Autowired
	private HealthPlanMapper healthPlanMapper;
	
	@Autowired
	private HealthRelGoodsMapper healthRelGoodsMapper;
	
	@Autowired
	private  HealthTagMapper healthTagMapper;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private RedisClient redisClient;
	
	/**
	 * 新建方案
	 * 1:获取方案编号
	 * 2.插入商品关联表
	 * 3.插入标签表
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor = {RuntimeException.class })
	public Response insertHealthPlanDto(HealthPlanDto dto) throws Exception {
		dto.setUpdateDate(new Date());
		List<HealthParentTag> healthParentTag1 = dto.getHealthParentTag();
		if(healthParentTag1!=null&&healthParentTag1.size()>0) {
			String sexTagType=env.getProperty("goods.tag.sex");
			for(HealthParentTag item:healthParentTag1) {
				if(item.getTagType()!=null&&item.equals(sexTagType)) {//设置性别
					dto.setSex(item.getTagValue());	
				}
			}
		}
		healthPlanMapper.insert(dto);
		Long id = dto.getId();//方案编号
		List<HealthRelGoods> healthRelGoodsList = dto.getHealthRelGoodsList();
		if(healthRelGoodsList!=null&&healthRelGoodsList.size()>0) {
			for(HealthRelGoods item:healthRelGoodsList) {
				item.setHealthId(id);
				healthRelGoodsMapper.insert(item);
			}
		}
		
		List<HealthParentTag> healthParentTag = dto.getHealthParentTag();
		if(healthParentTag!=null&&healthParentTag.size()>0) {
			String sexTagType=env.getProperty("goods.tag.sex");
			for(HealthParentTag item:healthParentTag) {
				if(item.getTagType()!=null&&!item.equals(sexTagType)) {//插入父标签
					HealthTag healthTagParent=new HealthTag();
					healthTagParent.setHealthId(id);
					healthTagParent.setTagLevel("1");
					healthTagParent.setTagType(item.getTagType());
					healthTagParent.setTagTypeName(item.getTagTypeName());
					healthTagParent.setTagValue(item.getTagValue());
					healthTagParent.setTagName(item.getTagName());
					healthTagMapper.insert(healthTagParent);
					List<HealthTag> healthTagList = item.getHealthTag();//插入子标签
					if(healthTagList!=null&&healthTagList.size()>0) {
						for(HealthTag  item1:healthTagList) {
							item1.setTagLevel("2");
							item1.setHealthId(id);
							item1.setTagType(item.getTagType());
							item1.setTagTypeName(item.getTagTypeName());
							item1.setParentId(healthTagParent.getId());	
							healthTagMapper.insert(item1);
						}
						
					}	
					
				}
			}
		}
		return Response.returnCode(ReturnCode.SUCCESS.getCode(),ReturnCode.SUCCESS.getMsg());
	}

	/**
	 * 新建方案
	 * 1:获取方案编号
	 * 2.插入商品关联表
	 * 3.插入标签表
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor = {RuntimeException.class })
	public Response updateHealthPlanDto(HealthPlanDto dto) throws Exception {
		dto.setUpdateDate(new Date());
		List<HealthParentTag> healthParentTag1 = dto.getHealthParentTag();
		if(healthParentTag1!=null&&healthParentTag1.size()>0) {
			String sexTagType=env.getProperty("goods.tag.sex");
			for(HealthParentTag item:healthParentTag1) {
				if(item.getTagType()!=null&&item.equals(sexTagType)) {//设置性别
					dto.setSex(item.getTagValue());	
				}
			}
		}
		healthPlanMapper.updateById(dto);
		Long id = dto.getId();
		
		// 跟新相关商品记录
		healthRelGoodsMapper.deleteHealthRelGoodsByHealthId(id);
		List<HealthRelGoods> healthRelGoodsList = dto.getHealthRelGoodsList();
		if(healthRelGoodsList!=null&&healthRelGoodsList.size()>0) {
			for(HealthRelGoods item:healthRelGoodsList) {
				item.setHealthId(id);
				healthRelGoodsMapper.insert(item);
			}
		}
		
		//跟新标签相关记录
		healthTagMapper.deleteHealthTagById(id);
		List<HealthParentTag> healthParentTag = dto.getHealthParentTag();
		if(healthParentTag!=null&&healthParentTag.size()>0) {
			String sexTagType=env.getProperty("goods.tag.sex");
			for(HealthParentTag item:healthParentTag) {
				if(item.getTagType()!=null&&!item.equals(sexTagType)) {//插入父标签
					HealthTag healthTagParent=new HealthTag();
					healthTagParent.setHealthId(id);
					healthTagParent.setTagLevel("1");
					healthTagParent.setTagType(item.getTagType());
					healthTagParent.setTagTypeName(item.getTagTypeName());
					healthTagParent.setTagValue(item.getTagValue());
					healthTagParent.setTagName(item.getTagName());
					healthTagMapper.insert(healthTagParent);
					List<HealthTag> healthTagList = item.getHealthTag();//插入子标签
					if(healthTagList!=null&&healthTagList.size()>0) {
						for(HealthTag  item1:healthTagList) {
							item1.setTagLevel("2");
							item1.setHealthId(id);
							item1.setTagType(item.getTagType());
							item1.setTagTypeName(item.getTagTypeName());
							item1.setParentId(healthTagParent.getId());	
							healthTagMapper.insert(item1);
						}
						
					}	
					
				}
			}
		}
		String str="healthPlan"+id;
		redisClient.del(str);
		return Response.returnCode(ReturnCode.SUCCESS.getCode(),ReturnCode.SUCCESS.getMsg());
	}

	/**
	 * 列表展示
	 */
	@Override
	public Map<String,Object> selectHealthPlanDto(HealthPlanDto dto) {
		List<HealthPlan> list = healthPlanMapper.selectHealthPlanForList(dto);
		Integer total = healthPlanMapper.selectHealthPlanForListCount(dto);
		Map<String,Object> hashMap=new HashMap<String,Object>();
		hashMap.put("list", list);
		hashMap.put("total", total);
		return hashMap;
	}

	/**
	 * 上架
	 */
	@Override
	public Response updateHealthPlanForUp(Long id) {
		healthPlanMapper.updateHealthPlanForUp(id);
		return Response.returnCode(ReturnCode.SUCCESS.getCode(),ReturnCode.SUCCESS.getMsg());
	}

	/**
	 * 下架
	 */
	@Override
	public Response updateHealthPlanForDown(Long id) {
		healthPlanMapper.updateHealthPlanForDown(id);
		return Response.returnCode(ReturnCode.SUCCESS.getCode(),ReturnCode.SUCCESS.getMsg());
	}

	
	@Override
	public Response selectHealthPlanById(Long id) throws Exception {
		String str="healthPlan"+id;
		String sowingListStr = redisClient.get(str);
		if(sowingListStr!=null&&!sowingListStr.equals("")) {
			HealthPlanDto dto = JSON.parseObject(sowingListStr, new TypeReference<HealthPlanDto>() {});
			return Response.ok(dto);
		}else {
			HealthPlanDto dto = healthPlanMapper.selectHealthPlanById(id);
			List<HealthRelGoods> healthRelGoodsByHealthId = healthRelGoodsMapper.selectHealthRelGoodsByHealthId(id);
			if(dto!=null) {
				dto.setHealthRelGoodsList(healthRelGoodsByHealthId);
				List<HealthParentTag> list = healthTagMapper.selectHealthTagById(id);
				if(list!=null&&list.size()>0) {
					for(HealthParentTag item:list) {
						Long id2 = item.getId();
						List<HealthTag> tagByParentId = healthTagMapper.selectHealthTagByParentId(id2);
						if(tagByParentId!=null&&tagByParentId.size()>0) {
							item.setHealthTag(tagByParentId);
						}	
					}
				}
				dto.setHealthParentTag(list);
				String sowingListNewStr = JsonUtil.object2Json(dto);
				redisClient.set(str,sowingListNewStr);	
			}
			return Response.ok(dto);
		}
		
	}

	@Override
	public Response deleteHealthPlanById(Long id) throws Exception {
		healthPlanMapper.deleteById(id);
		return Response.returnCode(ReturnCode.SUCCESS.getCode(),ReturnCode.SUCCESS.getMsg());
	}
}
