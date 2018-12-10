package com.wjj.application.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wjj.application.entity.User;
import com.wjj.application.mapper.UserMapper;
import com.wjj.application.service.UserService;
import com.wjj.application.vo.PageVO;
import com.wjj.application.vo.UserTestVO;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bob123
 * @since 2018-07-17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public Page<User> pageList(Integer pageNo, Integer pageSize) {
		Page<User> page = new Page<User>(pageNo, pageSize);
		List<User> list = userMapper.pageList(page);
		page.setRecords(list);
		return page;
	}

	@Override
	public List<User> wrapperList() {
		Wrapper<User> wrapper = new EntityWrapper<User>();
		List<User> list = userMapper.selectList(wrapper);
//		List<User> list = userMapper.selectList(wrapper.eq("id", 1));
		return list;
	}
	
	@Override
	public Page<UserTestVO> userList(PageVO vo) {
		Page<UserTestVO> page = new Page<UserTestVO>(vo.getPageNo(), vo.getPageSize());
		List<UserTestVO> list = userMapper.userList(page, "name");
		page.setRecords(list);
		return page;
	}

}
