package com.wjj.application.entity.video;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="视屏关联的商品")
@TableName("video_goods")
public class VideoGoodsEntity  extends Model<VideoGoodsEntity>  implements Serializable{
	
	private static final long serialVersionUID = 454334434L;

	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value="编号 " , name="id")
	private Long id;
	
	@ApiModelProperty(value="视屏编号 " , name="videoId")
	private  Long videoId;
	
	@ApiModelProperty(value="商品编号 " , name="goodsId")
	private  String goodsId;
	
	@ApiModelProperty(value="商品名称 " , name="goodsName")
	private  String goodsName;
	
	@ApiModelProperty(value="最低售价 " , name="minPrice")
	private  BigDecimal minPrice;
	
	
	@ApiModelProperty(value="商品最高售价 " , name="maxPrice")
	private  BigDecimal maxPrice;
	
	@ApiModelProperty(value="图片路径 " , name="movePicPath")
	private String movePicPath;
	
	@ApiModelProperty(value="库存 " , name="storeNum")
	private Integer storeNum;
	
	@ApiModelProperty(value="图片路径" , name="picPath")
	private String picPath;


	@Override
	protected Serializable pkVal() {
		return id;
	}

}
