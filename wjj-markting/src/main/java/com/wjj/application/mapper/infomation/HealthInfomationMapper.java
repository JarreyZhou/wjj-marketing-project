package com.wjj.application.mapper.infomation;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wjj.application.dto.index.InfomationIndexDto;
import com.wjj.application.dto.infomation.HealthInformationDto;
import com.wjj.application.entity.information.HealthInfomation;
public interface HealthInfomationMapper    extends BaseMapper<HealthInfomation>{
	
	public List<HealthInformationDto> selectHealthInfomationList(HealthInformationDto healthInformationDto);
	
	public Integer selectHealthInfomationListCount(HealthInformationDto healthInformationDto);
	
	public Integer updateHealthInfomation(HealthInformationDto dto);
	
	public Integer delHealthInformationDto(String id);
	
	HealthInformationDto selectHealthInformationById(String id);
	
	
	List<HealthInformationDto> selectRecondInfomation(InfomationIndexDto dto);
	
	Integer selectRecondInfomationCount(InfomationIndexDto dto);
	
	List<HealthInformationDto> selectHotInfomation(InfomationIndexDto dto);
	
	Integer selectHotInfomationCount(InfomationIndexDto dto);
	
	List<HealthInfomation> selectHealthInfomationId();
	
	Integer updateHealthInfomationActualNum(List<HealthInfomation> list);
}
