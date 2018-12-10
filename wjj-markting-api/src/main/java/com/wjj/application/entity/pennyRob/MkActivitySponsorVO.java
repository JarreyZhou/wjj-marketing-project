package com.wjj.application.entity.pennyRob;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 活动发起表
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */
@Data
public class MkActivitySponsorVO extends MkActivitySponsor {

    private static final long serialVersionUID = 1L;

    private List<MkActivityRob> mkActivityRobList;
    
    private MkActivity mkActivity;
    
    private MkGoodsSpuExt mkGoodsSpuExt;
    
    
}
