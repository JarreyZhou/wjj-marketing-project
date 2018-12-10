package com.wjj.application.entity.pennyRob;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 活动参与帮抢者
 * </p>
 *
 * @author long.zhou
 * @since 2018-10-30
 */
@Data
public class MkActivityRobExt {

    private static final long serialVersionUID = 1L;

    private MkActivitySponsor mkActivitySponsor;

    private List<MkActivityRob> mkActivityRobList;

    private MkGoodsSpu mkGoodsSpu;

    private MkActivity mkActivity;

    //首页列表产品图片路径
    private String movePicPath;

    //距离结束时间戳
    private Long distanceEndTime;

}
