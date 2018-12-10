package com.wjj.application.entity.video;

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
@ApiModel(value="视屏管理")
@TableName("video_manager")
public class VideoManagerEntity   extends Model<VideoManagerEntity>  implements Serializable{
	private static final long serialVersionUID = 454334434L;

	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value="编号 " , name="id")
	private Long id;
	
	@ApiModelProperty(value="视屏标题 " , name="title")
	private  String title;
	
	@ApiModelProperty(value="来源 " , name="sourceFrom")
	private String sourceFrom;
	
	@ApiModelProperty(value="视频封面 " , name="mainPic")
	private String mainPic;
	
	@ApiModelProperty(value="期数  " , name="degree")
	private String degree;
	
	@ApiModelProperty(value="链接 " , name="linkPath")
	private String linkPath;
	
	@ApiModelProperty(value="微信文案 " , name="weichartLetter")
	private String weichartLetter;
	
	@ApiModelProperty(value="微信图片路径 " , name="weichartPicPath")
	private String weichartPicPath;
	
	@ApiModelProperty(value="实际观看人数 " , name="actualNum")
	private Integer actualNum;
	
	@ApiModelProperty(value="初始观看人数 " , name="initNum")
	private Integer initNum;
	
	@ApiModelProperty(value="类型1:健康首页  2：商城首页 " , name="type")
	private String type;
	
	@ApiModelProperty(value="状态 1:草稿 2：已上架 3：已下架 " , name="updateStatus")
	private String updateStatus;
	
	@ApiModelProperty(value="创建时间 " , name="createTime")
	private Date createTime;

	
	@Override
	protected Serializable pkVal() {
		return id;
	}

}
