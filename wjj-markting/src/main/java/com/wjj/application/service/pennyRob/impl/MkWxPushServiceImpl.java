package com.wjj.application.service.pennyRob.impl;

import com.alibaba.fastjson.JSONObject;
import com.wjj.application.response.Response;
import com.wjj.application.service.pennyRob.MkWxPushService;
import com.wjj.application.util.RedisClient;
import com.wjj.application.util.WXUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MkWxPushServiceImpl implements MkWxPushService {

    @Autowired
    private RedisClient redisClient;

    @Override
    public Response sendTemplateMessage(String openId, String templateId,String formId, String data,String page) throws Exception {
        //查看redis有没有token，没有getToken
        if(!redisClient.exists("WX_PENNYROB_TOKEN")){
            JSONObject token = WXUtil.getToken();
            redisClient.setex("WX_PENNYROB_TOKEN", (String)token.get("access_token"), 3600);
        }
        Response response = WXUtil.sendTemplateMessage(openId, templateId,formId, redisClient.get("WX_PENNYROB_TOKEN"), data,page);
        return response;
    }

}
