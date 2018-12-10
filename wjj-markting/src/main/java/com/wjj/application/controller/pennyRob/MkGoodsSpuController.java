package com.wjj.application.controller.pennyRob;

import com.wjj.application.common.FastJsonUtils;
import com.wjj.application.dto.pennyRob.MkGenerateDTO;
import com.wjj.application.dto.pennyRob.MkGoodsSpuDTO;
import com.wjj.application.enums.BackCode;
import com.wjj.application.page.PageDTO;
import com.wjj.application.response.AccountReturnCode;
import com.wjj.application.response.Response;
import com.wjj.application.response.ReturnCode;
import com.wjj.application.service.pennyRob.MkGoodsSpuService;
import com.wjj.application.util.LockException;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 活动商品spu表 前端控制器
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */
@Controller
@Slf4j
@RequestMapping("/application/mkGoodsSpu")
public class MkGoodsSpuController {

	@Autowired
	private MkGoodsSpuService mkGoodsSpuService;

	// 商品详情页查询
    // add by long.zhou
	@ApiOperation(value = "商品详情页查询", tags = { "商品详情页查询" }, notes = "")
	@RequestMapping(value = "/select/product/detail", method = RequestMethod.POST)
	@ResponseBody
	public Response selectProductDetail(@RequestBody MkGenerateDTO mkGenerateDTO) {
		try {
			log.info("/select/product/detail入参:"+ FastJsonUtils.toJSONString(mkGenerateDTO));
			return mkGoodsSpuService.selectProductDetail(mkGenerateDTO.getActivityId(), mkGenerateDTO.getSpuId());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return Response.fail();
		}
	}


    //商品规格查询
    // add by long.zhou
    @ApiOperation(value = "商品规格查询",tags = {"商品规格查询"},notes = "")
    @RequestMapping(value = "/select/product/sku", method=RequestMethod.POST)
    @ResponseBody
    public Response selectSku(@RequestBody MkGenerateDTO mkGenerateDTO) {
    	try {
			log.info("/select/product/sku入参:"+ FastJsonUtils.toJSONString(mkGenerateDTO));
			return mkGoodsSpuService.selectSku(mkGenerateDTO.getSpuId());
    	} catch (Exception e) {
			log.error(e.getMessage(),e);
    		return Response.fail();
    	}
    }


    //根据规格查询库存
    // add by long.zhou
    @ApiOperation(value = "根据规格查询库存",tags = {"根据规格查询库存"},notes = "")
    @RequestMapping(value = "/select/product/stock", method=RequestMethod.POST)
    @ResponseBody
    public Response selectSkuStock(@RequestBody MkGenerateDTO mkGenerateDTO) {
    	try {
			log.info("/select/product/stock入参:"+ FastJsonUtils.toJSONString(mkGenerateDTO));
			return mkGoodsSpuService.selectSkuStock(mkGenerateDTO.getSpuId(), mkGenerateDTO.getSku());
    	} catch (Exception e) {
			log.error(e.getMessage(),e);
    		return Response.fail();
    	}
    }



    @ApiOperation(value = "立即抢购发起活动提交订单信息",tags = {"立即抢购发起活动提交订单信息"},notes = "")
	@PostMapping("/mkGoodsSpu/rush")
    @ResponseBody
	public Response rush(@RequestBody MkGoodsSpuDTO mkGoodsSpuDTO) {
    	try {
    		
    		log.info("/mkGoodsSpu/rush入参:"+FastJsonUtils.toJSONString(mkGoodsSpuDTO));
    		 Integer skuId=mkGoodsSpuDTO.getSkuId();
    		 Integer addrId=mkGoodsSpuDTO.getAddrId(); 
    		 String userId=mkGoodsSpuDTO.getUserId();
    		 String parentInviteCode=mkGoodsSpuDTO.getParentInviteCode();
    		 String orderRemarks =mkGoodsSpuDTO.getOrderRemarks();
    		 String nickname=mkGoodsSpuDTO.getNickname();
    		 String phone= mkGoodsSpuDTO.getPhone();
    		 String openId=mkGoodsSpuDTO.getOpenId();
    		 String formId=mkGoodsSpuDTO.getFormId();
    		if(skuId==null||addrId==null||StringUtils.isBlank(userId)||StringUtils.isBlank(parentInviteCode)
    				||StringUtils.isBlank(nickname)||StringUtils.isBlank(phone)
    				||StringUtils.isBlank(openId)||StringUtils.isBlank(formId)){
    			return Response.returnCode(ReturnCode.PARAM_INVALID.getCode(),ReturnCode.PARAM_INVALID.getMsg());
    		}
    		Response rushResponse = mkGoodsSpuService.rush(skuId,addrId,userId,parentInviteCode,orderRemarks,
    				nickname,phone,openId,formId);
    		log.info("/mkGoodsSpu/rush出参:"+FastJsonUtils.toJSONString(rushResponse));
    		return rushResponse;
		} catch (LockException e) {
            log.error("/mkGoodsSpu/rush error"+e.getMessage(),e);
            return Response.returnCode(BackCode.LOCK_FAIL.getCode(),
            		BackCode.LOCK_FAIL.getMsg());
        } catch (Exception e) {
			log.error("/mkGoodsSpu/rush error"+e.getMessage(), e);
			return Response.fail();
		}
	}


