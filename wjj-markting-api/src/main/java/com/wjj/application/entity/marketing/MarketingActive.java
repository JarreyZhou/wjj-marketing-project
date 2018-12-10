package com.wjj.application.entity.marketing;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.wjj.application.entity.advertising.AdvertisingManagerEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="活动表")
public class MarketingActive  extends Model<MarketingActive>  implements Serializable{
	private static final long serialVersionUID = 45897234L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value="编号   1.新人专享  2.折扣专区  3.限量秒杀  4.今日爆款  5.国民优选 6.精选好货 7.为您推荐   8：热销" , name="id")
	private Long id; //产品编号
	
	@ApiModelProperty(value="活动名称 " , name="activeName")
	private String activeName;
	
	@ApiModelProperty(value="跟新人 " , name="updateHuman")
	private String updateHuman;
	
	@ApiModelProperty(value="跟新时间 " , name="updateTime")
	private  Date updateTime;
	
	@ApiModelProperty(value="主图 " , name="mainPath")
	private String mainPath;

	@Override
	protected Serializable pkVal() {
		return id;
	}
	
}
