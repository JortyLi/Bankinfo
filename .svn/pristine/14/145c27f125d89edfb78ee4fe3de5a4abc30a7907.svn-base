package com.example.bankinfo.common;


import javazoom.jl.player.Player;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateConvert {

    static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    static final SimpleDateFormat sdfDateYer = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    static final SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static final SimpleDateFormat sdtim = new SimpleDateFormat("yyyyMMddHHmmss");
    static final SimpleDateFormat sdfds = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
    static final SimpleDateFormat sdfd1 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    public static String gainTime(String time){
        try {
            long gtim = sdtim.parse(time).getTime();
            return sdfd.format(gtim);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTime(String tiem) {
        try {
            long time = sdfd.parse(tiem).getTime();

            return sdfds.format(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String convertTurnString(String time) {
        try {
            return sdfd.format(new Date(time));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Long getNewDateToLong() {
        try {
            String newDateStr = sdfDate.format(new Date()) + " 00:00:00";
            return sdfd.parse(newDateStr).getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date convertDateYer(String time) {
        try {
            Date date = sdfDateYer.parse(time);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String consetDateYer(String time) {
        try {
            Long date = sdfDateYer.parse(time).getTime();
            return sdfd.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Date convertDate(String time) {
        try {
            Date date = sdfd.parse(time);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertString(Date date) {
        try {
            return sdfd.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertDateString(Date date) {
        try {
            return sdfDate.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertObjString(Object object) {
        if (object != null) {
            try {
                return sdf.format(object);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static Long convertLong(String time) {
        try {
            Date date = sdfd.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Long getLong(String time) {
        try {
            long date = sdfd.parse(time).getTime();
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * yyyymmdd格式字符转yyyy-mm-dd
     *
     * @return string
     */
    public static String convertStrToSdfStr(String date) {
        String str = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            sdf.setLenient(false);
            Date newDate = sdf.parse(date);
            str = sdfDate.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 时间转换成字符串
     *
     * @param date
     * @return
     * @pattern 格式
     */
    public static String convertString(Date date, String pattern) {
        try {
            return getDateFormat(pattern).format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据传入时间获取几小时以前的时间
     *
     * @param date 时间参数
     * @param hour 小时数
     * @return
     */
    public static Date convertBeforeHourDate(Date date, Integer hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - hour);
        return calendar.getTime();
    }

    /**
     * 根据传入时间获取几天以前的时间
     *
     * @param date 时间参数
     * @param day  小时数
     * @return
     */
    public static Date convertBeforeDayDate(Date date, Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - day);
        return calendar.getTime();
    }

    /**
     * yyyyMMdd HH:mm:ss 格式字符转时间类型
     * @param str yyyyMMdd HH:mm:ss 格式字符
     * @return Date
     */
    public static Date convertStringToDate(String str) {
        try {
            Date date = sdfd1.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取时间转换格式
     *
     * @return
     */
    public static SimpleDateFormat getDateFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }


    /**
     * 获取当天开始时间
     *
     * @return
     */
    public static String getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return convertString(todayStart.getTime());
    }

    /**
     * 获取当天结束时间
     *
     * @return
     */
    public static String getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return convertString(todayEnd.getTime());
    }
}
