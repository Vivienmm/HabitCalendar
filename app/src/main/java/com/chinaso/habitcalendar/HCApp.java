package com.chinaso.habitcalendar;

import android.app.Application;

/**
 * @auther Su Yuewen
 * @time 2019/4/8 10:52.
 */

public class HCApp extends Application {
    private  static HCApp hcApp;

    @Override
    public void onCreate() {
        super.onCreate();
        hcApp=this;
    }

    public static HCApp getHcApp() {
        return hcApp;
    }
}
