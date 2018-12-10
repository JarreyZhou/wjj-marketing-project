package com.wjj.application.service.pennyRob.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wjj.application.common.FastJsonUtils;
import com.wjj.application.dto.AccountCustomerInfoDTO;
import com.wjj.application.dto.CustomerLoginCommonDTO;
import com.wjj.application.dto.GoodsMainInfoDto;
import com.wjj.application.dto.MessageSmsCommonDTO;
import com.wjj.application.dto.OrdersCommitRequestDTO;
import com.wjj.application.dto.OrdersDetailDTO;
import com.wjj.application.dto.OrdersNoQueryDTO;
import com.wjj.application.dto.OrdersViewDTO;
import com.wjj.application.dto.RequestMainGoods;
import com.wjj.application.dto.pennyRob.MkActivitySponsorDTO;
import com.wjj.application.entity.OrdersProductInfo;
import com.wjj.application.entity.pennyRob.MkActivity;
import com.wjj.application.entity.pennyRob.MkActivityRob;
import com.wjj.application.entity.pennyRob.MkActivityRobExt;
import com.wjj.application.entity.pennyRob.MkActivitySponsor;
import com.wjj.application.entity.pennyRob.MkGoodsSku;
import com.wjj.application.entity.pennyRob.MkGoodsSpu;
import com.wjj.application.enums.ActivitySponsorStatus;
import com.wjj.application.enums.BackCode;
import com.wjj.application.feign.AccountFeignClient;
import com.wjj.application.feign.GoodsMainInfoFeignClient;
import com.wjj.application.feign.MessageFeignClient;
import com.wjj.application.feign.OrderFeignClient;
import com.wjj.application.mapper.pennyRob.MkActivityMapper;
import com.wjj.application.mapper.pennyRob.MkActivityRobMapper;
import com.wjj.application.mapper.pennyRob.MkActivitySponsorMapper;
import com.wjj.application.mapper.pennyRob.MkGoodsSpuMapper;
import com.wjj.application.page.PageDTO;
import com.wjj.application.response.Response;
import com.wjj.application.response.ReturnCode;
import com.wjj.application.security.AuthTokenDTO;
import com.wjj.application.service.pennyRob.MkActivityRobService;
import com.wjj.application.service.pennyRob.MkActivityService;
import com.wjj.application.service.pennyRob.MkActivitySponsorService;
import com.wjj.application.service.pennyRob.MkGoodsSkuService;
import com.wjj.application.service.pennyRob.MkGoodsSpuService;
import com.wjj.application.service.pennyRob.MkWxPushService;
import com.wjj.application.util.LockException;
import com.wjj.application.util.PageVO;
import com.wjj.application.util.RedisClient;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>
 * 活动发起表 服务实现类
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */
@Service
@Slf4j
public class MkActivitySponsorServiceImpl extends ServiceImpl<MkActivitySponsorMapper, MkActivitySponsor> implements MkActivitySponsorService {
	@Autowired
	private MkGoodsSpuService mkGoodsSpuService;
	@Autowired
	private MkGoodsSkuService mkGoodsSkuService;
	@Autowired
	private MkActivityRobService mkActivityRobService;
	@Autowired
	private MkActivityMapper mkActivityMapper;
	@Autowired
	private MkGoodsSpuMapper mkGoodsSpuMapper;
	@Autowired
	private MkActivityRobMapper mkActivityRobMapper;
	@Autowired
	private MkActivitySponsorMapper mkActivitySponsorMapper;
	@Autowired
	private GoodsMainInfoFeignClient goodsMainInfoFeignClient;

	@Autowired
	private OrderFeignClient orderFeignClient;

	@Autowired
	private AccountFeignClient accountFeignClient;

    @Autowired
    private RedisClient redisClient;

