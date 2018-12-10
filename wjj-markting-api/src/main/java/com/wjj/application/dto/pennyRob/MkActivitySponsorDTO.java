package com.wjj.application.dto.pennyRob;

import com.wjj.application.entity.pennyRob.MkActivitySponsor;
import com.wjj.application.security.AuthTokenDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "1分抢-活动发起表DTO")
public class MkActivitySponsorDTO extends MkActivitySponsor {
	@ApiModelProperty(value = "每页数量", name = "pageSize")
	private Integer pageSize;
	@ApiModelProperty(value = "当前页码", name = "currPage")
	private int currPage;

	//分页查询index参数
	private Integer limit;


	private String nicknameAndPhone;


	private Integer sponsorId;
	private String smsCode;
	private String graphCode;
	private String headPic;
	private String unionid;

    private String province;
    private String city;
    private String area;
    private String sex;
    private Long uniqueCode;

	//商品名称
	private String goodName;
	//支付时间
	private Date payTime;

	private Date payStartTime;
	private Date payEndTime;
	
	private AuthTokenDTO authTokenDTO;
	

}
