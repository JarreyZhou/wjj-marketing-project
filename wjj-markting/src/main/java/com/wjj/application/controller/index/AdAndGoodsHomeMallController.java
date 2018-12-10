package com.wjj.application.controller.index;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wjj.application.dto.index.InfomationIndexDto;
import com.wjj.application.dto.marketing.MarketingGoodsDto;
import com.wjj.application.dto.video.VideoManagerEntityDto;
import com.wjj.application.entity.information.HealthInfomation;
import com.wjj.application.response.Response;
import com.wjj.application.service.index.AdAndGoodsHomeMallService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "商城首页及其健康首页-居民端", tags = "商城首页及其健康首页-居民端")
@RestController
public class AdAndGoodsHomeMallController {
	
	
	@Autowired
	private AdAndGoodsHomeMallService adAndGoodsHomeMallService;
	
	
	@ApiOperation(value = "广告营销接口--居民端", notes = "注意问题点    \n  1>输入参数：   "
			+ "无 "
			+ "  \n2>输出参数："
			+ "  headAd;头部广告  	\n"
			+ " midAd;中间广告 	 \n"
			+ " todayBursting;今日爆款  	\n"
			+ " limitSecondKill;限量秒杀  	\n"
			+ " recommended;为您推荐 	 \n"
			+ "  hotSale;热销排行  	\n"
			+ "  videoHome;视频专区     \n"
			+ "  \n ")
	@PostMapping(value = "homeMall/adAndGoodsHomeMall/index")
	public Response selectAdAndGoodsHomeMall(){
		return adAndGoodsHomeMallService.selectAdAndGoodsHomeMall();
	}
	
	@ApiOperation(value = "查询营销商品--居民端(小程序 不带营销bannel)", notes = "注意问题点    \n  1>输入参数：   "
			+ "pageNo:必填    \n "
			+ "pageSizeo:必填    \n "
			+ "activeId:必填      1.新人专享  2.折扣专区  3.限量秒杀  4.今日爆款  5.国民优选 6.精选好货 7.为您推荐  8：热销 \n "
			+ "  \n ")
	@PostMapping(value = "homeMall/marketGoodsByActiveId")
	public Response selectMarketGoodsList(@RequestBody MarketingGoodsDto dto) {
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
		Map<String, Object> map = adAndGoodsHomeMallService.selectMarketGoodsList(dto);
		 map.put("pageNo", pageNos);
		 map.put("pageSize", pageSize);
		 return Response.ok(map);
		
	}
	
	@ApiOperation(value = "查询视屏--居民端", notes = "注意问题点    \n  1>输入参数：   "
			+ "pageNo:必填    \n "
			+ "pageSizeo:必填    \n "
			+ "type: 类型1:健康首页  2：商城首页 \n "
			+ "  \n ")
	@PostMapping(value = "homeMall/vedioGoodsByActiveId")
	public Response selectVedioGoodsList(@RequestBody VideoManagerEntityDto dto){
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
		Map<String, Object> map = adAndGoodsHomeMallService.selectVedioGoodsList(dto);
		 map.put("pageNo", pageNos);
		 map.put("pageSize", pageSize);
		 return Response.ok(map);
	}
	
	//健康首页
	@ApiOperation(value = "健康首页--居民端", notes = "注意问题点    \n  1>输入参数：   "
			+ "无   输出参数： \n "
			+ " healBananelList： 头部bananel newHumanGoodsList： 新人专享    discountActive：折扣转专区    goodChoiceActive：精选   videoHealth：健康视屏   infomationList：资讯列表  \n ")
	@PostMapping(value = "homeMall/healIndex")
	public Response selectHealthHomeMallIndex() {
		return adAndGoodsHomeMallService.selectHealthHomeMallIndex();
	}
	
	//健康首页获取资讯信息
	//健康首页
	@ApiOperation(value = "健康首页资讯信息--居民端", notes = "注意问题点    \n  1>输入参数：  { "
			+ "type  1：为您推荐   2：热门资讯      \n "
			+ "pageSize:每页记录数        \n  "
			+ "pageNo:当前页数     \n   } ")
	@PostMapping(value = "homeMall/healInfomation")
	public Response selectHealthInfo(@RequestBody InfomationIndexDto dto) {
		return adAndGoodsHomeMallService.selectHealthInfo(dto);
	}
	
	//增加阅读量资讯
	//健康首页
	@ApiOperation(value = "资讯阅读量新增--居民端", notes = "注意问题点    \n  1>输入参数：  { "
			+ "id:资讯编号  (喜欢)      \n  "
			+ "     \n   } ")
	@PostMapping(value = "homeMall/healInfomation/incread")
	public Response insertHealthRead(@RequestBody HealthInfomation dto) throws Exception{
		return adAndGoodsHomeMallService.insertHealthRead(dto);
	}
		
	//喜欢资讯
	@ApiOperation(value = "资讯喜欢新增--居民端", notes = "注意问题点    \n  1>输入参数：  { "
			+ "id:资讯编号    (喜欢)    \n "
			+ "     \n   } ")
	@PostMapping(value = "homeMall/healInfomation/inlike")
	public Response insertHealthLike(@RequestBody HealthInfomation dto) throws Exception{
		return adAndGoodsHomeMallService.insertHealthLike(dto);
	}
		
	//取消喜欢资讯
	@ApiOperation(value = "资讯取消喜欢--居民端", notes = "注意问题点    \n  1>输入参数：  { "
			+ "id:资讯编号   (喜欢)      \n "
			+ "    \n   } ")
	@PostMapping(value = "homeMall/healInfomation/cancelLike")
	public Response cancelHealthLike(@RequestBody HealthInfomation dto) throws Exception{
		return adAndGoodsHomeMallService.cancelHealthLike(dto);
	}

	@ApiOperation(value = "查询营销商品--居民端(app 带营销bannel)", notes = "注意问题点    \n  1>输入参数：   "
			+ "pageNo:必填    \n "
			+ "pageSizeo:必填    \n "
			+ "activeId:必填      1.新人专享  2.折扣专区  3.限量秒杀  4.今日爆款  5.国民优选 6.精选好货 7.为您推荐  8：热销 \n "
			+ "  \n ")
	@PostMapping(value = "homeMall/app/marketGoodsByActiveId")
	public Response selectAdvertisingManagerAndBannelList(@RequestBody MarketingGoodsDto dto) {
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
		return adAndGoodsHomeMallService.selectAdvertisingManagerAndBannelList(dto);
	}	
	
	@ApiOperation(value = "资讯详情--居民端", notes = "注意问题点    \n  1>输入参数：   "
			+ "id:资讯编号  (必填)    \n "
			+ "  \n ")
	@PostMapping(value = "homeMall/article/detail")
	public Response  selectHealthInformationDto(@RequestBody HealthInfomation entity) {
		return adAndGoodsHomeMallService.selectHealthInformationDto(entity);
	}
}
