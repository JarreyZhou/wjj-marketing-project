package com.wjj.application.entity.information;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;




@Data
@ApiModel(value="健康方案关联商品表")
public class HealthRelGoods  extends Model<HealthRelGoods> implements Serializable {

	private static final long serialVersionUID = 1232453L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value="编号 " , name="id")
	private Long id;
	
	@ApiModelProperty(value="商品编号 " , name="goodsId")
	private  String  goodsId;
	
	@ApiModelProperty(value="商品名称 " , name="goodsName")
	private  String goodsName;
	
	@ApiModelProperty(value="最低售价 " , name="salePrice")
	private BigDecimal  salePrice;
	
	@ApiModelProperty(value="最高售价 " , name="maxPrice")
	private BigDecimal maxPrice;
	
	@ApiModelProperty(value="方案编号 " , name="healthId")
	private Long healthId;
	
	
	
	@Override
	protected Serializable pkVal() {
		return id;
	}

}
