package com.wjj.application.enums;
/**
 * 活动发起状态
 * @author liuxin
 *	状态：上架状态含（未开始，进行中），下架状态（已过期、失效），草稿。
	未开始：指未到有效期开始时间，支持编辑、查看、下架 ；   *前端呈现距离进行中的活动最近一期的活动，下架进入草稿状态
	进行中：指正式进入活动进行中，支持查看、编辑； *前端呈现
	已过期：指到达有效期结束时间，支持查看；    *前端不呈现
	草稿：指未上架的活动，支持编辑、删除、上架；    *前端不呈现
	*过期的活动，如有商品剩余，活动结束时间后+【活动帮抢有效期】+【30分钟（支付有效期）】，活动库存自动回归商品库存
	*草稿的活动，因活动未上架，即不从商品库扣减库存；上架即从商品库扣减活动库存
 */
public enum ActivitySponsorStatus {
	_001(1, "抢购中!"), 
	_002(2, "失败!"), 
	_003(3, "成功!");
	
    public Integer key;
    public String value;


    private ActivitySponsorStatus(Integer key, String value) {
		this.key = key;
		this.value = value;
	}
    
}
