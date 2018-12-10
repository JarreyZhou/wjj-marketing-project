package com.wjj.application.service.advertising;

import com.wjj.application.dto.advertising.AdvertisingManagerEntityDto;
import com.wjj.application.entity.advertising.AdvertisingManagerEntity;
import com.wjj.application.response.Response;

public interface AdvertisingManagerService {
	
	//查询列表
	Response selectAdvertisingManagerList(AdvertisingManagerEntityDto dto);
	
	//查看
	Response selectOne(AdvertisingManagerEntity entity);
	
	//插入
	Response insertAdvertisingManager(AdvertisingManagerEntity entity);
	
	//编辑
	Response updateAdvertisingManager(AdvertisingManagerEntity entity);
	
	//删除
	Response deleteAdvertisingManager(AdvertisingManagerEntity entity);
	
	//上架
	Response updateAdvertisingManagerUp(AdvertisingManagerEntityDto dto);
	
	//下架
	Response updateAdvertisingManagerDown(AdvertisingManagerEntityDto dto);
	
	//app端查看广告位
	Response selectAdvertisByType(String type) throws Exception;
	

}
