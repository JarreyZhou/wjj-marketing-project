package com.wjj.application.mapper.marketing;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wjj.application.dto.GiftUpdateStoreDTO;
import com.wjj.application.dto.GoodsMainInfoDto;
import com.wjj.application.dto.marketing.MarketingGoodsDto;
import com.wjj.application.entity.StandardDetail;
import com.wjj.application.entity.marketing.MarketingGoods;

public interface MarketingGoodsMapper   extends BaseMapper<MarketingGoods>{
	
	
	
	List<MarketingGoods> selectMarketingGoodsList(Long activeId);
	
	Integer updateMarketingGoodsPart(MarketingGoods entity);
	
	Integer deleteMarketingGoodsByActiveId(Long activeId);
	
	List<MarketingGoodsDto> selectMarketingGoodsForApp(MarketingGoodsDto dto);
	
	Integer selectMarketingGoodsForAppCount(MarketingGoodsDto marketingGoodsDto);
	
	List<String> selectMarketingAllGoodsId();
	
	List<Long> selectMarketingAllDetailId();
	
	Integer updateMarketingDetailList(List<MarketingGoods> list);
	
	Integer updateMarketingGoodsList(List<MarketingGoods> list);
	
	Integer updateGiftUpdateStoreDTOByGoodsIdAndDetailId(List<GiftUpdateStoreDTO> list);
	
	//根据商品编号更新商品信息
	Integer updateMarketingGoodsByMq(GoodsMainInfoDto goodsMainInfoDto);
	
	//根据商品规格跟新商品相关信息
	
	Integer updateMarketingDetailListByMq(List<StandardDetail> list);
	
	Integer deleteMarketingDetailListByMq(List<String> goodsIdList);
	
	Integer deleteMarketingDetailListForTimer(List<Long> detailIdList);
	
	List<Long> selectDetaiListByActiveId(Long activeId);
	
	//查询所有活动和一分抢的商品编号
	List<String> selectActiveGoodsIdListAndOnePenctByList(List<String> activeIdList);
	
	//查询所有活动和一分抢的商品编号
	List<String> selectActiveGoodsIdListByList(List<String> activeIdList);
	
	List<MarketingGoodsDto> selectGoodsDetailBGoodsId(MarketingGoodsDto dto);
	
	Integer selectGoodsDetailBGoodsIdCount(MarketingGoodsDto dto);
	
	//根据营销编号查询商品编号
	List<String> selectGoodsIdListByActiveId(Long activeId);
	
	//根据活动编号查询 每种规格中对应的 商品编号 及其最小的活动价
	List<MarketingGoods> selectMInNewHUmanPriceAndGoodsId(Long activeId);
	
	//根据活动类型查询不同编号的商品编号
	List<String> selctMinAndMaxPriceGoodsIdByDetailId(Long activeId );
	
	//跟新最小和最大价格
	Integer updateMinAndMaxPriceByGoodsId(List<MarketingGoods> entity);
	
	List<MarketingGoods> selectMarketingGoodsListNew();

}