    @Autowired
	private MkActivitySponsorService mkActivitySponsorService;
    @Autowired
    private MkActivityService mkActivityService;
	@Autowired
	private MessageFeignClient messageFeignClient;
	 @Autowired
	    private MkWxPushService mkWxPushService;
	/**
	 * 帮抢者帮抢注册 liuxin
	 */
	@Transactional
	@Override
    public 	Response rob(Integer sponsorId, String phone, String openId, String smsCode, String graphCode, 
			String headPic, String nickname,  String unid,String province,String city,
			String area,String sex, AuthTokenDTO authTokenDTO,Long uniqueCode) throws Exception{
	
		//请求接口达到5次数，展示图形验证码
		if(StringUtils.isBlank(graphCode)){
			Long value=redisClient.setIncAndTime2("ROB_"+openId,60*60*24);
			if(value>5){
				return Response.returnCode(BackCode.GRAPH_CODE.getCode(),BackCode.GRAPH_CODE.getMsg());
			}
		}else{
			//校验图形验证码
			String captchaId = redisClient.get("kaptcha_"+openId);
			// 2018-11-25 报空指针
			if(captchaId!=null&&!captchaId.equals("")&&graphCode!=null&&!graphCode.equals("")) {
				if(!captchaId.toLowerCase().equals(graphCode.toLowerCase())){
					return Response.returnCode(BackCode.GRAPH_CODE_FAIL.getCode(),BackCode.GRAPH_CODE_FAIL.getMsg());
				}
			}	
		}
		
		//获取活动发起数据
		MkActivitySponsor mkActivitySponsor=this.selectById(sponsorId);
		if(mkActivitySponsor==null){
			return Response.returnCode(BackCode.FAIL.getCode(),"无效的sponsorId");
		}
		
		
//		//获取spu数据的所需帮抢者人数
//		MkGoodsSpu mkGoodsSpu3=mkGoodsSpuService.selectById(mkActivitySponsor.getSpuId());
//		//微信推送
//		return sendActivityFail(formId, mkActivitySponsor, mkGoodsSpu3);
		
		
		// 查询此时发起人的邀请码
		CustomerLoginCommonDTO request = new CustomerLoginCommonDTO();
		request.setAccountNo(mkActivitySponsor.getUserId());
		Response response= accountFeignClient.getMemberInfoByAccountNo(request);
		Object dataRes=response.getData();
		AccountCustomerInfoDTO account= FastJsonUtils.toBean(FastJsonUtils.toJSONString(dataRes), AccountCustomerInfoDTO.class);
		if(StringUtils.isEmpty(account.getInviteCode())){
			if(StringUtils.isEmpty(account.getParentInviteCode())){
				mkActivitySponsor.setParentInviteCode(null);
			}else{
				mkActivitySponsor.setParentInviteCode(account.getParentInviteCode());
			}
		}else{
			mkActivitySponsor.setParentInviteCode(account.getInviteCode());
		}
		
		// 消息中心进行验证码校验
		Response smsCoderesponse=smsCodeVa(phone, smsCode,uniqueCode);
		if(!smsCoderesponse.getCode().equals(BackCode.SUCCESS.getCode())){
			return smsCoderesponse;
		}
		//去调用注册接口
	    Response loginresponse = login(phone, smsCode, headPic, nickname, unid, province, city, area, sex,
				mkActivitySponsor,uniqueCode);
	    if(!loginresponse.getCode().equals(BackCode.SUCCESS.getCode())){
			return loginresponse;
		}
	  
		Object data=loginresponse.getData();
		AccountCustomerInfoDTO a= FastJsonUtils.toBean(FastJsonUtils.toJSONString(data), AccountCustomerInfoDTO.class);
		log.info(a.getAccountNo()+"是否新用户:"+(a.getIsNew()==null||!a.getIsNew().equals("1")));
		//注册过的用户不能再帮抢了 IsNew是否新用户,0否，1是
		if(a.getIsNew()==null||!a.getIsNew().equals("1")){
			Response res = new Response();
			res.setCode(BackCode.ROB_FAIL.getCode());
			res.setMessage(BackCode.ROB_FAIL.getMsg());
			res.setData(data);
			return res;
		}
		
		Integer spuId=mkActivitySponsor.getSpuId();
		Integer skuId=mkActivitySponsor.getSkuId();
		Integer newrobcount=mkActivitySponsor.getRobCount()+1;//帮强者+1
		//获取spu数据的所需帮抢者人数
		MkGoodsSpu mkGoodsSpu=mkGoodsSpuService.selectById(spuId);
		if(mkGoodsSpu==null){
			return Response.returnCode(BackCode.FAIL.getCode(),"查不到的spuId");
		}
		MkGoodsSku mkGoodsSku=mkGoodsSkuService.selectById(skuId);
		if(mkGoodsSku==null){
			return Response.returnCode(BackCode.FAIL.getCode(),"查不到的skuId");
		}
		Integer needrobCount=mkGoodsSpu.getNeedrobCount();
		if(needrobCount.equals(newrobcount)){
			//帮抢成功，达到帮抢人数
			Integer skuRemainStock=mkGoodsSku.getRemainStock();
			if(skuRemainStock<1){//库存不足无法下单
				log.info("库存不足无法下单【sku参数:】"+FastJsonUtils.toJSONString(mkGoodsSku)+"【用户参数昵称:】"+nickname+"【用户参数手机号:】"+phone);
				//活动表帮抢总数+1
				activityBangqiang(mkActivitySponsor,false);
				
				//spu 更新spu订单数和帮抢人总数
				updatespu(mkGoodsSpu,false);
				//活动发起表帮抢总数+1,插入帮强者表,更新订单状态
				bangqiangOrderFail(sponsorId, phone, headPic, nickname, mkActivitySponsor, a, newrobcount);
				
				//微信推送
				sendActivityFail(mkActivitySponsor, mkGoodsSpu);
			}else{
				log.info("库存足够开始下单【sku参数:】"+FastJsonUtils.toJSONString(mkGoodsSku)+"【用户参数昵称:】"+nickname+"【用户参数手机号:】"+phone);
				//活动表订单量+1,帮抢总数+1
				activityBangqiang(mkActivitySponsor,true);
				
				//spu 更新spu订单数和帮抢人总数,剩余库存
				updatespu(mkGoodsSpu,true);
				//sku 更新剩余库存
				updatesku(mkGoodsSku);
				
				//最后一个帮抢的，执行下单操作
				authTokenDTO.setAccountNo(mkActivitySponsor.getUserId());
				authTokenDTO.setPhone(mkActivitySponsor.getPhone());
				authTokenDTO.setUserName(mkActivitySponsor.getNickname());
				Response orderResponse=createOrder(mkActivitySponsor, mkGoodsSpu,mkGoodsSku,  authTokenDTO);
				if(!orderResponse.getCode().equals(Response.ok().getCode())){
					return orderResponse;
				}
				//发送短信
				sendSms(mkActivitySponsor);
				//微信推送
				sendActivitySuccess( mkActivitySponsor, mkGoodsSpu);
				
				JSONObject jo =JSON.parseObject(JSON.toJSONString((orderResponse.getData())));//订单号
				String tradeNo=jo.getString("tradeNo");//贸易单号
				JSONArray ordersNoList=jo.getJSONArray("ordersNoList");
				String orderNo=ordersNoList.getString(0);//订单号
				//活动发起表帮抢总数+1,设置订单号，贸易单号,插入帮强者表,更新订单状态
				bangqiangOrderSuccess(sponsorId, phone, headPic, nickname, mkActivitySponsor, a, newrobcount,tradeNo,orderNo);
			
			}
			
			return Response.ok(data);
		}else if(needrobCount>newrobcount){
			//不是最后一个帮抢的。发起表帮抢者+1，帮抢总数+1
			bangqiang(sponsorId, phone, headPic, nickname, mkActivitySponsor, a, newrobcount);
			//活动表订单量,帮抢总数+1
			activityHelp(mkActivitySponsor);
			
			//更新spu帮抢人总数
			Integer robCount=mkGoodsSpu.getRobCount();
			MkGoodsSpu mkGoodsSpu2=new MkGoodsSpu();
			mkGoodsSpu2.setId(mkGoodsSpu.getId());
			mkGoodsSpu2.setRobCount(++robCount);
			mkGoodsSpu2.setVersion(mkGoodsSpu.getVersion());
			boolean flag=mkGoodsSpuService.updateById(mkGoodsSpu2);
			if(!flag){
				throw new LockException("乐观锁异常！");
			}
			return Response.ok(data);
		}else if(needrobCount<newrobcount){
			//超出所需帮抢人数，帮抢失败
			return Response.returnCode(BackCode.FAIL.getCode(),"来晚啦，活动已发起成功，帮抢失败！");
		}
		return Response.fail();
	}


//	活动发起成功通知
	private Response sendActivitySuccess( MkActivitySponsor mkActivitySponsor, MkGoodsSpu mkGoodsSpu)
			 {
		try {
			/**
			 * 订单进度通知
	商品名称：XXXXXXXXX
	进度详情：恭喜您1分抢活动发起成功，赶紧在30分钟内支付1分钱完成订单吧！
	订单金额：0.01元
	备注：1分抢好货马上到手，快来拿~点击立即领取~
			 * */
			//订单进度提醒 vV8IT1DJjyiLayGM1JURsduLmw7egahEI-rvVZ6GFFo
			//待付款提醒 2AVs56U8pMFYhf25OL2Mzu3kugbRzItbtTk8opDa9rc
			JSONObject weixinjo=new JSONObject();
			weixinjo.put("keyword1",createWeixinData(mkGoodsSpu.getName()));
			weixinjo.put("keyword2",createWeixinData("恭喜您1分抢活动发起成功，赶紧在30分钟内支付1分钱完成订单吧！"));
			weixinjo.put("keyword3",createWeixinData("0.01元"));
			weixinjo.put("keyword4",createWeixinData("1分抢好货马上到手，快来拿~点击立即领取~"));
			String page="pages/organize/main?id="+mkActivitySponsor.getId();
			log.info("sendActivitySuccess微信推送入参："+mkActivitySponsor.getOpenId()+","+"vV8IT1DJjyiLayGM1JURsduLmw7egahEI-rvVZ6GFFo"+","+mkActivitySponsor.getFormId()+","+JSON.toJSONString(weixinjo)+","+page);
			Response response=mkWxPushService.sendTemplateMessage(mkActivitySponsor.getOpenId(), "vV8IT1DJjyiLayGM1JURsduLmw7egahEI-rvVZ6GFFo", mkActivitySponsor.getFormId(), JSON.toJSONString(weixinjo),page);
			log.info("sendActivitySuccess mkWxPushService.sendTemplateMessage返回参数："+FastJsonUtils.toJSONString(response));
			return response;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.fail();
		}
		
	}
	//活动发起失败通知
	private Response sendActivityFail( MkActivitySponsor mkActivitySponsor, MkGoodsSpu mkGoodsSpu)
			 {
		try{
			/**
			 * 订单进度通知
	商品名称：XXXXXXXXX
	进度详情：很遗憾1分抢活动失败，别灰心，下轮活动继续加油噢！
	订单金额：0.01元
	备注：1分抢好货马上到手，快来拿~点击立即领取~
			 * */
			//订单进度提醒 vV8IT1DJjyiLayGM1JURsduLmw7egahEI-rvVZ6GFFo
			//待付款提醒 2AVs56U8pMFYhf25OL2Mzu3kugbRzItbtTk8opDa9rc
			JSONObject weixinjo=new JSONObject();
			weixinjo.put("keyword1",createWeixinData(mkGoodsSpu.getName()));
			weixinjo.put("keyword2",createWeixinData("很遗憾1分抢活动失败，别灰心，下轮活动继续加油噢！"));
			weixinjo.put("keyword3",createWeixinData("0.01元"));
			weixinjo.put("keyword4",createWeixinData("1分抢好货马上到手，快来拿~点击立即领取~"));
			String page="pages/organize/main?id="+mkActivitySponsor.getId();
			log.info("sendActivityFail微信推送入参："+mkActivitySponsor.getOpenId()+","+"vV8IT1DJjyiLayGM1JURsduLmw7egahEI-rvVZ6GFFo"+","+mkActivitySponsor.getFormId()+","+JSON.toJSONString(weixinjo)+","+page);
			Response response=mkWxPushService.sendTemplateMessage(mkActivitySponsor.getOpenId(), "vV8IT1DJjyiLayGM1JURsduLmw7egahEI-rvVZ6GFFo", mkActivitySponsor.getFormId(), JSON.toJSONString(weixinjo),page);
			log.info("sendActivityFail mkWxPushService.sendTemplateMessage返回参数："+FastJsonUtils.toJSONString(response));
			return response;
		 } catch (Exception e) {
				log.error(e.getMessage(), e);
				return Response.fail();
		}
		
	}


