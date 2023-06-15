package com.tuntunhz.tools.faucet.util;

import com.tuntunhz.wallet.sdk.WalletSDK;
import com.tuntunhz.wallet.sdk.WalletSDKFactory;

public class RangersSDKUtil {

    private static WalletSDK rangersSDK;

    public static WalletSDK getRangersSDK(String gateAddress, String serviceAddress) {
        if (rangersSDK == null) {
            synchronized (RangersSDKUtil.class) {
                if (rangersSDK == null) {
                    rangersSDK = WalletSDKFactory.createInstance(gateAddress, serviceAddress, 3000L, null);
                }
            }
        }
        return rangersSDK;
    }
}
