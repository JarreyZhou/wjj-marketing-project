package com.wjj.application.mapper.marketing;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wjj.application.dto.marketing.MarketingActiveDto;
import com.wjj.application.entity.marketing.MarketingActive;

public interface MarketingActiveMapper extends BaseMapper<MarketingActive>{
	
	List<MarketingActive> selectMarketingActiveList();
	
	Integer updateMarketingActive(MarketingActive entity);
	
	MarketingActiveDto selectMarketingActiveDetail(MarketingActive entity);

}
