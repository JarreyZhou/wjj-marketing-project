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
 * 规格表
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */
@Data
public class MkStandardExt extends MkStandard {

    private static final long serialVersionUID = 1L;

    private List<MkStandardAttr> mkStandardAttrList;

}
