package com.wjj.application.enums;

import com.alibaba.fastjson.JSONObject;
import com.wjj.application.json.JsonUtil;

public enum BackCode {
    SUCCESS("1000", "接口调用成功"),
    RESPONSE_1001("1001", "参数错误"),
    FAIL("4000", "系统异常，请稍后再试"),
    LOCK_FAIL("4003", "网络异常，请稍后再试"),
    FEIGN_FAIL("4001", "系统异常fegin！"),
    GRAPH_CODE("5001", "缺少图形验证码"),
    ROB_FAIL("5004", "帮抢失败，之前已经注册过的用户不能再帮抢了"),
    GRAPH_CODE_FAIL("5003", "图形验证码错误"),

    TEMPLATE_ID_FAIL("40037", "template_id不正确"),
    FORM_ID_FAIL("41028", "form_id不正确，或者过期"),
    FORM_ID_USED("41029", "form_id已被使用"),
    PAGE_FAIL("41030", "page不正确"),
    EXCESS_FAIL("45009", "接口调用超过限额（目前默认每个帐号日调用限额为100万"),
    WX_PUSH_OK("0", "ok");


    private String code;
    private String msg;

    private BackCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }


    public String toString() {
        JSONObject object = new JSONObject();
        object.put("code", this.code);
        object.put("msg", this.msg);
        return object.toString();
    }
}
