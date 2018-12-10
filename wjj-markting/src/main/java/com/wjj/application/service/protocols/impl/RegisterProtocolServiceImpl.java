package com.wjj.application.service.protocols.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wjj.application.entity.protocol.RegisterProtocolEntity;
import com.wjj.application.json.JsonUtil;
import com.wjj.application.mapper.protocol.RegisterProtocolEntityMapper;
import com.wjj.application.service.protocols.RegisterProtocolEntityService;
import com.wjj.application.util.RedisClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RegisterProtocolServiceImpl extends ServiceImpl<RegisterProtocolEntityMapper, RegisterProtocolEntity> implements RegisterProtocolEntityService{
	
	@Autowired
	private RegisterProtocolEntityMapper registerProtocolEntityMapper;
	
	@Autowired
	private RedisClient  redisClient;
	
	@Override
	public List<RegisterProtocolEntity> selectRegisterProtocolEntityList(RegisterProtocolEntity entity) {
		return registerProtocolEntityMapper.selectRegisterProtocolEntityList(entity);
	}

	@Override
	public Integer updateRegisterProtocolEntity(RegisterProtocolEntity entity) {
		String keyStr="register_protocol"+entity.getId();
		try {
			redisClient.del(keyStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return registerProtocolEntityMapper.updateRegisterProtocolEntity(entity);
	}

	@Override
	public RegisterProtocolEntity selectRegisterProtocolEntity(RegisterProtocolEntity entity) throws Exception {
		String keyStr="register_protocol"+entity.getId();
	
		String keyValue = redisClient.get(keyStr);
		if(keyValue!=null&&!keyValue.equals("")) {
			RegisterProtocolEntity entityNew = JSON.parseObject(keyValue, new TypeReference<RegisterProtocolEntity>() {});
			return entityNew;
		}else {
			List<RegisterProtocolEntity> entity1 = registerProtocolEntityMapper.selectRegisterProtocolEntityList(entity);
			String sowingListNewStr = JsonUtil.object2Json(entity1.get(0));
			redisClient.set(keyStr, sowingListNewStr);
			return entity1.get(0);
		}
	}

}
