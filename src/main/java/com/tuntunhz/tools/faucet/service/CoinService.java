package com.tuntunhz.tools.faucet.service;

import com.alibaba.fastjson.JSONArray;
import com.tuntunhz.tools.faucet.dao.AccountMapper;
import com.tuntunhz.tools.faucet.dao.AssetStatisticsMapper;
import com.tuntunhz.tools.faucet.pojo.AccountInfo;
import com.tuntunhz.tools.faucet.pojo.ValidateAddrResult;
import com.tuntunhz.tools.faucet.util.MonitorLogger;
import com.tuntunhz.tools.faucet.util.ResponseUtil;
import com.tuntunhz.tools.faucet.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.Session;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.tuntunhz.tools.faucet.common.Constant.*;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

@RestController
public class CoinService {

    @Autowired
    HistoryService historyService;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    AssetStatisticsMapper assetStatisticsMapper;

    @Value("${gate.url}")
    String jsonRpcAddress;


    @Value("${service.url}")
    String serviceAddress;

    private static final Logger logger = LoggerFactory.getLogger(CoinService.class);
    private static final int ADDRESS_LENGTH = 42;
    //0x2c616a97d3d10e008f901b392986b1a65e0abbb7

    private String privateKey = "";
    private Credentials credentials = Credentials.create(privateKey);
    private String fromAddress = credentials.getAddress();
    private BigInteger amount = Convert.toWei("1.0", Convert.Unit.ETHER).toBigInteger();


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
        Web3j web3j = Web3j.build(new HttpService(jsonRpcAddress));

        EthGetTransactionCount ethGetTransactionCount = null;
        try {
            ethGetTransactionCount = web3j.ethGetTransactionCount(
                    fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce, Convert.toWei("18", Convert.Unit.GWEI).toBigInteger(),
                Convert.toWei("45000", Convert.Unit.WEI).toBigInteger(), address, amount);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        EthSendTransaction ethSendTransaction = null;
        try {
            ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        if (null != ethSendTransaction && ethSendTransaction.hasError()) {
            logger.info("transfer error:", ethSendTransaction.getError().getMessage());
        } else {
            String transactionHash = ethSendTransaction.getTransactionHash();
            logger.info("Transfer transactionHash:" + transactionHash);
        }

        accountMapper.updateGotCoinTime(address, TimeUtil.getCurrentTime());
        assetStatisticsMapper.increaseCoinFunded();
        return true;
    }


    private boolean isAddress(String address) {
        return (address != null && address.length() == ADDRESS_LENGTH);
    }
}
