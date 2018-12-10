package com.wjj.application.service.index;

import java.util.Map;

import com.wjj.application.dto.index.InfomationIndexDto;
import com.wjj.application.dto.marketing.MarketingGoodsDto;
import com.wjj.application.dto.video.VideoManagerEntityDto;
import com.wjj.application.entity.information.HealthInfomation;
import com.wjj.application.response.Response;

public interface AdAndGoodsHomeMallService {

	Response  selectAdAndGoodsHomeMall();
	
	public Map<String,Object> selectMarketGoodsList(MarketingGoodsDto dto);
	
	
	public Map<String,Object> selectVedioGoodsList(VideoManagerEntityDto dto);
	
	
	public Response selectHealthHomeMallIndex();
	
	public Response selectHealthInfo(InfomationIndexDto dto);
	
	//增加阅读量资讯
	public Response insertHealthRead(HealthInfomation dto) throws Exception;
	
	//喜欢资讯
	public Response insertHealthLike(HealthInfomation dto) throws Exception;
	
	//取消喜欢资讯
	public Response cancelHealthLike(HealthInfomation dto) throws Exception;
	
	//带bannel图的营销(app专用)
	Response selectAdvertisingManagerAndBannelList(MarketingGoodsDto dto);
	
	//查看文章详情
	Response  selectHealthInformationDto(HealthInfomation entity);
	
}
