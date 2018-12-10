package com.wjj.application.mapper.pennyRob;

import com.wjj.application.dto.pennyRob.MkGoodsSpuDTO;
import com.wjj.application.entity.pennyRob.MkGoodsSku;
import com.wjj.application.entity.pennyRob.MkGoodsSpu;
import com.wjj.application.entity.pennyRob.MkGoodsSpuVO;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wjj.application.vo.PageVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 活动商品spu表 Mapper 接口
 * </p>
 *
 * @author xg123
 * @since 2018-10-30
 */
public interface MkGoodsSpuMapper extends BaseMapper<MkGoodsSpu> {
	
	List<MkGoodsSpuDTO> snatchPageList(PageVO pageVO);

	Integer snatchCountList(PageVO pageVO);

	List<MkGoodsSpuDTO> comingPageList(PageVO pageVO);

	Integer comingCountList(PageVO pageVO);

	MkGoodsSpuVO selectSpu(Integer spuId);

	int selectPageCount(MkGoodsSpuDTO mkGoodsSpuDTO);

	List<MkGoodsSpuDTO> selectPageList(MkGoodsSpuDTO mkGoodsSpuDTO);

	List<MkGoodsSpuDTO> moreGoods(@Param("activityId") Integer activityId);
}
