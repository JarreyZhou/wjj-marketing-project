package com.wjj.application.mapper.infomation;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wjj.application.dto.GoodsMainInfoDto;
import com.wjj.application.entity.information.HealthRelGoods;
import com.wjj.application.entity.marketing.MarketingGoods;

public interface HealthRelGoodsMapper   extends BaseMapper<HealthRelGoods> {
	
	Integer deleteHealthRelGoodsByHealthId(Long healthId);
	
	List<HealthRelGoods> selectHealthRelGoodsByHealthId(Long healthId);
	
	Integer updateHealthRelGoodsByList(List<MarketingGoods> list);
	
	List<String> selectHealthRelGoodsAllGoodsId();
	
	Integer updateHealthRelGoodsByGoodsMain(GoodsMainInfoDto dto);
	
	Integer deleteHealthRelGoods(List<String> list);
}
