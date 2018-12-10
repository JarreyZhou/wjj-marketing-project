package com.wjj.application.dto.video;


import java.util.Date;
import java.util.List;

import com.wjj.application.entity.video.VideoArticleEntity;
import com.wjj.application.entity.video.VideoGoodsEntity;
import com.wjj.application.entity.video.VideoManagerEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
@Data
@ApiModel(value="视屏管理")
public class VideoManagerEntityDto extends VideoManagerEntity {
	
	private Integer pageNo;
	
	private Integer pageSize;
	
	private String fromTime;
	
	private String endTime;
	
	//文章列表
	private List<VideoArticleEntity>  videoArticleEntityList;
	
	//商品列表
	private List<VideoGoodsEntity>  videoGoodsEntityList;
	
	
}
