package com.chinaso.habitcalendar.data;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * @auther Su Yuewen
 * @time 2019/4/8 10:20.
 */

public class MarkEntity implements Comparable,Serializable {
    private int year;
    private int month;
    private int day;

    public MarkEntity(int mYear, int mMonth, int mDay) {
        year = mYear;
        month = mMonth;
        day = mDay;

    }


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }


    @Override
    public boolean equals(Object obj) {
        MarkEntity that = (MarkEntity) obj;
        if(year==that.year&&month==that.month&&day==that.day){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int compareTo(@NonNull Object obj) {
        //异常判断
        if (null == obj) {
            throw new NullPointerException("所比较对象不能为空");
        }

        MarkEntity entity = (MarkEntity) obj;
        if (entity.year != this.year) {
            return entity.year - this.year;
        }else if (entity.year == this.year && (entity.month != this.month)) {
            return this.month - entity.month;
        }else  {
            return this.day - entity.day;
        }
    }
}
