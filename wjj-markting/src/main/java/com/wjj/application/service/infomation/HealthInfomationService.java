package com.wjj.application.service.infomation;

import java.util.Map;

import com.wjj.application.dto.infomation.HealthInformationDto;
import com.wjj.application.entity.information.HealthInfomation;
import com.wjj.application.response.Response;

public interface HealthInfomationService {
	
	public Map<String,Object> list(HealthInformationDto dto);
	
	
	public Response insertHealthInformationDto(HealthInformationDto dto) throws Exception;
	
	public Response updateHealthInformationDto(HealthInformationDto dto) throws Exception;
	
	public Response delHealthInformationDto(HealthInformationDto dto);
	
	public Response updateHealthInformationDtoUp(HealthInfomation dto) throws Exception;
	
	public Response updateHealthInformationDtoDown(HealthInfomation dto) throws Exception;
	
	public Response selectHealthInformationById(HealthInfomation dto);

}
