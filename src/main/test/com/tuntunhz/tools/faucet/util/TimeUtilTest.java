package com.tuntunhz.tools.faucet.util;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtilTest {
    @Test
    public void testGetCurrentTime() {
        for (int i = 1; i < 2; i++) {
            System.out.println("i:" + i);
            String time = TimeUtil.getCurrentTime();
            System.out.println(time);
        }
    }

    @Test
    public void testAfterOneDay() {
        String time1 = "2021-07-20 15:15:28";
        boolean result1 = TimeUtil.overOneDay(time1);
        //true
        System.out.println(result1);

        String time2 = "2021-07-20 15:29:28";
        boolean result2 = TimeUtil.overOneDay(time2);
        //false
        System.out.println(result2);

        String time3 = "2021-07-21 15:15:28";
        boolean result3 = TimeUtil.overOneDay(time3);
        //false
        System.out.println(result3);
    }

    @Test
    public void testWithinSixSecond() {
        try {
            String format = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            String givenTimeStr = "2021-07-20 15:15:28";
            Date givenTime = simpleDateFormat.parse(givenTimeStr);

            String time1 = "2021-07-20 15:15:23";
            boolean result1 = TimeUtil.isWithInSixSecond(time1, givenTime);
            //true
            System.out.println(result1);

            String time2 = "2021-07-20 15:15:21";
            boolean result2 = TimeUtil.isWithInSixSecond(time2, givenTime);
            //false
            System.out.println(result2);

            String time3 = "2021-07-21 15:15:28";
            boolean result3 = TimeUtil.isWithInSixSecond(time3, givenTime);
            //false
            System.out.println(result3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
