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
@ApiModel(value="健康方案")
public class HealthPlan extends Model<HealthPlan> implements Serializable{
	
	private static final long serialVersionUID = 123423534L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value="编号 " , name="id")
	private Long id;
	
	@ApiModelProperty(value="名称 " , name="name")
	private  String  name;
	
	@ApiModelProperty(value="开始时间 " , name="fromTime")
	private Date fromTime;
	
	@ApiModelProperty(value="结束时间 " , name="endTime")
	private Date endTime;
	
	@ApiModelProperty(value="类型 " , name="type")
	private String type;
	
	@ApiModelProperty(value="性别 " , name="sex")
	private String sex;
	
	@ApiModelProperty(value="详情 " , name="detail")
	private String detail;
	
	@ApiModelProperty(value="状态 1:草稿   2：上架   3下架 " , name="healthSatus")
	private String healthStatus;
	
	@ApiModelProperty(value="创建人 " , name="createUser")
	private  String createUser;
	
	@ApiModelProperty(value="跟新时间 " , name="updateDate")
	private Date updateDate;
	
	
	@Override
	protected Serializable pkVal() {
		return id;
	}

}
