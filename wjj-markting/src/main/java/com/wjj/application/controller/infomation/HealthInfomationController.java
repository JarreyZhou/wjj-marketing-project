package com.wjj.application.controller.infomation;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wjj.application.dto.infomation.HealthInformationDto;
import com.wjj.application.entity.information.HealthInfomation;
import com.wjj.application.response.Response;
import com.wjj.application.service.infomation.HealthInfomationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "健康资讯相关接口", tags = "健康资讯")
@RestController
public class HealthInfomationController {

	@Autowired
	private HealthInfomationService healthInfomationService;
	
	
	@ApiOperation(value = "列表--运营端", notes = "注意问题点    \n  1>输入参数：   "
			+ "pageNo:当前页数（必填 ）  pageSize：每页记录数(必填)   titleName：标题    typeId：类型    infoStatus：状态  1:草稿   2：上架   3下架  groups:分类  "
			+ "  \n2>输出参数："

			+ "  \n ")
	@PostMapping(value = "healthInfomation/list")
	public Response selectHealthPlanDto(@RequestBody HealthInformationDto dto) throws Exception {
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
		 Map<String,Object> map = healthInfomationService.list(dto);
			 map.put("pageNo", pageNos);
			 map.put("pageSize", pageSize);
		return Response.ok(map);
	}
	
	@ApiOperation(value = "新增--运营端", notes = "注意问题点    \n  1>输入参数：   "
			+ " "
			+ "  \n2>输出参数："

			+ "  \n ")
	@PostMapping(value = "healthInfomation/insert")
	public Response insertHealthInformationDto(@RequestBody HealthInformationDto dto) throws Exception {
		return healthInfomationService.insertHealthInformationDto(dto);
	}
	
	@ApiOperation(value = "跟新--运营端", notes = "注意问题点    \n  1>输入参数：   "
			+ " "
			+ "  \n2>输出参数："

			+ "  \n ")
	@PostMapping(value = "healthInfomation/update")
	public Response updateHealthInformationDto(@RequestBody HealthInformationDto dto) throws Exception {
		return healthInfomationService.updateHealthInformationDto(dto);
	}
	
	@ApiOperation(value = "删除--运营端", notes = "注意问题点    \n  1>输入参数：   "
			+ "id:编号"
			+ "  \n2>输出参数："

			+ "  \n ")
	@PostMapping(value = "healthInfomation/delete")
	public Response delHealthInformationDto(@RequestBody HealthInformationDto dto) throws Exception {
		return healthInfomationService.delHealthInformationDto(dto);
	}
	
	@ApiOperation(value = "上架--运营端", notes = "注意问题点    \n  1>输入参数：   "
			+ "id:编号"
			+ "  \n2>输出参数："
			+ "  \n ")
	@PostMapping(value = "healthInfomation/upStore")
	public Response updateHealthInformationDtoUp(@RequestBody HealthInfomation dto) throws Exception {
		return healthInfomationService.updateHealthInformationDtoUp(dto);
	}
	
	@ApiOperation(value = "下架--运营端", notes = "注意问题点    \n  1>输入参数：   "
			+ "id:编号"
			+ "  \n2>输出参数："
			+ "  \n ")
	@PostMapping(value = "healthInfomation/downStore")
	public Response updateHealthInformationDtoDown(@RequestBody HealthInfomation dto) throws Exception {
		return healthInfomationService.updateHealthInformationDtoDown(dto);
	}	
	
	@ApiOperation(value = "查看详情--运营端", notes = "注意问题点    \n  1>输入参数：   "
			+ "id:编号"
			+ "  \n2>输出参数："
			+ "  \n ")
	@PostMapping(value = "healthInfomation/selectOne")
	public Response selectHealthInformationById(@RequestBody HealthInfomation dto) {
		return healthInfomationService.selectHealthInformationById(dto);
	}
}
