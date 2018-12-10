package com.wjj.application.mapper.marketing;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wjj.application.entity.marketing.MarketingBanalPic;

public interface MarketingBanalPicMapper  extends BaseMapper<MarketingBanalPic>{

	Integer deleteMarketingBanalPicByActive(Long activeId);
	
	List<MarketingBanalPic> selectMarketingBanalPicByActive(Long activeId);
}
