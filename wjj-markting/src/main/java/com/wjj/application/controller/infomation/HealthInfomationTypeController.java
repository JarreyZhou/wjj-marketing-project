package com.wjj.application.controller.infomation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wjj.application.entity.information.HealthInfomationType;
import com.wjj.application.response.Response;
import com.wjj.application.service.infomation.HealthInfomationTypeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "健康资讯类型相关接口", tags = "健康资讯类型")
@RestController
public class HealthInfomationTypeController {
	@Autowired
	private HealthInfomationTypeService healthInfomationTypeService;
	
	
	@ApiOperation(value = "列表--运营端", notes = "注意问题点    \n  1>输入参数：   "
			+ " name：名称    healthSatus：状态  1:草稿   2：上架   3下架  "
			+ "  \n2>输出参数："

			+ "  \n ")
	@PostMapping(value = "healthInfomationType/list")
	public Response selectHealthInfomationTypeList(@RequestBody HealthInfomationType healthInfomationType) throws Exception {
		return healthInfomationTypeService.selectHealthInfomationTypeList(healthInfomationType);
	}
	
	@ApiOperation(value = "新增--运营端", notes = "注意问题点    \n  1>输入参数：   "
			+ " name：名称   healthSatus：状态  1:草稿   2：上架   3下架  "
			+ "  \n2>输出参数："

			+ "  \n ")
	@PostMapping(value = "healthInfomationType/insert")
	public Response insertHealthInfomationType(@RequestBody HealthInfomationType healthInfomationType) throws Exception {
		return healthInfomationTypeService.insertHealthInfomationType(healthInfomationType);
	}
	
	
	@ApiOperation(value = "跟新或者上架--运营端", notes = "注意问题点    \n  1>输入参数：   "
			+ " name：名称   healthSatus：状态  1:草稿   2：上架   3下架  "
			+ "  \n2>输出参数："

			+ "  \n ")
	@PostMapping(value = "healthInfomationType/update")
	public Response updateHealthInfomationType(@RequestBody HealthInfomationType healthInfomationType) throws Exception {
		return healthInfomationTypeService.updateHealthInfomationType(healthInfomationType);
	}
	
	@ApiOperation(value = "删除--运营端", notes = "注意问题点    \n  1>输入参数：   "
			+ " id：编号   "
			+ "  \n2>输出参数："

			+ "  \n ")
	@PostMapping(value = "healthInfomationType/delete")
	public Response delHealthInfomationType(@RequestBody HealthInfomationType healthInfomationType) throws Exception {
		return healthInfomationTypeService.delHealthInfomationType(healthInfomationType);
	}
	
	@ApiOperation(value = "查看详情--运营端", notes = "注意问题点    \n  1>输入参数：   "
			+ " id：编号   "
			+ "  \n2>输出参数："

			+ "  \n ")
	@PostMapping(value = "healthInfomationType/selectOne")
	public Response selectOne(@RequestBody HealthInfomationType healthInfomationType) throws Exception {
		return healthInfomationTypeService.selectOne(healthInfomationType);
	}
	
	
	@ApiOperation(value = "下架--运营端", notes = "注意问题点    \n  1>输入参数：   "
			+ " id：编号   "
			+ "  \n2>输出参数："

			+ "  \n ")
	@PostMapping(value = "healthInfomationType/down")
	public Response downHealthInfomationType(@RequestBody HealthInfomationType healthInfomationType) throws Exception {
		return healthInfomationTypeService.downHealthInfomationType(healthInfomationType);
	}
	
	
	
	
	
	
	
	
	
}
