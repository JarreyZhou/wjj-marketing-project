package com.wjj.application.service.pennyRob.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wjj.application.common.FastJsonUtils;
import com.wjj.application.dto.GoodsMainInfoDto;
import com.wjj.application.dto.RequestMainGoods;
import com.wjj.application.dto.pennyRob.MkGoodsSpuDTO;
import com.wjj.application.dto.pennyRob.MkStandardDTO;
import com.wjj.application.entity.pennyRob.*;
import com.wjj.application.enums.ActivitySponsorStatus;
import com.wjj.application.enums.BackCode;
import com.wjj.application.enums.GoodsStatus;
import com.wjj.application.feign.GoodsMainInfoFeignClient;
import com.wjj.application.mapper.pennyRob.*;
import com.wjj.application.page.PageDTO;
import com.wjj.application.response.Response;
import com.wjj.application.response.ReturnCode;
import com.wjj.application.service.pennyRob.*;
import com.wjj.application.util.ActivityCodeUtil;
import com.wjj.application.util.LockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 活动商品spu表 服务实现类
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */
@Service
@Slf4j
public class MkGoodsSpuServiceImpl extends ServiceImpl<MkGoodsSpuMapper, MkGoodsSpu> implements MkGoodsSpuService {

	@Autowired
	private MkGoodsSpuService mkGoodsSpuService;

	@Autowired
	private MkGoodsSkuService mkGoodsSkuService;

	@Autowired
	private MkActivityService mkActivityService;

	@Autowired
	private MkStandardService mkStandardService;

	@Autowired
	private MkStandardAttrService mkStandardAttrService;

	@Autowired
	private MkGoodsSpuMapper mkGoodsSpuMapper;

	@Autowired
	private MkActivityMapper mkActivityMapper;

	@Autowired
	private MkStandardMapper mkStandardMapper;

	@Autowired
	private MkStandardAttrMapper mkStandardAttrMapper;

	@Autowired
	private MkGoodsSkuMapper mkGoodsSkuMapper;

	@Autowired
	private MkActivitySponsorMapper mkActivitySponsorMapper;

	@Autowired
	private GoodsMainInfoFeignClient goodsMainInfoFeignClient;

	/**
	 * 立即抢购 liuxin
	 */
	@Transactional
	@Override
	public Response rush(Integer skuId,Integer addrId, String userId,String parentInviteCode,String orderRemarks,
			String nickname,String phone,String openId, String formId) throws Exception{
		MkGoodsSku mkGoodsSku=mkGoodsSkuService.selectById(skuId);
		if(mkGoodsSku==null){
			return Response.returnCode(BackCode.FAIL.getCode(),"不存该商品sku");
		}
		MkGoodsSpu mkGoodsSpu=mkGoodsSpuService.selectById(mkGoodsSku.getSpuId());
		if(mkGoodsSpu==null){
			return Response.returnCode(BackCode.FAIL.getCode(),"不存该商品spu");
		}
		
		Integer activityId=mkGoodsSpu.getActivityId();
		MkActivity mkActivity=mkActivityService.selectById(activityId);
		if(mkActivity==null){
			return Response.returnCode(BackCode.FAIL.getCode(),"不存该活动");
		}
		
		Integer spuId=mkGoodsSku.getSpuId();

		//判断是否在活动期内
		Date startTime=mkActivity.getStartTime();
		Date endTime=mkActivity.getEndTime();
		Date now=new Date();
		if(now.before(startTime)||now.after(endTime)){
			return Response.returnCode(BackCode.FAIL.getCode(),"对不起，不在活动时间内！");
		}
		// 判断商品剩余库存
		if (mkGoodsSku.getRemainStock() <= 0) {
			return Response.returnCode(BackCode.FAIL.getCode(), "对不起，该商品剩余库存不足！");
		}

		//
		MkActivitySponsor m = new MkActivitySponsor();
		m.setActivityId(activityId);
		m.setUserId(userId);
		m.setSpuId(spuId);
		Integer spuCount = mkActivitySponsorMapper.selectSponsorSpuCount(m);
		if (spuCount > 0) {
			return Response.returnCode(BackCode.FAIL.getCode(), "发起失败，该活动一个商品只能发起一次帮抢！");
		}
		if(!mkActivity.getRobLimit().equals(0)){
			Integer activityCount = mkActivitySponsorMapper.selectSponsorActivityCount(m);
			if (activityCount >= mkActivity.getRobLimit()) {
				return Response.returnCode(BackCode.FAIL.getCode(),
						"发起失败，该活动每个用户最多开抢" + mkActivity.getRobLimit() + "个商品！");
			}
		}
		
		// 更新活动表活动发起总数
		int interesting = mkActivity.getInitiateCount();
		MkActivity mkActivity2 = new MkActivity();
		mkActivity2.setId(activityId);
		mkActivity2.setInitiateCount(++interesting);
		mkActivity2.setVersion(mkActivity.getVersion());
		boolean flag = mkActivityService.updateById(mkActivity2);
		if (!flag) {
			throw new LockException("更新活动发起记录");
		}
		// 增加活动发起
		MkActivitySponsor mkActivitySponsor = addSponsor(skuId, addrId, userId, parentInviteCode, orderRemarks,
				nickname, phone, activityId, spuId, now,openId,formId);
		
		

		
		MkGoodsSpu mkGoodsSpu2=new MkGoodsSpu();
		mkGoodsSpu2.setId(mkGoodsSpu.getId());
		Integer sponsorCount=mkGoodsSpu.getSponsorCount();
		mkGoodsSpu2.setSponsorCount(++sponsorCount);
		mkGoodsSpu2.setVersion(mkGoodsSpu.getVersion());
		flag =mkGoodsSpuService.updateById(mkGoodsSpu2);
		if (!flag) {
			throw new LockException("乐观锁异常");
		}
		
		return Response.ok(mkActivitySponsor);
	}

