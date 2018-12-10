package com.wjj.application.entity.marketing;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value="活动表")
public class MarketingGoods  extends Model<MarketingGoods>  implements Serializable{


	private static final long serialVersionUID = 9854745425447234L;


	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value="编号" , name="id")
	private Long id; //编号
	
	@ApiModelProperty(value="活动编号" , name="activeId")
	private Long activeId; //活动编号
	
	@ApiModelProperty(value="活动编号" , name="goodsId")
	private String goodsId;
	
	@ApiModelProperty(value="商品名称" , name="goodsName")
	private String goodsName;
	
	@ApiModelProperty(value="规格编号" , name="detailId")
	private Long detailId;
	
	@ApiModelProperty(value="规格名称" , name="detailName")
	private String detailName;
	
	@ApiModelProperty(value="三级分类名称" , name="classifyName")
	private String classifyName;
	
	@ApiModelProperty(value="可售库存" , name="storeNum")
	private Integer storeNum;
	
	@ApiModelProperty(value="售价" , name="salePrice")
	private BigDecimal salePrice;
	
	@ApiModelProperty(value="排序" , name="sortId")
	private Integer sortId;
	
	@ApiModelProperty(value="新人价" , name="newHumanPrice")
	private BigDecimal newHumanPrice;

	
	@ApiModelProperty(value="折扣力度" , name="discount")
	private String discount;
	
	@ApiModelProperty(value="最高售价" , name="maxPrice")
	private BigDecimal maxPrice;
	
	@ApiModelProperty(value="最低售价" , name="minPrice")
	private BigDecimal minPrice;
	
	@ApiModelProperty(value="图片路径" , name="picPath")
	private String picPath;
	
	@ApiModelProperty(value="销量" , name="saleNum")
	private Integer saleNum;
	
	@ApiModelProperty(value="用于字段" , name="goodTypeName")
	private String goodsTypeName;
	
	@ApiModelProperty(value="体质类型" , name="bodyType")
	private String bodyType;
	
	@ApiModelProperty(value="性别" , name="sex")
	private String sex;

	@Override
	protected Serializable pkVal() {
		return id;
	}

}
