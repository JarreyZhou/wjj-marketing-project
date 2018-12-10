package com.wjj.application.feign.pennyRob;

import com.wjj.application.dto.pennyRob.MkActivityDTO;
import com.wjj.application.entity.pennyRob.MkActivity;
import com.wjj.application.response.Response;
import com.wjj.application.vo.PageVO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 活动表 前端控制器
 * </p>
 *
 * @author long.zhou
 * @since 2018-10-30
 */
@FeignClient("wjj-marketing")
public interface MkActivityFeignClient {

	// 一分抢活动宣传图查询
	//add by long.zhou
	@PostMapping(value = "/application/mkActivity/list")
	Response list();

	// 正在疯抢列表
	//add by long.zhou
	@PostMapping(value = "/application/mkActivity/snatch/list")
	Response snatchList(@RequestBody PageVO pageVO);


	// 即将开始列表
	//add by long.zhou
	@PostMapping(value = "/application/mkActivity/coming/list")
	Response comingList(@RequestBody PageVO pageVO);

	/**
	 * 1分抢-添加活动
	 */
	@PostMapping(value = "/application/mkActivity/insertActivity")
	Response insertActivity(@RequestBody MkActivity mkActivity);

	/**
	 * 1分抢-活动列表
	 */
	@PostMapping(value = "/application/mkActivity/selectActivityList")
	Response selectActivityList(@RequestBody MkActivityDTO mkActivityDTO);

	/**
	 * 1分抢-查看活动
	 */
	@PostMapping(value = "/application/mkActivity/selectActivity")
	Response selectActivity(@RequestBody MkActivity mkActivity);

	/**
	 * 1分抢-编辑活动
	 */
	@PostMapping(value = "/application/mkActivity/updateActivity")
	Response updateActivity(@RequestBody MkActivity mkActivity);
	/**
	 * 1分抢-编辑活动OA审批单号
	 */
	@PostMapping(value = "/application/mkActivity/updateActivityOA")
	Response updateActivityOA(@RequestBody MkActivity mkActivity);

	/**
	 * 1分抢-下架活动
	 */
	@PostMapping(value = "/application/mkActivity/unshelveActivity")
	Response unshelveActivity(@RequestBody MkActivity mkActivity);

	/**
	 * 1分抢-上架活动
	 */
	@PostMapping(value = "/application/mkActivity/shelveActivity")
	Response shelveActivity(@RequestBody MkActivity mkActivity);

	/**
	 * 1分抢-删除活动草稿
	 */
	@PostMapping(value = "/application/mkActivity/deleteActivityDraft")
	Response deleteActivityDraft(@RequestBody MkActivity mkActivity);

}
