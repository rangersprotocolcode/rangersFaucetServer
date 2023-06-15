package com.tuntunhz.tools.faucet.util;

import com.alibaba.fastjson.JSONObject;
import com.tuntunhz.tools.faucet.common.Constant;
import com.tuntunhz.tools.faucet.common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.IOException;


public class ResponseUtil {
    private static Logger logger = LoggerFactory.getLogger(ResponseUtil.class);


    public static void webSocketReturnSuccess(Session session, JSONObject data, String id, String method) {
        Response response = new Response();
        response.setId(id);
        response.setMethod(method);
        response.setStatus(Constant.SUCCESS_CODE);
        response.setData(data);
        sendWebSocketResponse(session, response.toString());
    }

    public static void webSocketReturnSuccessWithStrData(Session session, String data, String id, String method) {
        JSONObject response = new JSONObject();
        response.put("id", id);
        response.put("method", method);
        response.put("status", Constant.SUCCESS_CODE);
        response.put("data", data);
        sendWebSocketResponse(session, response.toString());
    }


    public static void webSocketReturnFailed(Session session, String message, String id, String method) {
        Response response = new Response();
        response.setId(id);
        response.setMethod(method);
        response.setStatus(Constant.FAILED_CODE);
        response.setMessage(message);
        sendWebSocketResponse(session, response.toString());
    }


    public static void sendWebSocketResponse(Session session, String response) {
        try {
            session.getBasicRemote().sendText(response);
        } catch (IOException e) {
            logger.error("Web socket return exception:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
