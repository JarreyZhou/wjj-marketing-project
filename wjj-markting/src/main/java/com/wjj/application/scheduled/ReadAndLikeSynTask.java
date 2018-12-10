package com.wjj.application.scheduled;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wjj.application.entity.information.HealthInfomation;
import com.wjj.application.entity.video.VideoManagerEntity;
import com.wjj.application.mapper.infomation.HealthInfomationMapper;
import com.wjj.application.mapper.video.VideoArticleEntityMapper;
import com.wjj.application.mapper.video.VideoManagerMapper;
import com.wjj.application.util.RedisClient;

/**
 * 同步资讯及其视频的点击量
 * @author user
 *
 */
@Component
@EnableScheduling
public class ReadAndLikeSynTask {
	
	
	@Autowired
	private VideoManagerMapper videoManagerMapper;
	
	@Autowired
	private HealthInfomationMapper healthInfomationMapper;
	
	@Autowired
	private RedisClient redisClient;
	
	@Autowired
	private VideoArticleEntityMapper videoArticleEntityMapper;
	
	
	 @Autowired
	 private Environment env;
	
	 /**
     * 
     *
     * @throws 视屏观看量同步
     */
    //@Scheduled(cron = "0 0/1 * * * ?") // 每分钟执行一次
    @Scheduled(cron = "0 0 2 * * ?")// 每天凌晨1点跑一次
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor = {RuntimeException.class })
    public void insertVideoRead() throws Exception {
    	String sexTagType=env.getProperty("server.port");
    	if(sexTagType!=null&&sexTagType.equals("10086")) {
    	List<VideoManagerEntity> videoList = videoManagerMapper.selectVideoManagerActualNum();
    	List<VideoManagerEntity> list=new ArrayList<>();
    	if(videoList!=null&&videoList.size()>0) {
    		for(VideoManagerEntity item:videoList) {
    			String videoClickNum="videoclicknum"+item.getId();
    			String videoclicknum = redisClient.get(videoClickNum);
    			if(videoclicknum!=null&&!videoclicknum.equals("")) {
    				redisClient.del(videoClickNum);
    				item.setActualNum(Integer.valueOf(videoclicknum));
    			}
    			list.add(item);
    			if(list.size()>500) {
    				videoManagerMapper.updateVideoManagerActualNum(list);
    				
    				list.clear();
    			}	
    		}
    		videoManagerMapper.updateVideoManagerActualNum(list);
    	}
    	}
    	
    	
    	
    }
    
    /**
     * 资讯喜欢量及其观看同步
     * 
     */
   // @Scheduled(cron = "0 0/1 * * * ?") // 每分钟执行一次
    @Scheduled(cron = "0 0 3 * * ?")// 每天凌晨1点跑一次
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor = {RuntimeException.class })
    public void insertHealthInfomationReadLike() throws Exception {
    	String sexTagType=env.getProperty("server.port");
    	if(sexTagType!=null&&sexTagType.equals("10086")) {
    	List<HealthInfomation> infoList = healthInfomationMapper.selectHealthInfomationId();
    	List<HealthInfomation> list=new ArrayList<>();
    	if(infoList!=null&&infoList.size()>0) {
    		for(HealthInfomation item:infoList) {
    			String infoRead="infomationreadclicknum"+item.getId();
    			String infoLike="infomationlikeclicknum"+item.getId();
    			String readNum = redisClient.get(infoRead);
    			String likeNum = redisClient.get(infoLike);
    			if(readNum!=null&&!readNum.equals("")) {
    				redisClient.del(infoRead);
    				item.setRealRead(Integer.parseInt(readNum));
    				
    			}
    			if(likeNum!=null&&!likeNum.equals("")) {
    				redisClient.del(infoLike);
    				item.setRealLike(Integer.parseInt(likeNum));
    			}
    			list.add(item);
    			if(list.size()>500) {
    				videoArticleEntityMapper.updateRealReadByHealthInfomation(list);
    				List<HealthInfomation> newlist=new ArrayList<>();
    				for(HealthInfomation itemNew:list) {
    					Integer realRead = itemNew.getRealRead();
    					if(realRead!=null) {
    						newlist.add(itemNew);
    					}
    				}
    				if(newlist!=null&&newlist.size()>0) {
    					healthInfomationMapper.updateHealthInfomationActualNum(list);
    				}
    				
    				list.clear();
    			}	
    		}
    		videoArticleEntityMapper.updateRealReadByHealthInfomation(list);
    		List<HealthInfomation> newlist=new ArrayList<>();
			for(HealthInfomation itemNew:list) {
				Integer realRead = itemNew.getRealRead();
				if(realRead!=null) {
					newlist.add(itemNew);
				}
			}
			if(newlist!=null&&newlist.size()>0) {
				healthInfomationMapper.updateHealthInfomationActualNum(list);
			}
    		
    	}
    	}
    }
    

}
