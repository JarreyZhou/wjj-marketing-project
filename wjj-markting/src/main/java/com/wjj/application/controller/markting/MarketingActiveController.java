package com.wjj.application.controller.markting;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wjj.application.dto.marketing.MarketingActiveDto;
import com.wjj.application.entity.marketing.MarketingActive;
import com.wjj.application.response.Response;
import com.wjj.application.service.markting.MarketingActiveService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "营销活动接口", tags = "营销活动接口")
@RestController
public class MarketingActiveController {
	
	@Autowired
	private MarketingActiveService marketingActiveService;


    //查询列表
	@ApiOperation(value = "列表--运营端", notes = "注意问题点：\n 1>输入参数：  \n"
			+ " {  无   \n  "+
	
			"} \n")
	@PostMapping(value = "/marketingActive/platform/list")
	public Response selectMarketingActiveList() {
		return Response.ok(marketingActiveService.selectMarketingActiveList());
	}
	
	 //编辑
	@ApiOperation(value = "编辑--运营端", notes = "注意问题点：\n 1>输入参数：  \n"
			+ " { id：  编号 1.新人专享  2.折扣专区  3.限量秒杀  4.今日爆款  5.国民优选 6.精选好货 7.为您推荐  (必填)  \n  "+
			"  activeName:活动名称    \n"+
			"  mainPath:主图    \n"+
			"  updateHuman:跟新人    \n"+
			"} \n")
	@PostMapping(value = "/marketingActive/platform/edit")
	public Response updateMarketingActive(@RequestBody  MarketingActive entity) {
		entity.setUpdateTime(new Date());
		return Response.ok(marketingActiveService.updateMarketingActive(entity));
	}
	
	 //查看详情
	@ApiOperation(value = "详情--运营端", notes = "注意问题点：\n 1>输入参数：  \n"
			+ " { id：  编号 1.新人专享  2.折扣专区  3.限量秒杀  4.今日爆款  5.国民优选 6.精选好货 7.为您推荐  (必填)    \n  "+
	
			"} \n")
	@PostMapping(value = "/marketingActive/platform/detail")
	public Response selectMarketingActiveDetail(@RequestBody  MarketingActive entity) {
		return Response.ok(marketingActiveService.selectMarketingActiveDetail(entity));
	}
	
	

	 //编辑
	@ApiOperation(value = "带商品的详情--运营端", notes = "注意问题点：\n 1>输入参数：  \n"
			+ " {  id：  编号 1.新人专享  2.折扣专区  3.限量秒杀  4.今日爆款  5.国民优选 6.精选好货 7.为您推荐  (必填)   \n  "+
	
			"} \n")
	@PostMapping(value = "/marketingActive/platform/activeDetail")
	public Response selectMarketingActiveDetailByActive(@RequestBody  MarketingActive entity) {
		return Response.ok(marketingActiveService.selectMarketingActiveDetailByActive(entity));
	}
	
	
	 //删除
	@ApiOperation(value = "删除--运营端", notes = "注意问题点：\n 1>输入参数：  \n"
			+ " {     \n  "+
			"  	 marketingGoodsList：关联商品列表[{id：编号}]  \n  "+
			"} \n")
	@PostMapping(value = "/marketingActive/platform/delete")
	public Response deleteMarketingActiveByActive(@RequestBody MarketingActiveDto dto) {
		return Response.ok(marketingActiveService.deleteMarketingActiveByActive(dto));
	}
	
	 //编辑
	@ApiOperation(value = "带商品的详情编辑--运营端", notes = "注意问题点：\n 1>输入参数：  \n"
			+ " {   id：  编号 1.新人专享  2.折扣专区  3.限量秒杀  4.今日爆款  5.国民优选 6.精选好货 7.为您推荐  (必填)  \n  "+
			" updateHuman:跟新人      \n"+
			" mainPath：主图      \n"+
			" marketingBanalPicList：banal图列表[{picPath:图片路径}]      \n"+
			" marketingGoodsList：关联商品列表[{id：编号 ,goodsId:商品编号,goodsName:商品名称   detailId：规格编号   detailName：规格名称   leveThreeName：三级分类名称   storeNum：可售库存"
			+ "salePrice：售价    sortId：排序  newHumanPrice：新人价   discount：折扣  maxPrice：最高售价   minPrice：最低售价"
			+ "}]      \n"+
			"} \n")
	@PostMapping(value = "/marketingActive/platform/activeDetailUpdate")
	public Response updateMarketingActiveByActive(@RequestBody MarketingActiveDto dto) {
		return Response.ok(marketingActiveService.updateMarketingActiveByActive(dto));
	}
	
	//查询所有活动和一分抢的商品编号
	@ApiOperation(value = "查询所有活动和一分抢的商品编号--服务间调用", notes = "注意问题点：\n 1>输入参数：  \n"+
			"} \n")
	@PostMapping(value = "/marketingActive/selectActiveGoodsIdListAndOnePenctByList/service")
	public List<String> selectActiveGoodsIdListAndOnePenctByList(@RequestBody List<String> activeIdList){
		return  marketingActiveService.selectActiveGoodsIdListAndOnePenctByList(activeIdList);
	}
			
	//查询所有活动和一分抢的商品编号
	@ApiOperation(value = "查询所有活动和一分抢的商品编号--服务间调用", notes = "注意问题点：\n 1>输入参数：  \n"+
			"} \n")
	@PostMapping(value = "/marketingActive/selectActiveGoodsIdListByList/service")
	public List<String> selectActiveGoodsIdListByList(@RequestBody List<String> activeIdList){
		return  marketingActiveService.selectActiveGoodsIdListByList(activeIdList);
	}
		
	
}
