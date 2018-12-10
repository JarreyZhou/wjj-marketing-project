package com.wjj.application.dto.pennyRob;

import com.wjj.application.entity.pennyRob.MkActivityRob;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "")
public class MkActivityRobDTO extends MkActivityRob {
    private List<MkActivityRob> mkActivityRobs;

    private int size;

    //失效日期
    private Date failureDate;
    //倒计时
    private long countDown;
    //发起人姓名
    private String sponsorName;

    //商品名称
    private String goodsName;

    //商品主图
    private String goodsPic;

    //所需帮抢人数
    private int needRobCount;

    //价格
    private String price;

    private int mkActivityId;

    private int mkSponsorId;

    private int spuId;
}
