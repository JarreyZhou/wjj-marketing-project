package com.wjj.application.service.pennyRob;

import com.baomidou.mybatisplus.service.IService;
import com.wjj.application.dto.pennyRob.MkGoodsSpuDTO;
import com.wjj.application.entity.pennyRob.MkGoodsSku;
import com.wjj.application.entity.pennyRob.MkGoodsSpu;
import com.wjj.application.entity.pennyRob.MkGoodsSpuVO;
import com.wjj.application.entity.pennyRob.MkStandardExt;
import com.wjj.application.page.PageDTO;
import com.wjj.application.response.Response;
import com.wjj.application.util.LockException;

import java.util.List;

/**
 * <p>
 * 活动商品spu表 服务类
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */
public interface MkGoodsSpuService extends IService<MkGoodsSpu> {

	Response rush(Integer skuId, Integer addrId,String userId,String parentInviteCode,String orderRemarks,
			String nickname,String phone,String openId, String formId)  throws Exception;

	Response selectProductDetail(Integer activityId, Integer spuId);

	Response selectSku(Integer spuId);

	Response selectSkuStock(Integer spuId, String sku);
	/**
	 * 1分抢-添加活动商品
	 */
	Response insertActivityGoods(MkGoodsSpuDTO mkGoodsSpuDTO) throws Exception;

	/**
	 * 1分抢-活动商品列表
	 */
	PageDTO<MkGoodsSpuDTO> selectActivityGoodsList(MkGoodsSpuDTO mkGoodsSpuDTO);

	/**
	 * 1分抢-活动商品下架
	 */
	Response unshelveActivity(MkGoodsSpuDTO mkGoodsSpuDTO) throws LockException;

	/**
	 * 1分抢-活动商品上架
	 */
	Response shelveActivity(MkGoodsSpuDTO mkGoodsSpuDTO) throws LockException;

	/**
	 * 1分抢-编辑活动商品
	 */
	Response updateActivityGoods(MkGoodsSpuDTO mkGoodsSpuDTO) throws LockException;

	MkGoodsSpuDTO selectActivityGoodsDetail(MkGoodsSpuDTO mkGoodsSpuDTO);

	Response unshelveBatchActivity(List<MkGoodsSpuDTO> mkGoodsSpuDTOs) throws LockException;

	Response shelveBatchActivity(List<MkGoodsSpuDTO> mkGoodsSpuDTOs) throws LockException;

	Response stockBack(List<Integer> skuids);
}
