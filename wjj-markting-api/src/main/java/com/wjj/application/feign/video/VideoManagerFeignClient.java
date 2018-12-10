package com.wjj.application.feign.video;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.wjj.application.dto.video.VideoArticleEntityDto;
import com.wjj.application.dto.video.VideoManagerEntityDto;
import com.wjj.application.response.Response;



@FeignClient("wjj-marketing")
public interface VideoManagerFeignClient {

	@PostMapping(value = "/videoManager/platform/list")
	public Response selectVideoManagerList(@RequestBody VideoManagerEntityDto dto);
	

	@PostMapping(value = "/videoManager/platform/insert")
	public Response insertVideoManager(@RequestBody VideoManagerEntityDto dto) ;

	@PostMapping(value = "/videoManager/platform/update")
	public Response updateVideoManager(@RequestBody VideoManagerEntityDto dto);
	
	
	@PostMapping(value = "/videoManager/platform/detail")
	public Response selectVideoManagerDetail(@RequestBody VideoManagerEntityDto dto) ;
	

	@PostMapping(value = "/videoManager/platform/delete")
	public Response deleteManager(@RequestBody VideoManagerEntityDto dto) ;
	
	

	@PostMapping(value = "/videoManager/platform/up")
	public Response updateVideoManagerUp(@RequestBody VideoManagerEntityDto dto) ;
	

	@PostMapping(value = "/videoManager/platform/down")
	public Response updateVideoManagerDown(@RequestBody VideoManagerEntityDto dto) ;
	
	

	@PostMapping(value = "/videoManager/platform/article")
	public Response selectVideoArticleList(@RequestBody VideoArticleEntityDto dto) ;
	
	//查看文章列表  移动端用
	@PostMapping(value = "/videoManager/app/articleList")
	public Response selectVideoManagerListForApp(@RequestBody VideoManagerEntityDto dto) throws Exception;
			
	//视屏点击量
	@PostMapping(value = "/videoManager/app/insertVideoclickNum")
	public Response  insertVideoclickNum(@RequestBody VideoManagerEntityDto dto) throws Exception;
			
	//移动端查看视屏相关的商品及其文章
	@PostMapping(value = "/videoManager/app/artcleAndGoods")
	public	Response  selectVideoManagerRelative(@RequestBody VideoManagerEntityDto dto) ;
}
