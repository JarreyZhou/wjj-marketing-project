package com.wjj.application.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wjj.application.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

@Slf4j
public class WXUtil {


    //获得token
    public static JSONObject getToken(){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpEntity entity = null;
        CloseableHttpResponse response = null;
        try {
            // 创建httpget
            HttpGet httpget = new HttpGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx3375e18821c45cf4&secret=a71fa68956eb13d300e859739ea3a9cc");
            log.info("executing request " + httpget.getURI());
            // 执行get请求.
            response = httpclient.execute(httpget);
            // 获取响应实体
            entity = response.getEntity();
            if (entity != null) {
                // 打印响应内容长度
                log.info("Response content length: " + entity.getContentLength());
                // 打印响应内容
               // System.out.println("Response content: " + EntityUtils.toString(entity));
            }
            String responseJson = EntityUtils.toString(entity);
            log.info("微信获取token返回值" + responseJson);
            return JSON.parseObject(responseJson);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                response.close();
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //发送模板信息
    //订单进度提醒 vV8IT1DJjyiLayGM1JURsduLmw7egahEI-rvVZ6GFFo
    //待付款提醒 2AVs56U8pMFYhf25OL2Mzu3kugbRzItbtTk8opDa9rc
    public static Response sendTemplateMessage(String openId, String templateId,String formId, String token, String data,String page) throws Exception {

        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost("https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+token);

        // 构建消息实体
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("touser", openId);
        jsonObject.put("template_id", templateId);
        jsonObject.put("page", page);
        jsonObject.put("form_id", formId);
        JSONObject jsonObjectData = JSON.parseObject(data);
        jsonObject.put("data", jsonObjectData);
        jsonObject.put("emphasis_keyword", "");

        StringEntity requestEntity = new StringEntity(jsonObject.toString(), Charset.forName("UTF-8"));
        JSONObject jsonObjectResponse = null;
        try {
            httppost.setEntity(requestEntity);
            log.info("executing request " + httppost.getURI());
            log.info("推送入参" + jsonObject.toString());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                jsonObjectResponse = JSON.parseObject(EntityUtils.toString(entity, "UTF-8"));
                if (entity != null) {
                    log.info("微信推送返回值: " + jsonObjectResponse.toJSONString());
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Response.returnCode(String.valueOf((Integer)jsonObjectResponse.get("errcode")), (String)jsonObjectResponse.get("errmsg"));
    }

//    public static void main(String[] args) {
//        JSONObject jsonObject = getToken();
//        JSONObject jsonObject1 = new JSONObject();
//        JSONObject jsonObject2 = new JSONObject();
//        jsonObject2.put("value", "339208499");
//
//        jsonObject1.put("keyword1",jsonObject2);
//        sendTemplateMessage("obW575RdLKx-7nGOWdL3jLpNJYcQ", "vV8IT1DJjyiLayGM1JURsduLmw7egahEI-rvVZ6GFFo", (String)jsonObject.get("access_token"), jsonObject1.toJSONString());
//    }

}
