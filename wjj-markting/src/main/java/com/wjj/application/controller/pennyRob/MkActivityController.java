package com.wjj.application.controller.pennyRob;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wjj.application.common.FastJsonUtils;
import com.wjj.application.dto.pennyRob.MkActivityDTO;
import com.wjj.application.entity.pennyRob.MkActivity;
import com.wjj.application.enums.BackCode;
import com.wjj.application.page.PageDTO;
import com.wjj.application.response.Response;
import com.wjj.application.response.ReturnCode;
import com.wjj.application.service.pennyRob.MkActivityService;
import com.wjj.application.service.pennyRob.MkWxPushService;
import com.wjj.application.util.LockException;
import com.wjj.application.vo.PageVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 活动表 前端控制器
 * </p>
 *
 * @author long.zhou
 * @since 2018-10-30
 */
@Controller
@Slf4j
@RequestMapping("/application/mkActivity")
public class MkActivityController {

	@Autowired
	private MkActivityService mkActivityService;


	// 一分抢活动宣传图查询
	//add by long.zhou
	@ApiOperation(value = "一分抢活动宣传图查询", tags = { "一分抢活动宣传图查询" }, notes = "")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public Response list() {
		try {
			List<MkActivity> list = mkActivityService.list();
			// 判断有正在进行的用正在进行的,若无用即将开始的
			// 第一条要么是正在进行的,要么是即将进行的
            if(list == null || list.size() == 0){
                return Response.returnCode(ReturnCode.SUCCESS.getCode(), "没有活动");
            }
			log.info("活动宣传图出参list:"+ FastJsonUtils.toJSONString(list));
			return Response.ok(list.get(0));
		} catch (Exception e) {
			log.error("活动宣传图报错信息" + e.getMessage(),e);
			return Response.fail();
		}
	}

	// 正在疯抢列表
	//add by long.zhou
	@ApiOperation(value = "正在疯抢列表", tags = { "正在疯抢列表" }, notes = "")
	@RequestMapping(value = "/snatch/list", method = RequestMethod.POST)
	@ResponseBody
	public Response snatchList(@RequestBody PageVO pageVO) {
		try {
			log.info("正在疯抢列表/snatch/list入参:"+ FastJsonUtils.toJSONString(pageVO));
            Response response = mkActivityService.snatchList(pageVO);
            log.info("正在疯抢列表/snatch/list出参:"+ FastJsonUtils.toJSONString(response));
            return response;
		} catch (Exception e) {
			log.error("正在疯抢列表报错信息" + e.getMessage(),e);
			return Response.fail();
		}
	}

	// 即将开始列表
	//add by long.zhou
	@ApiOperation(value = "即将开始列表", tags = { "即将开始列表" }, notes = "")
	@RequestMapping(value = "/coming/list", method = RequestMethod.POST)
	@ResponseBody
	public Response comingList(@RequestBody PageVO pageVO) {
		try {
            log.info("/coming/list入参:"+ FastJsonUtils.toJSONString(pageVO));
			return mkActivityService.comingList(pageVO);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return Response.fail();
		}
	}

