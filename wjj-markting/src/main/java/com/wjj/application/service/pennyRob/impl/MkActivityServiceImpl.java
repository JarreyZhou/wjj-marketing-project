package com.wjj.application.service.pennyRob.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wjj.application.common.FastJsonUtils;
import com.wjj.application.dto.GoodsMainInfoDto;
import com.wjj.application.dto.RequestMainGoods;
import com.wjj.application.dto.pennyRob.MkActivityDTO;
import com.wjj.application.dto.pennyRob.MkGoodsSpuDTO;
import com.wjj.application.entity.pennyRob.MkActivity;
import com.wjj.application.enums.ActivityStatus;
import com.wjj.application.enums.BackCode;
import com.wjj.application.feign.GoodsMainInfoFeignClient;
import com.wjj.application.mapper.pennyRob.MkActivityMapper;
import com.wjj.application.mapper.pennyRob.MkGoodsSpuMapper;
import com.wjj.application.page.PageDTO;
import com.wjj.application.response.AccountReturnCode;
import com.wjj.application.response.Response;
import com.wjj.application.response.ReturnCode;
import com.wjj.application.service.pennyRob.MkActivityService;
import com.wjj.application.util.LockException;
import com.wjj.application.vo.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 活动表 服务实现类
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */
@Service
@Slf4j
public class MkActivityServiceImpl extends ServiceImpl<MkActivityMapper, MkActivity> implements MkActivityService {

	@Autowired
	private MkActivityMapper mkActivityMapper;

	@Autowired
	private MkGoodsSpuMapper mkGoodsSpuMapper;

	@Autowired
	private GoodsMainInfoFeignClient goodsMainInfoFeignClient;

	@Override
	public List<MkActivity> list() {
		List<MkActivity> list = mkActivityMapper.list();
		return list;
	}

	@Override
	public Response snatchList(PageVO pageVO) {
		pageVO.setIndex((pageVO.getPageNo() - 1) * pageVO.getPageSize());
		List<MkGoodsSpuDTO> mkGoodsSpuDTOs = mkGoodsSpuMapper.snatchPageList(pageVO);
		Integer count = mkGoodsSpuMapper.snatchCountList(pageVO);
		if(mkGoodsSpuDTOs == null|| mkGoodsSpuDTOs.size() ==0||count == 0){
            return Response.returnCode(ReturnCode.SUCCESS.getCode(),"没有商品");
        }

		//查询活动开始结束日期
		MkActivity mkActivityCondition = new MkActivity();
		mkActivityCondition.setId(mkGoodsSpuDTOs.get(0).getActivityId());
		MkActivity mkActivity = mkActivityMapper.selectOne(mkActivityCondition);

		Page<MkGoodsSpuDTO> page = new Page<>();
		//根据good_id查询商品图片
		for (MkGoodsSpuDTO goodsSpuDTO : mkGoodsSpuDTOs) {

			//计算活动结束时间
			Date endDate = mkActivity.getEndTime();
			goodsSpuDTO.setDistanceEndTime(endDate.getTime() - new Date().getTime());

			// TODO: 2018/11/14  替换图片真实路径
			//条件查询得到图片路径
			RequestMainGoods requestMainGoods = new RequestMainGoods();
			requestMainGoods.setGoodsId(goodsSpuDTO.getGoodId());
			Response response = null;
			try {
				response = goodsMainInfoFeignClient.selectGoodsBaseInfoDtoDetail(requestMainGoods);
			}catch (Exception e) {
				log.error(e.getMessage(), e);
				return Response.returnCode(BackCode.FEIGN_FAIL.getCode(),"goodsMainInfoFeignClient.selectGoodsBaseInfoDtoDetail: parameter" + goodsSpuDTO.getGoodId() + BackCode.FEIGN_FAIL.getMsg());
			}
            if(!response.getCode().equals(BackCode.SUCCESS.getCode())){
                return Response.returnCode(response.getCode(),"fegin:"+response.getMessage());
            }
			if(response.getData() == null){
				log.info("feign返回data为null");
			}else{
				goodsSpuDTO.setMovePicPath(FastJsonUtils.toBean(FastJsonUtils.toJSONString(response.getData()),GoodsMainInfoDto.class).getMovePicPath());
			}
//			goodsSpuDTO.setMovePicPath("http://pctr776pn.bkt.clouddn.com/FiVUl43wSHOmScsZs83UKjCC7Vvw?imageslim");
		}
		page.setTotal(count);
		page.setRecords(mkGoodsSpuDTOs);
		log.info("snatchList出参:"+ FastJsonUtils.toJSONString(page));
		return Response.ok(page);
	}

