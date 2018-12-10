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
 * 活动表
 * </p>
 *
 * @author xiaoguang123
 * @since 2018-11-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mk_activity")
public class MkActivity extends Model<MkActivity> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 活动名称
     */
    private String name;
    /**
     * 活动有效期开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("start_time")
    private Date startTime;
    /**
     * 活动有效期结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("end_time")
    private Date endTime;
    /**
     * OA审批单号
     */
    @TableField("oa_num")
    private String oaNum;
    /**
     * 每个用户最多开枪几个商品
     */
    @TableField("rob_limit")
    private Integer robLimit;
    /**
     * 帮抢有效期
     */
    @TableField("help_indate")
    private Integer helpIndate;
    /**
     * 微信分享文案
     */
    @TableField("weixin_wenan")
    private String weixinWenan;
    /**
     * 活动宣传图
     */
    @TableField("publicity_pic")
    private String publicityPic;
    /**
     * 1分抢活动规则
     */
    private String regulation;
    /**
     * 活动发起总数
     */
    @TableField("initiate_count")
    private Integer initiateCount;
    /**
     * 活动帮抢总数
     */
    @TableField("help_count")
    private Integer helpCount;
    /**
     * 订单量
     */
    @TableField("order_count")
    private Integer orderCount;
    /**
     * 活动状态()
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
