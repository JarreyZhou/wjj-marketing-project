package com.wjj.application.entity.video;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="视屏关联的文章")
@TableName("video_article")
public class VideoArticleEntity  extends Model<VideoArticleEntity>  implements Serializable{
	
	private static final long serialVersionUID = 454334434L;

	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value="编号 " , name="id")
	private Long id;
	
	@ApiModelProperty(value="视屏编号 " , name="videoId")
	private  Long videoId;
	
	@ApiModelProperty(value="文章编号 " , name="articleId")
	private  String articleId;
	
	
	@ApiModelProperty(value="文章标题 " , name="articleTitle")
	private  String articleTitle;
	
	@ApiModelProperty(value="图片路径 " , name="picPath")
	private String picPath;
	
	@ApiModelProperty(value="实际阅读量 " , name="realRead")
	private Integer realRead;
	
	@ApiModelProperty(value="文章来源 " , name="sourceFrom")
	private String sourceFrom;
	
	
	
	
	
	@Override
	protected Serializable pkVal() {
		return id;
	}

}
