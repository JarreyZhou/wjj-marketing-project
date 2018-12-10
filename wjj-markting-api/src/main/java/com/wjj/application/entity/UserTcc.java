/**  
* <p>Title: UserTcc.java</p>  
* <p>Description: </p>  
* <p>Copyright: 上海卫健家健康技术有限公司 (c) 2018</p>  
* <p>Company: www.waygiga.com</p>  
* @author bob
* @date 2018年7月19日  
* @version 1.0  
*/  
package com.wjj.application.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

/**  
* <p>Title: UserTcc.java</p>  
* <p>Description: </p>  
* <p>Copyright: 上海卫健家健康技术有限公司 (c) 2018</p>  
* <p>Company: www.waygiga.com</p>  
* @author bob
* @date 2018年7月19日  
* @version 1.0  
*/
@Data
@TableName("user_tcc")
public class UserTcc extends Model<UserTcc> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer price;
    @TableField("user_id")
    private Integer userId;
    private Integer status;
    
    private Date expire;
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
