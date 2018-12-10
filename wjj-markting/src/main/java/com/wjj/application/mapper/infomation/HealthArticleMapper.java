package com.wjj.application.mapper.infomation;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wjj.application.entity.information.HealthArticle;

public interface HealthArticleMapper   extends BaseMapper<HealthArticle> {
	
	Integer deleteHealthArticle(String infoId);
	
	HealthArticle selectHealthArticleById(String infoId);

}
