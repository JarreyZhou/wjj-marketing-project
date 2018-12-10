package com.wjj.application.service.protocols;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.wjj.application.entity.protocol.RegisterProtocolEntity;

public interface RegisterProtocolEntityService extends IService<RegisterProtocolEntity>{
	
	List<RegisterProtocolEntity> selectRegisterProtocolEntityList(RegisterProtocolEntity entity);
	
	Integer updateRegisterProtocolEntity(RegisterProtocolEntity entity);
	
	RegisterProtocolEntity selectRegisterProtocolEntity(RegisterProtocolEntity entity) throws Exception;

}
