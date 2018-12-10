package com.wjj.application.service.infomation.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wjj.application.dto.infomation.HealthInformationDto;
import com.wjj.application.entity.information.HealthArticle;
import com.wjj.application.entity.information.HealthInfoParentTag;
import com.wjj.application.entity.information.HealthInfoTag;
import com.wjj.application.entity.information.HealthInfomation;
import com.wjj.application.mapper.infomation.HealthArticleMapper;
import com.wjj.application.mapper.infomation.HealthInfoTagMapper;
import com.wjj.application.mapper.infomation.HealthInfomationMapper;
import com.wjj.application.mapper.video.VideoArticleEntityMapper;
import com.wjj.application.response.Response;
import com.wjj.application.response.ReturnCode;
import com.wjj.application.service.infomation.HealthInfomationService;
import com.wjj.application.util.RedisClient;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HealthInfomationServiceImpl   extends ServiceImpl<HealthInfomationMapper, HealthInfomation> implements HealthInfomationService {
	
	@Autowired
	private HealthInfomationMapper healthInfomationMapper;
	
	@Autowired
	private RedisClient  redisClient;
	
	@Autowired
	private  HealthArticleMapper healthArticleMapper;
	
	@Autowired
	private HealthInfoTagMapper healthInfoTagMapper;
	
	@Autowired
	private VideoArticleEntityMapper videoArticleEntityMapper;
	
	
	
	@Override
	public Map<String, Object> list(HealthInformationDto dto) {
		Map<String,Object> map=new HashMap<String,Object>();
		List<HealthInformationDto> list = healthInfomationMapper.selectHealthInfomationList(dto);
		if(list!=null&&list.size()>0) {
			for(HealthInformationDto items:list) {
				String infoRead="infomationreadclicknum"+items.getId();
    			String infoLike="infomationlikeclicknum"+items.getId();
    			try {
					String readNum = redisClient.get(infoRead);
					if(readNum!=null&&!readNum.equals("")) {
						Integer realRead = items.getRealRead();
						if(realRead!=null) {
							items.setRealRead(items.getRealRead()+Integer.parseInt(readNum));
						}else {
							items.setRealRead(Integer.parseInt(readNum));
						}
						
					}
					
					String likeNum = redisClient.get(infoLike);
					if(likeNum!=null&&!likeNum.equals("")) {
						Integer realLike = items.getRealLike();
						if(realLike!=null) {
							items.setRealLike(items.getRealLike()+Integer.parseInt(likeNum));
						}else {
							items.setRealLike(Integer.parseInt(likeNum));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			
			}
			
			
		}
		
		
		
		
		
		Integer total = healthInfomationMapper.selectHealthInfomationListCount(dto);
		map.put("list", list);
		map.put("total", total);
		return map;
	}

	public String increateNo() throws Exception {
		//  设置过期时间  24小时 =86400
		Long setInc = redisClient.setIncAndTime("HealthInfomationId",24*60*60);
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String format = df.format(new Date());
		String incStr=String.valueOf(setInc);
		String result = String.format("%0" + 6 + "d", Integer.parseInt(incStr));
		String goodsId="ZX"+format+result;
		return goodsId;
	}
	
	/**
	 * 1.新建资讯模块
	 * 2.新增文章记录
	 * 3.新建标签记录
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor = {RuntimeException.class })
	public Response insertHealthInformationDto(HealthInformationDto dto) throws Exception {
		dto.setCreateTime(new Date());
		String increateNo = this.increateNo();
		dto.setId(increateNo);
		healthInfomationMapper.insert(dto);
		String remark=dto.getRemark();
		if(remark!=null && !remark.equals("")) {
			HealthArticle healthArticle=new HealthArticle();
			healthArticle.setInfoId(increateNo);
			healthArticle.setRemark(remark);
			healthArticleMapper.insert(healthArticle);
		}
		List<HealthInfoParentTag> healthParentTag = dto.getHealthInfoParentTag();
		if(healthParentTag!=null&&healthParentTag.size()>0) {
			for(HealthInfoParentTag item:healthParentTag) {
				HealthInfoTag healthTagParent=new HealthInfoTag();
					healthTagParent.setInfoId(increateNo);
					healthTagParent.setTagLevel("1");
					healthTagParent.setTagType(item.getTagType());
					healthTagParent.setTagTypeName(item.getTagTypeName());
					healthTagParent.setTagValue(item.getTagValue());
					healthTagParent.setTagName(item.getTagName());
					healthInfoTagMapper.insert(healthTagParent);
					List<HealthInfoTag> healthTagList = item.getHealthInfoTagList();//插入子标签
					if(healthTagList!=null&&healthTagList.size()>0) {
						for(HealthInfoTag  item1:healthTagList) {
							item1.setTagLevel("2");
							item1.setTagType(item.getTagType());
							item1.setTagTypeName(item.getTagTypeName());
							item1.setInfoId(increateNo);
							item1.setParentId(healthTagParent.getId());	
							healthInfoTagMapper.insert(item1);
						}
						
					}	
			}
		}
		return Response.returnCode(ReturnCode.SUCCESS.getCode(),ReturnCode.SUCCESS.getMsg());
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor = {RuntimeException.class })
	public Response updateHealthInformationDto(HealthInformationDto dto) throws Exception {
		String increateNo =dto.getId();
		dto.setId(increateNo);
		healthInfomationMapper.updateHealthInfomation(dto);
		healthArticleMapper.deleteHealthArticle(increateNo);
		String remark=dto.getRemark();
		if(remark!=null && !remark.equals("")) {
			HealthArticle healthArticle=new HealthArticle();
			healthArticle.setInfoId(increateNo);
			healthArticle.setRemark(remark);
			healthArticleMapper.insert(healthArticle);
		}
		
		healthInfoTagMapper.deleteHealthInfoTag(increateNo);
		List<HealthInfoParentTag> healthParentTag = dto.getHealthInfoParentTag();
		if(healthParentTag!=null&&healthParentTag.size()>0) {
			for(HealthInfoParentTag item:healthParentTag) {
				HealthInfoTag healthTagParent=new HealthInfoTag();
					healthTagParent.setInfoId(increateNo);
					healthTagParent.setTagLevel("1");
					healthTagParent.setTagType(item.getTagType());
					healthTagParent.setTagTypeName(item.getTagTypeName());
					healthTagParent.setTagValue(item.getTagValue());
					healthTagParent.setTagName(item.getTagName());
					healthInfoTagMapper.insert(healthTagParent);
					List<HealthInfoTag> healthTagList = item.getHealthInfoTagList();//插入子标签
					if(healthTagList!=null&&healthTagList.size()>0) {
						for(HealthInfoTag  item1:healthTagList) {
							item1.setTagLevel("2");
							item1.setTagType(item.getTagType());
							item1.setTagTypeName(item.getTagTypeName());
							item1.setInfoId(increateNo);
							item1.setParentId(healthTagParent.getId());	
							healthInfoTagMapper.insert(item1);
						}
						
					}	
			}
		}
		videoArticleEntityMapper.updateVideoArticleByHealthInfomation(dto);
		return Response.returnCode(ReturnCode.SUCCESS.getCode(),ReturnCode.SUCCESS.getMsg());
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor = {RuntimeException.class })
	public Response delHealthInformationDto(HealthInformationDto dto) {
		String id = dto.getId();
		healthInfoTagMapper.deleteHealthInfoTag(id);
		healthArticleMapper.deleteHealthArticle(id);
		healthInfomationMapper.delHealthInformationDto(id);
		return Response.returnCode(ReturnCode.SUCCESS.getCode(),ReturnCode.SUCCESS.getMsg());
	}

	/**
	 * 上架
	 */
	@Override
	public Response updateHealthInformationDtoUp(HealthInfomation dto) throws Exception {
		HealthInformationDto dtoNew=new HealthInformationDto();
		dtoNew.setInfoStatus("2");
		dtoNew.setId(dto.getId());
		healthInfomationMapper.updateHealthInfomation(dtoNew);
		return Response.returnCode(ReturnCode.SUCCESS.getCode(),ReturnCode.SUCCESS.getMsg());
	}
	
	/**
	 * 下架
	 */
	@Override
	public Response updateHealthInformationDtoDown(HealthInfomation dto) throws Exception {
		HealthInformationDto dtoNew=new HealthInformationDto();
		dtoNew.setInfoStatus("3");
		dtoNew.setId(dto.getId());
		healthInfomationMapper.updateHealthInfomation(dtoNew);
		HealthInfomation entity = healthInfomationMapper.selectById(dto.getId());
		String groups = entity.getGroups();
		if(groups!=null&&groups.equals("1")) {
			String id = dto.getId();
			videoArticleEntityMapper.deleteArticleById(id);
		}
		
		
		return Response.returnCode(ReturnCode.SUCCESS.getCode(),ReturnCode.SUCCESS.getMsg());
	}
	
	/**
	 * 运营端查看详情
	 */
	@Override
	public Response selectHealthInformationById(HealthInfomation dto) {
		String id = dto.getId();
		HealthInformationDto dtoNew = healthInfomationMapper.selectHealthInformationById(id);
		String groups = dtoNew.getGroups();
		if(groups!=null && groups.equals("1")) {
			HealthArticle article = healthArticleMapper.selectHealthArticleById(id);
			if(article!=null) {
				dtoNew.setRemark(article.getRemark());
			}
		}
		List<HealthInfoParentTag> parentTag = healthInfoTagMapper.selectHealthInfoTagParent(id);
		if(parentTag!=null&&parentTag.size()>0) {
			for(HealthInfoParentTag item:parentTag) {
				Long id2 = item.getId();
				List<HealthInfoTag> list2 = healthInfoTagMapper.selectHealthInfoByTagParent(id2);
				if(list2!=null&&list2.size()>0) {
					item.setHealthInfoTagList(list2);
				}
			}
			dtoNew.setHealthInfoParentTag(parentTag);
		}
		return Response.ok(dtoNew);
	}	
}
