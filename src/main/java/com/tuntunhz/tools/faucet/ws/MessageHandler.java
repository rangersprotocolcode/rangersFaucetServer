package com.tuntunhz.tools.faucet.ws;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tuntunhz.tools.faucet.common.Constant;
import com.tuntunhz.tools.faucet.service.CoinService;
import com.tuntunhz.tools.faucet.service.HistoryService;
import com.tuntunhz.tools.faucet.service.StatisticsService;
import com.tuntunhz.tools.faucet.util.MonitorLogger;
import com.tuntunhz.tools.faucet.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import static com.tuntunhz.tools.faucet.common.Constant.*;


@Component
public class MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);


    @Autowired
    CoinService coinService;

    @Autowired
    StatisticsService statisticsService;

    @Autowired
    HistoryService historyService;


    public void handleMessage(String message, Session webSocketSession, HttpSession httpSession) {
        if (StringUtils.isEmpty(message)) {
            ResponseUtil.webSocketReturnFailed(webSocketSession, PARAM_ILLEGAL, "", "");
            return;
        }

        JSONObject data;
        String method;
        String id;
        try {
            data = JSONObject.parseObject(message);
            method = data.getString(METHOD);
            id = data.getString(PARAM_ID);
        } catch (JSONException e) {
            ResponseUtil.webSocketReturnFailed(webSocketSession, PARAM_BAD_FORMAT, "", "");
            return;
        }

        if (StringUtils.isEmpty(method)) {
            ResponseUtil.webSocketReturnFailed(webSocketSession, PARAM_ILLEGAL, id, method);
            return;
        }
        try {
            route(method, id, data, webSocketSession, httpSession);
        } catch (IllegalStateException e) {
            ResponseUtil.webSocketReturnFailed(webSocketSession, SESSION_EXPIRED, id, method);
            e.printStackTrace();
            return;
        } catch (Throwable e) {
            logger.error("System exception:" + e.getMessage());
            MonitorLogger.logException(e.getMessage());
            e.printStackTrace();
            ResponseUtil.webSocketReturnFailed(webSocketSession, SYSTEM_EXCEPTION, id, method);
            return;
        }
    }


    private void route(String method, String id, JSONObject data, Session webSocketSession, HttpSession httpSession) {
        if (METHOD_REQUEST_COIN.equals(method)) {
            JSONArray addressList = data.getJSONArray(Constant.PARAMS);
            coinService.requestCoin(addressList, id, webSocketSession);
            return;
        }

        if (METHOD_GET_RPG_REFUNDED.equals(method)) {
            statisticsService.getRPGRefunded(id, webSocketSession);
            return;
        }

        if (METHOD_GET_LATEST_REFUNDED_RECORD.equals(method)) {
            historyService.getLatestFundedRecord(id, webSocketSession);
            return;
        }

        if (METHOD_SHOW_MONITOR_LOG.equals(method)) {
            historyService.printMonitorLog(id, webSocketSession);
            return;
        }
        ResponseUtil.webSocketReturnFailed(webSocketSession, UNKNOWN_METHOD, id, method);
    }
}
