package com.moonma.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.unity3d.player.UnityPlayer;

import java.io.File;

//ad

import com.moonma.common.IAdInsertBase;
import com.moonma.common.IAdInsertBaseListener; 

import com.moonma.common.AdInsertAdmob;
import com.moonma.common.AdInsertXiaomi;
import com.moonma.common.AdInsertGdt;
import com.moonma.common.AdInsertAdView;
import com.moonma.common.AdInsertMobVista;
import com.moonma.common.AdInsertVideoMobVista;
import com.moonma.common.AdInsertVungle;
import com.moonma.common.AdInsertUnity;
/**
 * Created by jaykie on 16/5/24.
 */
public class AdInsertCommon implements IAdInsertBaseListener {
    private static final String TAG = CommonAd.class.getSimpleName();

    private static Activity sActivity = null;
    private static AdInsertCommon pthis;
    private boolean isAdInsertInit = false;
    private boolean isAdWallInit;
    private boolean isAdBannerInit;

    private static boolean sInited = false;
    FrameLayout framelayout;
    private GLSurfaceView gameSurfaceView;

    IAdInsertBase adInsert;
    private static String unityObjNameNative;
    private static String unityObjMethodNative;

    private String strAdSource;

    public void init(final Activity activity, FrameLayout layout) {

        pthis = this;
        sActivity = activity;
        framelayout = layout;

    }

    public void createAdInsert(String source) {
        Log.d(TAG,"createAdInsert:"+source);
        if (source.equals(Source.ADMOB)) {
            adInsert = new AdInsertAdmob();
        } else if (source.equals(Source.XIAOMI)) {
            adInsert = new AdInsertXiaomi();
        } else if (source.equals(Source.GDT)) {
            adInsert = new AdInsertGdt();
        } else if (source.equals(Source.ADVIEW)) {
            adInsert = new AdInsertAdView();
        }else if (source.equals(Source.MobVista)) {
          //  adInsert = new AdInsertMobVista();
            adInsert = new AdInsertVideoMobVista();
        }else if (source.equals(Source.VUNGLE)) {
            adInsert = new AdInsertVungle();
        }else if (source.equals(Source.UNITY)) {
            adInsert = new AdInsertUnity();
        }

        if (adInsert != null) {
            adInsert.init(sActivity, framelayout);
            adInsert.setListener(this);
        }
    }

    public static Activity getActivity() {

        return sActivity;
    }

    public void setFrameLayout(FrameLayout layout) {
        framelayout = layout;

    }

    public void setGameSurfaceView(GLSurfaceView view) {
        gameSurfaceView = view;

    }

    public void setInsertId(final String strAppId, final String strAppKey) {

    }

    public static void adIndsert_setAd(final String source) { 
        final String str = source;
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pthis.adIndsert_setAppIdOrKey_nonstatic(str);
            }
        });

    }

    public void adIndsert_setAppIdOrKey_nonstatic(String source) {
        // L.i("AdsMoGo", "adIndsert_setAppIdOrKey_nonstatic start");
        // L.i("AdsMoGo", "=====adIndsert_setAppIdOrKey_nonstatic=====:" + strAppId);
        // gdt
        strAdSource = source;
        {
            if (isAdInsertInit) {
               // return;
            }

            {
                isAdInsertInit = true;
                createAdInsert(source);
                adInsert.setAd();
            }
        }

    }

    public static void adIndsert_show() {
        Log.d(TAG,"adIndsert_show");
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pthis.adInsert.show();

            }
        });

    }

    @Override
    public void adInsertWillShow() {

        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UnityPlayer.UnitySendMessage("Scene", "AdInsertWillShow", "");
            }
        });
    }

    @Override
    public void adInsertDidClose() {
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UnityPlayer.UnitySendMessage("Scene", "AdInsertDidClose", "");
            }
        });

    }

    @Override
    public void adInsertDidFail() {
        isAdInsertInit = false;
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UnityPlayer.UnitySendMessage("Scene", "AdInsertDidFail", strAdSource);
            }
        });

    }

}
