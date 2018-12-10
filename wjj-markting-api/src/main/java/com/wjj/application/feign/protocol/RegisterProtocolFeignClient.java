package com.wjj.application.feign.protocol;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.wjj.application.entity.protocol.RegisterProtocolEntity;
import com.wjj.application.response.Response;


@FeignClient("wjj-marketing")
public interface RegisterProtocolFeignClient {
	
	@PostMapping(value = "/registerProtocol/list")
	public Response insertGoodsBaseInfo() throws Exception ;
	
	
	@PostMapping(value = "/registerProtocol/update")
	public Response updateRegisterProtocolEntity(@RequestBody RegisterProtocolEntity entity) throws Exception;
	
	
	@PostMapping(value = "/registerProtocol/slectOne")
	public Response selectRegisterProtocolEntity(@RequestBody RegisterProtocolEntity entity) throws Exception;
}
