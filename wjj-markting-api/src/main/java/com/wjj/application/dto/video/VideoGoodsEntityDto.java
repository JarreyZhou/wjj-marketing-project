package com.wjj.application.dto.video;

import java.util.Date;

import com.wjj.application.entity.video.VideoGoodsEntity;

public class VideoGoodsEntityDto extends VideoGoodsEntity {
	private Integer pageNo;
	
	private Integer pageSize;
	
	private Date fromTime;
	
	private Date endTime;
}
