package com.wjj.application.mapper.pennyRob;

import com.wjj.application.entity.pennyRob.MkActivityRobExt;
import com.wjj.application.dto.pennyRob.MkActivitySponsorDTO;
import com.wjj.application.entity.pennyRob.MkActivitySponsor;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wjj.application.util.PageVO;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 活动发起表 Mapper 接口
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */
public interface MkActivitySponsorMapper extends BaseMapper<MkActivitySponsor> {

	Integer selectSponsorSpuCount(MkActivitySponsor m);

	Integer selectSponsorActivityCount(MkActivitySponsor m);

	List<HashMap> myActivityList(HashMap map);
	List<MkActivitySponsor> selectPageList(MkActivitySponsorDTO mkActivitySponsorDTO);

	int selectPageCount(MkActivitySponsorDTO mkActivitySponsorDTO);

    List<HashMap> orderPageList(PageVO pageVO);

	Integer orderPageCount();

    List<MkActivitySponsorDTO> orderConditionPageList(MkActivitySponsorDTO mkActivitySponsorDTO);

	Integer orderConditionCountList(MkActivitySponsorDTO mkActivitySponsorDTO);
}
