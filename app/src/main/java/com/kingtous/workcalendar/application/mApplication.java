package com.kingtous.workcalendar.application;

import android.app.Application;

import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.tencent.bugly.crashreport.CrashReport;

public class mApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //QMUI
        QMUISwipeBackActivityManager.init(this);
        //Bugly
        CrashReport.initCrashReport(this,"d71a892e75",false);
    }

}
