package com.wjj.application.feign.infomation;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.wjj.application.entity.information.HealthInfomationType;
import com.wjj.application.response.Response;

@FeignClient("wjj-marketing")
public interface HealthInfomationTypeFeignClient {
	
	
	@PostMapping(value = "healthInfomationType/list")
	public Response selectHealthInfomationTypeList(@RequestBody HealthInfomationType healthInfomationType) throws Exception;
	

	@PostMapping(value = "healthInfomationType/insert")
	public Response insertHealthInfomationType(@RequestBody HealthInfomationType healthInfomationType) throws Exception ;
	
	
	
	@PostMapping(value = "healthInfomationType/update")
	public Response updateHealthInfomationType(@RequestBody HealthInfomationType healthInfomationType) throws Exception ;
	
	@PostMapping(value = "healthInfomationType/delete")
	public Response delHealthInfomationType(@RequestBody HealthInfomationType healthInfomationType) throws Exception ;
	
	
	@PostMapping(value = "healthInfomationType/selectOne")
	public Response selectOne(@RequestBody HealthInfomationType healthInfomationType) throws Exception;
	
	
	
	@PostMapping(value = "healthInfomationType/down")
	public Response downHealthInfomationType(@RequestBody HealthInfomationType healthInfomationType) throws Exception ;
	
}
