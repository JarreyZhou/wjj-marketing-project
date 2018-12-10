package com.wjj.application.controller.infomation;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wjj.application.dto.infomation.HealthPlanDto;
import com.wjj.application.entity.information.HealthPlan;
import com.wjj.application.response.Response;
import com.wjj.application.service.infomation.HealthPlanService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "健康方案相关接口", tags = "健康方案")
@RestController
public class HealthPlanController {

	@Autowired
	private HealthPlanService healthPlanService;
	
	
	@ApiOperation(value = "列表--运营端", notes = "注意问题点    \n  1>输入参数：   "
			+ "pageNo:当前页数（必填 ）  pageSize：每页记录数(必填)   name：名称    type：类型    healthSatus：状态  1:草稿   2：上架   3下架  "
			+ "  \n2>输出参数："

			+ "  \n ")
	@PostMapping(value = "healthPlan/list")
	public Response selectHealthPlanDto(@RequestBody HealthPlanDto dto) throws Exception {
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
		 Map<String,Object> map = healthPlanService.selectHealthPlanDto(dto);
			 map.put("pageNo", pageNos);
			 map.put("pageSize", pageSize);
		return Response.ok(map);
	}
	
	
	@ApiOperation(value = "新增--运营端", notes = "注意问题点    \n  1>输入参数：   "
			+ "name:名称        \n"
			+ "fromTime：开始时间           \n  "
			+ "endTime：结束时间          \n"
			+ "type：类型            \n"
			+ "healthSatus：状态  1:草稿   2：上架   3下架             \n"
			+ "detail:详情             \n "
			+ "healthRelGoodsList:{[goodsId:商品编号  goodsName：商品名称    salePrice：最低售价   maxPrice：最高售价]}             \n"
			+ "healthParentTag:标签列表 {[tagType:标签类型   tagValue：标签值   tagName:标签名称     healthTag:子标签列表{[tagType:标签类型   tagValue：标签值   tagName:标签名称]}   ]}             \n"
			+ " \n2>输出参数："
			+ "  \n ")
	@PostMapping(value = "healthPlan/insert")
	public Response insertHealthPlanDto(@RequestBody HealthPlanDto dto) throws Exception{
		return healthPlanService.insertHealthPlanDto(dto);
	}

	@ApiOperation(value = "编辑--运营端", notes = "注意问题点    \n  1>输入参数：   "
			+ "id:编号        \n"
			+ "name:名称        \n"
			+ "fromTime：开始时间           \n  "
			+ "endTime：结束时间          \n"
			+ "type：类型            \n"
			+ "healthSatus：状态  1:草稿   2：上架   3下架             \n"
			+ "detail:详情             \n "
			+ "healthRelGoodsList:{[goodsId:商品编号  goodsName：商品名称    salePrice：最低售价   maxPrice：最高售价]}             \n"
			+ "healthParentTag:标签列表 {[tagType:标签类型   tagValue：标签值   tagName:标签名称     healthTag:子标签列表{[tagType:标签类型   tagValue：标签值   tagName:标签名称]}   ]}             \n"
			+ " \n2>输出参数："
			+ "  \n ")
	@PostMapping(value = "healthPlan/update")
	public Response updateHealthPlanDto(@RequestBody HealthPlanDto dto) throws Exception{
		return healthPlanService.updateHealthPlanDto(dto);
	}
	
	@ApiOperation(value = "上架----运营端", notes = "注意问题点    \n  1>输入参数：   "
			+ "id:编号        \n"
			+ " \n2>输出参数："
			+ "  \n ")
	@PostMapping(value = "healthPlan/upStore")
	public Response updateHealthPlanForUp(@RequestBody HealthPlan healthPlan) {
		return healthPlanService.updateHealthPlanForUp(healthPlan.getId());
	}
	
	
	
	@ApiOperation(value = "下架----运营端", notes = "注意问题点    \n  1>输入参数：   "
			+ "id:编号        \n"
			+ " \n2>输出参数："
			+ "  \n ")
	@PostMapping(value = "healthPlan/downStore")
	public Response updateHealthPlanForDown(@RequestBody HealthPlan healthPlan) {
		return healthPlanService.updateHealthPlanForDown(healthPlan.getId());
	}
	
	
	@ApiOperation(value = "查看详情----运营端", notes = "注意问题点    \n  1>输入参数：   "
			+ "id:编号        \n"
			+ " \n2>输出参数："
			+ "  \n ")
	@PostMapping(value = "healthPlan/selectOne")
	public Response selectHealthPlanById(@RequestBody HealthPlan healthPlan)throws Exception{
		return healthPlanService.selectHealthPlanById(healthPlan.getId());
	}
	
	@ApiOperation(value = "删除----运营端", notes = "注意问题点    \n  1>输入参数：   "
			+ "id:编号        \n"
			+ " \n2>输出参数："
			+ "  \n ")
	@PostMapping(value = "healthPlan/delete")
	public Response delHealthPlanById(@RequestBody HealthPlan healthPlan)throws Exception{
		return healthPlanService.deleteHealthPlanById(healthPlan.getId());
	}
	
	
	
	
}
