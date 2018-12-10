package com.wjj.application.mapper.infomation;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wjj.application.entity.information.HealthParentTag;
import com.wjj.application.entity.information.HealthTag;

public interface HealthTagMapper extends BaseMapper<HealthTag>{
	
	Integer deleteHealthTagById(Long healthId);
	
	List<HealthParentTag> selectHealthTagById(Long healthId);
	
	List<HealthTag> selectHealthTagByParentId(Long parentId);
}
