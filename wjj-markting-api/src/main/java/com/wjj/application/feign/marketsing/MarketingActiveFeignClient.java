package com.wjj.application.feign.marketsing;


import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.wjj.application.dto.marketing.MarketingActiveDto;
import com.wjj.application.entity.marketing.MarketingActive;
import com.wjj.application.response.Response;



@FeignClient("wjj-marketing")
public interface MarketingActiveFeignClient {
	
	
		//查询列表
		@PostMapping(value = "/marketingActive/platform/list")
		public Response selectMarketingActiveList() ;
		
		 //编辑
		@PostMapping(value = "/marketingActive/platform/edit")
		public Response updateMarketingActive(@RequestBody  MarketingActive entity) ;
		
		 //查看详情
		@PostMapping(value = "/marketingActive/platform/detail")
		public Response selectMarketingActiveDetail(@RequestBody  MarketingActive entity) ;
		
		

		 //编辑
		@PostMapping(value = "/marketingActive/platform/activeDetail")
		public Response selectMarketingActiveDetailByActive(@RequestBody  MarketingActive entity);
		
		
		 //删除
		@PostMapping(value = "/marketingActive/platform/delete")
		public Response deleteMarketingActiveByActive(@RequestBody MarketingActiveDto dto) ;
		
		 //编辑
		@PostMapping(value = "/marketingActive/platform/activeDetailUpdate")
		public Response updateMarketingActiveByActive(@RequestBody MarketingActiveDto dto);
		
		
		@PostMapping(value = "/marketingActive/selectActiveGoodsIdListAndOnePenctByList/service")
		public List<String> selectActiveGoodsIdListAndOnePenctByList(@RequestBody List<String> activeIdList);
				

		@PostMapping(value = "/marketingActive/selectActiveGoodsIdListByList/service")
		public List<String> selectActiveGoodsIdListByList(@RequestBody List<String> activeIdList);
}
