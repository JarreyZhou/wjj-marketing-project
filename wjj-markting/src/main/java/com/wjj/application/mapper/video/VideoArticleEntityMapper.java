package com.wjj.application.mapper.video;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wjj.application.dto.infomation.HealthInformationDto;
import com.wjj.application.dto.video.VideoArticleEntityDto;
import com.wjj.application.entity.information.HealthInfomation;
import com.wjj.application.entity.video.VideoArticleEntity;

public interface VideoArticleEntityMapper extends BaseMapper<VideoArticleEntity> {
	
	Integer deleteByVideoArticleEntityByVideo(Long videoId);
	
	List<VideoArticleEntity> slectVideoArticleEntityByVideo(Long videoId);
	
	List<VideoArticleEntity> selectVideoArticleList(VideoArticleEntityDto dto);
	
	Integer selectVideoArticleListCount(VideoArticleEntityDto dto);
	
	Integer updateVideoArticleByHealthInfomation(HealthInformationDto dto);
	
	Integer  updateRealReadByHealthInfomation(List<HealthInfomation> entity);
	
	
	List<VideoArticleEntity> slectVideoArticleEntityByVideoNew();
	
	Integer deleteArticleById(String articleId);

}
