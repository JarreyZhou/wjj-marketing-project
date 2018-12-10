package com.wjj.application.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RabbitSender {
		@Autowired
		private RabbitTemplate rabbitTemplate;  
		

	    @Autowired
	    private Environment env;
		
		//回调函数: confirm确认
		final ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				log.info("correlationData: " + correlationData);
				log.info("ack: " + ack);
				if(!ack){
					log.info("-------消息异常-----");
				}
			}
		};
		
		
		//发送消息方法调用: 构建Message消息
		public void send(String message, Map<String, Object> properties) throws Exception {
			log.info("----send---message-----"+message);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			 String dateString = formatter.format(new Date());
			MessageHeaders mhs = new MessageHeaders(properties);
			Message msg = MessageBuilder.createMessage(message, mhs);
			rabbitTemplate.setConfirmCallback(confirmCallback);
			//id + 时间戳 全局唯一
			String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
			uuid="goods_"+dateString+"_"+uuid;
			CorrelationData correlationData = new CorrelationData(uuid);
			 String exchange=env.getProperty("spring.rabbitmq.exchange");
			 String goods_rount_key=env.getProperty("spring.rabbitmq.rount_key");
			rabbitTemplate.convertAndSend(exchange, goods_rount_key, msg, correlationData);
		}
}
