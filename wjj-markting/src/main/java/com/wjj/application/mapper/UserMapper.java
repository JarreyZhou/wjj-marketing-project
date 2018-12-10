package com.wjj.application.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wjj.application.entity.User;
import com.wjj.application.vo.UserTestVO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author bob123
 * @since 2018-07-17
 */
public interface UserMapper extends BaseMapper<User> {

	/**  
	 * <p>Title: pageList</p>  
	 * <p>Description: </p>  
	 * @param page
	 * @return  
	 * @author bob
	 */
	List<User> pageList(Page<User> page);
	
	List<UserTestVO> userList(Page<UserTestVO> page, @Param("name") String name);

}
