package com.tuntunhz.tools.faucet.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Response {

    private String id;

    private String method;

    private int status;

    private String message;

    private JSONObject data;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JSON getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


    public String toString() {
        return JSON.toJSONString(this);
    }
}