	private JSONObject createWeixinData(String value) {
		JSONObject jo2=new JSONObject();
		jo2.put("value", value);
		return jo2;
	}



	private Response sendSms(MkActivitySponsor mkActivitySponsor) {
		MessageSmsCommonDTO dto=new MessageSmsCommonDTO();
		dto.setPhoneNo(mkActivitySponsor.getPhone());
		dto.setTemplateCode("1003");
		Response response=null;
		try {
			log.info("messageFeignClient.sendSms入参:"+FastJsonUtils.toJSONString(dto));
			response=messageFeignClient.sendSms(dto);
		} catch (Exception e) {
			log.error(e.getMessage()+"feign error:rob()  messageFeignClient.sendSms() params参数:"+JSON.toJSONString(dto), e);
			return Response.returnCode(BackCode.FEIGN_FAIL.getCode(),"fegin error:rob() messageFeignClient.sendSms():"+BackCode.FEIGN_FAIL.getMsg());
		}
		if(!response.getCode().equals(Response.ok().getCode())){
			log.info("fegin error:rob()  messageFeignClient.sendSms():"+response.getMessage()+"[feign params参数]:"+JSON.toJSONString(dto));
			return response;
		}
		log.info("fegin messageFeignClient.sendSms()返回参数:"+FastJsonUtils.toJSONString(response));
		return response;
	}



