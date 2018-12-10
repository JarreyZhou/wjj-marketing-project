/**  
* <p>Title: MybatisPlusConfig.java</p>  
* <p>Description: </p>  
* <p>Copyright: 上海卫健家健康技术有限公司 (c) 2018</p>  
* <p>Company: www.waygiga.com</p>  
* @author bob
* @date 2018年9月12日  
* @version 1.0  
*/
package com.wjj.application.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;

/**
 * <p>
 * Title: MybatisPlusConfig.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: 上海卫健家健康技术有限公司 (c) 2018
 * </p>
 * <p>
 * Company: www.waygiga.com
 * </p>
 *
 * @author bob
 * @date 2018年9月12日
 * @version 1.0
 */
@Configuration
@MapperScan("com.wjj.application.mapper.*")
public class MybatisPlusConfig {

	/**
	 * mybatis-plus分页插件<br>
	 * 文档：http://mp.baomidou.com<br>
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		return paginationInterceptor;
	}

	/**
	 * 乐观锁 插件
	 *
	 * @return
	 */
	@Bean
	public OptimisticLockerInterceptor optimisticLoker() {
		return new OptimisticLockerInterceptor();
	}
}