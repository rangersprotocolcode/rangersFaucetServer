package com.tuntunhz.tools.faucet.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitorLogger {
    private static final Logger logger = LoggerFactory.getLogger(MonitorLogger.class);

    private static final String EVENT = "event";
    private static final String TARGET_ADDRESS = "target";
    private static final String STATUS = "status";
    private static final String TIMESTAMP = "timestamp";
    private static final String FAILED = "failed";
    private static final String EXCEPTION = "exception";

    public static void logFailedEvent(String event, String targetAddress) {
        JSONObject logJson = new JSONObject();
        logJson.put(EVENT, event);
        logJson.put(TARGET_ADDRESS, targetAddress);
        logJson.put(STATUS, FAILED);
        logJson.put(TIMESTAMP, TimeUtil.getCurrentTime());

        logger.info("|faucet|{}", logJson.toString());
    }

    public static void logException(String exception) {
        JSONObject logJson = new JSONObject();
        logJson.put(EXCEPTION, exception);
        logJson.put(TIMESTAMP, TimeUtil.getCurrentTime());

        logger.info("|faucet|{}", logJson.toString());
    }

}
