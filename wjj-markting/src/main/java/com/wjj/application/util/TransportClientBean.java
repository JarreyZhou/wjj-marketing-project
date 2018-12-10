package com.wjj.application.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class TransportClientBean {
	@Autowired
	private Environment environment; 
	
 	@Bean(name = "transportClient")
    public TransportClient getClient() throws UnknownHostException {
 		String host=environment.getProperty("spring.elasticsearch.host");
		String portStr=environment.getProperty("spring.elasticsearch.port");
		int port =Integer.parseInt(portStr);
		Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
		TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(new TransportAddress(InetAddress.getByName(host), port)); 
		return client;
    }

}
