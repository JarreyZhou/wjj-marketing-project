package com.wjj.application.mapper.pennyRob;

import com.wjj.application.dto.pennyRob.MkActivityRobDTO;
import com.wjj.application.entity.pennyRob.MkActivityRob;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 活动参与帮抢者 Mapper 接口
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */
public interface MkActivityRobMapper extends BaseMapper<MkActivityRob> {

    List<MkActivityRob> selectHelpRobList(@Param("sponsorId") Integer sponsorId);

    List<MkActivityRobDTO> selectActivityRobList(MkActivityRobDTO mkActivityRobDTO);
}
