package com.wjj.application.service.pennyRob;

import com.baomidou.mybatisplus.plugins.Page;
import com.wjj.application.entity.pennyRob.MkActivityRobExt;
import com.wjj.application.entity.pennyRob.MkActivitySponsor;
import com.baomidou.mybatisplus.service.IService;
import com.wjj.application.dto.pennyRob.MkActivitySponsorDTO;
import com.wjj.application.entity.pennyRob.MkActivitySponsor;
import com.wjj.application.page.PageDTO;
import com.wjj.application.response.Response;
import com.wjj.application.security.AuthTokenDTO;
import com.wjj.application.util.PageVO;

import java.util.HashMap;
import java.util.List;


/**
 * <p>
 * 活动发起表 服务类
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */
public interface MkActivitySponsorService extends IService<MkActivitySponsor> {


	PageDTO<MkActivitySponsor> selectActivitySponsorList(MkActivitySponsorDTO mkActivitySponsorDTO);

    Response inviteList(Integer id);

    Response myActivityList(String userId, Integer pageNo, Integer pageSize, Integer status);

	Response rob(Integer sponsorId, String phone, String openId, String smsCode, String graphCode,
			String headPic, String nickname,  String unid,String province,
			String city,String area,String sex, AuthTokenDTO authTokenDTO,Long uniqueCode)  throws Exception;



    Response orderList(PageVO pageVO);

    Response orderDetail(MkActivitySponsorDTO mkActivitySponsorDTO);

    void myActivityDelete(MkActivitySponsorDTO mkActivitySponsorDTO) throws Exception;

	void test111();
}
