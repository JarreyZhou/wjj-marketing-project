package com.wjj.application.entity.information;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel(value="健康方案标签表")
public class HealthInfoParentTag extends Model<HealthInfoParentTag> implements Serializable{
	private static final long serialVersionUID = 123533253L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value="编号 " , name="id")
	private Long id;
	
	@ApiModelProperty(value="标签类型 " , name="tagType")
	private  String  tagType;
	
	@ApiModelProperty(value="标签值" , name="tagValue")
	private  String  tagValue;
	
	@ApiModelProperty(value="标签名称 " , name="tagName")
	private  String  tagName;
	
	@ApiModelProperty(value="类型名称" , name="tagTypeName")
	private String tagTypeName;
	
	
	
	@ApiModelProperty(value="标签等级 " , name="tagLevel")
	private  String  tagLevel;
	
	@ApiModelProperty(value="父标号 " , name="parentId")
	private  Long  parentId;
	
	@ApiModelProperty(value="资讯编号" , name="infoId")
	private String infoId;
	
	@ApiModelProperty(value="子标签列表" , name="healthInfoTagList")
	private List<HealthInfoTag> healthInfoTagList;

	@Override
	protected Serializable pkVal() {
		return id;
	}
}
