package com.wjj.application.dto.pennyRob;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.bind.annotation.RequestParam;

import com.wjj.application.entity.pennyRob.MkGoodsSku;
import com.wjj.application.entity.pennyRob.MkGoodsSpu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "1分抢-活动商品spu")
public class MkGoodsSpuDTO extends MkGoodsSpu{
	@ApiModelProperty(value = "每页数量", name = "pageSize")
	private Integer pageSize;
	@ApiModelProperty(value = "当前页码", name = "currPage")
	private int currPage;

	private Integer limit;

	private List<MkGoodsSku> goodsSkuList;
	private List<MkStandardDTO> standardDTOList;

	private String updateStart;
	private String updateEnd;

	//首页列表产品图片路径
	private String movePicPath;
	//活动开始时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date activityStart;
	//活动结束时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date activityEnd;


	private Integer skuId;
	private Integer addrId;
	private String userId;
	private String orderRemarks;
	private String parentInviteCode;
	private String nickname;
	private String phone;
	private String openId;
	private String formId;

	//倒计时
	private Long distanceEndTime;
	
	private List<Integer> skuIds;
}
