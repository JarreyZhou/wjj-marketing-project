package com.wjj.application.entity.pennyRob;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wjj.application.dto.GoodsMainInfoDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 活动商品spu表
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */
@Data
public class MkGoodsSpuVO extends MkGoodsSpu {

    private static final long serialVersionUID = 1L;

    private List<MkGoodsSku> skuList;
    
    private MkActivity mkActivity;
    
    private GoodsMainInfoDto goodsMainInfoDto;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date date;
}
