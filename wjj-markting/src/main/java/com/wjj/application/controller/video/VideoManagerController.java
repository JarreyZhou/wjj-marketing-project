package com.wjj.application.controller.video;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wjj.application.dto.video.VideoArticleEntityDto;
import com.wjj.application.dto.video.VideoManagerEntityDto;
import com.wjj.application.response.Response;
import com.wjj.application.service.video.VideoManagerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "视频专区相关接口", tags = "视频专区相关接口")
@RestController
public class VideoManagerController {

	@Autowired
	private VideoManagerService videoManagerService;


    //查询列表
	@ApiOperation(value = "列表--运营端", notes = "注意问题点：\n 1>输入参数：  \n"
			+ " { title:标题   \n  "+
			"   type: 1:健康视频  2:商城视频 (必填)  \n "+
			"   pageNo:第几页 (必填)   pageSize:每页记录数    (必填)  \n "+
			"   fromTime:开始时间   \n "+
			"   endTime:结束时间   \n "+
			"} \n")
	@PostMapping(value = "/videoManager/platform/list")
	public Response selectVideoManagerList(@RequestBody VideoManagerEntityDto dto) {
		Integer pageNo = dto.getPageNo();
		Integer pageSize = dto.getPageSize();
		int pageNos=pageNo;
		if(pageNo!=null&&pageSize!=null) {
			if(pageNo!=0) {
				pageNo=(pageNo-1)*pageSize;
			}
			dto.setPageNo(pageNo);
			dto.setPageSize(pageSize);
		}else {
			dto.setPageNo(0);
			dto.setPageSize(10);
		}
		Map<String, Object> map = videoManagerService.selectVideoManagerList(dto);
		map.put("pageNo", pageNos);
		map.put("pageSize", pageSize);
		return Response.ok(map);
	}
	

    //插入
	@ApiOperation(value = "插入--运营端", notes = "注意问题点：\n 1>输入参数：  \n"
			+ " { title:标题   \n  "+
			"   type: 1:健康视频  2:商城视频 (必填)  \n "+
			"   mainPic:视频封面  \n "+
			"   degree:期数   \n "+
			"   linkPath:链接   \n "+
			"   weichartLetter:微信文案   \n "+
			"   weichartPicPath:微信图片路径   \n "+
			"   initNum:初始观看人数   \n "+
			"   updateStatus:状态 1:草稿 2：已上架 3：已下架   \n "+
			"   videoArticleEntityList:文章列表[{articleId:文章编号     articleTitle:文章标题}]   \n "+
			"   videoGoodsEntityList:商品列表[{goodsId：商品编号  goodsName：商品名称   minPrice：最低价   maxPrice：最高价}]   \n "+
			"} \n")
	@PostMapping(value = "/videoManager/platform/insert")
	public Response insertVideoManager(@RequestBody VideoManagerEntityDto dto) {
		return videoManagerService.insertVideoManagers(dto);
	}
	
	

    //插入
	@ApiOperation(value = "跟新--运营端", notes = "注意问题点：\n 1>输入参数：  \n"
			+ " { title:标题   \n  "+
			"   id: 编号  \n "+
			"   type: 1:健康视频  2:商城视频 (必填)  \n "+
			"   mainPic:视频封面  \n "+
			"   degree:期数   \n "+
			"   linkPath:链接   \n "+
			"   weichartLetter:微信文案   \n "+
			"   weichartPicPath:微信图片路径   \n "+
			"   initNum:初始观看人数   \n "+
			"   updateStatus:状态 1:草稿 2：已上架 3：已下架   \n "+
			"   videoArticleEntityList:文章列表[{articleId:文章编号     articleTitle:文章标题}]   \n "+
			"   videoGoodsEntityList:商品列表[{goodsId：商品编号  goodsName：商品名称   minPrice：最低价   maxPrice：最高价}]   \n "+
			"} \n")
	@PostMapping(value = "/videoManager/platform/update")
	public Response updateVideoManager(@RequestBody VideoManagerEntityDto dto) {

		return videoManagerService.updateVideoManagers(dto);
	}
	
	//查看详情
	@ApiOperation(value = "查看详情--运营端", notes = "注意问题点：\n 1>输入参数：  \n"
			+ " { "+
			"   id: 编号  \n "+
			"} \n")
	@PostMapping(value = "/videoManager/platform/detail")
	public Response selectVideoManagerDetail(@RequestBody VideoManagerEntityDto dto) {
		return videoManagerService.selectVideoManagerDetail(dto);
	}
	
