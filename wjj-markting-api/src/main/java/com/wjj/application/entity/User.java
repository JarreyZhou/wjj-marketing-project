package com.wjj.application.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author bob123
 * @since 2018-07-17
 */
@Data
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     *  名称
     */
    private String name;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * test主键
     */
    @TableField("test_id")
    private Integer testId;
    
    //金额
    private Integer amount;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
