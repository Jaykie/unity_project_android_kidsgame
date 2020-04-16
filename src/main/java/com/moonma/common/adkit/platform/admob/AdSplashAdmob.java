package com.moonma.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import com.unity3d.player.UnityPlayer;


import com.moonma.common.IAdInsertBase;
import com.moonma.common.IAdInsertBaseListener;
import com.moonma.common.AdInsertAdmob;
import com.moonma.common.IAdSplashBase;
import com.moonma.common.AdConfigAdmob;
/**
 * Created by jaykie on 2017/5/4.
 */

public class AdSplashAdmob implements IAdSplashBase,IAdInsertBaseListener {

    private static String TAG = "AdSplash";

    FrameLayout framelayout;
    Activity mainActivity;
    public boolean canJump = false;
    FrameLayout frameLayoutAd;
    //以下的POSITION_ID 需要使用您申请的值替换下面内容
    private static final String POSITION_ID = "b373ee903da0c6fc9c9da202df95a500";
    IAdInsertBase adInsert;

    //获取当前使用第几天 从1开始
    int getDayIndexOfUse() {
        int index = 1;
        long second_first_use = 0;
        String pkey_firstuse_second = "key_app_first_use_second";
        String pkeyfirstuse = "key_app_first_use";

        SharedPreferences prefs = mainActivity.getSharedPreferences(
                "cocos2dx_app", Context.MODE_PRIVATE);
        boolean isFirstUse = !prefs.getBoolean(pkeyfirstuse, false);
        if (isFirstUse) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(pkeyfirstuse, true);
            editor.commit();

            second_first_use = System.currentTimeMillis() / 1000;
//

            //默认设置为软件解码
//            int mode = 2;//DecoderMode.UM;
            editor.putLong(pkey_firstuse_second, second_first_use);
            editor.commit();


        }


        second_first_use = prefs.getLong(pkey_firstuse_second, 0);
        long second_cur = System.currentTimeMillis() / 1000;
        index = (int) ((second_cur - second_first_use) / (3600 * 24));
        if (index < 0) {
            index = 0;
        }
        index++;

        return index;
    }


    int getNoAdDay() {
        int index = 1;
        String pkey = "key_no_ad_day";
        SharedPreferences prefs = mainActivity.getSharedPreferences(
                "cocos2dx_app", Context.MODE_PRIVATE);

        index = prefs.getInt(pkey, 1);
        return index;
    }

    boolean isEnableAd() {

        String pkey = "key_enable_splash";
        SharedPreferences prefs = mainActivity.getSharedPreferences(
                "cocos2dx_app", Context.MODE_PRIVATE);

        boolean ret = prefs.getBoolean(pkey, true);
        return ret;
    }

    public void init(Activity activity, FrameLayout layout) {
        mainActivity = activity;
        framelayout = layout;


    }

    public void showAdSplashByInsert() {

    }

    public boolean isNoAd() {
        String pkey = "key_app_no_ad";
        SharedPreferences prefs = mainActivity.getSharedPreferences(
                "cocos2dx_app", Context.MODE_PRIVATE);

        boolean ret = prefs.getBoolean(pkey, false);
        return ret;
    }

    public void showAdSplash() {
        String APP_ID = "2882303761517411490";


        String strAppId = "";
        String strAppKeySplash = "";

        String pkey_splash_souce = "key_splash_souce";
        String pkey_splash_appid = "key_splash_appid";
        String pkey_splash_appkey = "key_splash_appkey";
        SharedPreferences prefs = mainActivity.getSharedPreferences(
                "cocos2dx_app", Context.MODE_PRIVATE);

        strAppId = prefs.getString(pkey_splash_appid, "0");
        strAppKeySplash = prefs.getString(pkey_splash_appkey, "0");

        //fetchSplashAD(this, container, skipView, strAppId, strAppKeySplash, this, 0);
        Log.i("AD_DEMO", "splash id=" + strAppId + " key=" + strAppKeySplash);
// R.drawable.default_splash

        if (strAppId == "0") {
            //next();
            return;
        }


        if (!isEnableAd()) {
            return;
        }

        if ((getDayIndexOfUse() <= getNoAdDay()) || isNoAd()) {
            return;
        }

        adInsert = new AdInsertAdmob();
        adInsert.init(mainActivity,null);
        adInsert.setListener(this);

        adInsert.setAd();
        adInsert.show();
    }


    @Override
    public void adInsertWillShow() {

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UnityPlayer.UnitySendMessage("Scene", "AdInsertWillShow", "");
            }
        });
    }

    @Override
    public void adInsertDidClose() {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UnityPlayer.UnitySendMessage("Scene", "AdInsertDidClose", "");
            }
        });

    }

    @Override
    public void adInsertDidFail() {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UnityPlayer.UnitySendMessage("Scene", "AdInsertDidFail",Source.ADMOB);
            }
        });

    }




}