	/**
	 * 1分抢-添加活动
	 */
	@ApiOperation(value = "1分抢-添加活动", tags = { "1分抢-添加活动" }, notes = "")
	@RequestMapping(value = "insertActivity", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Response insertActivity(@RequestBody MkActivity mkActivity) {
		try {
			log.info("/insertActivity入参:" + FastJsonUtils.toJSONString(mkActivity));
			if (mkActivity == null) {
				return Response.returnCode(BackCode.RESPONSE_1001.getCode(),
						BackCode.RESPONSE_1001.getMsg());
			}
			Response response = mkActivityService.insertActivity(mkActivity);
			log.info("/insertActivity出参:" + FastJsonUtils.toJSONString(response));
			return response;
		} catch (Exception e) {
			log.error("/insertActivity error" + e.getMessage(), e);
			return Response.returnCode(BackCode.FAIL.getCode(),
					e.toString());
		}
	}

	/**
	 * 1分抢-活动列表
	 */
	@ApiOperation(value = "1分抢-活动列表", tags = { "1分抢-活动列表" }, notes = "")
	@RequestMapping(value = "selectActivityList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Response selectActivityList(@RequestBody MkActivityDTO mkActivityDTO) {
		try {
			log.info("/selectActivityList入参:" + FastJsonUtils.toJSONString(mkActivityDTO));
			if (mkActivityDTO == null) {
				return Response.returnCode(BackCode.RESPONSE_1001.getCode(),
						BackCode.RESPONSE_1001.getMsg());
			}
			// mkActivityDTO.setPageSize(20);// 列表20个一分页
			mkActivityDTO.setLimit((mkActivityDTO.getCurrPage() - 1) * mkActivityDTO.getPageSize());
			PageDTO<MkActivity> mkActivityPageDTO = mkActivityService.selectActivityList(mkActivityDTO);
			log.info("/selectActivityList出参:" + FastJsonUtils.toJSONString(mkActivityPageDTO));
			return Response.ok(mkActivityPageDTO);
		} catch (Exception e) {
			log.error("/selectActivityList error" + e.getMessage(), e);
			return Response.fail();
		}
	}

	/**
	 * 1分抢-查看活动
	 */
	@ApiOperation(value = "1分抢-查看活动", tags = { "1分抢-查看活动" }, notes = "")
	@RequestMapping(value = "selectActivity", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Response selectActivity(@RequestBody MkActivity mkActivity) {
		try {
			log.info("/selectActivity入参:" + FastJsonUtils.toJSONString(mkActivity));
			if (mkActivity == null) {
				return Response.returnCode(BackCode.RESPONSE_1001.getCode(),
						BackCode.RESPONSE_1001.getMsg());
			}
			Response response = mkActivityService.selectActivity(mkActivity);
			log.info("/selectActivity出参:" + FastJsonUtils.toJSONString(response));
			return response;
		} catch (Exception e) {
			log.error("/selectActivity error" + e.getMessage(), e);
			return Response.fail();
		}
	}

	/**
	 * 1分抢-编辑活动
	 */
	@ApiOperation(value = "1分抢-编辑活动", tags = { "1分抢-编辑活动" }, notes = "")
	@RequestMapping(value = "updateActivity", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Response updateActivity(@RequestBody MkActivity mkActivity) {
		try {
			log.info("/updateActivity入参:" + FastJsonUtils.toJSONString(mkActivity));
			if (mkActivity == null) {
				return Response.returnCode(BackCode.RESPONSE_1001.getCode(),
						BackCode.RESPONSE_1001.getMsg());
			}
			Response response = mkActivityService.updateActivity(mkActivity);
			log.info("/updateActivity出参:" + FastJsonUtils.toJSONString(response));
			return response;
		} catch (LockException e) {
			log.error("/updateActivity error" + e.getMessage(), e);
			return Response.returnCode(BackCode.LOCK_FAIL.getCode(),
					BackCode.LOCK_FAIL.getMsg());
		} catch (Exception e) {
			log.error("/updateActivity error" + e.getMessage(), e);
			return Response.returnCode(BackCode.FAIL.getCode(),
					e.toString());
		}
	}
	/**
	 * 1分抢-编辑活动OA审批单号
	 */
	@ApiOperation(value = "1分抢-编辑活动OA审批单号", tags = { "1分抢-编辑活动OA审批单号" }, notes = "")
	@RequestMapping(value = "updateActivityOA", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Response updateActivityOA(@RequestBody MkActivity mkActivity) {
		try {
			log.info("/updateActivityOA入参:" + FastJsonUtils.toJSONString(mkActivity));
			if (mkActivity == null) {
				return Response.returnCode(BackCode.RESPONSE_1001.getCode(),
						BackCode.RESPONSE_1001.getMsg());
			}
			Response response = mkActivityService.updateActivityOA(mkActivity);
			log.info("/updateActivityOA出参:" + FastJsonUtils.toJSONString(response));
			return response;
		} catch (LockException e) {
			log.error("/updateActivityOA error" + e.getMessage(), e);
			return Response.returnCode(BackCode.LOCK_FAIL.getCode(),
					BackCode.LOCK_FAIL.getMsg());
		} catch (Exception e) {
			log.error("/updateActivityOA error" + e.getMessage(), e);
			return Response.returnCode(BackCode.FAIL.getCode(),
					e.toString());
		}
	}

	/**
	 * 1分抢-下架活动
	 */
	@ApiOperation(value = "1分抢-下架活动", tags = { "1分抢-下架活动" }, notes = "")
	@RequestMapping(value = "unshelveActivity", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Response unshelveActivity(@RequestBody MkActivity mkActivity) {
		try {
			log.info("/unshelveActivity入参:" + FastJsonUtils.toJSONString(mkActivity));
			if (mkActivity == null) {
				return Response.returnCode(BackCode.RESPONSE_1001.getCode(),
						BackCode.RESPONSE_1001.getMsg());
			}
			Response response = mkActivityService.unshelveActivity(mkActivity);
			log.info("/unshelveActivity出参:" + FastJsonUtils.toJSONString(response));
			return response;
		} catch (LockException e) {
			log.error("/unshelveActivity error" + e.getMessage(), e);
			return Response.returnCode(BackCode.LOCK_FAIL.getCode(),
					BackCode.LOCK_FAIL.getMsg());
		} catch (Exception e) {
			log.error("/unshelveActivity error" + e.getMessage(), e);
			return Response.returnCode(BackCode.FAIL.getCode(),
					e.toString());
		}
	}

	/**
	 * 1分抢-上架活动
	 */
	@ApiOperation(value = "1分抢-上架活动", tags = { "1分抢-上架活动" }, notes = "")
	@RequestMapping(value = "shelveActivity", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Response shelveActivity(@RequestBody MkActivity mkActivity) {
		try {
			log.info("/shelveActivity入参:" + FastJsonUtils.toJSONString(mkActivity));
			if (mkActivity == null) {
				return Response.returnCode(BackCode.RESPONSE_1001.getCode(),
						BackCode.RESPONSE_1001.getMsg());
			}
			Response response = mkActivityService.shelveActivity(mkActivity);
			log.info("/shelveActivity出参:" + FastJsonUtils.toJSONString(response));
			return response;
		} catch (LockException e) {
			log.error("/shelveActivity error" + e.getMessage(), e);
			return Response.returnCode(BackCode.LOCK_FAIL.getCode(),
					BackCode.LOCK_FAIL.getMsg());
		} catch (Exception e) {
			log.error("/shelveActivity error" + e.getMessage(), e);
			return Response.returnCode(BackCode.FAIL.getCode(),
					e.toString());
		}
	}

	/**
	 * 1分抢-删除活动草稿
	 */
	@ApiOperation(value = "1分抢-删除活动草稿", tags = { "1分抢-删除活动草稿" }, notes = "")
	@RequestMapping(value = "deleteActivityDraft", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Response deleteActivityDraft(@RequestBody MkActivity mkActivity) {
		try {
			log.info("/deleteActivityDraft入参:" + FastJsonUtils.toJSONString(mkActivity));
			if (mkActivity == null) {
				return Response.returnCode(BackCode.RESPONSE_1001.getCode(),
						BackCode.RESPONSE_1001.getMsg());
			}
			Response response = mkActivityService.deleteActivityDraft(mkActivity);
			log.info("/deleteActivityDraft出参:" + FastJsonUtils.toJSONString(response));
			return response;
		} catch (Exception e) {
			log.error("/deleteActivityDraft error" + e.getMessage(), e);
			return Response.returnCode(BackCode.FAIL.getCode(),
					e.toString());
		}
	}

//	@Autowired
//	private MkWxPushService mkWxPushService;
//
//	@ApiOperation(value = "1分抢-微信消息推送", tags = { "1分抢-微信消息推送" }, notes = "")
//	@PostMapping("/test/wxtoken")
//	public Response test(){
//		String openID = "obW575b-henORB0yl3oM-bpTjGDQ";
//		String templateId = "vV8IT1DJjyiLayGM1JURsduLmw7egahEI-rvVZ6GFFo";
//		String formId = "9f5b57283b8c48e61ec9745d7929ca11";
//		String data = "{\"keyword3\":{\"value\":\"0.01元\"},\"keyword4\":{\"value\":\"1分抢好货马上到手，快来拿~点击立即领取~\"},\"keyword1\":{\"value\":\"特仑苏纯牛奶\"},\"keyword2\":{\"value\":\"恭喜您1分抢活动发起成功，赶紧在30分钟内支付1分钱完成>订单吧！\"}}";
//		System.out.println(data);
//		JSONObject jsonObject = JSON.parseObject(data);
//		try {
//			mkWxPushService.sendTemplateMessage(openID, templateId,formId, data);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}


}
