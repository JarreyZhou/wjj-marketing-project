package com.wjj.application.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger配置 如果不想开启把下面的注解注释掉
 * @author lli
 * @version 1.0
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
	
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
//				.securitySchemes(Arrays.asList(new ApiKey[]{this.apiKey()}))
				.securitySchemes(Arrays.asList(new BasicAuth("test"))) //账号密码登录
				.apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.wjj.application.controller")).paths(PathSelectors.any())
				.build();
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("商品及其商品类型").description("商品及其商品类型接口一览表").version("1.0").build();
	}
	
	ApiKey apiKey() {  
        return new ApiKey("sessionId", "sessionId", "header");  
    }  

}
