package com.wjj.application.scheduled;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.wjj.application.entity.pennyRob.MkActivity;
import com.wjj.application.entity.pennyRob.MkActivitySponsor;
import com.wjj.application.enums.ActivitySponsorStatus;
import com.wjj.application.enums.ActivityStatus;
import com.wjj.application.mapper.pennyRob.MkActivityMapper;
import com.wjj.application.mapper.pennyRob.MkActivitySponsorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 活动结束逻辑处理
 */
@Component
@EnableScheduling
public class pennyRobActivityTask {
    @Autowired
    private MkActivityMapper mkActivityMapper;

    @Autowired
    private MkActivitySponsorMapper mkActivitySponsorMapper;
    
    @Autowired
	 private Environment env;

    /**
     * 活动结束
     */
    @Scheduled(cron = "0 0/1 * * * ?")// 10s跑一次
    @Transactional
    public void finishActivity(){
      //  MkActivity mkActivity = new MkActivity();
        //mkActivity.setStatus(ActivityStatus._003.key);
    	String sexTagType=env.getProperty("server.port");
    	if(sexTagType!=null&&sexTagType.equals("10086")) {
        Wrapper<MkActivity> wrapper = new EntityWrapper<>();
        wrapper.eq("status", (ActivityStatus._003.key));
        List<MkActivity> mkActivities = mkActivityMapper.selectList(wrapper);
        if(mkActivities.size()<1){
            return;
        }
        MkActivity conditionMkActivity = mkActivities.get(0);
        if(conditionMkActivity.getEndTime().before(new Date())){
            conditionMkActivity.setStatus(ActivityStatus._004.key);
            mkActivityMapper.updateById(conditionMkActivity);
        }
    	}
    }
    /**
     * 活动开始
     */
    @Scheduled(cron = "0 0/1 * * * ?")// 10s跑一次
    @Transactional
    public void startActivity(){
        //  MkActivity mkActivity = new MkActivity();
        //mkActivity.setStatus(ActivityStatus._003.key);
    	String sexTagType=env.getProperty("server.port");
    	if(sexTagType!=null&&sexTagType.equals("10086")) {
        Wrapper<MkActivity> wrapper = new EntityWrapper<>();
        wrapper.eq("status", (ActivityStatus._002.key));
        List<MkActivity> mkActivities = mkActivityMapper.selectList(wrapper);
        if(mkActivities.size()<1){
            return;
        }
        for (MkActivity mkActivity : mkActivities) {
            if(mkActivity.getStartTime().before(new Date())&&mkActivity.getEndTime().after(new Date())){
                mkActivity.setStatus(ActivityStatus._003.key);
                mkActivityMapper.updateById(mkActivity);
            }
        }
    	}
    }

    /**
     * 活动发起结束
     */
    @Scheduled(cron = "0 0/1 * * * ?")// 10s跑一次
    @Transactional
    public void finishActivitySponsor(){
    	String sexTagType=env.getProperty("server.port");
    	if(sexTagType!=null&&sexTagType.equals("10086")) {
        Wrapper<MkActivitySponsor> wrapper = new EntityWrapper<>();
        wrapper.eq("status", ActivitySponsorStatus._001.key);
        List<MkActivitySponsor> mkActivitySponsors = mkActivitySponsorMapper.selectList(wrapper);
        if(mkActivitySponsors.size()<1){
            return;
        }
        for (MkActivitySponsor activitySponsor : mkActivitySponsors) {
            Integer id = activitySponsor.getActivityId();
            MkActivity mkActivity = mkActivityMapper.selectById(id);
            if(mkActivity==null){
                continue;
            }
            long finishTime = activitySponsor.getCreateDate().getTime() + mkActivity.getHelpIndate() * 3600 * 1000;
            if(new Date().getTime() > finishTime){
                activitySponsor.setStatus(ActivitySponsorStatus._002.key);
                mkActivitySponsorMapper.updateById(activitySponsor);
            }
        }
    	}
    }
}
