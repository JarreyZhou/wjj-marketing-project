package com.wjj.application.service.markting;

import java.util.List;

import com.wjj.application.dto.marketing.MarketingActiveDto;
import com.wjj.application.entity.StandardDetailSupplement;
import com.wjj.application.entity.marketing.MarketingActive;
import com.wjj.application.response.Response;

public interface MarketingActiveService {
	//活动列表
	List<MarketingActive> selectMarketingActiveList();
	
	//活动列表编辑
	Integer updateMarketingActive(MarketingActive entity);
	
	//活动列表查看相情
	MarketingActiveDto selectMarketingActiveDetail(MarketingActive entity);
	
	
	//具体活动列表
	MarketingActiveDto  selectMarketingActiveDetailByActive(MarketingActive entity);
	
	//删除
	Response  deleteMarketingActiveByActive(MarketingActiveDto dto);
	
	
	//跟新
	Response  updateMarketingActiveByActive(MarketingActiveDto dto);
	
	//查询所有活动和一分抢的商品编号
	List<String> selectActiveGoodsIdListAndOnePenctByList(List<String> activeIdList);
		
	//查询所有活动和一分抢的商品编号
	List<String> selectActiveGoodsIdListByList(List<String> activeIdList);
	
	
	
}
