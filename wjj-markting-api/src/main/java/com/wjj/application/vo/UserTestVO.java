/**  
* <p>Title: UserTestVO.java</p>  
* <p>Description: </p>  
* <p>Copyright: 上海卫健家健康技术有限公司 (c) 2018</p>  
* <p>Company: www.waygiga.com</p>  
* @author bob
* @date 2018年7月17日  
* @version 1.0  
*/  
package com.wjj.application.vo;

import java.sql.Date;

import lombok.Data;

/**  
* <p>Title: UserTestVO.java</p>  
* <p>Description: </p>  
* <p>Copyright: 上海卫健家健康技术有限公司 (c) 2018</p>  
* <p>Company: www.waygiga.com</p>  
* @author bob
* @date 2018年7月17日  
* @version 1.0  
*/
@Data
public class UserTestVO {
	
    private Integer id;
    private String name;
    private Integer age;
    private Integer testId;
	
    private String testName;
    private String description;
    private Date createTime;
    private Date updateTime;
    private Long number;
    private String lifecycle;
    private String dekes;
    
}
