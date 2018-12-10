package com.wjj.application.entity.information;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="健康资讯")
public class HealthInfomation  extends Model<HealthInfomation> implements Serializable{

private static final long serialVersionUID = 1243234534L;
	
	@ApiModelProperty(value="编号 " , name="id")
	private String id;
	
	@ApiModelProperty(value="名称 " , name="titleName")
	private  String  titleName;
	
	@ApiModelProperty(value="资讯类型  1；文章   2视频 " , name="groups")
	private  String groups;
	
	@ApiModelProperty(value="类型编号" , name="typeId")
	private Long typeId;
	
	@ApiModelProperty(value="来源" , name="sourceFrom")
	private String sourceFrom;
	
	@ApiModelProperty(value="微信分享" , name="wechart")
	private String wechart;
	
	@ApiModelProperty(value="虚拟阅读" , name="dummyRead")
	private Integer dummyRead;
	
	@ApiModelProperty(value="虚拟喜欢" , name="dummyLike")
	private Integer dummyLike;
	
	@ApiModelProperty(value="真实阅读" , name="realRead")
	private Integer realRead;
	
	@ApiModelProperty(value="真实喜欢" , name="realLike")
	private Integer realLike;
	
	@ApiModelProperty(value="图片路径" , name="picPath")
	private String picPath;
	
	@ApiModelProperty(value="链接" , name="linkPath")
	private String linkPath;
	
	@ApiModelProperty(value="创建" , name="createUser")
	private String createUser;
	
	@ApiModelProperty(value="创建时间" , name="createTime")
	private Date createTime;
	
	@ApiModelProperty(value="状态  1:草稿   2：上架  3下架" , name="infoStatus")
	private String infoStatus;
	

	@Override
	protected Serializable pkVal() {
		return id;
	}

}
