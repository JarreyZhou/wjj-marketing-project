package com.wjj.application.service.video.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.wjj.application.dto.video.VideoArticleEntityDto;
import com.wjj.application.dto.video.VideoManagerEntityDto;
import com.wjj.application.entity.video.VideoArticleEntity;
import com.wjj.application.entity.video.VideoGoodsEntity;
import com.wjj.application.json.JsonUtil;
import com.wjj.application.mapper.video.VideoArticleEntityMapper;
import com.wjj.application.mapper.video.VideoGoodsMapper;
import com.wjj.application.mapper.video.VideoManagerMapper;
import com.wjj.application.response.Response;
import com.wjj.application.response.ReturnCode;
import com.wjj.application.service.video.VideoManagerService;
import com.wjj.application.util.RedisClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VideoManagerServiceImpl implements VideoManagerService {

	@Autowired
	private VideoManagerMapper videoManagerMapper;
	
	@Autowired
	private VideoArticleEntityMapper videoArticleEntityMapper;
	
	@Autowired
	private VideoGoodsMapper videoGoodsMapper;
	
	@Autowired
	private RedisClient redisClient;
	
	private final int  pageNoInit=0;
	
	private final int  pageSizeInit=100000;
	
	/**
	 * 查看列表
	 */
	@Override
	public Map<String, Object> selectVideoManagerList(VideoManagerEntityDto dto) {
		Map<String,Object> hashMap=new HashMap<>();
		List<VideoManagerEntityDto> list = videoManagerMapper.selectVideoManagerList(dto);
		Integer total = videoManagerMapper.selectVideoManagerListCount(dto);
		hashMap.put("list", list);
		hashMap.put("total", total);
		return hashMap;
	}
	
	/**
	 * 插入接口
	 *1.插入视屏基本信息
	 *2.插入文章信息
	 *3.插入商品相关信息
	 * 
	 */
	@Override
	public Response insertVideoManagers(VideoManagerEntityDto dto) {
		dto.setCreateTime(new Date());
		videoManagerMapper.insert(dto);
		Long id = dto.getId();
		initQuence(dto.getType());
		List<VideoArticleEntity> articleList = dto.getVideoArticleEntityList();
		if(articleList!=null&&articleList.size()>0) {
			for(VideoArticleEntity item:articleList) {
				item.setVideoId(id);
				videoArticleEntityMapper.insert(item);
			}
		}
		List<VideoGoodsEntity> goodsList = dto.getVideoGoodsEntityList();
		if(goodsList!=null&&goodsList.size()>0) {
			for(VideoGoodsEntity item:goodsList) {
				item.setVideoId(id);
				String movePicPath = item.getMovePicPath();
				String picPath = item.getPicPath();
				if(movePicPath!=null&&!movePicPath.equals("")) {
					item.setPicPath(movePicPath);
				}
				if(picPath!=null&&!picPath.equals("")) {
					item.setMovePicPath(picPath);
				}
				videoGoodsMapper.insert(item);
			}
		}
		return Response.returnCode(ReturnCode.SUCCESS.getCode(),ReturnCode.SUCCESS.getMsg());	
	}
	
	public void initQuence(String type) {
		VideoManagerEntityDto dto=new VideoManagerEntityDto();
		dto.setPageNo(pageNoInit);
		dto.setPageSize(pageSizeInit);
		String keyStr="viveomanager"+type;
		List<VideoManagerEntityDto> listNew = videoManagerMapper.selectVideoManagerListForApp(dto);//查询所有的视频
		if(listNew!=null && listNew.size()>0) {
			try {
				redisClient.del(keyStr);
				for(VideoManagerEntityDto item:listNew) {
					Long id = item.getId();
					String  viveoStr="viveomanageritem"+id;	
					String itemStr = JsonUtil.object2Json(item);
					redisClient.set(viveoStr, itemStr);
					redisClient.setList(keyStr, viveoStr);	
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 先删除后跟新
	 * 1.跟新视屏基本信息
	 * 2.跟新文章信息
	 * 3.跟新视屏信息
	 */
	@Override
	public Response updateVideoManagers(VideoManagerEntityDto dto) {
		 Long id = dto.getId();
		 dto.setCreateTime(new Date());
		videoArticleEntityMapper.deleteByVideoArticleEntityByVideo(id);
		videoGoodsMapper.deleteVideoGoodsByVideo(id);
		List<VideoArticleEntity> articleList = dto.getVideoArticleEntityList();
		if(articleList!=null&&articleList.size()>0) {
			for(VideoArticleEntity item:articleList) {
				item.setVideoId(id);
				videoArticleEntityMapper.insert(item);
			}
		}
		List<VideoGoodsEntity> goodsList = dto.getVideoGoodsEntityList();
		if(goodsList!=null&&goodsList.size()>0) {
			for(VideoGoodsEntity item:goodsList) {
				item.setVideoId(id);
				String movePicPath = item.getMovePicPath();
				String picPath = item.getPicPath();
				if(movePicPath!=null&&!movePicPath.equals("")) {
					item.setPicPath(movePicPath);
				}
				if(picPath!=null&&!picPath.equals("")) {
					item.setMovePicPath(picPath);
				}
				videoGoodsMapper.insert(item);
			}
		}
		videoManagerMapper.updateById(dto);
		initQuence(dto.getType());
		return Response.returnCode(ReturnCode.SUCCESS.getCode(),ReturnCode.SUCCESS.getMsg());	
	}

	/**
	 * 查看详情
	 */
	@Override
	public Response selectVideoManagerDetail(VideoManagerEntityDto dto) {
		Long id = dto.getId();
		String videoClickNum="videoclicknum"+id;
		VideoManagerEntityDto dtoNew = videoManagerMapper.selectVideoManagerDetail(id);
		try {
			String actuNum = redisClient.get(videoClickNum);
			if(actuNum!=null&&actuNum.equals("")) {
				int actuNumInt = Integer.parseInt(actuNum);
				dtoNew.setActualNum(dtoNew.getActualNum()+actuNumInt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<VideoArticleEntity> artList = videoArticleEntityMapper.slectVideoArticleEntityByVideo(id);
		List<VideoGoodsEntity> goodsList = videoGoodsMapper.selectVideoGoodsByVideo(id);
		if(artList!=null&&artList.size()>0) {
			dtoNew.setVideoArticleEntityList(artList);
		}
		if(goodsList!=null&&goodsList.size()>0) {
			dtoNew.setVideoGoodsEntityList(goodsList);
		}
		return Response.ok(dtoNew);
	}

	@Override
	public Response deleteManager(VideoManagerEntityDto dto) {
	    Long id = dto.getId();
	    
		videoArticleEntityMapper.deleteByVideoArticleEntityByVideo(id);
		videoGoodsMapper.deleteVideoGoodsByVideo(id);
		videoManagerMapper.deleteById(id);
		initQuence(dto.getType());
		return Response.returnCode(ReturnCode.SUCCESS.getCode(),ReturnCode.SUCCESS.getMsg());	
	}

	@Override
	public Response updateVideoManagerUp(VideoManagerEntityDto dto) {
		dto.setUpdateStatus("2");
		initQuence(dto.getType());
		dto.setCreateTime(new Date());
		return Response.ok(videoManagerMapper.updateById(dto));
	}

	@Override
	public Response updateVideoManagerDown(VideoManagerEntityDto dto) {
		dto.setUpdateStatus("3");
		initQuence(dto.getType());
		dto.setCreateTime(new Date());
		return Response.ok(videoManagerMapper.updateById(dto));
	}

	//查询文章列表
	@Override
	public Map<String, Object> selectVideoArticleList(VideoArticleEntityDto dto) {
		Map<String,Object> map=new HashMap<>();
		List<VideoArticleEntity> list = videoArticleEntityMapper.selectVideoArticleList(dto);
		Integer totals = videoArticleEntityMapper.selectVideoArticleListCount(dto);
		map.put("list", list);
		map.put("totals", totals);
		return map;
	}

	//视频查看
	/**
	 * 1.首先判断是否存在该list 如果不存在则创建，如果存在直接取出来
	 * 2.list里面存放的是视频id相关信息，根据id 再从缓存中取出来，放到list中
	 * 
	 */
	@Override
	public Response selectVideoManagerListForApp(VideoManagerEntityDto dto) throws Exception {
		Integer pageNo = dto.getPageNo();
		Integer sizes = dto.getPageSize();
		String type = dto.getType();//1是健康首页  2商城首页
		String keyStr="viveomanager"+type;
		List<String> list = redisClient.getList(keyStr, Long.valueOf(pageNo), Long.valueOf(sizes));
		List<VideoManagerEntityDto> dtoList=new ArrayList<>();
		if(!(list!=null&&list.size()>0)) {
			initQuence(type);
			list = redisClient.getList(keyStr, Long.valueOf(pageNo), Long.valueOf(sizes));
		}
		if(list!=null&&list.size()>0) {
			for(String item:list) {
				String itemValue = redisClient.get(item);
				VideoManagerEntityDto dto1 = JSON.parseObject(itemValue, new TypeReference<VideoManagerEntityDto>() {});
				String videoClickNum="videoclicknum"+dto1.getId();
				String actuNum = redisClient.get(videoClickNum);
				if(actuNum!=null&&actuNum.equals("")) {//此地方需要   展示阅读量问题
					int actuNumInt = Integer.parseInt(actuNum);
					dto1.setActualNum(actuNumInt+dto1.getActualNum()+dto1.getInitNum());
				}else {
					dto1.setActualNum(dto1.getInitNum()+dto1.getActualNum());
				}
				dtoList.add(dto1);
			}
		}
		return Response.ok(dtoList);
	}
	
	
	// 增加视屏点击量
	@Override
	public Response insertVideoclickNum(VideoManagerEntityDto dto) throws Exception {
		Long id = dto.getId();
		String videoClickNum="videoclicknum"+id;
		return Response.ok(redisClient.setInc(videoClickNum));
	}
	
	
	//移动端查看视屏相关的文章
	@Override
	public Response selectVideoManagerRelative(VideoManagerEntityDto dto) {
		Long id = dto.getId();
		VideoManagerEntityDto dtoNew = videoManagerMapper.selectVideoManagerDetail(id);
		long ids=dtoNew.getId();
		int actuanum=dtoNew.getActualNum();
		String videoClickNum="videoclicknum"+ids;
		try {
			String incNum = redisClient.get(videoClickNum);
			if(incNum!=null&&!incNum.equals("")) {
				dtoNew.setActualNum(actuanum+Integer.parseInt(incNum));
			}else {
				dtoNew.setActualNum(actuanum);
			}				
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<VideoArticleEntity> artList = videoArticleEntityMapper.slectVideoArticleEntityByVideo(id);
		List<VideoGoodsEntity> goodsList = videoGoodsMapper.selectVideoGoodsByVideo(id);
		if(artList!=null&&artList.size()>0) {
			for(VideoArticleEntity item:artList) {
				int realReadint=0;
				Integer realRead = item.getRealRead();
				if(realRead!=null) {
					realReadint=realRead;
				}
				try {
					String infoRead="infomationreadclicknum"+item.getArticleId();
					String readNum = redisClient.get(infoRead);
					if(readNum!=null&&!readNum.equals("")) {
						item.setRealRead(realReadint+Integer.parseInt(readNum));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			dtoNew.setVideoArticleEntityList(artList);
		}
		if(goodsList!=null&&goodsList.size()>0) {
			dtoNew.setVideoGoodsEntityList(goodsList);
		}
		return Response.ok(dtoNew);
	}
}
