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
 * 活动发起表
 * </p>
 *
 * @author xiaoguang123
 * @since 2018-11-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mk_activity_sponsor")
public class MkActivitySponsor extends Model<MkActivitySponsor> {

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
     * 活动发起编号
     */
    @TableField("sponsor_code")
    private String sponsorCode;
    /**
     * 发起者id
     */
    @TableField("user_id")
    private String userId;
    /**
     * 发起者id
     */
    @TableField("parentInvite_code")
    private String parentInviteCode;
    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;
    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;
    /**
     * 帮抢人总数
     */
    @TableField("rob_count")
    private Integer robCount;
    /**
     * 商品spuid
     */
    @TableField("spu_id")
    private Integer spuId;
    
    @TableField("form_id")
    private String formId;
    /**
     * 商品skuid
     */
    @TableField("sku_id")
    private Integer skuId;
    /**
     * 活动发起状态
     */
    @TableField("status")
    private Integer status;
    /**
     * 订单状态
     */
    @TableField("order_status")
    private Integer orderStatus;
    /**
     * 订单号
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 订单贸易单号
     */
    @TableField("order_trade_no")
    private String orderTradeNo;
    /**
     * 订单备注
     */
    @TableField("order_remarks")
    private String orderRemarks;
    /**
     *	订单地址id
     */
    @TableField("addr_id")
    private Integer addrId;
    @TableField("open_id")
    private String openId;
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
    @TableField("remarks")
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