	private void updatesku(MkGoodsSku mkGoodsSku) {
		Integer skuremainStock=mkGoodsSku.getRemainStock();
		MkGoodsSku m=new MkGoodsSku();
		m.setId(mkGoodsSku.getId());
		m.setRemainStock(--skuremainStock);
		m.setVersion(mkGoodsSku.getVersion());
		boolean flag=mkGoodsSkuService.updateById(m);
		if(!flag){
			throw new LockException("乐观锁异常！");
		}
	}



	private void updatespu(MkGoodsSpu mkGoodsSpu,boolean isOrder) {
		Integer orderCount=mkGoodsSpu.getOrderCount();
		Integer robCount=mkGoodsSpu.getRobCount();
		Integer remainStock=mkGoodsSpu.getRemainStock();
		MkGoodsSpu m=new MkGoodsSpu();
		m.setId(mkGoodsSpu.getId());
		if(isOrder){
			m.setRemainStock(--remainStock);
			m.setOrderCount(++orderCount);
		}
		m.setRobCount(++robCount);
		m.setVersion(mkGoodsSpu.getVersion());
		boolean flag=mkGoodsSpuService.updateById(m);
		if(!flag){
			throw new LockException("乐观锁异常！");
		}
	}



	private Response login(String phone, String smsCode, String headPic, String nickname, String unid, String province,
			String city, String area, String sex, MkActivitySponsor mkActivitySponsor,Long uniqueCode) {
		//去调用注册接口
        CustomerLoginCommonDTO customerLoginCommonDTO = new CustomerLoginCommonDTO();
        customerLoginCommonDTO.setAppKey("03");
        customerLoginCommonDTO.setThirdCode("01");
        customerLoginCommonDTO.setLoginType("0");
        customerLoginCommonDTO.setPhoneNo(phone);
        if(StringUtils.isEmpty(mkActivitySponsor.getParentInviteCode())){
        	customerLoginCommonDTO.setParentInviteCode(mkActivitySponsor.getParentInviteCode());
        	customerLoginCommonDTO.setIsMall("0");//是否商城端分享，0否，1是（通过别人分享的分销链接注册时必传）
        }else{
        	customerLoginCommonDTO.setParentInviteCode(mkActivitySponsor.getParentInviteCode());
        	customerLoginCommonDTO.setIsMall("1");//是否商城端分享，0否，1是（通过别人分享的分销链接注册时必传）
        }
        
        customerLoginCommonDTO.setUniqueCode(uniqueCode);//登录类型为0验证码登录是必填。验证码唯一标识（发送验证码按钮成功后后台返回的）
        customerLoginCommonDTO.setIdentifyCode(smsCode);//短信验证码
        customerLoginCommonDTO.setUnionid(unid);
        customerLoginCommonDTO.setNickName(nickname);
        customerLoginCommonDTO.setHeadImgUrl(headPic);
        customerLoginCommonDTO.setProvince(province);
        customerLoginCommonDTO.setCity(city);
        customerLoginCommonDTO.setArea(area);
        customerLoginCommonDTO.setSex(sex);
        Response response=null;
        try {
        	log.info("accountFeignClient.login入参:"+FastJsonUtils.toJSONString(customerLoginCommonDTO));
        	response= accountFeignClient.login(customerLoginCommonDTO);
		} catch (Exception e) {
			log.error(e.getMessage()+"feign  error:rob()  accountFeignClient.login() params参数:"+JSON.toJSONString(customerLoginCommonDTO), e);
			return Response.returnCode(BackCode.FEIGN_FAIL.getCode(),"fegin error:rob() accountFeignClient.login():"+BackCode.FEIGN_FAIL.getMsg());
		}
		if(!response.getCode().equals(BackCode.SUCCESS.getCode())){
			log.info("fegin error:rob()  accountFeignClient.login():"+response.getMessage()+"[feign  params参数]:"+JSON.toJSONString(customerLoginCommonDTO));
			return response;
		}
		  log.info("fegin accountFeignClient.login()返回参数:"+FastJsonUtils.toJSONString(response));
		return response;
	}



