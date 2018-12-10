package com.wjj.application.dto.infomation;

import java.util.List;

import com.wjj.application.entity.information.HealthInfoParentTag;
import com.wjj.application.entity.information.HealthInfomation;
import com.wjj.application.entity.marketing.MarketingGoods;
import com.wjj.application.entity.video.VideoArticleEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



@Data
@ApiModel(value="健康资讯扩展")
public class HealthInformationDto  extends HealthInfomation{

	private Integer pageNo;
	
	private Integer pageSize;

	
	@ApiModelProperty(value="类型名称" , name="typeName")
	private String typeName;
	
	@ApiModelProperty(value="文章内容" , name="remark")
	private String remark;
	
	@ApiModelProperty(value="标签列表" , name="healthInfoParentTag")
	private  List<HealthInfoParentTag> healthInfoParentTag;
	
	
	@ApiModelProperty(value="视频相关商品" , name="videoGoodsEntityList")
	private List<MarketingGoods> marketingGoodsList;
	
	@ApiModelProperty(value="视频相关文章" , name="videoArticleEntityList")
	private List<VideoArticleEntity>  videoArticleEntityList;
}
