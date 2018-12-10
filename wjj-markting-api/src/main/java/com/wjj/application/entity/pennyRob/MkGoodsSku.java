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
 * 活动商品sku表
 * </p>
 *
 * @author xiaoguang123
 * @since 2018-11-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mk_goods_sku")
public class MkGoodsSku extends Model<MkGoodsSku> {

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
     * 商品库商品规格编号
     */
    @TableField("goods_sku_id")
    private Integer goodsSkuId;
    /**
     * 规格json
     */
    @TableField("guige_json")
    private String guigeJson;

    @TableField("goods_pic")
    private String goodsPic;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 商品库存
     */
    @TableField("goods_stock")
    private Integer goodsStock;
    /**
     * 总库存
     */
    private Integer stock;
    /**
     * 剩余库存
     */
    @TableField("remain_stock")
    private Integer remainStock;
    @TableField("eclp_no")
    private String eclpNo;//'商品eclp编号',
    @TableField("detail_param")
    private String detailParam;//'规格参数',
    @TableField("app_simple_path")
    private String appSimplePath;//'商品图片地址'
    private String supplier;//'供应商'
    /**
     * 创建者
     */
    @TableField("create_by")
    private Integer createBy;
    @TableField("sale_price")
    private String salePrice;
    @TableField("line_price")
    private String linePrice;
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
