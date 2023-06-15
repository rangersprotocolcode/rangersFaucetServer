package com.tuntunhz.tools.faucet.service;

import com.tuntunhz.tools.faucet.util.TimeUtil;
import org.junit.Test;
import org.web3j.crypto.Credentials;

public class CoinServiceTest {
    @Test
    public void testCredentials() {
        Credentials credentials = Credentials.create("");
        System.out.println(credentials.getAddress());
    }

}
