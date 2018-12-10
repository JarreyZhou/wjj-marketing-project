package com.wjj.application.feign.pennyRob;

import com.wjj.application.dto.pennyRob.MkActivityRobDTO;
import com.wjj.application.dto.pennyRob.MkGoodsSpuDTO;
import com.wjj.application.response.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 活动参与帮抢者 前端控制器
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */
@FeignClient("wjj-marketing")
public interface MkActivityRobFeignClient {

	/**
	 * 活动帮抢人列表
	 */
	@PostMapping(value = "/application/mkActivityRob/activityRobList")
	Response activityRobList(@RequestBody MkActivityRobDTO mkActivityRobDTO);

	/**
     * 帮抢页面
	 */
    @PostMapping(value = "/application/mkActivityRob/helpRob")
    Response helpRob(@RequestBody MkActivityRobDTO mkActivityRobDTO);

	/**
     * 查看更多活动商品
	 */
    @PostMapping(value = "/application/mkActivityRob/moreGoods")
    Response moreGoods(@RequestBody MkGoodsSpuDTO mkGoodsSpuDTO);

}
