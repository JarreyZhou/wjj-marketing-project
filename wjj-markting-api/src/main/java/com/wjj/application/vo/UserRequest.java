/**  
* <p>Title: UserRequest.java</p>  
* <p>Description: </p>  
* <p>Copyright: 上海卫健家健康技术有限公司 (c) 2018</p>  
* <p>Company: www.waygiga.com</p>  
* @author bob
* @date 2018年7月19日  
* @version 1.0  
*/  
package com.wjj.application.vo;

import java.io.Serializable;

import lombok.Data;

/**  
* <p>Title: UserRequest.java</p>  
* <p>Description: </p>  
* <p>Copyright: 上海卫健家健康技术有限公司 (c) 2018</p>  
* <p>Company: www.waygiga.com</p>  
* @author bob
* @date 2018年7月19日  
* @version 1.0  
*/
@Data
public class UserRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer userId;
	
	private Integer price;

	public UserRequest(Integer userId, Integer price) {
		super();
		this.userId = userId;
		this.price = price;
	}

	public UserRequest() {
		super();
	}
}
