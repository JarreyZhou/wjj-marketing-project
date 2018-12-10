package com.wjj.application.controller.pennyRob;


import com.wjj.application.common.FastJsonUtils;
import com.wjj.application.dto.pennyRob.MkActivitySponsorDTO;
import com.wjj.application.dto.pennyRob.MkGenerateDTO;
import com.wjj.application.enums.BackCode;
import com.wjj.application.feign.GoodsMainInfoFeignClient;
import com.wjj.application.response.Response;
import com.wjj.application.security.AuthTokenDTO;
import com.wjj.application.service.pennyRob.MkActivitySponsorService;
import com.wjj.application.util.LockException;
import com.wjj.application.util.PageVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 活动发起表 前端控制器
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */
@Controller
@Slf4j
@RequestMapping("/application/mkActivitySponsor")
public class MkActivitySponsorController {

    @Autowired
    private MkActivitySponsorService mkActivitySponsorService;

    @Autowired
    private GoodsMainInfoFeignClient goodsMainInfoFeignClient;

    //邀请好友助力页面查询
    //add by long.zhou
    @ApiOperation(value = "邀请好友助力页面查询", tags = {"邀请好友助力页面查询"}, notes = "")
    @RequestMapping(value = "/invite/list", method = RequestMethod.POST)
    @ResponseBody
    public Response inviteList(@RequestBody MkGenerateDTO mkGenerateDTO) {
        try {
            log.info("/invite/list入参:"+ FastJsonUtils.toJSONString(mkGenerateDTO));
            return mkActivitySponsorService.inviteList(mkGenerateDTO.getSponsorId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.fail();
        }
    }



	@ApiOperation(value = "帮抢者帮抢注册", tags = { "帮抢者帮抢注册" }, notes = "")
	@PostMapping("/activitySponsor/rob")
	@ResponseBody
        public Response rob(@RequestBody MkActivitySponsorDTO mkActivitySponsorDTO) {
            try {
            	 log.info("/activitySponsor/rob入参:"+ FastJsonUtils.toJSONString(mkActivitySponsorDTO));
                if (mkActivitySponsorDTO == null) {
                    return Response.returnCode(BackCode.RESPONSE_1001.getCode(),
                            BackCode.RESPONSE_1001.getMsg());
                }
                Integer sponsorId =mkActivitySponsorDTO.getSponsorId();
                String phone =mkActivitySponsorDTO.getPhone();
                String openId =mkActivitySponsorDTO.getOpenId();
                String smsCode =mkActivitySponsorDTO.getSmsCode();
                String graphCode =mkActivitySponsorDTO.getGraphCode();
                String headPic =mkActivitySponsorDTO.getHeadPic();
                String nickname =mkActivitySponsorDTO.getNickname();
                String unid =mkActivitySponsorDTO.getUnionid();

        		 String province=mkActivitySponsorDTO.getProvince();
        		 String city=mkActivitySponsorDTO.getCity();
        		 String area=mkActivitySponsorDTO.getArea();
        		 String sex=mkActivitySponsorDTO.getSex();
        		 Long uniqueCode = mkActivitySponsorDTO.getUniqueCode();
        		 
                if(sponsorId==null|| StringUtils.isBlank(phone)|| StringUtils.isBlank(openId)
                        || StringUtils.isBlank(smsCode)|| StringUtils.isBlank(headPic)|| StringUtils.isBlank(nickname)
                        || StringUtils.isBlank(unid)
                        || StringUtils.isBlank(sex)
                        || uniqueCode==null){
                    return Response.returnCode(BackCode.RESPONSE_1001.getCode(),
                            BackCode.RESPONSE_1001.getMsg());
                }
                AuthTokenDTO authTokenDTO=mkActivitySponsorDTO.getAuthTokenDTO();
                Response robResponse = mkActivitySponsorService.rob(sponsorId,  phone,openId,smsCode,graphCode,headPic,nickname,unid
                		,province,city,area,sex,authTokenDTO,uniqueCode);
           	 	log.info("/activitySponsor/rob出参:"+ FastJsonUtils.toJSONString(robResponse));
                return robResponse;
            } catch (LockException e) {
                log.error("/activitySponsor/rob error"+e.getMessage(),e);
                return Response.returnCode(BackCode.LOCK_FAIL.getCode(),
                		BackCode.LOCK_FAIL.getMsg());
            } catch (Exception e) {
                log.error("/activitySponsor/rob error"+e.getMessage(),e);
                return Response.returnCode(BackCode.FAIL.getCode(),
                        e.toString());
            }
        }


    //我的活动列表
    //0表示全部，1活动抢购中，2活动失败，3活动成功
    //add by long.zhou
    @ApiOperation(value = "我的活动列表", tags = {"我的活动列表"}, notes = "")
    @RequestMapping(value = "/myActivity/list", method = RequestMethod.POST)
    @ResponseBody
    public Response myActivityList(@RequestBody MkGenerateDTO mkGenerateDTO) {
        try {
            log.info("/myActivity/list入参:"+ FastJsonUtils.toJSONString(mkGenerateDTO));
            return mkActivitySponsorService.myActivityList(mkGenerateDTO.getUserId(), mkGenerateDTO.getPageNo(), mkGenerateDTO.getPageSize(), mkGenerateDTO.getStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.fail();
        }
    }

    /**
     * 1分抢-查看明细-活动参与明细
     */
    @ApiOperation(value = "1分抢-查看明细-活动参与明细", tags = { "1分抢-查看明细-活动参与明细" }, notes = "")
    @RequestMapping(value = "selectActivitySponsorList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response selectActivitySponsorList(@RequestBody MkActivitySponsorDTO mkActivitySponsorDTO) {
        try {
            if (mkActivitySponsorDTO == null) {
                return Response.returnCode(BackCode.RESPONSE_1001.getCode(),
                        BackCode.RESPONSE_1001.getMsg());
            }
            mkActivitySponsorDTO.setLimit((mkActivitySponsorDTO.getCurrPage() - 1) * mkActivitySponsorDTO.getPageSize());
            return Response.ok(mkActivitySponsorService.selectActivitySponsorList(mkActivitySponsorDTO));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.fail();
        }
    }


    /**
     * 订单记录-列表接口
     */
    //add by long.zhou
    @ApiOperation(value = "订单记录-列表接口", tags = {"订单记录-列表接口"}, notes = "")
    @RequestMapping(value = "/orderList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response orderList(@RequestBody PageVO pageVO) {
        try {
            log.info("/orderList入参:"+ FastJsonUtils.toJSONString(pageVO));
            return mkActivitySponsorService.orderList(pageVO);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.fail();
        }
    }


    /**
     * 订单记录-查询详情
     */
    // add by long.zhou
    @ApiOperation(value = "订单记录-查询详情", tags = {"订单记录-查询详情"}, notes = "")
    @RequestMapping(value = "/orderDetail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response orderDetail(@RequestBody MkActivitySponsorDTO mkActivitySponsorDTO) {
        try {
            log.info("/orderDetail入参:"+ FastJsonUtils.toJSONString(mkActivitySponsorDTO));
            return mkActivitySponsorService.orderDetail(mkActivitySponsorDTO);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.fail();
        }
    }

    /**
     * 我的活动删除
     */
    @ApiOperation(value = "我的活动删除", tags = {"我的活动删除"}, notes = "")
    @RequestMapping(value = "/myActivityDelete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response myActivityDelete(@RequestBody MkActivitySponsorDTO mkActivitySponsorDTO) {
        try {
            log.info("/myActivityDelete入参:"+ FastJsonUtils.toJSONString(mkActivitySponsorDTO));
            mkActivitySponsorService.myActivityDelete(mkActivitySponsorDTO);
            return Response.ok();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.fail();
        }
    }


    /**
     * test
     */
    @RequestMapping(value = "/test111", method = RequestMethod.POST)
    @ResponseBody
    public Response test111() {
        try {
            mkActivitySponsorService.test111();
            return Response.ok();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.fail();
        }
    }



}