	/**
	 * 1分抢-添加活动商品
	 */
	@ApiOperation(value = "1分抢-添加活动商品", tags = { "1分抢-添加活动商品" }, notes = "")
	@RequestMapping(value = "insertActivityGoods", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Response insertActivityGoods(@RequestBody MkGoodsSpuDTO mkGoodsSpuDTO) {
		try {
			log.info("/insertActivityGoods入参:" + FastJsonUtils.toJSONString(mkGoodsSpuDTO));
			if (mkGoodsSpuDTO == null) {
				return Response.returnCode(AccountReturnCode.RESPONSE_1001.getCode(),
						AccountReturnCode.RESPONSE_1001.getMsg());
			}
			Response response = mkGoodsSpuService.insertActivityGoods(mkGoodsSpuDTO);
			log.info("/insertActivityGoods出参:" + FastJsonUtils.toJSONString(response));
			return response;
		} catch (Exception e) {
			log.error("/insertActivityGoods error" + e.getMessage(), e);
			return Response.returnCode(BackCode.FAIL.getCode(),
					e.toString());
		}
	}

	/**
	 * 1分抢-活动商品列表
	 */
	@ApiOperation(value = "1分抢-活动商品列表", tags = { "1分抢-活动商品列表" }, notes = "")
	@RequestMapping(value = "selectActivityGoodsList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Response selectActivityGoodsList(@RequestBody MkGoodsSpuDTO mkGoodsSpuDTO) {
		try {
			log.info("/selectActivityGoodsList入参:" + FastJsonUtils.toJSONString(mkGoodsSpuDTO));
			if (mkGoodsSpuDTO == null) {
				return Response.returnCode(AccountReturnCode.RESPONSE_1001.getCode(),
						AccountReturnCode.RESPONSE_1001.getMsg());
			}
			mkGoodsSpuDTO.setLimit((mkGoodsSpuDTO.getCurrPage() - 1) * mkGoodsSpuDTO.getPageSize());
			PageDTO<MkGoodsSpuDTO> mkGoodsSpuDTOPageDTO = mkGoodsSpuService.selectActivityGoodsList(mkGoodsSpuDTO);
			log.info("/selectActivityGoodsList出参:" + FastJsonUtils.toJSONString(mkGoodsSpuDTOPageDTO));
			return Response.ok(mkGoodsSpuDTOPageDTO);
		} catch (Exception e) {
			log.error("/selectActivityGoodsList error" + e.getMessage(), e);
			return Response.fail();
		}
	}

	/**
	 * 1分抢-活动商品批量下架
	 */
	@ApiOperation(value = "1分抢-活动商品批量下架", tags = {"1分抢-活动商品批量下架"}, notes = "")
	@RequestMapping(value = "unshelveBatchActivityGoods", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Response unshelveBatchActivityGoods(@RequestBody List<MkGoodsSpuDTO> mkGoodsSpuDTOs) {
		try {
			log.info("/unshelveBatchActivityGoods入参:" + FastJsonUtils.toJSONString(mkGoodsSpuDTOs));
			if (mkGoodsSpuDTOs == null) {
				return Response.returnCode(AccountReturnCode.RESPONSE_1001.getCode(),
						AccountReturnCode.RESPONSE_1001.getMsg());
			}
			Response response = mkGoodsSpuService.unshelveBatchActivity(mkGoodsSpuDTOs);
			log.info("/unshelveBatchActivityGoods出参:" + FastJsonUtils.toJSONString(response));
			return response;
		} catch (LockException e) {
			log.error("/unshelveBatchActivityGoods error" + e.getMessage(), e);
			return Response.returnCode(BackCode.LOCK_FAIL.getCode(),
					BackCode.LOCK_FAIL.getMsg());
		} catch (Exception e) {
			log.error("/unshelveBatchActivityGoods error" + e.getMessage(), e);
			return Response.returnCode(BackCode.FAIL.getCode(),
					e.toString());
		}
	}

	/**
	 * 1分抢-活动商品下架
	 */
	@ApiOperation(value = "1分抢-活动商品下架", tags = { "1分抢-活动商品下架" }, notes = "")
	@RequestMapping(value = "unshelveActivityGoods", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Response unshelveActivityGoods(@RequestBody MkGoodsSpuDTO mkGoodsSpuDTO) {
		try {
			log.info("/unshelveActivityGoods入参:" + FastJsonUtils.toJSONString(mkGoodsSpuDTO));
			if (mkGoodsSpuDTO == null) {
				return Response.returnCode(AccountReturnCode.RESPONSE_1001.getCode(),
						AccountReturnCode.RESPONSE_1001.getMsg());
			}
			Response response = mkGoodsSpuService.unshelveActivity(mkGoodsSpuDTO);
			log.info("/unshelveActivityGoods出参:" + FastJsonUtils.toJSONString(response));
			return response;
		} catch (LockException e) {
			log.error("/unshelveActivityGoods error" + e.getMessage(), e);
			return Response.returnCode(BackCode.LOCK_FAIL.getCode(),
					BackCode.LOCK_FAIL.getMsg());
		} catch (Exception e) {
			log.error("/unshelveActivityGoods error" + e.getMessage(), e);
			return Response.returnCode(BackCode.FAIL.getCode(),
					e.toString());
		}
	}

	/**
	 * 1分抢-活动商品批量上架
	 */
	@ApiOperation(value = "1分抢-活动商品批量上架", tags = {"1分抢-活动商品批量上架"}, notes = "")
	@RequestMapping(value = "shelveBatchActivityGoods", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Response shelveBatchActivityGoods(@RequestBody List<MkGoodsSpuDTO> mkGoodsSpuDTOs) {
		try {
			log.info("/shelveBatchActivityGoods入参:" + FastJsonUtils.toJSONString(mkGoodsSpuDTOs));
			if (mkGoodsSpuDTOs == null) {
				return Response.returnCode(AccountReturnCode.RESPONSE_1001.getCode(),
						AccountReturnCode.RESPONSE_1001.getMsg());
			}
			Response response = mkGoodsSpuService.shelveBatchActivity(mkGoodsSpuDTOs);
			log.info("/shelveBatchActivityGoods出参:" + FastJsonUtils.toJSONString(response));
			return response;
		} catch (LockException e) {
			log.error("/shelveBatchActivityGoods error" + e.getMessage(), e);
			return Response.returnCode(BackCode.LOCK_FAIL.getCode(),
					BackCode.LOCK_FAIL.getMsg());
		} catch (Exception e) {
			log.error("/shelveBatchActivityGoods error" + e.getMessage(), e);
			return Response.returnCode(BackCode.FAIL.getCode(),
					e.toString());
		}
	}
	/**
	 * 1分抢-活动商品上架
	 */
	@ApiOperation(value = "1分抢-活动商品上架", tags = { "1分抢-活动商品上架" }, notes = "")
	@RequestMapping(value = "shelveActivityGoods", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Response shelveActivityGoods(@RequestBody MkGoodsSpuDTO mkGoodsSpuDTO) {
		try {
			log.info("/shelveActivityGoods入参:" + FastJsonUtils.toJSONString(mkGoodsSpuDTO));
			if (mkGoodsSpuDTO == null) {
				return Response.returnCode(AccountReturnCode.RESPONSE_1001.getCode(),
						AccountReturnCode.RESPONSE_1001.getMsg());
			}
			Response response = mkGoodsSpuService.shelveActivity(mkGoodsSpuDTO);
			log.info("/shelveActivityGoods出参:" + FastJsonUtils.toJSONString(response));
			return response;
		} catch (LockException e) {
			log.error("/shelveActivityGoods error" + e.getMessage(), e);
			return Response.returnCode(BackCode.LOCK_FAIL.getCode(),
					BackCode.LOCK_FAIL.getMsg());
		} catch (Exception e) {
			log.error("/shelveActivityGoods error" + e.getMessage(), e);
			return Response.returnCode(BackCode.FAIL.getCode(),
					e.toString());
		}
	}

	/**
	 * 1分抢-编辑活动商品
	 */
	@ApiOperation(value = "1分抢-编辑活动商品", tags = { "1分抢-编辑活动商品" }, notes = "")
	@RequestMapping(value = "updateActivityGoods", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Response updateActivityGoods(@RequestBody MkGoodsSpuDTO mkGoodsSpuDTO) {
		try {
			log.info("/updateActivityGoods入参:" + FastJsonUtils.toJSONString(mkGoodsSpuDTO));
			if (mkGoodsSpuDTO == null) {
				return Response.returnCode(AccountReturnCode.RESPONSE_1001.getCode(),
						AccountReturnCode.RESPONSE_1001.getMsg());
			}
			Response response = mkGoodsSpuService.updateActivityGoods(mkGoodsSpuDTO);
			log.info("/updateActivityGoods出参:" + FastJsonUtils.toJSONString(response));
			return response;
		} catch (LockException e) {
			log.error("/updateActivityGoods error" + e.getMessage(), e);
			return Response.returnCode(BackCode.LOCK_FAIL.getCode(),
					BackCode.LOCK_FAIL.getMsg());
		} catch (Exception e) {
			log.error("/updateActivityGoods error" + e.getMessage(), e);
			return Response.returnCode(BackCode.FAIL.getCode(),
					e.toString());
		}
	}

	/**
	 * 1分抢-活动商品列表-查看明细
	 */
	@ApiOperation(value = "1分抢-活动商品列表-查看明细", tags = { "1分抢-活动商品列表-查看明细" }, notes = "")
	@RequestMapping(value = "selectActivityGoodsDetail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Response selectActivityGoodsDetail(@RequestBody MkGoodsSpuDTO mkGoodsSpuDTO) {
		try {
			log.info("/selectActivityGoodsDetail入参:" + FastJsonUtils.toJSONString(mkGoodsSpuDTO));
			if (mkGoodsSpuDTO == null) {
				return Response.returnCode(AccountReturnCode.RESPONSE_1001.getCode(),
						AccountReturnCode.RESPONSE_1001.getMsg());
			}
			MkGoodsSpuDTO mkGoodsSpuDTO1 = mkGoodsSpuService.selectActivityGoodsDetail(mkGoodsSpuDTO);
			log.info("/selectActivityGoodsDetail出参:" + FastJsonUtils.toJSONString(mkGoodsSpuDTO1));
			return Response.ok(mkGoodsSpuDTO1);
		} catch (Exception e) {
			log.error("/selectActivityGoodsDetail error" + e.getMessage(), e);
			return Response.fail();
		}
	}
	
	
	/**
	 * 商品库存回滚
	 */
	@ApiOperation(value = "商品库存回滚", tags = { "商品库存回滚" }, notes = "")
	@RequestMapping(value = "stockBack", method = RequestMethod.POST)
	@ResponseBody
	public Response stockBack(@RequestBody MkGoodsSpuDTO mkGoodsSpuDTO) {
		try {
			 log.info("/application/mkGoodsSpu/stockBack入参:"+ FastJsonUtils.toJSONString(mkGoodsSpuDTO));
			if (mkGoodsSpuDTO == null) {
				return Response.returnCode(AccountReturnCode.RESPONSE_1001.getCode(),
						AccountReturnCode.RESPONSE_1001.getMsg());
			}
			List<Integer> skuids=mkGoodsSpuDTO.getSkuIds();
			if (skuids == null||skuids.isEmpty()) {
				return Response.returnCode(AccountReturnCode.RESPONSE_1001.getCode(),
						AccountReturnCode.RESPONSE_1001.getMsg());
			}
			return mkGoodsSpuService.stockBack(skuids);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.fail();
		}
	}
}