	private void addInitiateCount(MkActivity mkActivity, Integer activityId) throws LockException {
		MkActivity mActivity3=new MkActivity();
		mActivity3.setId(activityId);
		mActivity3.setVersion(mkActivity.getVersion());
		Integer initiateCount=mkActivity.getInitiateCount();
		mActivity3.setInitiateCount(++initiateCount);
		boolean flag=mkActivityService.updateById(mActivity3);
		if(!flag){
			throw new LockException("乐观锁异常！");
		}
	}

	private MkActivitySponsor addSponsor(Integer skuId, Integer addrId, String userId, String parentInviteCode,
			String orderRemarks, String nickname, String phone, Integer activityId, Integer spuId, Date now,
			String openId,String formId) throws Exception {
		MkActivitySponsor mkActivitySponsor = new MkActivitySponsor();
		mkActivitySponsor.setActivityId(activityId);
		mkActivitySponsor.setSponsorCode(ActivityCodeUtil.createCode());
		mkActivitySponsor.setOpenId(openId);
		mkActivitySponsor.setFormId(formId);
		mkActivitySponsor.setUserId(userId);
		mkActivitySponsor.setRobCount(0);
		mkActivitySponsor.setSpuId(spuId);
		mkActivitySponsor.setAddrId(addrId);
		mkActivitySponsor.setOrderRemarks(orderRemarks);
		mkActivitySponsor.setParentInviteCode(parentInviteCode);
		mkActivitySponsor.setSkuId(skuId);
		mkActivitySponsor.setNickname(nickname);
		mkActivitySponsor.setPhone(phone);
		mkActivitySponsor.setStatus(ActivitySponsorStatus._001.key);
		mkActivitySponsor.setCreateBy(1);
		mkActivitySponsor.setCreateDate(now);
		mkActivitySponsor.setUpdateDate(now);
		mkActivitySponsor.setUpdateBy(1);
		mkActivitySponsor.setOrderRemarks(orderRemarks);
		Integer flag=mkActivitySponsorMapper.insert(mkActivitySponsor);
		if(flag==0){
			throw new RuntimeException("活动发起失败！");
		}
		return mkActivitySponsor;
	}

