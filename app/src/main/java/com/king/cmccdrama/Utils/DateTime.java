package com.king.cmccdrama.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTime {
    /**
     * 获取当前Unix时间戳
     *
     * @return 秒, 1252639886
     */
    public static long getCurrUnixTimeSec() {
        long sec = System.currentTimeMillis() / 1000;
        return sec;
    }

    /**
     * unix时间戳转为为formats指定的日期格式
     *
     * @param unixtime unix时间戳,"1252639886"
     * @param formats  要转换的时间格式,"yyyy-MM-dd HH:mm:ss"
     * @return 2011-11-11 11:11:11
     */
    public static String unixTime2Date(String unixtime, String formats) {
        Long timestamp = Long.parseLong(unixtime) * 1000;
        String date = new SimpleDateFormat(formats).format(new Date(timestamp));
        return date;
    }

    /**
     * 获取当前的日期时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDateTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date()).toString();
    }

    /**
     * 获取当前的日期时间
     *
     * @return yyyyMMdd
     */
    public static String getCurrentDateTime2() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(new Date()).toString();
    }

    /**
     * 获取当前的日期时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDateTime3() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        return df.format(new Date()).toString();
    }

    /**
     * 获取当前的日期时间
     *
     * @return yyyy-MM
     */
    public static String getCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        return df.format(new Date()).toString();
    }

    /**
     * 获取指定时间对应的毫秒数
     *
     * @param time "HH:mm:ss"
     * @return
     */
    public static long getTimeMillis(String time) {
        DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
        Date curDate;
        try {
            curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);
            return curDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /*
     * 将秒转换为时分秒
     *
     */
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99) {
                    return "99:59:59";
                }
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strdate) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("zh", "CN"));
        Date date = null;
        try {
            date = format.parse(strdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    /*
    Calendar calendar=Calendar.getInstance();  //获取当前时间，作为图标的名字
            String year=calendar.get(Calendar.YEAR)+"";
            String month=calendar.get(Calendar.MONTH)+1+"";
            String day=calendar.get(Calendar.DAY_OF_MONTH)+"";
            String hour=calendar.get(Calendar.HOUR_OF_DAY)+"";
            String minute=calendar.get(Calendar.MINUTE)+"";
            String second=calendar.get(Calendar.SECOND)+"";
            String time=year+month+day+hour+minute+second;
    */
}