	private Response smsCodeVa(String phone, String smsCode,Long uniqueCode) {
		// 消息中心进行验证码校验
		MessageSmsCommonDTO messageSmsCommonDTO = new MessageSmsCommonDTO();
		messageSmsCommonDTO.setPhoneNo(phone);
		messageSmsCommonDTO.setIdentifyCode(smsCode);
		messageSmsCommonDTO.setUniqueCode(uniqueCode);
		Response messageFeignClientresponse = null;
		try {
        	log.info("messageFeignClient.checkSms入参:"+FastJsonUtils.toJSONString(messageSmsCommonDTO));
        	messageFeignClientresponse= this.messageFeignClient.checkSms(messageSmsCommonDTO);
		} catch (Exception e) {
			log.error(e.getMessage()+"feign  error:rob()  messageFeignClient.checkSms params参数:"+JSON.toJSONString(messageSmsCommonDTO), e);
			return Response.returnCode(BackCode.FEIGN_FAIL.getCode(),"fegin error:rob()messageFeignClient.checkSms:"+BackCode.FEIGN_FAIL.getMsg());
		}
		
		if(!messageFeignClientresponse.getCode().equals(BackCode.SUCCESS.getCode())){
			log.info("fegin error:rob()  messageFeignClient.checkSms:"+messageFeignClientresponse.getMessage()+"[feign  params参数]:"+JSON.toJSONString(messageSmsCommonDTO));
			return messageFeignClientresponse;
		}
		return messageFeignClientresponse;
	}



	private void activityHelp(MkActivitySponsor mkActivitySponsor) throws LockException {
		Integer activityId=mkActivitySponsor.getActivityId();
		MkActivity mkActivity=mkActivityService.selectById(activityId);
		MkActivity mkActivity2=new MkActivity();
		mkActivity2.setId(activityId);
		mkActivity2.setVersion(mkActivity.getVersion());
		Integer helpCount=mkActivity.getHelpCount();
		mkActivity2.setHelpCount(++helpCount);
		boolean flag=mkActivityService.updateById(mkActivity2);
		if(!flag){
			throw new LockException("乐观锁异常！");
		}
	}



	private void activityBangqiang(MkActivitySponsor mkActivitySponsor,boolean isOrder) throws LockException {
		Integer activityId=mkActivitySponsor.getActivityId();
		MkActivity mkActivity=mkActivityService.selectById(activityId);
		MkActivity mkActivity2=new MkActivity();
		mkActivity2.setId(activityId);
		mkActivity2.setVersion(mkActivity.getVersion());
		if(isOrder){
			Integer orderCount=mkActivity.getOrderCount();
			mkActivity2.setOrderCount(++orderCount);
		}
		Integer helpCount=mkActivity.getHelpCount();
		mkActivity2.setHelpCount(++helpCount);
		 boolean flag=mkActivityService.updateById(mkActivity2);
		if(!flag){
			throw new LockException("乐观锁异常！");
		}
		
	}


	//帮抢下单成功
	private void bangqiangOrderSuccess(Integer sponsorId, String phone, String headPic, String nickname, MkActivitySponsor mkActivitySponsor, AccountCustomerInfoDTO a, Integer nowrobcount,String tradeNo,String orderNo) throws Exception {
		
		MkActivityRob rob=new MkActivityRob();
		rob.setActivityId(mkActivitySponsor.getActivityId());
		rob.setSponsorId(sponsorId);
		rob.setUserId(a.getAccountNo());
		rob.setHeadPic(headPic);
		rob.setPhone(phone);
		rob.setNickname(nickname);
		rob.setCreateBy(0);
		rob.setCreateDate(new Date());
		rob.setUpdateBy(0);
		rob.setUpdateDate(new Date());
		rob.setRemarks("");
		rob.setDelFlag(0);
		rob.setVersion(0);
		boolean flag=mkActivityRobService.insert(rob);
		if(!flag){
			throw new RuntimeException("插入异常！");
		}

		MkActivitySponsor mk=new MkActivitySponsor();
		mk.setId(mkActivitySponsor.getId());
		mk.setRobCount(nowrobcount);
		if(orderNo!=null){
			mk.setOrderNo(orderNo);
			mk.setStatus(ActivitySponsorStatus._003.key);
			mk.setOrderTradeNo(tradeNo);
		}
		mk.setVersion(mkActivitySponsor.getVersion());
		flag=this.updateById(mk);
		if(!flag){
			throw new LockException("乐观锁异常！");
		}
	}
	//帮抢成功
	private void bangqiang(Integer sponsorId, String phone, String headPic, String nickname, MkActivitySponsor mkActivitySponsor, AccountCustomerInfoDTO a, Integer nowrobcount) throws Exception {
		
		MkActivityRob rob=new MkActivityRob();
		rob.setActivityId(mkActivitySponsor.getActivityId());
		rob.setSponsorId(sponsorId);
		rob.setUserId(a.getAccountNo());
		rob.setHeadPic(headPic);
		rob.setPhone(phone);
		rob.setNickname(nickname);
		rob.setCreateBy(0);
		rob.setCreateDate(new Date());
		rob.setUpdateBy(0);
		rob.setUpdateDate(new Date());
		rob.setRemarks("");
		rob.setDelFlag(0);
		rob.setVersion(0);
		boolean flag=mkActivityRobService.insert(rob);
		if(!flag){
			throw new RuntimeException("插入异常！");
		}
		
		MkActivitySponsor mk=new MkActivitySponsor();
		mk.setId(mkActivitySponsor.getId());
		mk.setRobCount(nowrobcount);
		mk.setVersion(mkActivitySponsor.getVersion());
		flag=this.updateById(mk);
		if(!flag){
			throw new LockException("乐观锁异常！");
		}
	}
	//帮抢下单失败
	private void bangqiangOrderFail(Integer sponsorId, String phone, String headPic, String nickname, MkActivitySponsor mkActivitySponsor, AccountCustomerInfoDTO a, Integer nowrobcount) throws Exception {
		
		MkActivityRob rob=new MkActivityRob();
		rob.setActivityId(mkActivitySponsor.getActivityId());
		rob.setSponsorId(sponsorId);
		rob.setUserId(a.getAccountNo());
		rob.setHeadPic(headPic);
		rob.setPhone(phone);
		rob.setNickname(nickname);
		rob.setCreateBy(0);
		rob.setCreateDate(new Date());
		rob.setUpdateBy(0);
		rob.setUpdateDate(new Date());
		rob.setRemarks("");
		rob.setDelFlag(0);
		rob.setVersion(0);
		boolean flag=mkActivityRobService.insert(rob);
		if(!flag){
			throw new RuntimeException("插入异常！");
		}
		
		MkActivitySponsor mk=new MkActivitySponsor();
		mk.setId(mkActivitySponsor.getId());
		mk.setRobCount(nowrobcount);
		mk.setStatus(ActivitySponsorStatus._002.key);
		mk.setRemarks("下单时库存不足,活动失败");
		mk.setVersion(mkActivitySponsor.getVersion());
		flag=this.updateById(mk);
		if(!flag){
			throw new LockException("乐观锁异常！");
		}
	}

