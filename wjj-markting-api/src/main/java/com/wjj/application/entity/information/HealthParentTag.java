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
public class HealthParentTag   extends Model<HealthParentTag> implements Serializable {
	private static final long serialVersionUID = 1235433253L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value="编号 " , name="id")
	private Long id;
	
	@ApiModelProperty(value="标签类型 " , name="tagType")
	private  String  tagType;
	

	@ApiModelProperty(value="标签类型 名称" , name="tag_type_name")
	private  String  tagTypeName;
	
	@ApiModelProperty(value="标签值" , name="tagValue")
	private  String  tagValue;
	
	@ApiModelProperty(value="标签名称 " , name="tagName")
	private  String  tagName;
	
	
	@ApiModelProperty(value="标签等级 " , name="tagLevel")
	private  String  tagLevel;
	
	@ApiModelProperty(value="父标号 " , name="parentId")
	private  Long  parentId;
	
	@ApiModelProperty(value="健康编号" , name="healthId")
	private Long healthId;
	
	@ApiModelProperty(value="子标签 " , name="healthTag")
	private  List<HealthTag> healthTag;

	@Override
	protected Serializable pkVal() {
		return id;
	}
}
