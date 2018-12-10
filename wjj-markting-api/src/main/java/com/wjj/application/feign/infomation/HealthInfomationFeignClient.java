package com.wjj.application.feign.infomation;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.wjj.application.dto.infomation.HealthInformationDto;
import com.wjj.application.entity.information.HealthInfomation;
import com.wjj.application.response.Response;

@FeignClient("wjj-marketing")
public interface HealthInfomationFeignClient {
	
	
	@PostMapping(value = "healthInfomation/list")
	public Response selectHealthPlanDto(@RequestBody HealthInformationDto dto) throws Exception ;
	
	
	@PostMapping(value = "healthInfomation/insert")
	public Response insertHealthInformationDto(@RequestBody HealthInformationDto dto) throws Exception ;
	

	@PostMapping(value = "healthInfomation/update")
	public Response updateHealthInformationDto(@RequestBody HealthInformationDto dto) throws Exception ;
	
	
	@PostMapping(value = "healthInfomation/delete")
	public Response delHealthInformationDto(@RequestBody HealthInformationDto dto) throws Exception ;
	
	
	@PostMapping(value = "healthInfomation/upStore")
	public Response updateHealthInformationDtoUp(@RequestBody HealthInfomation dto) throws Exception ;
	

	@PostMapping(value = "healthInfomation/downStore")
	public Response updateHealthInformationDtoDown(@RequestBody HealthInfomation dto) throws Exception ;
	

	@PostMapping(value = "healthInfomation/selectOne")
	public Response selectHealthInformationById(@RequestBody HealthInfomation dto) ;
}
