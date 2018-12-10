package com.wjj.application.mapper.pennyRob;

import com.wjj.application.dto.pennyRob.MkStandardDTO;
import com.wjj.application.entity.pennyRob.MkStandard;
import com.wjj.application.entity.pennyRob.MkStandardExt;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 规格表 Mapper 接口
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */
public interface MkStandardMapper extends BaseMapper<MkStandard> {

	List<MkStandardExt> selectSku(Integer spuId);

	Integer insertBatchStandard(List<MkStandardDTO> standardDTOList);
	
}
