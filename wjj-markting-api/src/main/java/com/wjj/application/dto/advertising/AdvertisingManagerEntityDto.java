package com.wjj.application.dto.advertising;


import com.wjj.application.entity.advertising.AdvertisingManagerEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="广告位管理")
public class AdvertisingManagerEntityDto extends AdvertisingManagerEntity {
	
	
	private Integer pageNo;
	
	private Integer pageSize;
	
	@ApiModelProperty(value="开始时间 " , name="beginTime")
	private String fromTime;
	
	@ApiModelProperty(value="结束时间 " , name="endTime")
	private String endTime;
	
	@ApiModelProperty(value="开始时间 " , name="createTimeStart")
	private String createTimeStart;
	
	@ApiModelProperty(value="结束时间 " , name="createTimeEnd")
	private String createTimeEnd;
}
