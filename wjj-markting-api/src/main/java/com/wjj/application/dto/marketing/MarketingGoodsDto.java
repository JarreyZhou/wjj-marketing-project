package com.wjj.application.dto.marketing;


import com.wjj.application.entity.marketing.MarketingGoods;

import lombok.Data;

@Data
public class MarketingGoodsDto extends MarketingGoods{
	
	
	private String goodsTypeName;
	
	
	private int pageNo;
	
	private int pageSize;
	
	private String activeType;//  1.新人专享  2.折扣专区  3.限量秒杀  4.今日爆款  5.国民优选 6.精选好货 7.为您推荐  8：热销
	
}
