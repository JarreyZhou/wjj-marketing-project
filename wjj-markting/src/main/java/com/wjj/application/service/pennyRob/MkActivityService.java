package com.wjj.application.service.pennyRob;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.wjj.application.dto.pennyRob.MkActivityDTO;
import com.wjj.application.dto.pennyRob.MkGoodsSpuDTO;
import com.wjj.application.entity.pennyRob.MkActivity;
import com.wjj.application.entity.pennyRob.MkGoodsSpu;
import com.wjj.application.page.PageDTO;
import com.wjj.application.response.Response;
import com.wjj.application.util.LockException;
import com.wjj.application.vo.PageVO;

/**
 * <p>
 * 活动表 服务类
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */
public interface MkActivityService extends IService<MkActivity> {

	List<MkActivity> list();

	Response snatchList(PageVO pageVO);

	Response comingList(PageVO pageVO);


	/**
	 * 1分抢-添加活动
	 */
	Response insertActivity(MkActivity mkActivity) throws Exception;

	/**
	 * 1分抢-活动列表
	 */
	PageDTO<MkActivity> selectActivityList(MkActivityDTO mkActivityDTO);

	/**
	 * 1分抢-查看活动
	 */
	Response selectActivity(MkActivity mkActivity);

	/**
	 * 1分抢-编辑活动
	 */
	Response updateActivity(MkActivity mkActivity) throws LockException;
	/**
	 * 1分抢-编辑活动OA审批单号
	 */
	Response updateActivityOA(MkActivity mkActivity) throws LockException;

	/**
	 * 1分抢-下架活动
	 */
	Response unshelveActivity(MkActivity mkActivity) throws LockException;

	/**
	 * 1分抢-上架活动
	 */
	Response shelveActivity(MkActivity mkActivity) throws LockException;

	/**
	 * 1分抢-删除活动草稿
	 */
	Response deleteActivityDraft(MkActivity mkActivity) throws Exception;
}
