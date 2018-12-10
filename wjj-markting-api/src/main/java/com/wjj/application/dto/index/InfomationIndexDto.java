package com.wjj.application.dto.index;



import java.util.Map;


import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value="健康首页资讯信息-居民端")
public class InfomationIndexDto {
	
	private String type;//类型  1：为您推荐   2：热门资讯
	
	private Integer pageNo;
	
	private Integer pageSize;
	
	private Map<String,Object> infomationMap;//为您推荐
	
	//private Map<String,Object>  hotMap;//热门资讯
	

}
