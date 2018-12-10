package com.wjj.application.service.pennyRob.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wjj.application.common.FastJsonUtils;
import com.wjj.application.dto.GoodsMainInfoDto;
import com.wjj.application.dto.RequestMainGoods;
import com.wjj.application.dto.pennyRob.MkActivityRobDTO;
import com.wjj.application.dto.pennyRob.MkGoodsSpuDTO;
import com.wjj.application.entity.pennyRob.MkActivity;
import com.wjj.application.entity.pennyRob.MkActivityRob;
import com.wjj.application.entity.pennyRob.MkActivitySponsor;
import com.wjj.application.enums.BackCode;
import com.wjj.application.feign.GoodsMainInfoFeignClient;
import com.wjj.application.mapper.pennyRob.MkActivityMapper;
import com.wjj.application.mapper.pennyRob.MkActivityRobMapper;
import com.wjj.application.mapper.pennyRob.MkActivitySponsorMapper;
import com.wjj.application.mapper.pennyRob.MkGoodsSpuMapper;
import com.wjj.application.response.Response;
import com.wjj.application.service.pennyRob.MkActivityRobService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 活动参与帮抢者 服务实现类
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */
@Slf4j
@Service
public class MkActivityRobServiceImpl extends ServiceImpl<MkActivityRobMapper, MkActivityRob> implements MkActivityRobService {

    @Autowired
    private MkActivityRobMapper mkActivityRobMapper;

    @Autowired
    private MkActivityMapper mkActivityMapper;

    @Autowired
    private MkActivitySponsorMapper mkActivitySponsorMapper;

    @Autowired
    private MkGoodsSpuMapper mkGoodsSpuMapper;

    @Autowired
    private GoodsMainInfoFeignClient goodsMainInfoFeignClient;

    @Override
    public Response helpRob(MkActivityRobDTO mkActivityRobDTO) {
        Integer activityId = mkActivityRobDTO.getMkActivityId();
        Integer sponsorId = mkActivityRobDTO.getMkSponsorId();
        if (activityId == null || sponsorId == null) {
            return Response.returnCode(BackCode.RESPONSE_1001.getCode(), BackCode.RESPONSE_1001.getMsg());
        }
        //帮抢人列表
        List<MkActivityRob> mkActivityRobs = mkActivityRobMapper.selectHelpRobList(sponsorId);
        //帮抢人数
        int size = mkActivityRobs.size();
        //活动失效时间
        MkActivity mkActivity = mkActivityMapper.selectById(activityId);
        Integer helpIndate = mkActivity.getHelpIndate();
        MkActivitySponsor mkActivitySponsor = mkActivitySponsorMapper.selectById(sponsorId);
        Date createDate = mkActivitySponsor.getCreateDate();
        Calendar c = Calendar.getInstance();
        c.setTime(createDate);
        c.add(Calendar.HOUR, helpIndate);
        Date failureDate = c.getTime();
        long countDown = new Date().getTime() - failureDate.getTime();

        MkActivityRobDTO mkActivityRobDTO1 = new MkActivityRobDTO();
        mkActivityRobDTO1.setMkActivityRobs(mkActivityRobs);
        mkActivityRobDTO1.setSize(size);
        mkActivityRobDTO1.setCountDown(countDown);
        return Response.ok(mkActivityRobDTO1);
    }

    @Override
    public Response moreGoods(MkGoodsSpuDTO mkGoodsSpuDTO) {
        Integer activityId = mkGoodsSpuDTO.getActivityId();
        if (activityId == null) {
            return Response.returnCode(BackCode.RESPONSE_1001.getCode(), BackCode.RESPONSE_1001.getMsg());
        }
        List<MkGoodsSpuDTO> mkGoodsSpuDTOs = mkGoodsSpuMapper.moreGoods(activityId);
        for (MkGoodsSpuDTO spu : mkGoodsSpuDTOs) {
            String goodId = spu.getGoodId();
            if (goodId == null) {
                return Response.returnCode(BackCode.RESPONSE_1001.getCode(), BackCode.RESPONSE_1001.getMsg());
            }
            //商品主图
            RequestMainGoods requestMainGoods = new RequestMainGoods();
            requestMainGoods.setGoodsId(spu.getGoodId());
            Response response = null;
            try {
                response = goodsMainInfoFeignClient.selectGoodsBaseInfoDtoDetail(requestMainGoods);
                log.info(FastJsonUtils.toJSONString(response.getData()));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Response.returnCode(BackCode.FEIGN_FAIL.getCode(), "goodsMainInfoFeignClient.selectGoodsBaseInfoDtoDetail: parameter" + spu.getGoodId() + BackCode.FEIGN_FAIL.getMsg());
            }
            if(!response.getCode().equals(BackCode.SUCCESS.getCode())){
                return Response.returnCode(response.getCode(),"fegin:"+response.getMessage());
            }
            if (response.getData() == null) {
                log.info("feign返回data为null");
            } else {
                spu.setMovePicPath(FastJsonUtils.toBean(FastJsonUtils.toJSONString(response.getData()), GoodsMainInfoDto.class).getMovePicPath());
            }
        }
        return Response.ok(mkGoodsSpuDTOs);
    }

    @Override
    public List<MkActivityRobDTO> selectActivityRobList(MkActivityRobDTO mkActivityRobDTO) {
        List<MkActivityRobDTO> mkActivityRobDTOS = mkActivityRobMapper.selectActivityRobList(mkActivityRobDTO);
        return mkActivityRobDTOS;
    }
}
