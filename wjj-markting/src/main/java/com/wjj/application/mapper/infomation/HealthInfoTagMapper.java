package com.wjj.application.mapper.infomation;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wjj.application.entity.information.HealthInfoParentTag;
import com.wjj.application.entity.information.HealthInfoTag;
public interface HealthInfoTagMapper  extends BaseMapper<HealthInfoTag>{
	
	Integer deleteHealthInfoTag(String infoId);
	
	List<HealthInfoParentTag> selectHealthInfoTagParent(String infoId);
	
	List<HealthInfoTag> selectHealthInfoByTagParent(Long id);

}
