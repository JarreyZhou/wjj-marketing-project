package com.wjj.application.dto.marketing;


import java.util.List;

import com.wjj.application.entity.marketing.MarketingActive;
import com.wjj.application.entity.marketing.MarketingBanalPic;
import com.wjj.application.entity.marketing.MarketingGoods;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value="活动关联商品表")
public class MarketingActiveDto extends MarketingActive {
	
	private Integer pageNo;
	
	private Integer pageSize;
	
	private List<MarketingBanalPic> marketingBanalPicList;
	
	private List<MarketingGoods> marketingGoodsList;
	
}
