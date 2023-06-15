package com.tuntunhz.tools.faucet.service;

import com.alibaba.fastjson.JSONObject;
import com.tuntunhz.tools.faucet.dao.AssetStatisticsMapper;
import com.tuntunhz.tools.faucet.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.Session;

import static com.tuntunhz.tools.faucet.common.Constant.METHOD_GET_RPG_REFUNDED;
import static com.tuntunhz.tools.faucet.common.Constant.PARAM_RPG_REFUNDED;

@RestController
public class StatisticsService {

    @Autowired
    AssetStatisticsMapper assetStatisticsMapper;

    public void getRPGRefunded(String id, Session webSocketSession) {
        Integer rpgRefunded = assetStatisticsMapper.getRPGFunded();
        JSONObject result = new JSONObject();
        result.put(PARAM_RPG_REFUNDED, rpgRefunded);
        ResponseUtil.webSocketReturnSuccess(webSocketSession, result, id, METHOD_GET_RPG_REFUNDED);
    }
}
