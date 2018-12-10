package com.wjj.application.mapper.protocol;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wjj.application.entity.protocol.RegisterProtocolEntity;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author bob123
 * @since 2018-07-17
 */
public interface RegisterProtocolEntityMapper extends BaseMapper<RegisterProtocolEntity> {

	/**  
	 * <p>Title: pageList</p>  
	 * <p>Description: </p>  
	 * @param page
	 * @return  
	 * @author bob
	 */
	List<RegisterProtocolEntity> selectRegisterProtocolEntityList(RegisterProtocolEntity entity);
	
	Integer updateRegisterProtocolEntity(RegisterProtocolEntity entity);

}
