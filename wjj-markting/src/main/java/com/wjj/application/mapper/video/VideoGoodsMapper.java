package com.wjj.application.mapper.video;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wjj.application.dto.GiftUpdateStoreDTO;
import com.wjj.application.dto.GoodsMainInfoDto;
import com.wjj.application.entity.marketing.MarketingGoods;
import com.wjj.application.entity.video.VideoGoodsEntity;

public interface VideoGoodsMapper  extends BaseMapper<VideoGoodsEntity> {
	
	Integer deleteVideoGoodsByVideo(Long videoId);
	
	List<VideoGoodsEntity> selectVideoGoodsByVideo(Long videoId);
	
	Integer updateVideoGoodsByList(List<MarketingGoods> list);
	
	List<String> selectVideoGoodsAllGoodsId();
	
	Integer updateVideoGoodsByGoodsId(List<GiftUpdateStoreDTO> list);
	
	Integer  updateVideoGoodsByUpdateGoodsBase(GoodsMainInfoDto dto);
	
	Integer deleteVideoGoodsByUpdateGoodsBase(List<String> goodsIdlist);
}
