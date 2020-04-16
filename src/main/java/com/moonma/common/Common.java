package com.moonma.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.view.View;

//import com.moonma.common.UIView;

import android.content.Intent;
/*
http://mumu.163.com/2017/12/19/25241_730476.html?type=notice
adb如何连接mumu模拟器
adb connect 127.0.0.1:7555
adb shell
*/
public class Common {

    static public Context appContext() {
        return MyApplication.getAppContext();
    }

    static public Activity getMainActivity() {
        MyApplication app = (MyApplication) MyApplication.getAppContext();
        return app.getMainActivity();
    }


//    static public String getRunningActivityName() {
//        // <uses-permission android:name="android.permission.GET_TASKS" />
//        ActivityManager activityManager = (ActivityManager) appContext().getSystemService(Context.ACTIVITY_SERVICE);
//        // Activity ac = null;
//        String str = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
//        return str;
//    }


//    static public String stringFromResId(int res) {
//        return appContext().getResources().getString(res);
//    }

    public static boolean isBlankString(String str) {

        if (str == null) {
            //为null;
            return true;
        }
        if (str.length() == 0) {
            //为空字符串;
            return true;
        }
        if (str.equals("")) {
            //为空字符串;
            return true;
        }

        return false;
    }

    public static boolean isAppForPad(Context ac) {
        String packageName = ac.getPackageName();
        boolean ret = false;
        if ((packageName.indexOf(".pad") != -1) || (packageName.indexOf(".ipad") != -1))
        {
            ret = true;
        }
        return ret;
    }
}
