package com.wjj.application.entity.pennyRob;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.Version;
import com.baomidou.mybatisplus.enums.IdType;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 规格表
 * </p>
 *
 * @author xiaoguang123
 * @since 2018-11-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mk_standard")
public class MkStandard extends Model<MkStandard> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 商品spu id
     */
    @TableField("spu_id")
    private Integer spuId;
    /**
     * 规格名称
     */
    private String name;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 创建者
     */
    @TableField("create_by")
    private Integer createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("create_date")
    private Date createDate;
    /**
     * 更新者
     */
    @TableField("update_by")
    private Integer updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("update_date")
    private Date updateDate;
    /**
     * 备注信息
     */
    private String remarks;
    /**
     * 删除标记
     */
    @TableField("del_flag")
    private Integer delFlag;
    /**
     * 乐观锁标志
     */
    @Version
    private Integer version;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
