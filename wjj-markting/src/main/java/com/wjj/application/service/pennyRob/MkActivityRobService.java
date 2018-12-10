package com.wjj.application.service.pennyRob;

import com.baomidou.mybatisplus.service.IService;
import com.wjj.application.dto.pennyRob.MkActivityRobDTO;
import com.wjj.application.dto.pennyRob.MkGoodsSpuDTO;
import com.wjj.application.entity.pennyRob.MkActivityRob;
import com.wjj.application.response.Response;

import java.util.List;

/**
 * <p>
 * 活动参与帮抢者 服务类
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */
public interface MkActivityRobService extends IService<MkActivityRob> {

    Response helpRob(MkActivityRobDTO mkActivityRobDTO);

    Response moreGoods(MkGoodsSpuDTO activityId);

    List<MkActivityRobDTO> selectActivityRobList(MkActivityRobDTO mkActivityRobDTO);
}
