package com.wjj.application.feign.index;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.wjj.application.dto.index.InfomationIndexDto;
import com.wjj.application.dto.marketing.MarketingGoodsDto;
import com.wjj.application.dto.video.VideoManagerEntityDto;
import com.wjj.application.entity.information.HealthInfomation;
import com.wjj.application.response.Response;

import io.swagger.annotations.ApiOperation;


@FeignClient("wjj-marketing")
public interface HomeMallFeignClient {
	
	@PostMapping(value = "homeMall/adAndGoodsHomeMall/index")
	public Response selectAdAndGoodsHomeMall();
	
	
	@PostMapping(value = "homeMall/marketGoodsByActiveId")
	public Response selectMarketGoodsList(@RequestBody MarketingGoodsDto dto);
	
	
	@PostMapping(value = "homeMall/vedioGoodsByActiveId")
	public Response selectVedioGoodsList(@RequestBody VideoManagerEntityDto dto);
	
	
	@PostMapping(value = "homeMall/healIndex")
	public Response selectHealthHomeMallIndex();
	
	//健康首页获取资讯信息
	//健康首页
	@PostMapping(value = "homeMall/healInfomation")
	public Response selectHealthInfo(@RequestBody InfomationIndexDto dto);

	@PostMapping(value = "homeMall/healInfomation/incread")
	public Response insertHealthRead(@RequestBody HealthInfomation dto) throws Exception;
		

	@PostMapping(value = "homeMall/healInfomation/inlike")
	public Response insertHealthLike(@RequestBody HealthInfomation dto) throws Exception;
		

	@PostMapping(value = "homeMall/healInfomation/cancelLike")
	public Response cancelHealthLike(@RequestBody HealthInfomation dto) throws Exception;
	
	@PostMapping(value = "homeMall/app/marketGoodsByActiveId")
	public Response selectAdvertisingManagerAndBannelList(@RequestBody MarketingGoodsDto dto);
	
	@PostMapping(value = "homeMall/article/detail")
	public Response  selectHealthInformationDto(@RequestBody HealthInfomation entity);
	
}
