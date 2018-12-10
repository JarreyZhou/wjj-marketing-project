package com.wjj.application.feign.pennyRob;


import com.wjj.application.dto.pennyRob.MkActivitySponsorDTO;
import com.wjj.application.dto.pennyRob.MkGenerateDTO;
import com.wjj.application.response.Response;
import com.wjj.application.vo.PageVO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 * 活动发起表 前端控制器
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */
@FeignClient("wjj-marketing")
public interface MkActivitySponsorFeignClient {

    @PostMapping(value = "/application/mkActivitySponsor/activitySponsor/rob")
    Response rob(@RequestBody MkActivitySponsorDTO mkActivitySponsorDTO);


    //add by long.zhou
    //邀请好友助力页面查询
    @PostMapping(value = "/application/mkActivitySponsor/invite/list")
    Response inviteList(@RequestBody MkGenerateDTO mkGenerateDTO);

    //add by long.zhou
    //我的活动列表
    //0表示全部，1活动抢购中，2活动失败，3活动成功
    @PostMapping(value = "/application/mkActivitySponsor/myActivity/list")
    Response myActivityList(@RequestBody MkGenerateDTO mkGenerateDTO);

    /**
     * 1分抢-查看明细-活动参与明细
     */
    @PostMapping(value = "/application/mkActivitySponsor/selectActivitySponsorList")
    Response selectActivitySponsorList(@RequestBody MkActivitySponsorDTO mkActivitySponsorDTO);


    /**
     * 订单记录-列表接口
     */
    @PostMapping(value = "/application/mkActivitySponsor/orderList")
    Response orderList(@RequestBody PageVO pageVO);


    /**
     * 订单记录-查询详情
     */
    @PostMapping(value = "/application/mkActivitySponsor/orderDetail")
    Response orderDetail(@RequestBody MkActivitySponsorDTO mkActivitySponsorDTO);

    /**
     * 我的活动删除
     */
    @PostMapping(value = "/application/mkActivitySponsor/myActivityDelete")
    Response myActivityDelete(@RequestBody MkActivitySponsorDTO mkActivitySponsorDTO);






}
