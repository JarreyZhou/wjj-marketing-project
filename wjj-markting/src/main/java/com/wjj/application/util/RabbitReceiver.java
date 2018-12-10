package com.wjj.application.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.rabbitmq.client.Channel;
import com.wjj.application.dto.GiftUpdateStoreDTO;
import com.wjj.application.dto.GoodsMainInfoDto;
import com.wjj.application.entity.StandardDetail;
import com.wjj.application.json.JsonUtil;
import com.wjj.application.mapper.infomation.HealthRelGoodsMapper;
import com.wjj.application.mapper.marketing.MarketingGoodsMapper;
import com.wjj.application.mapper.video.VideoGoodsMapper;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class RabbitReceiver {
	
	@Autowired
	private RedisClient redisClient;
	
	
	@Autowired
	private MarketingGoodsMapper marketingGoodsMapper;
	
	@Autowired
	private VideoGoodsMapper videoGoodsMapper;

	
	 @Autowired
	 private HealthRelGoodsMapper healthRelGoodsMapper;
	
	
	
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = "${spring.rabbitmq.listener.queue.name}", 
			durable="${spring.rabbitmq.listener.queue.durable}"),
			exchange = @Exchange(value = "${spring.rabbitmq.listener.exchange.name}", 
			durable="${spring.rabbitmq.listener.exchange.durable}", 
			type= "${spring.rabbitmq.listener.exchange.type}", 
			ignoreDeclarationExceptions = "${spring.rabbitmq.listener.exchange.ignoreDeclarationExceptions}"),
			key = "${spring.rabbitmq.listener.key}"
			)
	)
	@RabbitHandler
	public void onMessage(Message message, Channel channel) throws Exception {
		log.info("-------------message-----------"+message.getPayload());
		Long deliveryTag = (Long)message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
		String mesType=(String) message.getHeaders().get("mesType");
		String mesId=(String) message.getHeaders().get("mesId");
		String messInfo = (String) message.getPayload();
		String mesIdStr = redisClient.get(mesId);
		if(!(mesIdStr!=null)) {
			//  过期 =600秒 10分钟
			redisClient.setTimes(mesId, "1",600);
			if(mesType!=null&&mesType.equals("1")) {//下单
				List<GiftUpdateStoreDTO> giftList = JsonUtil.json2List(messInfo,GiftUpdateStoreDTO.class);
				if(giftList!=null&&giftList.size()>0) {
					//  营销商品
					marketingGoodsMapper.updateGiftUpdateStoreDTOByGoodsIdAndDetailId(giftList);
					//videoGoodsMapper.updateVideoGoodsByGoodsId(giftList);
				}
			}else if(mesType!=null&&mesType.equals("2")) {//跟新商品信息
				GoodsMainInfoDto dtoNew = JSON.parseObject(messInfo, new TypeReference<GoodsMainInfoDto>() {});
				if(dtoNew!=null) {
					String goodsStatus = dtoNew.getGoodsStatus();
					if(goodsStatus.equals("1")) {//保存草稿 删除相关商品
						String goodsId = dtoNew.getGoodsId();
						if(goodsId!=null&&!goodsId.equals("")) {
							List<String> list=new ArrayList<>();
							list.add(goodsId);
							marketingGoodsMapper.deleteMarketingDetailListByMq(list);//营销商品
							videoGoodsMapper.deleteVideoGoodsByUpdateGoodsBase(list);//视频关联商品
							healthRelGoodsMapper.deleteHealthRelGoods(list);//资讯相关商品
						}	
					}else {//更新商品信息
						marketingGoodsMapper.updateMarketingGoodsByMq(dtoNew);
						List<StandardDetail> standardDetailListNew=new ArrayList<>();
						List<StandardDetail> standardDetailList = dtoNew.getStandardDetailList();//营销商品
						if(standardDetailList!=null && standardDetailList.size()>0) {
							for(StandardDetail item:standardDetailList) {
								Long detailId = item.getDetailId();
								if(detailId!=null) {
									standardDetailListNew.add(item);
								}
							}
							if(standardDetailListNew.size()>0) {
								marketingGoodsMapper.updateMarketingDetailListByMq(standardDetailListNew);//营销商品
							}
						}	
						videoGoodsMapper.updateVideoGoodsByUpdateGoodsBase(dtoNew);
						healthRelGoodsMapper.updateHealthRelGoodsByGoodsMain(dtoNew);
					}		
				}
			}else if(mesType!=null&&mesType.equals("3")) {//下架
				 List<String> json2List = JsonUtil.json2List(messInfo,String.class);
				 if(json2List!=null&& json2List.size()>0) {
						marketingGoodsMapper.deleteMarketingDetailListByMq(json2List);//营销商品
						videoGoodsMapper.deleteVideoGoodsByUpdateGoodsBase(json2List);//视频关联商品
						healthRelGoodsMapper.deleteHealthRelGoods(json2List);//资讯相关商品
					}	
			}else if(mesType!=null&&mesType.equals("4")) {//上架
				// List<String> json2List = JsonUtil.json2List(messInfo,String.class);
				// relGoodsMapper.updateUp(json2List);
			}
		}
		
		//手工ACK
		channel.basicAck(deliveryTag, false);
	
		
/*		.
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
		
		
		Long deliveryTag = (Long)message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
		//手工ACK
		channel.basicAck(deliveryTag, false);*/
		
		
	}
	

}