	@Override
	public Response comingList(PageVO pageVO) {
		pageVO.setIndex((pageVO.getPageNo() - 1) * pageVO.getPageSize());
		List<MkGoodsSpuDTO> mkGoodsSpuDTOs = mkGoodsSpuMapper.comingPageList(pageVO);
		Integer count = mkGoodsSpuMapper.comingCountList(pageVO);
		if(mkGoodsSpuDTOs == null || mkGoodsSpuDTOs.size() == 0 || count == 0){
            return Response.returnCode(ReturnCode.SUCCESS.getCode(),"没有商品");
        }
		Page<MkGoodsSpuDTO> page = new Page<>();

		//查询活动开始结束日期
		MkActivity mkActivityCondition = new MkActivity();
		mkActivityCondition.setId(mkGoodsSpuDTOs.get(0).getActivityId());
		MkActivity mkActivity = mkActivityMapper.selectOne(mkActivityCondition);

		//根据good_id查询商品图片
		for (MkGoodsSpuDTO goodsSpuDTO : mkGoodsSpuDTOs) {

			//计算活动结束时间
			Date createDate = mkActivity.getStartTime();
			goodsSpuDTO.setDistanceEndTime(createDate.getTime() - new Date().getTime());

			// TODO: 2018/11/14  替换图片真实路径
			//条件查询得到图片路径
			RequestMainGoods requestMainGoods = new RequestMainGoods();
			requestMainGoods.setGoodsId(goodsSpuDTO.getGoodId());
			Response response = null;
			try {
				response = goodsMainInfoFeignClient.selectGoodsBaseInfoDtoDetail(requestMainGoods);
			}catch (Exception e) {
				log.error(e.getMessage(), e);
                return Response.returnCode(BackCode.FEIGN_FAIL.getCode(),"goodsMainInfoFeignClient.selectGoodsBaseInfoDtoDetail: parameter" + goodsSpuDTO.getGoodId() + BackCode.FEIGN_FAIL.getMsg());
			}
            if(!response.getCode().equals(BackCode.SUCCESS.getCode())){
                return Response.returnCode(response.getCode(),"fegin:"+response.getMessage());
            }
            if(response.getData() == null){
                log.info("feign返回data为null");
            }else{
                goodsSpuDTO.setMovePicPath(FastJsonUtils.toBean(FastJsonUtils.toJSONString(response.getData()),GoodsMainInfoDto.class).getMovePicPath());
            }
//			goodsSpuDTO.setMovePicPath("http://pctr776pn.bkt.clouddn.com/FiVUl43wSHOmScsZs83UKjCC7Vvw?imageslim");
		}
		page.setTotal(count);
		page.setRecords(mkGoodsSpuDTOs);
		log.info("comingList出参:"+ FastJsonUtils.toJSONString(page));
		return Response.ok(page);
	}

	/**
	 * 1分抢-添加活动
	 */
	@Transactional
	@Override
	public Response insertActivity(MkActivity mkActivity) throws Exception {
		String name = mkActivity.getName();// 活动名称
		Date startTime = mkActivity.getStartTime();// 活动有效期开始时间
		Date endTime = mkActivity.getEndTime();// 活动有效期结束时间
		Integer robLimit = mkActivity.getRobLimit();// 每个用户最多开枪几个商品
		Integer helpIndate = mkActivity.getHelpIndate();// 帮抢有效期
		String weixinWenan = mkActivity.getWeixinWenan();// 微信分享文案
		String publicityPic = mkActivity.getPublicityPic();// 活动宣传图
		String regulation = mkActivity.getRegulation();// 1分抢活动规则
		if (StringUtils.isEmpty(name) || startTime == null || endTime == null || robLimit == null || helpIndate == null
				|| StringUtils.isEmpty(weixinWenan) || StringUtils.isEmpty(publicityPic)
				|| StringUtils.isEmpty(regulation)) {
			return Response.returnCode(AccountReturnCode.RESPONSE_1001.getCode(),
					AccountReturnCode.RESPONSE_1001.getMsg());
		}
		// 活动状态()
		if (mkActivity.getStatus() != 1) {// 草稿!
			if (startTime.after(new Date())) {
				mkActivity.setStatus(ActivityStatus._002.key);// 未开始!
			} else if (startTime.before(new Date()) && endTime.after(new Date())) {
				mkActivity.setStatus(ActivityStatus._003.key);// 进行中!
			} else if (endTime.before(new Date())) {
				mkActivity.setStatus(ActivityStatus._004.key);// 已过期!
			}
		}
		mkActivity.setCreateBy(007);// 创建者
		mkActivity.setCreateDate(new Date());// 创建时间
		mkActivity.setUpdateBy(mkActivity.getCreateBy());// 更新者
		mkActivity.setUpdateDate(mkActivity.getCreateDate());// 更新时间
		mkActivity.setVersion(0);// 乐观锁标志

		// OA审批单号
		// 活动发起总数
		// 活动帮抢总数
		// 订单量
		// 备注信息
		// 删除标记

		//活动有效期只能选择在【进行中】和【即将开始】活动的时间段外的时间，一个时间段内只能存在一个活动
		List<MkActivity> mkActivities = mkActivityMapper.selectAllActivity();
		for (MkActivity activity : mkActivities) {
			if(startTime.getTime()<activity.getEndTime().getTime()&&endTime.getTime()>activity.getStartTime().getTime()){
				return Response.returnCode(ReturnCode.BIZ_FAIL.getCode(), "对不起，时间冲突，请选择还没有活动安排的时间！");
			}
		}

		Integer insert = baseMapper.insert(mkActivity);
		if (insert == 0) {
			throw new RuntimeException("插入异常！");
		}
		return Response.ok(mkActivity);

	}

