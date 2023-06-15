package com.tuntunhz.tools.faucet.common;

public class Constant {
    //返回状态码
    public static int SUCCESS_CODE = 0;

    public static int FAILED_CODE = 1;

    //返回消息信息

    public static final String PARAM_ILLEGAL = "Param Illegal";

    public static final String BAD_FORMAT_ADDRESS = "Bad Format Address";

    public static final String REQUEST_COIN_TOO_OFTEN = "Request Coin Too Often";

    public static final String PARAM_BAD_FORMAT = "Param Bad Format";

    public static final String DB_EXCEPTION = "Db Exception";

    public static final String SESSION_EXPIRED = "Session expired";

    public static final String UNKNOWN_METHOD = "Unknown Method";

    public static final String SYSTEM_EXCEPTION = "System Exception";

    //客户端web socket请求 方法名称
    public static final String METHOD_REQUEST_COIN = "requestCoin";

    public static final String METHOD_GET_RPG_REFUNDED = "getRPGRefunded";

    public static final String METHOD_GET_LATEST_REFUNDED_RECORD = "getLatestRefundedRecord";

    public static final String METHOD_SHOW_MONITOR_LOG = "testMonitorLog";

    public static final String METHOD = "method";

    public static final String PARAMS = "params";

    public static final String PARAM_ID = "id";

    //参数

    public static final String PARAM_RPG_REFUNDED = "rpgRefunded";

    public static final String PARAM_ADDRESS = "address";

    public static final String PARAM_TYPE = "type";

    public static final String PARAM_LATEST_REFUNDED_RECORD = "latestRefundedRecord";

    public static final String PARAM_COIN = "coin";

    public static final String PARAM_TIMESTAMP = "timestamp";

}
