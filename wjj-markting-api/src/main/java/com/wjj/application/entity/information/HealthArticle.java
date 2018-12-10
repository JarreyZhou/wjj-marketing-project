package com.wjj.application.entity.information;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="健康资讯文章")
public class HealthArticle    extends Model<HealthArticle> implements Serializable {
	private static final long serialVersionUID = 1235433253L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value="编号 " , name="id")
	private Long id;
	
	@ApiModelProperty(value="文章内容 " , name="remark")
	private  String  remark;
	
	
	@ApiModelProperty(value="资讯编号" , name="infoId")
	private String infoId;

	@Override
	protected Serializable pkVal() {
		return id;
	}
}
