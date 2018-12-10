package com.wjj.application.mapper.infomation;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wjj.application.dto.infomation.HealthPlanDto;
import com.wjj.application.entity.information.HealthPlan;

public interface HealthPlanMapper  extends BaseMapper<HealthPlan> {
	
	public 	List<HealthPlan> selectHealthPlanForList(HealthPlanDto dto);
	
	public Integer selectHealthPlanForListCount(HealthPlanDto dto);
	
	public Integer updateHealthPlanForUp(Long id);
	
	public Integer updateHealthPlanForDown(Long id);
	
	public HealthPlanDto selectHealthPlanById(Long id);
	
}
