package com.wjj.application.service.infomation.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wjj.application.entity.information.HealthInfomationType;
import com.wjj.application.mapper.infomation.HealthInfomationTypeMapper;
import com.wjj.application.response.Response;
import com.wjj.application.response.ReturnCode;
import com.wjj.application.service.infomation.HealthInfomationTypeService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HealthInfomationTypeServiceImpl   extends ServiceImpl<HealthInfomationTypeMapper, HealthInfomationType> implements HealthInfomationTypeService {

	@Autowired
	private HealthInfomationTypeMapper healthInfomationTypeMapper;
	
	
	@Override
	public Response selectHealthInfomationTypeList(HealthInfomationType healthInfomationType) {
		List<HealthInfomationType> list = healthInfomationTypeMapper.selectHealthInfomationTypeList(healthInfomationType);
		return Response.ok(list);
	}
	
	@Override
	public Response insertHealthInfomationType (HealthInfomationType healthInfomationType) {
		healthInfomationTypeMapper.insert(healthInfomationType);
		return Response.returnCode(ReturnCode.SUCCESS.getCode(),ReturnCode.SUCCESS.getMsg());
	}

	@Override
	public Response selectOne(HealthInfomationType healthInfomationType) {
		HealthInfomationType selectById = healthInfomationTypeMapper.selectById(healthInfomationType.getId());
		return Response.ok(selectById);
	}
	
	
	/**
	 * 上架  和更新
	 */
	@Override
	public Response updateHealthInfomationType(HealthInfomationType healthInfomationType) {
		healthInfomationType.setTypeStatus("2");
		healthInfomationTypeMapper.updateById(healthInfomationType);
		return Response.returnCode(ReturnCode.SUCCESS.getCode(),ReturnCode.SUCCESS.getMsg());
	}

	@Override
	public Response delHealthInfomationType(HealthInfomationType healthInfomationType) {
		
		Long id = healthInfomationType.getId();
		Integer totals = healthInfomationTypeMapper.selectInformationByTypeId(id);
		if(totals!=null &&totals>0) {
			return Response.returnCodeAndData(ReturnCode.UPDATE_FAIL.getCode(),ReturnCode.UPDATE_FAIL.getMsg(), "所属资讯不为空，不能删除");
		}else {
			healthInfomationTypeMapper.deleteById(id);
		}
		return Response.returnCode(ReturnCode.SUCCESS.getCode(),ReturnCode.SUCCESS.getMsg());
	}

	@Override
	public Response downHealthInfomationType(HealthInfomationType healthInfomationType) {
		Long id = healthInfomationType.getId();
		Integer totals = healthInfomationTypeMapper.selectInformationByTypeId(id);
		if(totals!=null &&totals>0) {
			return Response.returnCodeAndData(ReturnCode.UPDATE_FAIL.getCode(),ReturnCode.UPDATE_FAIL.getMsg(), "所属资讯不为空，不能删除");
		}else {
			healthInfomationType.setTypeStatus("3");
			healthInfomationTypeMapper.updateById(healthInfomationType);
		}
		return Response.returnCode(ReturnCode.SUCCESS.getCode(),ReturnCode.SUCCESS.getMsg());
	}
	
	
	
	
	
	
	

}
