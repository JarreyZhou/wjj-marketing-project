package com.wjj.application.dto.index;



import java.util.List;

import com.wjj.application.dto.marketing.MarketGoodsAndActiveTypeDto;
import com.wjj.application.dto.marketing.MarketingGoodsDto;
import com.wjj.application.entity.advertising.AdvertisingManagerEntity;
import com.wjj.application.entity.marketing.MarketingActive;
import com.wjj.application.entity.video.VideoManagerEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;


@Data
@ApiModel(value="健康首页接口-居民端")
public class HealthHomeMall {
	
	//头部bananel
	private List<AdvertisingManagerEntity> healBananelList;
	
	//新人专享
	private List<MarketingGoodsDto> newHumanGoodsList;
	
	
	//折扣转专区
	private MarketingActive discountActive;
	
	//精选 choice
	private MarketingActive  goodChoiceActive;
	
	//健康视屏
	private VideoManagerEntity videoHealth;
	


}
