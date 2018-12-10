package com.wjj.application.entity.advertising;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="广告位管理")
@TableName("advertising_manager")
public class AdvertisingManagerEntity  extends Model<AdvertisingManagerEntity>  implements Serializable{

	private static final long serialVersionUID = 423455434L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value="编号 " , name="id")
	private Long id; //
	
	@ApiModelProperty(value="类型 1:健康bana  2:商城bana   3:活动banna " , name="type")
	private String type;//类型 1:健康bana  2:商城bana   3:活动banna
	
	@ApiModelProperty(value="名称 " , name="name")
	private String name;//名称
	
	@ApiModelProperty(value="排序 " , name="sortId")
	private Integer sortId;//排序
	
	@ApiModelProperty(value="创建时间 " , name="createTime")
	private Date createTime;//创建时间
	

	@ApiModelProperty(value="状态  1:草稿 2：上架  3：下架 " , name="updateStatus")
	private String updateStatus;//状态  1:草稿 2：上架  3：下架
	
	@ApiModelProperty(value="专题图片 " , name="mainPic")
	private String mainPic;//专题图片
	
	@ApiModelProperty(value="链接 " , name="linkPath")
	private String linkPath;//链接
	
	@ApiModelProperty(value="微信文案 " , name="weichartLetter")
	private String weichartLetter;//微信文案
	
	@ApiModelProperty(value="微信图片路径" , name="weichartPicPath")
	private String weichartPicPath;//weichartPicPath
	
	
	
	@Override
	protected Serializable pkVal() {
		return id;
	}
	
	
	

}
