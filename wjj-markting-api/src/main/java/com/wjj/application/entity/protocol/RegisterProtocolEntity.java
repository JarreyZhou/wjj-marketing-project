package com.wjj.application.entity.protocol;


import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="协议表")
public class RegisterProtocolEntity extends Model<RegisterProtocolEntity>  implements Serializable{
	
	
	private static final long serialVersionUID = 4543345434L;

	@ApiModelProperty(value="编号 " , name="id")
	private Integer id; //产品编号
	
	@ApiModelProperty(value="名称 " , name="name")
	private String name; //名称
	
	
	@ApiModelProperty(value="跟新时间 " , name="updateTime")
	private Date updateTime; //产品编号
	
	
	@ApiModelProperty(value="协议内容" , name="remark")
	private String remark; //协议内容
	
	
	@Override
	protected Serializable pkVal() {
		
		return id;
	}
}
