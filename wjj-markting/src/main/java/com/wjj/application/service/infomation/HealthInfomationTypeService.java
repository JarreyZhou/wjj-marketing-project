package com.wjj.application.service.infomation;


import com.wjj.application.entity.information.HealthInfomationType;
import com.wjj.application.response.Response;

public interface HealthInfomationTypeService {

	public Response selectHealthInfomationTypeList(HealthInfomationType healthInfomationType);
	
	public Response insertHealthInfomationType (HealthInfomationType healthInfomationType);
	
	public Response selectOne (HealthInfomationType healthInfomationType);
	
	public Response updateHealthInfomationType (HealthInfomationType healthInfomationType);
	
	public Response delHealthInfomationType (HealthInfomationType healthInfomationType);
	
	public Response downHealthInfomationType (HealthInfomationType healthInfomationType);
	
}
