package com.wjj.application.mapper.video;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wjj.application.dto.video.VideoManagerEntityDto;
import com.wjj.application.entity.video.VideoManagerEntity;

public interface VideoManagerMapper extends BaseMapper<VideoManagerEntity> {
	
	List<VideoManagerEntityDto> selectVideoManagerList(VideoManagerEntityDto dto);
	
	List<VideoManagerEntityDto> selectVideoManagerListForApp(VideoManagerEntityDto dto);
	
	Integer selectVideoManagerListCount(VideoManagerEntityDto dto);
	
	VideoManagerEntityDto selectVideoManagerDetail(Long id);
	
	
	List<VideoManagerEntityDto> selectVideoManagerListForIndex(VideoManagerEntityDto dto);
	
	List<VideoManagerEntity> selectVideoManagerActualNum();
	
	Integer updateVideoManagerActualNum(List<VideoManagerEntity> list);
	

}
