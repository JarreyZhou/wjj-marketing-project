package com.wjj.application.feign.adverising;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.wjj.application.dto.advertising.AdvertisingManagerEntityDto;
import com.wjj.application.entity.advertising.AdvertisingManagerEntity;
import com.wjj.application.response.Response;


@FeignClient("wjj-marketing")
public interface AdvertisingManagerFeighClent {
	  
	
	@PostMapping(value = "/advertisingManager/platform/list")
	public Response selectAdvertisingManagerList(@RequestBody AdvertisingManagerEntityDto dto) ;
	
	
	@PostMapping(value = "/advertisingManager/platform/detail")
	public Response selectOne(@RequestBody AdvertisingManagerEntity entity);
	
	
	@PostMapping(value = "/advertisingManager/platform/insert")
	public Response insertAdvertisingManager(@RequestBody AdvertisingManagerEntity entity) ;
	

	@PostMapping(value = "/advertisingManager/platform/update")
	Response updateAdvertisingManager(@RequestBody AdvertisingManagerEntity entity) ;
	
	//删除
	@PostMapping(value = "/advertisingManager/platform/delete")
	Response deleteAdvertisingManager(@RequestBody AdvertisingManagerEntity entity) ;
	
	//上架
	@PostMapping(value = "/advertisingManager/platform/upStore")
	public Response updateAdvertisingManagerUp(@RequestBody AdvertisingManagerEntityDto dto) ;
	
	//下架
	@PostMapping(value = "/advertisingManager/platform/downStore")
	public  Response updateAdvertisingManagerDown(@RequestBody AdvertisingManagerEntityDto dto) ;
	
	
	@PostMapping(value = "/advertisingManager/app/list")
	public Response selectAdvertisByType(@RequestBody AdvertisingManagerEntity entity) throws Exception;
}
