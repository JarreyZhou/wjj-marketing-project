package com.wjj.application.entity.marketing;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="活动表")
public class MarketingBanalPic  extends Model<MarketingBanalPic>  implements Serializable{
	
private static final long serialVersionUID = 89577234L;


	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value="编号" , name="id")
	private Long id; //产品编号
	
	@ApiModelProperty(value="编号   1.新人专享  2.折扣专区  3.限量秒杀  4.今日爆款  5.国民优选 6.精选好货 7.为您推荐" , name="activeId")
	private Long activeId; //产品编号
	
	@ApiModelProperty(value="图片路径" , name="picPath")
	private String picPath;
	
	@Override
	protected Serializable pkVal() {
		return id;
	}

}