	/**
	 * 1分抢-活动列表
	 */
	@Override
	public PageDTO<MkActivity> selectActivityList(MkActivityDTO mkActivityDTO) {
		List<MkActivity> list = mkActivityMapper.selectPageList(mkActivityDTO);
		for (MkActivity mkActivity : list) {
			if (mkActivity.getStatus() != 1) {// 草稿!
				if (mkActivity.getStartTime().after(new Date())) {
					mkActivity.setStatus(ActivityStatus._002.key);// 未开始!
				} else if (mkActivity.getStartTime().before(new Date()) && mkActivity.getEndTime().after(new Date())) {
					mkActivity.setStatus(ActivityStatus._003.key);// 进行中!
				} else if (mkActivity.getEndTime().before(new Date())) {
					mkActivity.setStatus(ActivityStatus._004.key);// 已过期!
				}
			}
		}
		int totalCount = mkActivityMapper.selectPageCount(mkActivityDTO);
		return new PageDTO<MkActivity>(totalCount, list);
	}

	/**
	 * 1分抢-查看活动
	 */
	@Override
	public Response selectActivity(MkActivity mkActivity) {
		Integer id = mkActivity.getId();
		if (id == null) {
			return Response.returnCode(AccountReturnCode.RESPONSE_1001.getCode(),
					AccountReturnCode.RESPONSE_1001.getMsg());
		}
		return Response.ok(baseMapper.selectById(id));
	}

	/**
	 * 1分抢-编辑活动
	 */
	@Transactional
	@Override
	public Response updateActivity(MkActivity mkActivity) throws LockException {
		Integer id = mkActivity.getId();
		Integer version = mkActivity.getVersion();
		Integer status = mkActivity.getStatus();
		Date startTime = mkActivity.getStartTime();// 活动有效期开始时间
		Date endTime = mkActivity.getEndTime();// 活动有效期结束时间
		if (id == null || version == null || status == null|| startTime == null || endTime == null) {
			return Response.returnCode(AccountReturnCode.RESPONSE_1001.getCode(),
					AccountReturnCode.RESPONSE_1001.getMsg());
		}
		// 活动状态()
		if (mkActivity.getStatus() != 1) {// 草稿!
			if (startTime.after(new Date())) {
				mkActivity.setStatus(ActivityStatus._002.key);// 未开始!
			} else if (startTime.before(new Date()) && endTime.after(new Date())) {
				mkActivity.setStatus(ActivityStatus._003.key);// 进行中!
			} else if (endTime.before(new Date())) {
				mkActivity.setStatus(ActivityStatus._004.key);// 已过期!
			}
		}
		if (status == 4) {
			return Response.returnCode(ReturnCode.BIZ_FAIL.getCode(), "当前活动状态无法编辑！");
		}
		//活动有效期只能选择在【进行中】和【即将开始】活动的时间段外的时间，一个时间段内只能存在一个活动
		List<MkActivity> mkActivities = mkActivityMapper.selectAllActivity();
		for (MkActivity activity : mkActivities) {
			if(!id.equals(activity.getId())) {
				if (startTime.getTime() < activity.getEndTime().getTime() && endTime.getTime() > activity.getStartTime().getTime()) {
					return Response.returnCode(ReturnCode.BIZ_FAIL.getCode(), "对不起，时间冲突，请选择还没有活动安排的时间！");
				}
			}
		}
		Integer i = baseMapper.updateById(mkActivity);
		if (i == 0) {
			throw new LockException("乐观锁异常！");
		}
		return Response.ok();

	}
	/**
	 * 1分抢-编辑活动OA审批单号
	 */
	@Transactional
	@Override
	public Response updateActivityOA(MkActivity mkActivity) throws LockException {
		Integer id = mkActivity.getId();
		Integer version = mkActivity.getVersion();
		if (id == null || version == null) {
			return Response.returnCode(AccountReturnCode.RESPONSE_1001.getCode(),
					AccountReturnCode.RESPONSE_1001.getMsg());
		}
		Integer i = baseMapper.updateById(mkActivity);
		if (i == 0) {
			throw new LockException("乐观锁异常！");
		}
		return Response.ok();
	}