	//删除
	@ApiOperation(value = "删除--运营端", notes = "注意问题点：\n 1>输入参数：  \n"
			+ " { "+
			"   id: 编号  \n "+
			"} \n")
	@PostMapping(value = "/videoManager/platform/delete")
	public Response deleteManager(@RequestBody VideoManagerEntityDto dto) {
		return videoManagerService.deleteManager(dto);
	}
	
	
	//上架
	@ApiOperation(value = "上架--运营端", notes = "注意问题点：\n 1>输入参数：  \n"
			+ " { "+
			"   id: 编号  \n "+
			"} \n")
	@PostMapping(value = "/videoManager/platform/up")
	public Response updateVideoManagerUp(@RequestBody VideoManagerEntityDto dto) {
		return videoManagerService.updateVideoManagerUp(dto);
	}
	
	//下架
	@ApiOperation(value = "下架--运营端", notes = "注意问题点：\n 1>输入参数：  \n"
			+ " { "+
			"   id: 编号  \n "+
			"} \n")
	@PostMapping(value = "/videoManager/platform/down")
	public Response updateVideoManagerDown(@RequestBody VideoManagerEntityDto dto) {
		return videoManagerService.updateVideoManagerDown(dto);
	}
	
	
	//下架
	@ApiOperation(value = "查看所有文章--运营端", notes = "注意问题点：\n 1>输入参数：  \n"
			+ " { "+
			"   id: 编号  \n "+
			"} \n")
	@PostMapping(value = "/videoManager/platform/article")
	public Response selectVideoArticleList(@RequestBody VideoArticleEntityDto dto) {
		Integer pageNo = dto.getPageNo();
		Integer pageSize = dto.getPageSize();
		int pageNos=pageNo;
		if(pageNo!=null&&pageSize!=null) {
			if(pageNo!=0) {
				pageNo=(pageNo-1)*pageSize;
			}
			dto.setPageNo(pageNo);
			dto.setPageSize(pageSize);
		}else {
			dto.setPageNo(0);
			dto.setPageSize(10);
		}
		Map<String, Object> map = videoManagerService.selectVideoArticleList(dto);
		map.put("pageNo", pageNos);
		map.put("pageSize", pageSize);
		return Response.ok(map);
	}
	
	
	//查看文章列表  移动端用
	@ApiOperation(value = "查看文章列表--移动端", notes = "注意问题点：\n 1>输入参数：  \n"
			+ " { "+
			"type: 类型1:健康首页  2：商城首页(必填)  \n "+
			" pageNo:第几页  \n "+
			" pageSize:每一页记录数    \n "+
			"} \n")
	@PostMapping(value = "/videoManager/app/articleList")
	public Response selectVideoManagerListForApp(@RequestBody VideoManagerEntityDto dto) throws Exception{
		Integer pageNo = dto.getPageNo();
		Integer pageSize = dto.getPageSize();
		int pageNos=pageNo;
		if(pageNo!=null&&pageSize!=null) {
			if(pageNo!=0) {
				pageNo=(pageNo-1)*pageSize;
				pageSize=pageNos*pageSize;
			}
			dto.setPageNo(pageNo);
			dto.setPageSize(pageSize);
		}else {
			dto.setPageNo(0);
			dto.setPageSize(10);
		}
		return videoManagerService.selectVideoManagerListForApp(dto);
	}
		
	//视屏点击量
	@ApiOperation(value = "视屏点击量--移动端", notes = "注意问题点：\n 1>输入参数：  \n"
			+ " { "+
			"   id: 视屏编号  \n "+
			"} \n")
	@PostMapping(value = "/videoManager/app/insertVideoclickNum")
	public Response  insertVideoclickNum(@RequestBody VideoManagerEntityDto dto) throws Exception{
		return videoManagerService.insertVideoclickNum(dto);
	}
		
	//移动端查看视屏相关的商品及其文章
	@ApiOperation(value = "移动端查看视屏相关的商品及其文章--移动端", notes = "注意问题点：\n 1>输入参数：  \n"
			+ " { "+
			"   id: 视屏编号  \n "+
			"} \n")
	@PostMapping(value = "/videoManager/app/artcleAndGoods")
	public	Response  selectVideoManagerRelative(@RequestBody VideoManagerEntityDto dto) {
		return videoManagerService.selectVideoManagerRelative(dto);
	}
}
