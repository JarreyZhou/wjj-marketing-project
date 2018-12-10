package com.wjj.application.service.video;

import java.util.Map;

import com.wjj.application.dto.video.VideoArticleEntityDto;
import com.wjj.application.dto.video.VideoManagerEntityDto;
import com.wjj.application.response.Response;

public interface VideoManagerService {
	// 查询列表
	Map<String,Object> selectVideoManagerList(VideoManagerEntityDto dto);
	
	//插入
	Response  insertVideoManagers(VideoManagerEntityDto dto);
	
	//跟新
	Response  updateVideoManagers(VideoManagerEntityDto dto);
	
	//查看详情
	Response selectVideoManagerDetail(VideoManagerEntityDto dto);
	
	
	//删除
	Response deleteManager(VideoManagerEntityDto dto);
	
	//上架
	Response updateVideoManagerUp(VideoManagerEntityDto dto);
	
	//下架
	Response updateVideoManagerDown(VideoManagerEntityDto dto);
	
	//查询所有文章列表
	Map<String,Object>  selectVideoArticleList(VideoArticleEntityDto dto);
	
	//查看文章列表  移动端用
	Response selectVideoManagerListForApp(VideoManagerEntityDto dto) throws Exception;
	
	//视屏点击量
	Response  insertVideoclickNum(VideoManagerEntityDto dto) throws Exception;
	
	
	//移动端查看视屏相关的商品及其文章
	Response  selectVideoManagerRelative(VideoManagerEntityDto dto);
	
}
