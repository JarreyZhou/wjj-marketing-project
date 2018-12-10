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

import com.wjj.application.entity.marketing.MarketingGoods;
import com.wjj.application.feign.GoodsMainInfoFeignClient;
import com.wjj.application.mapper.infomation.HealthRelGoodsMapper;
import com.wjj.application.mapper.marketing.MarketingGoodsMapper;
import com.wjj.application.mapper.video.VideoGoodsMapper;

/**
 * 商品信息和营销商品信息信息同步
 * @author user
 *
 */
@Component
@EnableScheduling
public class GoodsInfoSynchronizedTask {
	
	 @Autowired
	 private GoodsMainInfoFeignClient goodsMainInfoFeignClient;
	 
	 @Autowired
	 private MarketingGoodsMapper marketingGoodsMapper;
	 
	 @Autowired
	 private VideoGoodsMapper videoGoodsMapper;
	 
	 @Autowired
	 private HealthRelGoodsMapper healthRelGoodsMapper;
	 
	 @Autowired
	 private Environment env;
	
	 /**
     * 
     *
     * @throws 视屏观看量同步
     */
   // @Scheduled(cron = "0 0/1 * * * ?") // 每分钟执行一次
    @Scheduled(cron = "0 0 2 * * ?")// 每天凌晨2点跑一次
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor = {RuntimeException.class })
    public void insertMarketingGoods() throws Exception {
    	String sexTagType=env.getProperty("server.port");
    	if(sexTagType!=null&&sexTagType.equals("10086")) {
    		//List<String> list = marketingGoodsMapper.selectMarketingAllGoodsId();
        	//selectMarketingAllDetailId
        	List<MarketingGoods> goodsArr =new ArrayList<>();
        	List<MarketingGoods> detailArr =new ArrayList<>();
        	List<MarketingGoods> videoGoodsArr=new ArrayList<>();
        	//营销  商品级别
        	List<String> strList = marketingGoodsMapper.selectMarketingAllGoodsId();
        	if(strList!=null&&strList.size()>0) {
        		List<MarketingGoods> goodsList = goodsMainInfoFeignClient.selectMarketingGoodsList(strList);
        		if(goodsList!=null&&goodsList.size()>0) {
        			for(MarketingGoods item:goodsList) {
        				goodsArr.add(item);
        				if(goodsArr.size()>500) {
        					marketingGoodsMapper.updateMarketingGoodsList(goodsArr);
        					goodsArr.clear();
        				}
        			}
        			marketingGoodsMapper.updateMarketingGoodsList(goodsArr);
        		}
        		
        	}
        	//规格级别
        	List<Long> lonList = marketingGoodsMapper.selectMarketingAllDetailId();
        	if(lonList!=null&&lonList.size()>0) {
        		List<MarketingGoods> goodsList = goodsMainInfoFeignClient.selectMarketingDetailList(lonList);
        		if(goodsList!=null&&goodsList.size()>0) {
        			for(MarketingGoods item:goodsList) {
        				detailArr.add(item);
        				if(goodsArr.size()>500) {
        					marketingGoodsMapper.updateMarketingDetailList(detailArr);
        					detailArr.clear();
        				}
        			}
        			marketingGoodsMapper.updateMarketingDetailList(detailArr);
        		}
        	}
        	
        	//同步视屏相关商品
        	List<String> videoStrList = videoGoodsMapper.selectVideoGoodsAllGoodsId();
        	if(videoStrList!=null&&videoStrList.size()>0) {
        		List<MarketingGoods> goodsList = goodsMainInfoFeignClient.selectMarketingGoodsList(videoStrList);
        		if(goodsList!=null&&goodsList.size()>0) {
        			for(MarketingGoods item:goodsList) {
        				videoGoodsArr.add(item);
        				if(goodsArr.size()>500) {
        					videoGoodsMapper.updateVideoGoodsByList(goodsArr);
        					videoGoodsArr.clear();
        				}
        			}
        			videoGoodsMapper.updateVideoGoodsByList(goodsArr);
        		}
        		
        	}
        	// 资讯健康相关的商品
        	List<String> healthRelGoodsStrList = healthRelGoodsMapper.selectHealthRelGoodsAllGoodsId();
        	if(healthRelGoodsStrList!=null&&healthRelGoodsStrList.size()>0) {
        		List<MarketingGoods> goodsList = goodsMainInfoFeignClient.selectMarketingGoodsList(healthRelGoodsStrList);
        		if(goodsList!=null&&goodsList.size()>0) {
        			for(MarketingGoods item:goodsList) {
        				videoGoodsArr.add(item);
        				if(goodsArr.size()>500) {
        					healthRelGoodsMapper.updateHealthRelGoodsByList(goodsArr);
        					videoGoodsArr.clear();
        				}
        			}
        			healthRelGoodsMapper.updateHealthRelGoodsByList(goodsArr);
        		}
        		
        	}
    	}
    	
    	
    }
    
    //商品的shangxai
   // @Scheduled(cron = "0 0/1 * * * ?") // 每分钟执行一次
    @Scheduled(cron = "0 0 3 * * ?")// 每天凌晨3点跑一次
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor = {RuntimeException.class })
    public void selectGoodsidListForDraftAll() throws Exception {
    	String sexTagType=env.getProperty("server.port");
    	if(sexTagType!=null&&sexTagType.equals("10086")) {
	    	List<String> goodsArr =new ArrayList<>();
	    	List<Long> detailArr =new ArrayList<>();
	    	
	    	List<String> goodsList = goodsMainInfoFeignClient.selectGoodsidListForDraftAll();
	    	
	    	if(goodsList!=null&&goodsList.size()>0) {
	    		if(goodsList!=null&&goodsList.size()>0) {
	    			for(String item:goodsList) {
	    				goodsArr.add(item);
	    				if(goodsArr.size()>500) {
	    					marketingGoodsMapper.deleteMarketingDetailListByMq(goodsList);
	    					videoGoodsMapper.deleteVideoGoodsByUpdateGoodsBase(goodsList);//视频关联商品
							healthRelGoodsMapper.deleteHealthRelGoods(goodsList);//资讯相关商品
	    					goodsArr.clear();
	    				}
	    			}
	    			marketingGoodsMapper.deleteMarketingDetailListByMq(goodsList);
					videoGoodsMapper.deleteVideoGoodsByUpdateGoodsBase(goodsList);//视频关联商品
					healthRelGoodsMapper.deleteHealthRelGoods(goodsList);//资讯相关商品
	    		}
	    		
	    	}
	    	//规格级别
	    	List<Long> lonList = goodsMainInfoFeignClient.selectDetailListForDraftAll();
	    	if(lonList!=null&&lonList.size()>0) {
	    		
	    		if(lonList!=null&&lonList.size()>0) {
	    			for(Long item:lonList) {
	    				detailArr.add(item);
	    				if(goodsArr.size()>500) {
	    					marketingGoodsMapper.deleteMarketingDetailListForTimer(detailArr);
	    					detailArr.clear();
	    				}
	    			}
	    			marketingGoodsMapper.deleteMarketingDetailListForTimer(detailArr);
	    		}
	    	}
    	}
    }
	

}
