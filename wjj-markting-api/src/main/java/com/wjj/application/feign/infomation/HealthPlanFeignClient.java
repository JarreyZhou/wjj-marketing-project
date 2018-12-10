package com.wjj.application.feign.infomation;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.wjj.application.dto.infomation.HealthPlanDto;
import com.wjj.application.entity.information.HealthPlan;
import com.wjj.application.response.Response;


@FeignClient("wjj-marketing")
public interface HealthPlanFeignClient {
	
	@PostMapping(value = "healthPlan/list")
	public Response selectHealthPlanDto(@RequestBody HealthPlanDto dto) throws Exception;
	
	
	@PostMapping(value = "healthPlan/insert")
	public Response insertHealthPlanDto(@RequestBody HealthPlanDto dto) throws Exception;

	
	@PostMapping(value = "healthPlan/update")
	public Response updateHealthPlanDto(@RequestBody HealthPlanDto dto) throws Exception;
	
	
	@PostMapping(value = "healthPlan/upStore")
	public Response updateHealthPlanForUp(@RequestBody HealthPlan healthPlan) ;
	
	
	
	
	@PostMapping(value = "healthPlan/downStore")
	public Response updateHealthPlanForDown(@RequestBody HealthPlan healthPlan) ;
	
	
	
	@PostMapping(value = "healthPlan/selectOne")
	public Response selectHealthPlanById(@RequestBody HealthPlan healthPlan)throws Exception;
	
	
	@PostMapping(value = "healthPlan/delete")
	public Response delHealthPlanById(@RequestBody HealthPlan healthPlan)throws Exception;
}
