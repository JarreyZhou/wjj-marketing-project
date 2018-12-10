package com.wjj.application.mapper.infomation;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wjj.application.entity.information.HealthInfomationType;

public interface HealthInfomationTypeMapper    extends BaseMapper<HealthInfomationType>{
	
	List<HealthInfomationType> selectHealthInfomationTypeList(HealthInfomationType healthInfomationType);
	
	Integer selectInformationByTypeId(Long typeId);
}
