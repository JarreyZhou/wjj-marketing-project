package com.wjj.application.dto.index;

import java.util.List;

import com.wjj.application.dto.marketing.MarketingGoodsDto;
import com.wjj.application.entity.marketing.MarketingActive;
import com.wjj.application.entity.marketing.MarketingBanalPic;

import lombok.Data;
@Data
public class MarketingActiveIndexDto extends MarketingActive{
	private Integer pageNo;
	
	private Integer pageSize;
	
	private List<MarketingBanalPic> marketingBanalPicList;
	
	private List<MarketingGoodsDto> marketingGoodsList;
}
