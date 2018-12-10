package com.wjj.application.service.advertising.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wjj.application.dto.GoodsClassifyTypeDto;
import com.wjj.application.dto.advertising.AdvertisingManagerEntityDto;
import com.wjj.application.entity.advertising.AdvertisingManagerEntity;
import com.wjj.application.json.JsonUtil;
import com.wjj.application.mapper.advertising.AdvertisingManagerMapper;
import com.wjj.application.response.GoodsReturnCode;
import com.wjj.application.response.Response;
import com.wjj.application.service.advertising.AdvertisingManagerService;
import com.wjj.application.util.RedisClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdvertisingManagerServiceImp  extends ServiceImpl<AdvertisingManagerMapper, AdvertisingManagerEntity> implements AdvertisingManagerService{
	
	@Autowired
	private AdvertisingManagerMapper advertisingManagerMapper;
	
	@Autowired
	private RedisClient  redisClient;
	
	/**
	 * 查询列表
	 */
	@Override
	public Response selectAdvertisingManagerList(AdvertisingManagerEntityDto dto) {
		return Response.ok(advertisingManagerMapper.selectAdvertisingManagerList(dto));
	}

	/**
	 * 查看详情
	 */
	@Override
	public Response selectOne(AdvertisingManagerEntity entity) {
		return Response.ok(advertisingManagerMapper.selectById(entity.getId()));
	}
	
	/**
	 * 更新缓存同步问题
	 * 
	 * 
	 */
	@Override
	public Response updateAdvertisingManager(AdvertisingManagerEntity entity) {
		this.synchnizeRedis(entity.getType());
		entity.setCreateTime(new Date());
		return Response.ok(advertisingManagerMapper.updateById(entity));
	}
	
	/**
	 * 删除
	 */
	@Override
	public Response deleteAdvertisingManager(AdvertisingManagerEntity entity) {
		this.synchnizeRedis(entity.getType());
		return Response.ok(advertisingManagerMapper.deleteById(entity.getId()));
	}
	
	/**
	 * 上架前判断状态 1:如果上架大于6  不让其上架
	 */
	@Override
	public Response updateAdvertisingManagerUp(AdvertisingManagerEntityDto dto) {
		String type = dto.getType();
		if(type!=null&&type.equals("3")) {
			dto.setUpdateStatus("2");
			Integer totals = advertisingManagerMapper.selectAdvertisingManagerListCount(dto);
			if(totals!=null&&totals>0) {
				String errOr="上架的广告已超过1个";
				return Response.returnCodeAndData(GoodsReturnCode.RESPONSE_3907.getCode(),GoodsReturnCode.RESPONSE_3907.getMsg(),errOr);
			}
			
			
		}else {
			dto.setUpdateStatus("2");
			Integer totals = advertisingManagerMapper.selectAdvertisingManagerListCount(dto);
			if(totals!=null&&totals>5) {
				String errOr="上架的广告已超过6个";
				return Response.returnCodeAndData(GoodsReturnCode.RESPONSE_3906.getCode(),GoodsReturnCode.RESPONSE_3906.getMsg(),errOr);
			}
		}
		this.synchnizeRedis(dto.getType());
		Long id = dto.getId();
		dto.setCreateTime(new Date());
		return Response.ok(advertisingManagerMapper.updateAdvertisingManagerUp(id));
	}
	
	/**
	 * 下架
	 */
	@Override
	public Response updateAdvertisingManagerDown(AdvertisingManagerEntityDto dto) {
		Long id = dto.getId();
		this.synchnizeRedis(dto.getType());
		return Response.ok(advertisingManagerMapper.updateAdvertisingManagerDown(id));
	}

	/**
	 * 新增
	 */
	@Override
	public Response insertAdvertisingManager(AdvertisingManagerEntity entity) {
		entity.setCreateTime(new Date());
		return Response.ok(advertisingManagerMapper.insert(entity));
	}
	
	/**
	 * app查看列表
	 * @throws Exception 
	 */
	@Override
	public Response selectAdvertisByType(String type) throws Exception {
		String keyStr="advertising"+type;
		String valueStr = redisClient.get(keyStr);
		List<AdvertisingManagerEntity> list=null;
		if(valueStr!=null&&!valueStr.equals("")) {
			list = JsonUtil.json2List(valueStr,AdvertisingManagerEntity.class);
		}else {
			AdvertisingManagerEntityDto dto=new AdvertisingManagerEntityDto();
			dto.setType(type);
			List<AdvertisingManagerEntity> dtoList = advertisingManagerMapper.selectAdvertisingManagerList(dto);
			valueStr = JsonUtil.list2Json(dtoList);
			redisClient.set(keyStr, valueStr);
		}
		return Response.ok(list);
	}
	
	
	
	
	
	/**
	 * 缓存同步问题
	 * @param type
	 */
	public void synchnizeRedis(String type) {
		String keyStr="advertising"+type;
		try {
			redisClient.del(keyStr);
			AdvertisingManagerEntityDto dto=new AdvertisingManagerEntityDto();
			dto.setType(type);
			List<AdvertisingManagerEntity> dtoList = advertisingManagerMapper.selectAdvertisingManagerList(dto);
			String valueStr = JsonUtil.list2Json(dtoList);
			redisClient.set(keyStr, valueStr);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

}
