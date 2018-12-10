package com.wjj.application.scheduled;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wjj.application.dto.GoodsMainInfoDto;
import com.wjj.application.entity.GoodsMainInfo;
import com.wjj.application.entity.marketing.MarketingGoods;
import com.wjj.application.feign.GoodsMainInfoFeignClient;
import com.wjj.application.mapper.marketing.MarketingGoodsMapper;

/**
 * 定时同步营销的  推荐及其 热销商品
 * @author user
 *
 */
@Component
@EnableScheduling
public class GoodsRecommendTask {


    @Autowired
    private GoodsMainInfoFeignClient goodsMainInfoFeignClient;

   
    @Autowired
    private MarketingGoodsMapper marketingGoodsMapper;
    
    @Autowired
	 private Environment env;

    /**
     * 这个地方埋个坑 ：7  表示按照推荐序号
     *
     * @throws 定时定时取出按推荐序号，排行的前10商品--运营端
     */
    //@Scheduled(cron = "0 0/3 * * * ?")
    @Scheduled(cron = "0 0 2 * * ?")// 每天凌晨1点跑一次
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor = {RuntimeException.class })
    public void insertRelGoodsBySort() throws Exception {
    	String sexTagType=env.getProperty("server.port");
    	if(sexTagType!=null&&sexTagType.equals("10086")) {
    	  List<GoodsMainInfoDto> list = goodsMainInfoFeignClient.selectGoodsByTimes();
    	  if(list!=null&&list.size()>0) {
    		  marketingGoodsMapper.deleteMarketingGoodsByActiveId(7L);
    		  for(GoodsMainInfoDto item:list) {
        		  MarketingGoods marketingGoods=new MarketingGoods();
        		  BeanUtils.copyProperties(item, marketingGoods);
        		  marketingGoods.setActiveId(7L);
        		  marketingGoods.setPicPath(item.getMovePicPath());
        		  marketingGoods.setNewHumanPrice(new BigDecimal(item.getNewHumanPrice()));
        		  marketingGoodsMapper.insert(marketingGoods);
        	  } 
    	  }
    	}
    }
    
    /**
     * 这个地方埋个坑 ：8  表示按照热销序号
     *
     * @throws 定时定时取出按推荐序号，排行的前10商品--运营端
     */
   // @Scheduled(cron = "0 0/3 * * * ?") // 3分钟跑一次开发查看效果
    @Scheduled(cron = "0 0 1 * * ?")// 每天凌晨1点跑一次
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor = {RuntimeException.class })
    public void insertHotGoodsBySort() throws Exception {
    	String sexTagType=env.getProperty("server.port");
    	if(sexTagType!=null&&sexTagType.equals("10086")) {
    	  List<GoodsMainInfoDto> list = goodsMainInfoFeignClient.selectHotGoods();
    	  if(list!=null&&list.size()>0) {
    		  marketingGoodsMapper.deleteMarketingGoodsByActiveId(8L);
    		  for(GoodsMainInfoDto item:list) {
        		  MarketingGoods marketingGoods=new MarketingGoods();
        		  BeanUtils.copyProperties(item, marketingGoods);
        		  marketingGoods.setActiveId(8L);
        		  marketingGoods.setPicPath(item.getMovePicPath());
        		  
        		  marketingGoods.setNewHumanPrice(new BigDecimal(item.getNewHumanPrice()));
        		  marketingGoodsMapper.insert(marketingGoods);
        	  } 
    	  }
    	}
    }
   

}
