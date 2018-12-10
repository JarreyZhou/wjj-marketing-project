package com.wjj.application.util;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class RabitMq {

	public final static String QUEUE_NAME="rabbitMQ.test";
	/*public static void main(String[] args) throws Exception{
		send();
		//review();
	}
*/	
	public static void send() throws Exception{/*
		  //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置RabbitMQ相关信息
        factory.setHost("192.168.1.68");
       factory.setUsername("guest");
       factory.setPassword("guest");
       factory.setPort(5672);
        //创建一个新的连接
        Connection connection = factory.newConnection();
        //创建一个通道
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello RabbitMQ  ssss ";
        //发送消息到队列中
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        System.out.println("Producer Send +'" + message + "'");
        //关闭通道和连接
        channel.close();
        connection.close();
	*/}
	
	public static void review()throws Exception {/*
		  ConnectionFactory factory = new ConnectionFactory();
	        //设置RabbitMQ相关信息
	        factory.setHost("192.168.1.68");
	       factory.setUsername("guest");
	       factory.setPassword("guest");
	       factory.setPort(5672);
	        //创建一个新的连接
	        Connection connection = factory.newConnection();
        // 从连接中创建通道
        Channel channel = connection.createChannel();
 
        // 声明队列(如果你已经明确的知道有这个队列,那么下面这句代码可以注释掉,如果不注释掉的话,也可以理解为消费者必须监听一个队列,如果没有就创建一个)
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
 
        // 定义队列的消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);
       
         * 监听队列
         * 参数1:队列名称
         * 参数2：是否发送ack包，不发送ack消息会持续在服务端保存，直到收到ack。  可以通过channel.basicAck手动回复ack
         * 参数3：消费者
          
        channel.basicConsume(QUEUE_NAME, true, consumer);
 
        // 获取消息
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(" [消费者] Received '" + message + "'");
        }

	*/}

}
