package com.wjj.application.dto.pennyRob;

import lombok.Data;

@Data
public class MkGenerateDTO {
    private Integer activityId;
    private Integer spuId;
    private String userId;
    private Integer skuId;
    private Integer pageNo;
    private Integer pageSize;
    private Integer status;

    //具体规格
    private String sku;

    //活动发起表的id
    private Integer sponsorId;
}
