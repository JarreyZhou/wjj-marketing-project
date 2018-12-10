package com.wjj.application.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.wjj.application.entity.User;
import com.wjj.application.vo.PageVO;
import com.wjj.application.vo.UserTestVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author bob123
 * @since 2018-07-17
 */
public interface UserService extends IService<User> {

	/**  
	 * <p>Title: pageList</p>  
	 * <p>Description: </p>  
	 * @param pageNo
	 * @param pageSize  
	 * @author bob
	 */
	Page<User> pageList(Integer pageNo, Integer pageSize);

	/**  
	 * <p>Title: wrapperList</p>  
	 * <p>Description: </p>  
	 * @return  
	 * @author bob
	 */
	List<User> wrapperList();
	
	Page<UserTestVO> userList(PageVO vo);

}
