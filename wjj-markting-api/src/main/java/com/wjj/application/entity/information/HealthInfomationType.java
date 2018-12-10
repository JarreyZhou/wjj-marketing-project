package com.wjj.application.entity.information;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="健康资讯类型")
public class HealthInfomationType   extends Model<HealthInfomationType> implements Serializable{
	
	
private static final long serialVersionUID = 1243644534L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value="编号 " , name="id")
	private Long id;
	
	@ApiModelProperty(value="名称 " , name="name")
	private  String  name;
	
	
	@ApiModelProperty(value="状态  2:上架  3;下架 " , name="typeStatus")
	private  String  typeStatus;
	
	@Override
	protected Serializable pkVal() {
		return id;
	}

}
