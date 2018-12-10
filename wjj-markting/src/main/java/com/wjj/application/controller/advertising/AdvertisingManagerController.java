package com.wjj.application.controller.advertising;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wjj.application.dto.advertising.AdvertisingManagerEntityDto;
import com.wjj.application.entity.advertising.AdvertisingManagerEntity;
import com.wjj.application.response.Response;
import com.wjj.application.service.advertising.AdvertisingManagerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "广告位相关接口", tags = "广告位相关接口")
@RestController
public class AdvertisingManagerController {
	
		@Autowired
		private AdvertisingManagerService advertisingManagerService;
	

	    //查询列表
		@ApiOperation(value = "列表--运营端", notes = "注意问题点：\n 1>输入参数：  \n"
				+ " { name:名称    \n  "+
				"   type: 1:健康bana  2:商城bana   3:活动banna (必填)  \n "+
				"   updateStatus:状态   \n "+
				"   fromTime:开始时间   \n "+
				"   endTime:结束时间   \n "+
				"} \n")
		@PostMapping(value = "/advertisingManager/platform/list")
		public Response selectAdvertisingManagerList(@RequestBody AdvertisingManagerEntityDto dto) {
			String createTimeStart = dto.getCreateTimeStart();
			if(createTimeStart!=null&&!createTimeStart.equals("")) {
				dto.setFromTime(createTimeStart);
			}
			String createTimeEnd = dto.getCreateTimeEnd();
			if(createTimeEnd!=null&&!createTimeEnd.equals("")) {
				dto.setEndTime(createTimeEnd);
			}
			return advertisingManagerService.selectAdvertisingManagerList(dto);
		}
		
		
		
		//查看
		  //查询列表
		@ApiOperation(value = "查看详情--运营端/居民端", notes = "注意问题点：\n 1>输入参数：  \n"
				+ " { id:编号(必填)    \n  "+
				"} \n")
		@PostMapping(value = "/advertisingManager/platform/detail")
		public Response selectOne(@RequestBody AdvertisingManagerEntity entity) {
			return advertisingManagerService.selectOne(entity);
		}
		
		//插入
		@ApiOperation(value = "插入--运营端", notes = "注意问题点：\n 1>输入参数：  \n"
				+ " { type: 1:健康bana  2:商城bana   3:活动banna (必填)    \n  "+
				"} \n")
		@PostMapping(value = "/advertisingManager/platform/insert")
		public Response insertAdvertisingManager(@RequestBody AdvertisingManagerEntity entity) {
			return advertisingManagerService.insertAdvertisingManager(entity);
		}
		
		//编辑
		@ApiOperation(value = "编辑--运营端", notes = "注意问题点：\n 1>输入参数：  \n"
				+ " { id:编号(必填)   \n  "+
				"} \n")
		@PostMapping(value = "/advertisingManager/platform/update")
		Response updateAdvertisingManager(@RequestBody AdvertisingManagerEntity entity) {
			return advertisingManagerService.updateAdvertisingManager(entity);
		}
		
		//删除
		@ApiOperation(value = "删除--运营端", notes = "注意问题点：\n 1>输入参数：  \n"
				+ " { id:编号(必填)   \n  "+
				"} \n")
		@PostMapping(value = "/advertisingManager/platform/delete")
		Response deleteAdvertisingManager(@RequestBody AdvertisingManagerEntity entity) {
			return advertisingManagerService.deleteAdvertisingManager(entity);
		}
		
		//上架
		@ApiOperation(value = "上架--运营端", notes = "注意问题点：\n 1>输入参数：  \n"
				+ " { id:编号(必填)   \n  "+
				"} \n")
		@PostMapping(value = "/advertisingManager/platform/upStore")
		public Response updateAdvertisingManagerUp(@RequestBody AdvertisingManagerEntityDto dto) {
			return advertisingManagerService.updateAdvertisingManagerUp(dto);
		}
		
		//下架
		@ApiOperation(value = "下架--运营端", notes = "注意问题点：\n 1>输入参数：  \n"
				+ " { id:编号(必填)   \n  "+
				"} \n")
		@PostMapping(value = "/advertisingManager/platform/downStore")
		public  Response updateAdvertisingManagerDown(@RequestBody AdvertisingManagerEntityDto dto) {
			return advertisingManagerService.updateAdvertisingManagerDown(dto);
		}
		
		//根据类型查看广告
		@ApiOperation(value = "根据类型查看广告--居民端", notes = "注意问题点：\n 1>输入参数：  \n"
				+ " { type: 1:健康bana  2:商城bana   3:活动banna (必填)   \n  "+
				"} \n")
		@PostMapping(value = "/advertisingManager/app/list")
		public Response selectAdvertisByType(@RequestBody AdvertisingManagerEntity entity) throws Exception{
			return advertisingManagerService.selectAdvertisByType(entity.getType());
		}
}
