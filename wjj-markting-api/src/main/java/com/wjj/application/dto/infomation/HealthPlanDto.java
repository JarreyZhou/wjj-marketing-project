package com.wjj.application.dto.infomation;

import java.util.List;

import com.wjj.application.entity.information.HealthParentTag;
import com.wjj.application.entity.information.HealthPlan;
import com.wjj.application.entity.information.HealthRelGoods;

import io.swagger.annotations.ApiModel;
import lombok.Data;


@Data
@ApiModel(value="健康方案扩展")
public class HealthPlanDto extends HealthPlan{
	
	private  List<HealthRelGoods> healthRelGoodsList;
	
	private List<HealthParentTag> healthParentTag;
	
	private int pageNo;
	
	private int pageSize;
	
	
	
	

}
