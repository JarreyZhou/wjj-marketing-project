package com.wjj.application.dto.pennyRob;

import com.wjj.application.entity.pennyRob.MkActivity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "1分抢-活动列表")
public class MkActivityDTO extends MkActivity {

	@ApiModelProperty(value = "每页数量", name = "pageSize")
	private Integer pageSize;
	@ApiModelProperty(value = "当前页码", name = "currPage")
	private int currPage;

	private Integer limit;
}