	@Override
	public Response selectProductDetail(Integer activityId, Integer spuId) {

		//查询活动是否完成
		MkActivity mkActivityCondition = new MkActivity();
		mkActivityCondition.setId(activityId);
		MkActivity mkActivity = mkActivityMapper.selectOne(mkActivityCondition);

		//查询spu详细信息
		MkGoodsSpu mkGoodsSpuCondition = new MkGoodsSpu();
		mkGoodsSpuCondition.setId(spuId);
		MkGoodsSpuVO mkGoodsSpuVO  = mkGoodsSpuMapper.selectSpu(spuId);

		//查询sku详细信息
		List<MkGoodsSku> skuList = mkGoodsSkuMapper.selectList(new EntityWrapper<MkGoodsSku>().eq("spu_id", spuId));
		mkGoodsSpuVO.setMkActivity(mkActivity);
		mkGoodsSpuVO.setSkuList(skuList);

		// TODO: 2018/11/14  替换图片真实路径
		//查询商品图片信息
		//条件查询得到图片路径
		RequestMainGoods requestMainGoods = new RequestMainGoods();
		requestMainGoods.setGoodsId(mkGoodsSpuVO.getGoodId());
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
			GoodsMainInfoDto goodsMainInfoDto = FastJsonUtils.toBean(FastJsonUtils.toJSONString(response.getData()), GoodsMainInfoDto.class);
			mkGoodsSpuVO.setGoodsMainInfoDto(goodsMainInfoDto);
		}
		//添加测试路径
//		GoodsMainInfoDto goodsMainInfoDto = new GoodsMainInfoDto();
//		goodsMainInfoDto.setMovePicPath("http://pctr776pn.bkt.clouddn.com/FiVUl43wSHOmScsZs83UKjCC7Vvw?imageslim");
//		mkGoodsSpuVO.setGoodsMainInfoDto(goodsMainInfoDto);
		mkGoodsSpuVO.setDate(new Date());
		log.info("selectProductDetail出参:"+ FastJsonUtils.toJSONString(mkGoodsSpuVO));
		return Response.ok(mkGoodsSpuVO);
	}

	// 排序规格和规格属性
	@Override
	public Response selectSku(Integer spuId) {
		List<MkStandardExt> mkStandardExtList = mkStandardMapper.selectSku(spuId);
		if(mkStandardExtList == null || mkStandardExtList.size() == 0){
			Response.returnCode(BackCode.SUCCESS.getCode(), "没有规格数据");
		}
		log.info("selectSku出参:"+ FastJsonUtils.toJSONString(mkStandardExtList));
		return Response.ok(mkStandardExtList);
	}

	// 查询sku的库存
	@Override
	public Response selectSkuStock(Integer spuId, String sku) {
		MkGoodsSku mkGoodsSkuCondition = new MkGoodsSku();
		mkGoodsSkuCondition.setSpuId(spuId);
		mkGoodsSkuCondition.setGuigeJson(sku);
		MkGoodsSku mkGoodsSku = mkGoodsSkuMapper.selectOne(mkGoodsSkuCondition);
		log.info("selectSkuStock出参:"+ FastJsonUtils.toJSONString(mkGoodsSku));
		return Response.ok(mkGoodsSku);
	}

	/**
	 * 1分抢-添加活动商品
	 */
	@Transactional
	@Override
	public Response insertActivityGoods(MkGoodsSpuDTO mkGoodsSpuDTO) throws Exception {
	    //mkGoodsSpuDTO.getPageSize();
	    //mkGoodsSpuDTO.getCurrPage();
	    //mkGoodsSpuDTO.getLimit();
		List<MkGoodsSku> goodsSkuList = mkGoodsSpuDTO.getGoodsSkuList();
		List<MkStandardDTO> standardDTOList = mkGoodsSpuDTO.getStandardDTOList();
		//mkGoodsSpuDTO.getUpdateStart();
	    //mkGoodsSpuDTO.getUpdateEnd();
		//String movePicPath = mkGoodsSpuDTO.getMovePicPath();
		//mkGoodsSpuDTO.getId();
		Integer activityId = mkGoodsSpuDTO.getActivityId();
		String name = mkGoodsSpuDTO.getName();
		String goodId = mkGoodsSpuDTO.getGoodId();
		Integer typeId = mkGoodsSpuDTO.getTypeId();
		String typeName = mkGoodsSpuDTO.getTypeName();
		String price = mkGoodsSpuDTO.getPrice();
		Integer needrobCount = mkGoodsSpuDTO.getNeedrobCount();
		Integer initCount = mkGoodsSpuDTO.getInitCount();
		Integer sort = mkGoodsSpuDTO.getSort();
		Integer sponsorLimit = mkGoodsSpuDTO.getSponsorLimit();
		//Integer orderCount = mkGoodsSpuDTO.getOrderCount();
		Integer stock = mkGoodsSpuDTO.getStock();
		//Integer remainStock = mkGoodsSpuDTO.getRemainStock();
		//Integer sponsorCount = mkGoodsSpuDTO.getSponsorCount();
		//Integer robCount = mkGoodsSpuDTO.getRobCount();
		//Integer status = mkGoodsSpuDTO.getStatus();
		//Integer createBy = mkGoodsSpuDTO.getCreateBy();
		//Date createDate = mkGoodsSpuDTO.getCreateDate();
		//Integer updateBy = mkGoodsSpuDTO.getUpdateBy();
		//Date updateDate = mkGoodsSpuDTO.getUpdateDate();
		//mkGoodsSpuDTO.getRemarks();
	    //mkGoodsSpuDTO.getDelFlag();
		//Integer version = mkGoodsSpuDTO.getVersion();
		if (activityId == null || StringUtils.isEmpty(name) || StringUtils.isEmpty(goodId) || typeId == null
				|| StringUtils.isEmpty(typeName) || StringUtils.isEmpty(price) || needrobCount == null
				|| initCount == null || sort == null || sponsorLimit == null || goodsSkuList == null
				|| standardDTOList == null || stock == null) {
			return Response.returnCode(BackCode.RESPONSE_1001.getCode(), BackCode.RESPONSE_1001.getMsg());
		}

		mkGoodsSpuDTO.setOrderCount(0);
		mkGoodsSpuDTO.setRemainStock(stock);
		mkGoodsSpuDTO.setSponsorCount(0);
		mkGoodsSpuDTO.setRobCount(0);

		mkGoodsSpuDTO.setCreateBy(007);// 创建者
		mkGoodsSpuDTO.setCreateDate(new Date());// 创建时间
		mkGoodsSpuDTO.setUpdateBy(mkGoodsSpuDTO.getCreateBy());// 更新者
		mkGoodsSpuDTO.setUpdateDate(mkGoodsSpuDTO.getCreateDate());// 更新时间
		mkGoodsSpuDTO.setVersion(0);// 乐观锁标志
		mkGoodsSpuDTO.setStatus(GoodsStatus._002.key);// 添加商品后，设置活动商品后，保存后状态即为上架

		//同一个活动不能选择重复的商品
		Wrapper<MkGoodsSpu> mkGoodsSpuWrapper = new EntityWrapper<>();
		mkGoodsSpuWrapper.eq("activity_id", activityId);
		List<MkGoodsSpu> mkGoodsSpus = mkGoodsSpuService.selectList(mkGoodsSpuWrapper);
		if (mkGoodsSpus.size() > 0) {
			for (MkGoodsSpu goodsSpus : mkGoodsSpus) {
				if (goodId.equals(goodsSpus.getGoodId())) {
					return Response.returnCode(BackCode.RESPONSE_1001.getCode(), "同一个活动不能选择重复的商品！");
				}
			}
		}

		boolean flag = mkGoodsSpuService.insert(mkGoodsSpuDTO);
		if (!flag) {
			throw new RuntimeException("插入spu异常！");
		}

		//关联插入sku
		for (MkGoodsSku mkGoodsSku : goodsSkuList) {
			mkGoodsSku.setSpuId(mkGoodsSpuDTO.getId());
			String guigeJson = mkGoodsSku.getGuigeJson();
			Integer goodsStock = mkGoodsSku.getGoodsStock();//商品库存
			Integer stock1 = mkGoodsSku.getStock();//活动库存
            String salePrice = mkGoodsSku.getSalePrice();
            String linePrice = mkGoodsSku.getLinePrice();
            if (StringUtils.isEmpty(guigeJson) || goodsStock == null || stock1 == null || StringUtils.isEmpty(salePrice) || StringUtils.isEmpty(linePrice)) {
				return Response.returnCode(BackCode.RESPONSE_1001.getCode(), BackCode.RESPONSE_1001.getMsg());
			}

			mkGoodsSku.setRemainStock(stock1);//剩余库存

			mkGoodsSku.setCreateBy(007);// 创建者
			mkGoodsSku.setCreateDate(new Date());// 创建时间
			mkGoodsSku.setUpdateBy(mkGoodsSpuDTO.getCreateBy());// 更新者
			mkGoodsSku.setUpdateDate(mkGoodsSpuDTO.getCreateDate());// 更新时间
			mkGoodsSku.setVersion(0);// 乐观锁标志
			mkGoodsSku.setStatus(GoodsStatus._002.key);
		}
		boolean flag2 = mkGoodsSkuService.insertBatch(goodsSkuList);
		if (!flag2) {
			throw new RuntimeException("插入sku异常！");
		}
		// 关联插入规格
		for (MkStandardDTO mkStandardDTO : standardDTOList) {
			mkStandardDTO.setSpuId(mkGoodsSpuDTO.getId());
			String name1 = mkStandardDTO.getName();
			Integer sort1 = mkStandardDTO.getSort();
			if (StringUtils.isEmpty(name1)||sort1==null) {
				return Response.returnCode(BackCode.RESPONSE_1001.getCode(), BackCode.RESPONSE_1001.getMsg());
			}

			mkStandardDTO.setCreateBy(007);// 创建者
			mkStandardDTO.setCreateDate(new Date());// 创建时间
			mkStandardDTO.setUpdateBy(mkGoodsSpuDTO.getCreateBy());// 更新者
			mkStandardDTO.setUpdateDate(mkGoodsSpuDTO.getCreateDate());// 更新时间
			mkStandardDTO.setVersion(0);// 乐观锁标志

			Integer insert = mkStandardMapper.insert(mkStandardDTO);
			if (insert == 0) {
				throw new RuntimeException("插入规格异常！");
			}
			// 规格属性
			//for (MkStandardDTO mkStandardDTO : standardDTOList) {
			List<MkStandardAttr> standardAttrList = mkStandardDTO.getStandardAttrList();
			if (standardAttrList.size() < 1) {
				return Response.returnCode(ReturnCode.BIZ_FAIL.getCode(), "对不起，请至少勾选一个规格属性！");
			}
			for (MkStandardAttr mkStandardAttr : standardAttrList) {
				mkStandardAttr.setStandardId(mkStandardDTO.getId());
				String attrValue = mkStandardAttr.getAttrValue();
				Integer sort2 = mkStandardAttr.getSort();
				if (StringUtils.isEmpty(attrValue) || sort2 == null) {
					return Response.returnCode(BackCode.RESPONSE_1001.getCode(), BackCode.RESPONSE_1001.getMsg());
				}

				mkStandardAttr.setCreateBy(007);// 创建者
				mkStandardAttr.setCreateDate(new Date());// 创建时间
				mkStandardAttr.setUpdateBy(mkGoodsSpuDTO.getCreateBy());// 更新者
				mkStandardAttr.setUpdateDate(mkGoodsSpuDTO.getCreateDate());// 更新时间
				mkStandardAttr.setVersion(0);// 乐观锁标志
			}
			boolean flag3 = mkStandardAttrService.insertBatch(standardAttrList);
			if (!flag3) {
				throw new RuntimeException("插入规格属性异常！");
			}
		}
		return Response.ok();
	}

	/**
	 * 1分抢-活动商品列表
	 */
	@Override
	public PageDTO<MkGoodsSpuDTO> selectActivityGoodsList(MkGoodsSpuDTO mkGoodsSpuDTO) {
		List<MkGoodsSpuDTO> list = baseMapper.selectPageList(mkGoodsSpuDTO);
		int totalCount = baseMapper.selectPageCount(mkGoodsSpuDTO);
		return new PageDTO<MkGoodsSpuDTO>(totalCount, list);
	}

	/**
	 * 1分抢-活动商品批量下架
	 */
	@Transactional
	@Override
	public Response unshelveBatchActivity(List<MkGoodsSpuDTO> mkGoodsSpuDTOs) throws LockException {
		for (MkGoodsSpuDTO mkGoodsSpuDTO : mkGoodsSpuDTOs) {
			unshelveActivity(mkGoodsSpuDTO);
		}
		return Response.ok();
	}

	/**
	 * 1分抢-活动商品下架
	 */
	@Transactional
	@Override
	public Response unshelveActivity(MkGoodsSpuDTO mkGoodsSpuDTO) throws LockException {
		Integer status = mkGoodsSpuDTO.getStatus();
		if (status != 2) {
			return Response.returnCode(ReturnCode.BIZ_FAIL.getCode(), "对不起，只有已上架的商品才能下架！");
		}
		Integer activityId = mkGoodsSpuDTO.getActivityId();
		MkActivity mkActivity = mkActivityMapper.selectById(activityId);
		if (mkActivity.getStatus() != 2 && mkActivity.getStatus() != 1) {
			return Response.returnCode(ReturnCode.BIZ_FAIL.getCode(), "对不起，进行中和已过期的活动不能下架商品！");
		}
		//mkGoodsSpuDTO.setStatus(ActivityStatus._001.key);
		MkGoodsSpu mkGoodsSpu = new MkGoodsSpu();
		mkGoodsSpu.setId(mkGoodsSpuDTO.getId());
		mkGoodsSpu.setVersion(mkGoodsSpuDTO.getVersion());
		mkGoodsSpu.setStatus(GoodsStatus._001.key);
		Integer i = mkGoodsSpuMapper.updateById(mkGoodsSpu);
		if (i == 0) {
			throw new LockException("乐观锁异常！");
		}
		return Response.ok();
	}

	/**
	 * 1分抢-活动商品批量上架
	 */
	@Transactional
	@Override
	public Response shelveBatchActivity(List<MkGoodsSpuDTO> mkGoodsSpuDTOs) throws LockException {
		for (MkGoodsSpuDTO mkGoodsSpuDTO : mkGoodsSpuDTOs) {
			shelveActivity(mkGoodsSpuDTO);
		}
		return Response.ok();
	}
	/**
	 * 1分抢-活动商品上架
	 */
	@Transactional
	@Override
	public Response shelveActivity(MkGoodsSpuDTO mkGoodsSpuDTO) throws LockException {
		Integer status = mkGoodsSpuDTO.getStatus();
		if (status != 1) {
			return Response.returnCode(ReturnCode.BIZ_FAIL.getCode(), "对不起，只有已下架的商品才能上架！");
		}
		Integer activityId = mkGoodsSpuDTO.getActivityId();
		MkActivity mkActivity = mkActivityMapper.selectById(activityId);
		if (mkActivity.getStatus() == 4) {
			return Response.returnCode(ReturnCode.BIZ_FAIL.getCode(), "对不起，已过期的活动不能上架商品！");
		}
		MkGoodsSpu mkGoodsSpu = new MkGoodsSpu();
		mkGoodsSpu.setId(mkGoodsSpuDTO.getId());
		mkGoodsSpu.setVersion(mkGoodsSpuDTO.getVersion());
		mkGoodsSpu.setStatus(GoodsStatus._002.key);
		Integer i = mkGoodsSpuMapper.updateById(mkGoodsSpu);
		if (i == 0) {
			throw new LockException("乐观锁异常！");
		}
		return Response.ok();
	}

	/**
	 * 1分抢-编辑活动商品
	 */
	@Override
	public Response updateActivityGoods(MkGoodsSpuDTO mkGoodsSpuDTO) throws LockException {
		//mkGoodsSpuDTO.getPageSize();
		//mkGoodsSpuDTO.getCurrPage();
		//mkGoodsSpuDTO.getLimit();
		List<MkGoodsSku> goodsSkuList = mkGoodsSpuDTO.getGoodsSkuList();
		List<MkStandardDTO> standardDTOList = mkGoodsSpuDTO.getStandardDTOList();
		//mkGoodsSpuDTO.getUpdateStart();
		//mkGoodsSpuDTO.getUpdateEnd();
		//String movePicPath = mkGoodsSpuDTO.getMovePicPath();
		//mkGoodsSpuDTO.getId();
		//Integer activityId = mkGoodsSpuDTO.getActivityId();
		//String name = mkGoodsSpuDTO.getName();
		//String goodId = mkGoodsSpuDTO.getGoodId();
		//Integer typeId = mkGoodsSpuDTO.getTypeId();
		//String typeName = mkGoodsSpuDTO.getTypeName();
		//String price = mkGoodsSpuDTO.getPrice();
		Integer needrobCount = mkGoodsSpuDTO.getNeedrobCount();
		Integer initCount = mkGoodsSpuDTO.getInitCount();
		Integer sort = mkGoodsSpuDTO.getSort();
		Integer sponsorLimit = mkGoodsSpuDTO.getSponsorLimit();
		//Integer orderCount = mkGoodsSpuDTO.getOrderCount();
		Integer stock = mkGoodsSpuDTO.getStock();
		Integer remainStock = mkGoodsSpuDTO.getRemainStock();
		Integer sponsorCount = mkGoodsSpuDTO.getSponsorCount();
		Integer robCount = mkGoodsSpuDTO.getRobCount();
		Integer status = mkGoodsSpuDTO.getStatus();
		//Integer createBy = mkGoodsSpuDTO.getCreateBy();
		//Date createDate = mkGoodsSpuDTO.getCreateDate();
		//Integer updateBy = mkGoodsSpuDTO.getUpdateBy();
		//Date updateDate = mkGoodsSpuDTO.getUpdateDate();
		//mkGoodsSpuDTO.getRemarks();
		//mkGoodsSpuDTO.getDelFlag();
		Integer version = mkGoodsSpuDTO.getVersion();
		if (needrobCount==null||initCount==null||sort==null||sponsorLimit==null||goodsSkuList==null||version==null) {
			return Response.returnCode(BackCode.RESPONSE_1001.getCode(),
					BackCode.RESPONSE_1001.getMsg());
		}
		MkGoodsSpu mkGoodsSpu= new MkGoodsSpu();
		mkGoodsSpu.setId(mkGoodsSpuDTO.getId());
		//mkGoodsSpu.setActivityId(mkGoodsSpuDTO.getActivityId());
		//mkGoodsSpu.setName(mkGoodsSpuDTO.getName());
		//mkGoodsSpu.setGoodId(mkGoodsSpuDTO.getGoodId());
		//mkGoodsSpu.setTypeId(mkGoodsSpuDTO.getTypeId());
		//mkGoodsSpu.setTypeName(mkGoodsSpuDTO.getTypeName());
		//mkGoodsSpu.setPrice(mkGoodsSpuDTO.getPrice());
		mkGoodsSpu.setNeedrobCount(mkGoodsSpuDTO.getNeedrobCount());
		mkGoodsSpu.setInitCount(mkGoodsSpuDTO.getInitCount());
		mkGoodsSpu.setSort(mkGoodsSpuDTO.getSort());
		mkGoodsSpu.setSponsorLimit(mkGoodsSpuDTO.getSponsorLimit());
		mkGoodsSpu.setOrderCount(mkGoodsSpuDTO.getOrderCount());
		mkGoodsSpu.setStock(mkGoodsSpuDTO.getStock());
		//mkGoodsSpu.setRemainStock(mkGoodsSpuDTO.getRemainStock());
		mkGoodsSpu.setSponsorCount(mkGoodsSpuDTO.getSponsorCount());
		mkGoodsSpu.setRobCount(mkGoodsSpuDTO.getRobCount());
		mkGoodsSpu.setStatus(mkGoodsSpuDTO.getStatus());
		//mkGoodsSpu.setCreateBy(mkGoodsSpuDTO.getCreateBy());
		//mkGoodsSpu.setCreateDate(mkGoodsSpuDTO.getCreateDate());
		//mkGoodsSpu.setUpdateBy(mkGoodsSpuDTO.getUpdateBy());
		//mkGoodsSpu.setUpdateDate(mkGoodsSpuDTO.getUpdateDate());
		//mkGoodsSpu.setRemarks(mkGoodsSpuDTO.getRemarks());
		//mkGoodsSpu.setDelFlag(mkGoodsSpuDTO.getDelFlag());
		mkGoodsSpu.setVersion(mkGoodsSpuDTO.getVersion());

		//mkGoodsSpu.setOrderCount(0);
		//mkGoodsSpu.setStock(100);
		//mkGoodsSpu.setRemainStock(mkGoodsSpuDTO.getStock());
		//mkGoodsSpu.setSponsorCount(0);
		//mkGoodsSpu.setRobCount(0);

		mkGoodsSpu.setCreateBy(007);// 创建者
		mkGoodsSpu.setCreateDate(new Date());// 创建时间
		mkGoodsSpu.setUpdateBy(mkGoodsSpuDTO.getCreateBy());// 更新者
		mkGoodsSpu.setUpdateDate(mkGoodsSpuDTO.getCreateDate());// 更新时间
		mkGoodsSpu.setStatus(GoodsStatus._002.key);// 添加商品后，设置活动商品后，保存后状态即为上架

//		if (i<=0) {
//			return Response.returnCode(ReturnCode.BIZ_FAIL.getCode(), "商品编辑失败，请重试！");
//		}
		//sku
		//修改库存
//		MkGoodsSku mkGoodsSku1 = new MkGoodsSku();
//		mkGoodsSku1.setRemainStock(0);
//		mkGoodsSku1.setVersion(mkGoodsSpuDTO.getVersion());
//		EntityWrapper<MkGoodsSku> wrapper = new EntityWrapper<>();
//		wrapper.eq("spu_id",mkGoodsSpuDTO.getId());
//		Integer i2 = mkGoodsSkuMapper.update(mkGoodsSku1, wrapper);
//		if (i2<=0) {
//			return Response.returnCode(ReturnCode.BIZ_FAIL.getCode(), "商品编辑失败，请重试！");
//		}
		//Integer spuId = mkGoodsSpuDTO.getId();

        Integer stockTotal = 0;
        Integer remainStockTotal = 0;
		for (MkGoodsSku mkGoodsSku : goodsSkuList) {
			Integer id = mkGoodsSku.getId();
			Integer goodsSkuId = mkGoodsSku.getGoodsSkuId();
			String guigeJson = mkGoodsSku.getGuigeJson();
			Integer status1 = mkGoodsSku.getStatus();
			Integer remainStock1 = mkGoodsSku.getRemainStock();
            Integer stock1 = mkGoodsSku.getStock();
			Integer delFlag = mkGoodsSku.getDelFlag();
			if (StringUtils.isEmpty(guigeJson) || remainStock1 == null || stock1 == null || delFlag == null || id == null) {
				return Response.returnCode(BackCode.RESPONSE_1001.getCode(),
						BackCode.RESPONSE_1001.getMsg());
			}
			mkGoodsSku.setCreateBy(007);// 创建者
			mkGoodsSku.setCreateDate(new Date());// 创建时间
			mkGoodsSku.setUpdateBy(mkGoodsSpuDTO.getCreateBy());// 更新者
			mkGoodsSku.setUpdateDate(mkGoodsSpuDTO.getCreateDate());// 更新时间

			mkGoodsSku.setStatus(GoodsStatus._002.key);
			//mkGoodsSku.setDelFlag(1);
			//EntityWrapper<MkGoodsSku> wrapper1 = new EntityWrapper<>();
			//wrapper1.eq("goods_sku_id",goodsSkuId).eq("id",mkGoodsSku.getId());
			//Integer i3 =mkGoodsSkuMapper.update(mkGoodsSku, wrapper1);
//			if (i3 <= 0) {
//				return Response.returnCode(ReturnCode.BIZ_FAIL.getCode(), "商品编辑失败，请重试！");
//			}
			// 修改库存
			MkGoodsSku oldMkGoodsSku = mkGoodsSkuService.selectById(id);
			Integer goodsStock = oldMkGoodsSku.getGoodsStock();//商品库存
			Integer oldStock = oldMkGoodsSku.getStock();//活动库存
			Integer oldRemainStock = oldMkGoodsSku.getRemainStock();//剩余活动库存
			Integer robNum = oldStock - oldRemainStock;//抢了几个
			Integer remainGoodsStock = goodsStock - robNum;//剩余商品库存
			if (stock1 > remainGoodsStock) {
				return Response.returnCode(ReturnCode.BIZ_FAIL.getCode(), "剩余库存不足！");
			}
			Integer offset = stock1 - oldStock;
			remainStock1 = oldRemainStock + offset;
			mkGoodsSku.setRemainStock(remainStock1);
			boolean flag = mkGoodsSkuService.updateById(mkGoodsSku);
			if (!flag) {
				throw new LockException("乐观锁异常！");
			}
			if (delFlag == 0) {
				remainStockTotal += remainStock1;
				stockTotal += stock1;
			}
		}
        mkGoodsSpu.setRemainStock(remainStockTotal);
        mkGoodsSpu.setStock(stockTotal);
		Integer flag2 = mkGoodsSpuMapper.updateById(mkGoodsSpu);
		if (flag2 == 0) {
			throw new LockException("乐观锁异常！");
		}
		// 规格
		for (MkStandardDTO mkStandardDTO : standardDTOList) {
//			mkStandardDTO.setSpuId(mkGoodsSpuDTO.getId());
//			String name1 = mkStandardDTO.getName();
//			Integer sort1 = mkStandardDTO.getSort();
//			if (StringUtils.isEmpty(name1)||sort1==null) {
//				return Response.returnCode(BackCode.RESPONSE_1001.getCode(),
//						BackCode.RESPONSE_1001.getMsg());
//			}
//			mkStandardDTO.setCreateBy(007);// 创建者
//			mkStandardDTO.setCreateDate(new Date());// 创建时间
//			mkStandardDTO.setUpdateBy(mkGoodsSpuDTO.getCreateBy());// 更新者
//			mkStandardDTO.setUpdateDate(mkGoodsSpuDTO.getCreateDate());// 更新时间
//			mkStandardDTO.setVersion(0);// 乐观锁标志
//			mkStandardMapper.insert(mkStandardDTO);
			// 规格属性
			//for (MkStandardDTO mkStandardDTO : standardDTOList) {
			List<MkStandardAttr> standardAttrList = mkStandardDTO.getStandardAttrList();
			if (standardAttrList.size() < 1) {
				return Response.returnCode(ReturnCode.BIZ_FAIL.getCode(), "对不起，请至少勾选一个规格属性！");
			}
			for (MkStandardAttr mkStandardAttr : standardAttrList) {
				mkStandardAttr.setStandardId(mkStandardDTO.getId());
				String attrValue = mkStandardAttr.getAttrValue();
				Integer sort2 = mkStandardAttr.getSort();
				if (StringUtils.isEmpty(attrValue)||sort2==null) {
					return Response.returnCode(BackCode.RESPONSE_1001.getCode(),
							BackCode.RESPONSE_1001.getMsg());
				}
				mkStandardAttr.setCreateBy(007);// 创建者
				mkStandardAttr.setCreateDate(new Date());// 创建时间
				mkStandardAttr.setUpdateBy(mkGoodsSpuDTO.getCreateBy());// 更新者
				mkStandardAttr.setUpdateDate(mkGoodsSpuDTO.getCreateDate());// 更新时间
				//mkStandardAttr.setVersion(0);// 乐观锁标志
				//校验
				//mkStandardAttr.getId();
				//mkStandardAttr.getVersion();
				boolean flag3 = mkStandardAttrService.updateById(mkStandardAttr);
				if (!flag3) {
					throw new LockException("乐观锁异常！");
				}
			}
			//mkStandardAttrService.insertBatch(standardAttrList);
		}

		return Response.ok();
	}

	@Override
	public MkGoodsSpuDTO selectActivityGoodsDetail(MkGoodsSpuDTO mkGoodsSpuDTO) {
		Integer id = mkGoodsSpuDTO.getId();
		MkGoodsSpu mkGoodsSpu = mkGoodsSpuMapper.selectById(id);
		MkGoodsSpuDTO mkGoodsSpuDTO1 = new MkGoodsSpuDTO();
		BeanUtils.copyProperties(mkGoodsSpu,mkGoodsSpuDTO1);
		EntityWrapper<MkGoodsSku> wrapper = new EntityWrapper<>();
		wrapper.eq("spu_id",id);
		List<MkGoodsSku> mkGoodsSkus = mkGoodsSkuMapper.selectList(wrapper);
		mkGoodsSpuDTO1.setGoodsSkuList(mkGoodsSkus);
		EntityWrapper<MkStandard> wrapper1 = new EntityWrapper<>();
		wrapper1.eq("spu_id",id);
		List<MkStandard> mkStandards = mkStandardMapper.selectList(wrapper1);
		List<MkStandardDTO> mkStandardDTOList = new LinkedList<>();
		for (MkStandard mkStandard : mkStandards) {
			Integer id1 = mkStandard.getId();
			EntityWrapper<MkStandardAttr> wrapper2 = new EntityWrapper<>();
			wrapper2.eq("standard_id",id1);
			List<MkStandardAttr> mkStandardAttrList = mkStandardAttrMapper.selectList(wrapper2);
			MkStandardDTO mkStandardDTO = new MkStandardDTO();
			BeanUtils.copyProperties(mkStandard,mkStandardDTO);
			mkStandardDTO.setStandardAttrList(mkStandardAttrList);
			mkStandardDTOList.add(mkStandardDTO);
		}
		mkGoodsSpuDTO1.setStandardDTOList(mkStandardDTOList);
		return mkGoodsSpuDTO1;
	}

	@Override
	@Transactional
	public Response stockBack(List<Integer> skuids) {
		for (Integer skuid : skuids) {
			MkGoodsSku mkGoodsSku=mkGoodsSkuMapper.selectSkuByGoodsSkuId(skuid);
			if(mkGoodsSku==null){
				throw new RuntimeException("无效的skuid!");
			}
			Integer remainStock=mkGoodsSku.getRemainStock();
			MkGoodsSku mkGoodsSku2=new MkGoodsSku();
			mkGoodsSku2.setId(mkGoodsSku.getId());
			mkGoodsSku2.setRemainStock(++remainStock);
			mkGoodsSku2.setVersion(mkGoodsSku.getVersion());
			boolean flag=mkGoodsSkuService.updateById(mkGoodsSku2);
			if(!flag){
				throw new LockException("乐观锁异常");
			}
			
			 
			Integer spuId=mkGoodsSku.getSpuId();
			MkGoodsSpu mkGoodsSpu=this.selectById(spuId);
			if(mkGoodsSpu==null){
				throw new RuntimeException("无效的spuId! skuid:"+skuid);
			}
			Integer spuremainStock=mkGoodsSpu.getRemainStock();
			MkGoodsSpu mkGoodsSpu2=new MkGoodsSpu();
			mkGoodsSpu2.setId(mkGoodsSpu.getId());
			mkGoodsSpu2.setRemainStock(++spuremainStock);
			mkGoodsSpu2.setVersion(mkGoodsSpu.getVersion());
			flag=this.updateById(mkGoodsSpu2);
			if(!flag){
				throw new LockException("乐观锁异常");
			}
		}
		return Response.ok();
	}



}
