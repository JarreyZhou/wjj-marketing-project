package com.wjj.application.controller.pennyRob;

import com.wjj.application.common.FastJsonUtils;
import com.wjj.application.dto.pennyRob.MkActivityRobDTO;
import com.wjj.application.dto.pennyRob.MkGoodsSpuDTO;
import com.wjj.application.enums.BackCode;
import com.wjj.application.response.Response;
import com.wjj.application.service.pennyRob.MkActivityRobService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 * 活动参与帮抢者 前端控制器
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */
@Slf4j
@Controller
@RequestMapping("/application/mkActivityRob")
public class MkActivityRobController {
	@Autowired
	private MkActivityRobService mkActivityRobService;

	/**
	 * 活动帮抢人列表
	 */
	@ApiOperation(value = "1分抢-查看明细-活动帮抢人列表", tags = { "1分抢-查看明细-活动帮抢人列表" }, notes = "")
	@RequestMapping(value = "activityRobList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Response activityRobList(@RequestBody MkActivityRobDTO mkActivityRobDTO) {
		try {
			log.info("/activityRobList入参:" + FastJsonUtils.toJSONString(mkActivityRobDTO));
			if (mkActivityRobDTO == null) {
				return Response.returnCode(BackCode.RESPONSE_1001.getCode(),
						BackCode.RESPONSE_1001.getMsg());
			}
			Integer activityId = mkActivityRobDTO.getActivityId();//活动id
			Integer sponsorId = mkActivityRobDTO.getSponsorId();//发起人id
			if (activityId == null || sponsorId == null) {
				return Response.returnCode(BackCode.RESPONSE_1001.getCode(),
						BackCode.RESPONSE_1001.getMsg());
			}
//			EntityWrapper<MkActivityRob> wrapper = new EntityWrapper<>();
//			wrapper.eq("sponsor_id", sponsorId);
			List<MkActivityRobDTO> mkActivityRobDTOS = mkActivityRobService.selectActivityRobList(mkActivityRobDTO);
			log.info("/activityRobList出参:" + FastJsonUtils.toJSONString(mkActivityRobDTOS));
			return Response.ok(mkActivityRobDTOS);
		} catch (Exception e) {
			log.error("/activityRobList error" + e.getMessage(), e);
			return Response.fail();
		}
	}

	/**
     * 帮抢页面
	 */
    @ApiOperation(value = "1分抢-帮抢页面", tags = { "1分抢-帮抢页面" }, notes = "")
    @RequestMapping(value = "helpRob", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
	public Response helpRob(@RequestBody MkActivityRobDTO mkActivityRobDTO) {
        try {
			log.info("/helpRob入参:" + FastJsonUtils.toJSONString(mkActivityRobDTO));
			if (mkActivityRobDTO == null) {
				return Response.returnCode(BackCode.RESPONSE_1001.getCode(),
						BackCode.RESPONSE_1001.getMsg());
			}
			Response response = mkActivityRobService.helpRob(mkActivityRobDTO);
			log.info("/helpRob出参:" + FastJsonUtils.toJSONString(response));
			return response;
        } catch (Exception e) {
			log.error("/helpRob error" + e.getMessage(), e);
            return Response.fail();
        }
	}

	/**
     * 查看更多活动商品
	 */
    @ApiOperation(value = "1分抢-查看更多活动商品", tags = { "1分抢-查看更多活动商品" }, notes = "")
    @RequestMapping(value = "moreGoods", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response moreGoods(@RequestBody MkGoodsSpuDTO mkGoodsSpuDTO) {
        try {
			log.info("/moreGoods入参:" + FastJsonUtils.toJSONString(mkGoodsSpuDTO));
			if (mkGoodsSpuDTO == null) {
				return Response.returnCode(BackCode.RESPONSE_1001.getCode(),
						BackCode.RESPONSE_1001.getMsg());
			}
			Response response = mkActivityRobService.moreGoods(mkGoodsSpuDTO);
			log.info("/moreGoods出参:" + FastJsonUtils.toJSONString(response));
			return response;
        } catch (Exception e) {
			log.error("/moreGoods error" + e.getMessage(), e);
            return Response.fail();
        }
    }
}