	/**
	 * 1分抢-下架活动
	 */
	@Transactional
	@Override
	public Response unshelveActivity(MkActivity mkActivity) throws LockException {
		//String name = mkActivity.getName();// 活动名称
		Date startTime = mkActivity.getStartTime();// 活动有效期开始时间
		Date endTime = mkActivity.getEndTime();// 活动有效期结束时间
		//Integer robLimit = mkActivity.getRobLimit();// 每个用户最多开枪几个商品
		//Integer helpIndate = mkActivity.getHelpIndate();// 帮抢有效期
		//String weixinWenan = mkActivity.getWeixinWenan();// 微信分享文案
		//String publicityPic = mkActivity.getPublicityPic();// 活动宣传图
		//String regulation = mkActivity.getRegulation();// 1分抢活动规则
		Integer status = mkActivity.getStatus();// 活动状态()
		Integer id = mkActivity.getId();
		Integer version = mkActivity.getVersion();
		if (status == null || id == null || version == null ||startTime==null||endTime==null) {
			return Response.returnCode(AccountReturnCode.RESPONSE_1001.getCode(),
					AccountReturnCode.RESPONSE_1001.getMsg());
		}
		// 活动状态()
		if (mkActivity.getStatus() != 1) {// 草稿!
			if (startTime.after(new Date())) {
				mkActivity.setStatus(ActivityStatus._002.key);// 未开始!
			} else if (startTime.before(new Date()) && endTime.after(new Date())) {
				mkActivity.setStatus(ActivityStatus._003.key);// 进行中!
			} else if (endTime.before(new Date())) {
				mkActivity.setStatus(ActivityStatus._004.key);// 已过期!
			}
		}
		if (status != 2) {
			return Response.returnCode(ReturnCode.BIZ_FAIL.getCode(), "对不起，只有未开始的活动才能下架！");
		}
		mkActivity.setStatus(ActivityStatus._001.key);// 下架后进入草稿状态
		Integer i = baseMapper.updateById(mkActivity);
		if (i == 0) {
			throw new LockException("乐观锁异常！");
		}
		return Response.ok();
	}

	/**
	 * 1分抢-上架活动
	 */
	@Transactional
	@Override
	public Response shelveActivity(MkActivity mkActivity) throws LockException {
		Date startTime = mkActivity.getStartTime();// 活动有效期开始时间
		Date endTime = mkActivity.getEndTime();// 活动有效期结束时间
		Integer id = mkActivity.getId();
		Integer status = mkActivity.getStatus();
		Integer version = mkActivity.getVersion();
		if (startTime == null || endTime == null || status == null || id == null || version == null) {
			return Response.returnCode(AccountReturnCode.RESPONSE_1001.getCode(),
					AccountReturnCode.RESPONSE_1001.getMsg());
		}
		if (status != 1) {
			return Response.returnCode(ReturnCode.BIZ_FAIL.getCode(), "对不起，只有草稿状态的活动才能上架！");
		}
		if (startTime.after(new Date())) {
			mkActivity.setStatus(ActivityStatus._002.key);// 未开始!
		} else if (startTime.before(new Date()) && endTime.after(new Date())) {
			mkActivity.setStatus(ActivityStatus._003.key);// 进行中!
		} else if (endTime.before(new Date())) {
			return Response.returnCode(ReturnCode.BIZ_FAIL.getCode(), "对不起，已过期的活动不能上架！");
		}
		Integer i = baseMapper.updateById(mkActivity);
		if (i == 0) {
			throw new LockException("乐观锁异常！");
		}
		return Response.ok();
	}

	/**
	 * 1分抢-删除活动草稿
	 */
	@Transactional
	@Override
	public Response deleteActivityDraft(MkActivity mkActivity) throws Exception {
		Integer id = mkActivity.getId();
		if (id == null) {
			return Response.returnCode(AccountReturnCode.RESPONSE_1001.getCode(),
					AccountReturnCode.RESPONSE_1001.getMsg());
		}
		if (mkActivity.getStatus() != 1) {
			return Response.returnCode(ReturnCode.BIZ_FAIL.getCode(), "对不起，只有草稿才能删除！");
		}
		Integer i = baseMapper.deleteById(id);
		if (i == 0) {
			throw new RuntimeException("删除异常！");
		}
		return Response.ok();
	}
}
