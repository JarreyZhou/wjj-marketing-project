package com.wjj.application.dto.index;



import java.util.List;

import com.wjj.application.dto.marketing.MarketingGoodsDto;
import com.wjj.application.dto.video.VideoManagerEntityDto;
import com.wjj.application.entity.advertising.AdvertisingManagerEntity;
import com.wjj.application.entity.marketing.MarketingActive;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value="商城首页广告商品接口-居民端")
public class AdAndGoodsHomeMall {
	
	private List<AdvertisingManagerEntity> headAd;//头部广告
	
	private List<AdvertisingManagerEntity> midAd;//中间广告
	
	private MarketingActive todayBursting;//今日爆款
	
	private MarketingActive limitSecondKill;//限量秒杀
	
	private MarketingActive recommended;//为您推荐
	
	private List<MarketingGoodsDto> hotSale;//热销排行
	
	private List<VideoManagerEntityDto> videoHome;//视频专区
	
	private List<MarketingGoodsDto>  superiorProducts;//国民优品
	
	
	
	
	

}
