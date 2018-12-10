package com.wjj.application.entity.pennyRob;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 活动商品spu表
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */
public class MkGoodsSpuExt extends MkGoodsSpu {

    private static final long serialVersionUID = 1L;

    private MkGoodsSku mkGoodsSku;
    
}