	/**
	 * 下单
	 * @param mkActivitySponsor
	 * @param mkGoodsSpu
	 */
	private Response createOrder(MkActivitySponsor mkActivitySponsor, MkGoodsSpu mkGoodsSpu,MkGoodsSku mkGoodsSku, AuthTokenDTO authTokenDTO) {
		OrdersCommitRequestDTO ordersCommitRequest=new OrdersCommitRequestDTO();
		ordersCommitRequest.setAddressId(Long.parseLong(mkActivitySponsor.getAddrId().toString()));//收获地址id
		
		List<OrdersViewDTO> ordersDtoList=new ArrayList<OrdersViewDTO>();
		OrdersViewDTO ordersViewDTO=new OrdersViewDTO();
		ordersViewDTO.setRemark(mkActivitySponsor.getOrderRemarks());
		ordersViewDTO.setRealityPrice(new BigDecimal("0.01"));//订单总实付
		ordersViewDTO.setIsGift(2);// 是否是大礼包 0 是  1 正常订单   2  优惠礼包
		ordersViewDTO.setMerchantNo("1");//商家编号
		
		List<OrdersProductInfo> ordersProductInfos = new ArrayList<>();
		OrdersProductInfo ordersProductInfo=new OrdersProductInfo();
		ordersProductInfo.setDetailId(Long.parseLong(mkGoodsSku.getGoodsSkuId().toString()));//规格编号
		ordersProductInfo.setProductId(mkGoodsSpu.getGoodId().toString());//商品编号
		ordersProductInfo.setSkuId(mkGoodsSku.getEclpNo());
		ordersProductInfo.setProductName(mkGoodsSpu.getName());
		ordersProductInfo.setDetailParam(mkGoodsSku.getDetailParam());
		ordersProductInfo.setProductPrice(new BigDecimal("0.01"));
		ordersProductInfo.setAppSimplePath(mkGoodsSku.getAppSimplePath());
		ordersProductInfo.setSupplier(mkGoodsSku.getSupplier());
		ordersProductInfo.setProductNum(1);//商品数量
		ordersProductInfo.setProductUnitPrice(new BigDecimal("0.01"));//商品总实付价格
		ordersProductInfo.setProductRealityPrice(new BigDecimal("0.01"));
		ordersProductInfos.add(ordersProductInfo);
		ordersViewDTO.setOrdersProductInfos(ordersProductInfos);
		
		ordersDtoList.add(ordersViewDTO);
		ordersCommitRequest.setOrdersDtoList(ordersDtoList);
		ordersCommitRequest.setAuthUser(authTokenDTO);
		Response response=null;
		try {
			log.info("orderFeignClient.insertOneCentPurchase入参:"+FastJsonUtils.toJSONString(ordersCommitRequest));
			 response=orderFeignClient.insertOneCentPurchase(ordersCommitRequest);
		} catch (Exception e) {
			log.error(e.getMessage()+"feign error:rob()  orderFeignClient.insertOneCentPurchase() params参数:"+JSON.toJSONString(ordersCommitRequest), e);
			return Response.returnCode(BackCode.FEIGN_FAIL.getCode(),"fegin error:rob() orderFeignClient.insertOneCentPurchase():"+BackCode.FEIGN_FAIL.getMsg());
		}
		if(!response.getCode().equals(Response.ok().getCode())){
			log.info("fegin error:rob()  orderFeignClient.insertOneCentPurchase():"+response.getMessage()+"[feign params参数]:"+JSON.toJSONString(ordersCommitRequest));
			return response;
		}
		log.info("fegin orderFeignClient.insertOneCentPurchase()返回参数:"+FastJsonUtils.toJSONString(response));
		return response;
	}



