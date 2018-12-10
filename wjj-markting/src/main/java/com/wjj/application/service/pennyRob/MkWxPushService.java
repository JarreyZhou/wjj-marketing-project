package com.wjj.application.service.pennyRob;

import com.wjj.application.response.Response;

public interface MkWxPushService {

    Response sendTemplateMessage(String openId, String templateId,String formId, String data,String page) throws Exception;

}
