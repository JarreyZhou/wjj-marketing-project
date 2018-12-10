package com.wjj.application.controller.protocol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wjj.application.entity.protocol.RegisterProtocolEntity;
import com.wjj.application.response.Response;
import com.wjj.application.service.protocols.RegisterProtocolEntityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "协议相关接口", tags = "商品控制器 v0.2")
@RestController
public class RegisterProtocolController {
	
	
	@Autowired
	private RegisterProtocolEntityService registerProtocolEntityService;
	
	
	@ApiOperation(value = "列表--运营端", notes = "注意问题点：\n 1>输入参数：  \n"
			+ " {     \n  "
			+ "} \n")
	@PostMapping(value = "/registerProtocol/list")
	public Response insertGoodsBaseInfo() throws Exception {
		return Response.ok(registerProtocolEntityService.selectRegisterProtocolEntityList(null));
	}
	
	@ApiOperation(value = "跟新--运营端", notes = "注意问题点：\n 1>输入参数：  \n"
			+ " {     \n  "
			+ "} \n")
	@PostMapping(value = "/registerProtocol/update")
	public Response updateRegisterProtocolEntity(@RequestBody RegisterProtocolEntity entity) throws Exception {
		return Response.ok(registerProtocolEntityService.updateRegisterProtocolEntity(entity));
	}
	
	@ApiOperation(value = "查看详情--运营端和居民端", notes = "注意问题点：\n 1>输入参数：  \n"
			+ " {     \n  "
			+ "} \n")
	@PostMapping(value = "/registerProtocol/slectOne")
	public Response selectRegisterProtocolEntity(@RequestBody RegisterProtocolEntity entity) throws Exception {
		return Response.ok(registerProtocolEntityService.selectRegisterProtocolEntity(entity));
	}
	
	
}