	@Override
	public PageDTO<MkActivitySponsor> selectActivitySponsorList(MkActivitySponsorDTO mkActivitySponsorDTO) {
		List<MkActivitySponsor> list = baseMapper.selectPageList(mkActivitySponsorDTO);
		for (MkActivitySponsor mkActivitySponsor : list) {
			OrdersNoQueryDTO ordersNoQueryDTO = new OrdersNoQueryDTO();
			ordersNoQueryDTO.setOrdersNo(mkActivitySponsor.getOrderNo());
			OrdersDetailDTO ordersDetailDTO = null;
			try {
				ordersDetailDTO = orderFeignClient.selectByOrderNoByOperator(ordersNoQueryDTO);
				if(ordersDetailDTO == null){
					log.info("feign返回ordersDetailDTO为null");
				}
				mkActivitySponsor.setOrderStatus(ordersDetailDTO.getOrdersStatus());
			}catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		int totalCount = baseMapper.selectPageCount(mkActivitySponsorDTO);
		return new PageDTO<MkActivitySponsor>(totalCount, list);
	}



	//sponsorId为活动发起表id
	@Override
	public Response inviteList(Integer sponsorId) {
        MkActivitySponsor mkActivitySponsorCondition = new MkActivitySponsor();
        mkActivitySponsorCondition.setId(sponsorId);
        MkActivitySponsor mkActivitySponsor = mkActivitySponsorMapper.selectOne(mkActivitySponsorCondition);
        if(mkActivitySponsor == null){
			return Response.returnCode(ReturnCode.SUCCESS.getCode(),"没有活动发起记录");
		}

        MkActivity mkActivityCondition = new MkActivity();
		mkActivityCondition.setId(mkActivitySponsor.getActivityId());
		MkActivity mkActivity = mkActivityMapper.selectOne(mkActivityCondition);
		if(mkActivity == null){
			return Response.returnCode(ReturnCode.SUCCESS.getCode(),"没有活动");
		}

		MkGoodsSpu mkGoodsSpuCondition = new MkGoodsSpu();
		mkGoodsSpuCondition.setId(mkActivitySponsor.getSpuId());
		MkGoodsSpu mkGoodsSpu = mkGoodsSpuMapper.selectOne(mkGoodsSpuCondition);
		if(mkGoodsSpu == null){
			return Response.returnCode(ReturnCode.SUCCESS.getCode(),"没有商品");
		}

		List<MkActivityRob> activityRobList = mkActivityRobMapper.selectList(new EntityWrapper<MkActivityRob>().eq("sponsor_id", sponsorId));

		MkActivityRobExt mkActivityRobExt = new MkActivityRobExt();
		mkActivityRobExt.setMkActivity(mkActivity);
		mkActivityRobExt.setMkActivitySponsor(mkActivitySponsor);
		mkActivityRobExt.setMkGoodsSpu(mkGoodsSpu);
		mkActivityRobExt.setMkActivityRobList(activityRobList);

		//计算活动结束时间
        Date createDate = mkActivitySponsor.getCreateDate();
        Integer helpIndate = mkActivity.getHelpIndate();
        mkActivityRobExt.setDistanceEndTime((createDate.getTime() + helpIndate * 3600 * 1000) - new Date().getTime() );

		//判断时间，如果活动失败，更新活动发起表
		if ( new Date().getTime() > (createDate.getTime() + helpIndate * 3600 * 1000)) {
			//过期，活动失败。更新数据库状态
			MkActivitySponsor mkActivitySponsorUpdate = new MkActivitySponsor();
			mkActivitySponsorUpdate.setStatus(2);
			mkActivitySponsorService.update(mkActivitySponsorUpdate, new EntityWrapper<MkActivitySponsor>().eq("id", sponsorId));
			mkActivityRobExt.getMkActivitySponsor().setStatus(2);
		}
		// TODO: 2018/11/14  替换图片真实路径
//			添加图片路径
		RequestMainGoods requestMainGoods = new RequestMainGoods();
		requestMainGoods.setGoodsId(mkActivityRobExt.getMkGoodsSpu().getGoodId());
		Response response = null;
		try {
			response = goodsMainInfoFeignClient.selectGoodsBaseInfoDtoDetail(requestMainGoods);
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.returnCode(BackCode.FEIGN_FAIL.getCode(),"goodsMainInfoFeignClient.selectGoodsBaseInfoDtoDetail: parameter" + mkActivityRobExt.getMkGoodsSpu().getGoodId() + BackCode.FEIGN_FAIL.getMsg());
		}
		if(!response.getCode().equals(BackCode.SUCCESS.getCode())){
			return Response.returnCode(response.getCode(),"fegin:"+response.getMessage());
		}
		if(response.getData() == null){
			log.info("feign返回data为null");
		}else{
			mkActivityRobExt.setMovePicPath(FastJsonUtils.toBean(FastJsonUtils.toJSONString(response.getData()),GoodsMainInfoDto.class).getMovePicPath());
		}
//            mkActivityRobExt.setMovePicPath("http://pctr776pn.bkt.clouddn.com/Ftdmh4Hc3vrWwrJKaZxIsfblCguu?imageslim");
		log.info("inviteList出参:"+ FastJsonUtils.toJSONString(mkActivityRobExt));
		return Response.ok(mkActivityRobExt);
	}

	@Override
	public Response myActivityList(String userId, Integer pageNo, Integer pageSize, Integer status) {
		HashMap map = new HashMap();
		map.put("userId", userId);
		map.put("index", (pageNo-1) * pageSize);
		map.put("pageSize", pageSize);
		map.put("status", status);
		List<HashMap> list = mkActivitySponsorMapper.myActivityList(map);
		if(list == null || list.size() == 0){
			return Response.returnCode(BackCode.SUCCESS.getCode(), "没有活动数据");
		}
		for (HashMap hashMap :list){
			Date createDate = (Date) hashMap.get("createDate");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String formatDate = sdf.format(createDate);
			hashMap.put("createDate", formatDate);

            // TODO: 2018/11/14  替换图片真实路径
			RequestMainGoods requestMainGoods = new RequestMainGoods();
			requestMainGoods.setGoodsId((String) hashMap.get("goodId"));
			Response response = null;
			try {
				response = goodsMainInfoFeignClient.selectGoodsBaseInfoDtoDetail(requestMainGoods);
			}catch (Exception e) {
				log.error(e.getMessage(), e);
				return Response.returnCode(BackCode.FEIGN_FAIL.getCode(),"goodsMainInfoFeignClient.selectGoodsBaseInfoDtoDetail: parameter" + requestMainGoods.getGoodsId() + BackCode.FEIGN_FAIL.getMsg());
			}
			if(!response.getCode().equals(BackCode.SUCCESS.getCode())){
				return Response.returnCode(response.getCode(),"fegin:"+response.getMessage());
			}
			if(response.getData() == null){
				log.info("feign返回data为null");
			}else{
				hashMap.put("movePicPath",(FastJsonUtils.toBean(FastJsonUtils.toJSONString(response.getData()),GoodsMainInfoDto.class).getMovePicPath()));
			}
//			hashMap.put("movePicPath", "http://pctr776pn.bkt.clouddn.com/Ftdmh4Hc3vrWwrJKaZxIsfblCguu?imageslim");
            //计算活动结束时间
            Integer helpIndate = (Integer)hashMap.get("helpIndate");
            hashMap.put("distanceEndTime",(createDate.getTime() + helpIndate * 3600 * 1000) - new Date().getTime());
		}
		log.info("myActivityList出参:"+ FastJsonUtils.toJSONString(list));
		return Response.ok(list);
	}

	@Override
	public Response orderList(PageVO pageVO) {
		List<HashMap> list = mkActivitySponsorMapper.orderPageList(pageVO);
		Integer count = mkActivitySponsorMapper.orderPageCount();
		if(list == null || list.size() == 0 || count == 0){
			Response.returnCode(BackCode.SUCCESS.getCode(), "没有订单记录");
		}
		//调用订单服务，获取字段信息
		for (HashMap map : list) {
			OrdersNoQueryDTO ordersNoQueryDTO = new OrdersNoQueryDTO();
			ordersNoQueryDTO.setOrdersNo((String)map.get("orderNo"));
			OrdersDetailDTO ordersDetailDTO = null;
			try {
				ordersDetailDTO = orderFeignClient.selectByOrderNoByOperator(ordersNoQueryDTO);
			}catch (Exception e) {
				log.error(e.getMessage(), e);
				return Response.returnCode(BackCode.FEIGN_FAIL.getCode(),"goodsMainInfoFeignClient.selectGoodsBaseInfoDtoDetail: parameter" + ordersDetailDTO.getOrdersNo() + BackCode.FEIGN_FAIL.getMsg());
			}
			if(ordersDetailDTO == null){
				log.info("feign返回ordersDetailDTO为null");
			}else{
				map.put("ordersPrice", ordersDetailDTO.getOrdersPrice());
				map.put("ordersStatus", ordersDetailDTO.getOrdersStatus());
				map.put("payTime", ordersDetailDTO.getPayTime());
			}
		}
		Page<HashMap> page = new Page<>();
		page.setTotal(count);
		page.setRecords(list);
		return Response.ok(page);
	}


    @Override
    public Response orderDetail(MkActivitySponsorDTO mkActivitySponsorDTO) {
		HashMap map = new HashMap();
        OrdersNoQueryDTO ordersNoQueryDTO = new OrdersNoQueryDTO();
        ordersNoQueryDTO.setOrdersNo(mkActivitySponsorDTO.getOrderNo());

		OrdersDetailDTO ordersDetailDTO = null;
		try {
			ordersDetailDTO = orderFeignClient.selectByOrderNoByOperator(ordersNoQueryDTO);
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.returnCode(BackCode.FEIGN_FAIL.getCode(),"goodsMainInfoFeignClient.selectGoodsBaseInfoDtoDetail: parameter" + ordersDetailDTO.getOrdersNo() + BackCode.FEIGN_FAIL.getMsg());
		}
		if(ordersDetailDTO == null){
			log.info("feign返回ordersDetailDTO为null");
		}else{
			//根据订单编号查询活动id，活动发起编号
			MkActivitySponsor mkActivitySponsor = mkActivitySponsorMapper.selectOne(mkActivitySponsorDTO);
			map.put("activityId", mkActivitySponsor.getActivityId());
			map.put("sponsorCode", mkActivitySponsor.getSponsorCode());
			map.put("orderDetail", ordersDetailDTO);
		}
		log.info("orderDetail出参:"+ FastJsonUtils.toJSONString(map));
        return Response.ok(map);
    }

    @Override
    public void myActivityDelete(MkActivitySponsorDTO mkActivitySponsorDTO) throws Exception {
        MkActivitySponsor mkActivitySponsor = new MkActivitySponsor();
        mkActivitySponsor.setDelFlag(1);
		Integer count = mkActivitySponsorMapper.update(mkActivitySponsor, new EntityWrapper<MkActivitySponsor>().eq("id", mkActivitySponsorDTO.getId()));
		if(count == 0){
			throw new Exception();
		}
	}



	@Override
	@Transactional
	public void test111() {
		MkActivitySponsor mkActivitySponsor =mkActivitySponsorMapper.selectById(45);
		System.out.println("11");
		MkActivitySponsor m =new MkActivitySponsor();
		m.setId(mkActivitySponsor.getId());
		m.setDelFlag(1);
		mkActivitySponsorMapper.updateById(m);
	}


}
