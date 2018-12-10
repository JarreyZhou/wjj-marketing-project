/**  
* <p>Title: Participant.java</p>  
* <p>Description: </p>  
* <p>Copyright: 上海卫健家健康技术有限公司 (c) 2018</p>  
* <p>Company: www.waygiga.com</p>  
* @author bob
* @date 2018年7月19日  
* @version 1.0  
*/  
package com.wjj.application.vo;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Date;

import lombok.Data;

/**  
* <p>Title: Participant.java</p>  
* <p>Description: </p>  
* <p>Copyright: 上海卫健家健康技术有限公司 (c) 2018</p>  
* <p>Company: www.waygiga.com</p>  
* @author bob
* @date 2018年7月19日  
* @version 1.0  
*/
@Data
public class Participant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String uri;
	private Date expires;

	public Participant(String uri, Date expires) {
		super();
		this.uri = uri;
		this.expires = expires;
	}

	public Participant() {
		super();
	}
}
