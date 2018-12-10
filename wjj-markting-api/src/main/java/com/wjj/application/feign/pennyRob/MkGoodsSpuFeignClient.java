package com.wjj.application.feign.pennyRob;

import com.wjj.application.dto.pennyRob.MkGenerateDTO;
import com.wjj.application.dto.pennyRob.MkGoodsSpuDTO;
import com.wjj.application.response.AccountReturnCode;
import com.wjj.application.response.Response;

import io.swagger.annotations.ApiOperation;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 * 活动商品spu表 前端控制器
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */

@FeignClient("wjj-marketing")
public interface MkGoodsSpuFeignClient {


	// 商品详情页查询
	@PostMapping(value = "/application/mkGoodsSpu/select/product/detail")
	Response selectProductDetail(@RequestBody MkGenerateDTO mkGenerateDTO);


    //商品规格查询
    @PostMapping(value = "/application/mkGoodsSpu/select/product/sku")
    Response selectSku(@RequestBody MkGenerateDTO mkGenerateDTO);


    //根据规格查询库存
    @PostMapping(value = "/application/mkGoodsSpu/select/product/stock")
    Response selectSkuStock(@RequestBody MkGenerateDTO mkGenerateDTO);

	@PostMapping("/application/mkGoodsSpu/mkGoodsSpu/rush")
	Response rush(@RequestBody MkGoodsSpuDTO mkGoodsSpuDTO);

	/**
	 * 1分抢-添加活动商品
	 */
	@PostMapping(value = "/application/mkGoodsSpu/insertActivityGoods")
	Response insertActivityGoods(@RequestBody MkGoodsSpuDTO mkGoodsSpuDTO);

	/**
	 * 1分抢-活动商品列表
	 */
	@PostMapping(value = "/application/mkGoodsSpu/selectActivityGoodsList")
	Response selectActivityGoodsList(@RequestBody MkGoodsSpuDTO mkGoodsSpuDTO);

	/**
	 * 1分抢-活动商品批量下架
	 */
	@PostMapping(value = "/application/mkGoodsSpu/unshelveBatchActivityGoods")
	Response unshelveBatchActivityGoods(@RequestBody List<MkGoodsSpuDTO> mkGoodsSpuDTOs);

	/**
	 * 1分抢-活动商品下架
	 */
	@PostMapping(value = "/application/mkGoodsSpu/unshelveActivityGoods")
	Response unshelveActivityGoods(@RequestBody MkGoodsSpuDTO mkGoodsSpuDTO);

	/**
	 * 1分抢-活动商品批量上架
	 */
	@PostMapping(value = "/application/mkGoodsSpu/shelveBatchActivityGoods")
	Response shelveBatchActivityGoods(@RequestBody List<MkGoodsSpuDTO> mkGoodsSpuDTOs);

	/**
	 * 1分抢-活动商品上架
	 */
	@PostMapping(value = "/application/mkGoodsSpu/shelveActivityGoods")
	Response shelveActivityGoods(@RequestBody MkGoodsSpuDTO mkGoodsSpuDTO);

	/**
	 * 1分抢-编辑活动商品
	 */
	@PostMapping(value = "/application/mkGoodsSpu/updateActivityGoods")
	Response updateActivityGoods(@RequestBody MkGoodsSpuDTO mkGoodsSpuDTO);

	/**
	 * 1分抢-活动商品列表-查看明细
	 */
	@PostMapping(value = "/application/mkGoodsSpu/selectActivityGoodsDetail")
	Response selectActivityGoodsDetail(@RequestBody MkGoodsSpuDTO mkGoodsSpuDTO);

	/**
	 * 商品库存回滚
	 */
	@PostMapping(value = "/application/mkGoodsSpu/stockBack")
	Response stockBack(@RequestBody MkGoodsSpuDTO mkGoodsSpuDTO);
}
