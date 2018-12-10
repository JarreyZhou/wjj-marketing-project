package com.wjj.application.dto.pennyRob;

import java.util.List;

import com.wjj.application.entity.pennyRob.MkStandard;
import com.wjj.application.entity.pennyRob.MkStandardAttr;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "1分抢-活动商品规格")
public class MkStandardDTO extends MkStandard{
	private List<MkStandardAttr> standardAttrList;
}
