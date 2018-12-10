package com.wjj.application.mapper.pennyRob;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wjj.application.dto.pennyRob.MkActivityDTO;
import com.wjj.application.entity.pennyRob.MkActivity;

/**
 * <p>
 * 活动表 Mapper 接口
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */
public interface MkActivityMapper extends BaseMapper<MkActivity> {
	
	List<MkActivity> list();

	List<MkActivity> selectPageList(MkActivityDTO mkActivityDTO);

	int selectPageCount(MkActivityDTO mkActivityDTO);

	List<MkActivity> selectAllActivity();

	int selectCountByStatus();
}
