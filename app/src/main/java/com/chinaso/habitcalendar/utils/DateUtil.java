package com.chinaso.habitcalendar.utils;


import android.util.Log;

import com.chinaso.habitcalendar.data.MarkEntity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @auther Su Yuewen
 * @time 2019/4/8 15:11.
 */

public class DateUtil {
    public static int getTimeDistance(Date beginDate, Date endDate) {
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(beginDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        long beginTime = beginCalendar.getTime().getTime();
        long endTime = endCalendar.getTime().getTime();
        int betweenDays = (int) ((endTime - beginTime) / (1000 * 60 * 60 * 24));//先算出两时间的毫秒数之差大于一天的天数

        endCalendar.add(Calendar.DAY_OF_MONTH, -betweenDays);//使endCalendar减去这些天数，将问题转换为两时间的毫秒数之差不足一天的情况
        endCalendar.add(Calendar.DAY_OF_MONTH, -1);//再使endCalendar减去1天
        if (beginCalendar.get(Calendar.DAY_OF_MONTH) == endCalendar.get(Calendar.DAY_OF_MONTH))//比较两日期的DAY_OF_MONTH是否相等
            return betweenDays + 1;    //相等说明确实跨天了
        else
            return betweenDays + 0;    //不相等说明确实未跨天
    }

    public static Date entittyToDate(MarkEntity lastMarkEntity) {
        DateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        Date dateLast= null;
        String dayLast = lastMarkEntity.getYear() + "." + lastMarkEntity.getMonth() + "." + lastMarkEntity.getDay();
        try {
            dateLast = format.parse(dayLast);  // Thu Jan 18 00:00:00 CST 2007
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateLast;
    }
    public static MarkEntity getSomeDay(Date date, int day) {
        Log.i("date--", "date"+date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,day);

        Log.i("date--", "getSomeDay"+calendar.get(Calendar.YEAR)+calendar.get(Calendar.MONTH)+calendar.get(Calendar.DATE)+"time"+calendar.getTime());
        return new MarkEntity(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DATE));

    }


}
