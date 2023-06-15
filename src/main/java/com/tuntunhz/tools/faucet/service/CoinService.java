package com.tuntunhz.tools.faucet.service;

import com.alibaba.fastjson.JSONArray;
import com.tuntunhz.tools.faucet.dao.AccountMapper;
import com.tuntunhz.tools.faucet.dao.AssetStatisticsMapper;
import com.tuntunhz.tools.faucet.pojo.AccountInfo;
import com.tuntunhz.tools.faucet.pojo.ValidateAddrResult;
import com.tuntunhz.tools.faucet.util.MonitorLogger;
import com.tuntunhz.tools.faucet.util.RangersSDKUtil;
import com.tuntunhz.tools.faucet.util.ResponseUtil;
import com.tuntunhz.tools.faucet.util.TimeUtil;
import com.tuntunhz.wallet.sdk.WalletSDK;
import com.tuntunhz.wallet.sdk.response.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.Session;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.tuntunhz.tools.faucet.common.Constant.*;

@RestController
public class CoinService {

    @Autowired
    HistoryService historyService;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    AssetStatisticsMapper assetStatisticsMapper;

    @Value("${gate.url}")
    String gateAddress;


    @Value("${service.url}")
    String serviceAddress;

    private static final Logger logger = LoggerFactory.getLogger(CoinService.class);
    private static final int ADDRESS_LENGTH = 42;
    //0x2c616a97d3d10e008f901b392986b1a65e0abbb7

    private String privateKey = "";


    private BigDecimal transferRPGValue = new BigDecimal(1);


    public void requestCoin(JSONArray addressList, String id, Session webSocketSession) {
        if (addressList == null || addressList.size() == 0) {
            ResponseUtil.webSocketReturnFailed(webSocketSession, PARAM_ILLEGAL, id, METHOD_REQUEST_COIN);
            return;
        }
        List<String> addrStrList = new ArrayList<>();
        for (Object addressObj : addressList) {
            String address = (String) addressObj;
            addrStrList.add(address);
        }

        ValidateAddrResult verifyResult = validateAddress(addrStrList);
        if (!verifyResult.isValid()) {
            ResponseUtil.webSocketReturnFailed(webSocketSession, verifyResult.getReturnMessage(), id, METHOD_REQUEST_COIN);
            return;
        }

        boolean transferResult = transferTestCoin(addrStrList);
        if (transferResult) {
            ResponseUtil.webSocketReturnSuccess(webSocketSession, null, id, METHOD_REQUEST_COIN);
        } else {
            ResponseUtil.webSocketReturnFailed(webSocketSession, SYSTEM_EXCEPTION, id, METHOD_REQUEST_COIN);
        }
    }

    private ValidateAddrResult validateAddress(List<String> addressList) {
        ValidateAddrResult result = new ValidateAddrResult();
        result.setValid(true);

        for (String address : addressList) {
            if (!isAddress(address)) {
                result.setValid(false);
                result.setReason(BAD_FORMAT_ADDRESS);
                result.setTroubleAddress(address);
                break;
            }
            AccountInfo accountInfo = accountMapper.getAccountInfo(address);
            if (accountInfo != null && !TimeUtil.overOneDay(accountInfo.getGotCoinTime())) {
                result.setValid(false);
                result.setReason(REQUEST_COIN_TOO_OFTEN);
                result.setTroubleAddress(address);
                break;
            }
        }
        return result;
    }

    private boolean transferTestCoin(List<String> addressList) {
        boolean result = true;
        for (String address : addressList) {
            boolean transferResult = singleTransfer(address);
            if (transferResult) {
                logger.debug("Transfer test coin  to {} success.", address);
                historyService.addRecord(address, PARAM_COIN);
            } else {
                MonitorLogger.logFailedEvent(METHOD_REQUEST_COIN, address);
                result = false;
                break;
            }
        }
        return result;
    }

    private boolean singleTransfer(String address) {
        WalletSDK sdk = RangersSDKUtil.getRangersSDK(gateAddress, serviceAddress);
        Result transferRPGResult = sdk.transferRPG(privateKey, address, transferRPGValue);
        if (!transferRPGResult.isSuccess()) {
            logger.info("Transfer RPG to {} failed:{}", address, transferRPGResult.getReason());
            return false;
        }

        accountMapper.updateGotCoinTime(address, TimeUtil.getCurrentTime());
        assetStatisticsMapper.increaseCoinFunded();
        return true;
    }


    private boolean isAddress(String address) {
        return (address != null && address.length() == ADDRESS_LENGTH);
    }
}
