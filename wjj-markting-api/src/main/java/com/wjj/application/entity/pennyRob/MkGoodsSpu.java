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
 * 活动商品spu表
 * </p>
 *
 * @author xiaoguang123
 * @since 2018-11-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mk_goods_spu")
public class MkGoodsSpu extends Model<MkGoodsSpu> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 活动id
     */
    @TableField("activity_id")
    private Integer activityId;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品主库id
     */
    @TableField("good_id")
    private String goodId;
    /**
     * 商品分类id
     */
    @TableField("type_id")
    private Integer typeId;
    /**
     * 商品分类名称
     */
    @TableField("type_name")
    private String typeName;
    /**
     * 商城售价
     */
    private String price;
    /**
     * 所需帮抢人数
     */
    @TableField("needrob_count")
    private Integer needrobCount;
    /**
     * 初始参与活动数
     */
    @TableField("init_count")
    private Integer initCount;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 商品发起限制
     */
    @TableField("sponsor_limit")
    private Integer sponsorLimit;
    /**
     * 成单数
     */
    @TableField("order_count")
    private Integer orderCount;
    /**
     * 总库存
     */
    private Integer stock;
    /**
     * 剩余库存
     */
    @TableField("remain_stock")
    private Integer remainStock;
    /**
     * 发起数
     */
    @TableField("sponsor_count")
    private Integer sponsorCount;
    /**
     * 帮抢人总数
     */
    @TableField("rob_count")
    private Integer robCount;
    /**
     * 状态
     */
    private Integer status;
    
    

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
