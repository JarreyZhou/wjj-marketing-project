package com.wjj.application.mapper.pennyRob;

import com.wjj.application.entity.pennyRob.MkGoodsSku;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 活动商品sku表 Mapper 接口
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */
public interface MkGoodsSkuMapper extends BaseMapper<MkGoodsSku> {

	MkGoodsSku selectSkuByGoodsSkuId(Integer skuid);

}
