/**  
* <p>Title: PageVO.java</p>  
* <p>Description: </p>  
* <p>Copyright: 上海卫健家健康技术有限公司 (c) 2018</p>  
* <p>Company: www.waygiga.com</p>  
* @author bob
* @date 2018年7月9日  
* @version 1.0  
*/  
package com.wjj.application.util;

import lombok.Data;

/**  
* <p>Title: PageVO.java</p>  
* <p>Description: </p>  
* <p>Copyright: 上海卫健家健康技术有限公司 (c) 2018</p>  
* <p>Company: www.waygiga.com</p>  
* @author bob
* @date 2018年7月9日  
* @version 1.0  
*/

@Data
public class PageVO {

	private Integer pageSize;
	
	private Integer pageNo;

	//分页查询开始参数
	private Integer index;
}
