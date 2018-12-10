package com.wjj.application.dto.marketing;


import java.util.List;

import com.wjj.application.entity.marketing.MarketingGoods;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value="商品带活动类型的")
public class MarketGoodsAndActiveTypeDto {
	
	
	private String activeType;//0 不参加活动    1.新人专享  2.折扣专区  3.限量秒杀  4.今日爆款  5.国民优选 6.精选好货 7.为您推荐  8：热销
	
	private List<MarketingGoods> newHumanGoodsList;//新人专享 

}
