package com.wjj.application.service.infomation;

import java.util.Map;

import com.wjj.application.dto.infomation.HealthPlanDto;
import com.wjj.application.response.Response;

public interface HealthPlanService {
	
	public Response insertHealthPlanDto(HealthPlanDto dto) throws Exception;
	
	public Response updateHealthPlanDto(HealthPlanDto dto) throws Exception;
	
	public Map<String,Object>  selectHealthPlanDto(HealthPlanDto dto);

	public Response updateHealthPlanForUp(Long id);
	
	public Response updateHealthPlanForDown(Long id);
	
	public Response selectHealthPlanById(Long id)throws Exception;
	
	public Response deleteHealthPlanById(Long id)throws Exception;
	
}
