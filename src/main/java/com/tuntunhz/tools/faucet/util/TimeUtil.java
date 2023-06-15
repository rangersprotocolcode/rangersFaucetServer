package com.tuntunhz.tools.faucet.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil {
    private static Logger logger = LoggerFactory.getLogger(TimeUtil.class);
    private static String format = "yyyy-MM-dd HH:mm:ss";
    private static String timeZone = "Asia/Shanghai";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
    private static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern(format);


    static {
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
    }

    public static boolean overOneDay(String timeStr) {
        return isOverDay(timeStr, 1);
    }

    public static boolean overFiveDay(String timeStr) {
        return isOverDay(timeStr, 5);
    }

    public static boolean isWithInSixSecond(String timeStr, Date givenTime) {
        if (StringUtils.isEmpty(timeStr) || givenTime == null) {
            return true;
        }
        try {
            Date time = simpleDateFormat.parse(timeStr);
            if (time.after(givenTime)) {
                return false;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(time);
            calendar.add(Calendar.SECOND, 6);
            Date afterSixSecondTime = calendar.getTime();

            return afterSixSecondTime.after(givenTime);
        } catch (ParseException e) {
            logger.error("Time format illegal.{}", timeStr);
            return false;
        }
    }

    public static String getCurrentTime() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(timeZone));
        return zonedDateTime.format(dateTimeFormat);
    }

    private static boolean isOverDay(String timeStr, int dayNum) {
        if (StringUtils.isEmpty(timeStr)) {
            return true;
        }
        try {
            Date time = simpleDateFormat.parse(timeStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(time);
            calendar.add(Calendar.DATE, dayNum);
            Date afterOneDayTime = calendar.getTime();

            String timeNowStr = getCurrentTime();
            Date timeNow = simpleDateFormat.parse(timeNowStr);
            return afterOneDayTime.before(timeNow);
        } catch (ParseException e) {
            logger.error("Time format illegal.{}", timeStr);
            return false;
        }
    }
}
