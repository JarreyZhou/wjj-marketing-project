package com.wjj.application.dto.video;

import com.wjj.application.entity.video.VideoArticleEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value="视屏关联的文章扩展")
public class VideoArticleEntityDto extends VideoArticleEntity{
	
	private Integer pageNo;
	
	private Integer pageSize;

}
