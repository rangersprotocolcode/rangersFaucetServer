package com.tuntunhz.tools.faucet.service;

import com.alibaba.fastjson.JSONObject;
import com.tuntunhz.tools.faucet.util.MonitorLogger;
import com.tuntunhz.tools.faucet.util.ResponseUtil;
import com.tuntunhz.tools.faucet.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.tuntunhz.tools.faucet.common.Constant.*;

@RestController
public class HistoryService {

    private static final Logger logger = LoggerFactory.getLogger(CoinService.class);

    private Queue queue = new ConcurrentLinkedQueue();

    private int elementCount = 8;


    public void getLatestFundedRecord(String id, Session webSocketSession) {
        Date now = new Date();
        List<JSONObject> validRecord = new ArrayList();

        Object[] data = queue.toArray();
        for (Object o : data) {
            JSONObject element = (JSONObject) o;
            String fundedTime = element.getString(PARAM_TIMESTAMP);
            if (TimeUtil.isWithInSixSecond(fundedTime, now)) {
                validRecord.add(element);
            }
        }

        JSONObject result = new JSONObject();
        result.put(PARAM_LATEST_REFUNDED_RECORD, validRecord);
        ResponseUtil.webSocketReturnSuccess(webSocketSession, result, id, METHOD_GET_LATEST_REFUNDED_RECORD);
    }


    public void addRecord(String address, String type) {
        while (queue.size() > elementCount) {
            queue.poll();
        }
        JSONObject element = new JSONObject();
        element.put(PARAM_ADDRESS, address);
        element.put(PARAM_TYPE, type);
        element.put(PARAM_TIMESTAMP, TimeUtil.getCurrentTime());
        queue.add(element);
    }

    public void printMonitorLog(String id, Session webSocketSession) {
        MonitorLogger.logException("Test Exception");
        MonitorLogger.logFailedEvent("Test Event", "0x0001");

        JSONObject result = new JSONObject();
        result.put("data", "OK");
        ResponseUtil.webSocketReturnSuccess(webSocketSession, result, id, METHOD_SHOW_MONITOR_LOG);
    }
}
