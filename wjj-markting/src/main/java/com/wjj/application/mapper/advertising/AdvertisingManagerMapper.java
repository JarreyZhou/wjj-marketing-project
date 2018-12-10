package com.wjj.application.mapper.advertising;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wjj.application.dto.advertising.AdvertisingManagerEntityDto;
import com.wjj.application.entity.advertising.AdvertisingManagerEntity;

public interface AdvertisingManagerMapper  extends BaseMapper<AdvertisingManagerEntity> {
	
	List<AdvertisingManagerEntity> selectAdvertisingManagerList(AdvertisingManagerEntityDto dto);
	
	Integer selectAdvertisingManagerListCount(AdvertisingManagerEntityDto dto);
	
	Integer updateAdvertisingManagerUp(Long id);
	
	Integer updateAdvertisingManagerDown(Long id);

}
