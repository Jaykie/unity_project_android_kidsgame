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
import com.moonma.common.AdBannerAdmob;
import com.moonma.common.AdBannerXiaomi;
import com.moonma.common.AdBannerGdt;
import com.moonma.common.AdBannerAdView;
import com.moonma.common.AdBannerMobVista;

import com.moonma.common.IAdBannerBase;
import com.moonma.common.IAdBannerBaseListener;

/**
 * Created by jaykie on 16/5/24.
 */
public class AdBannerCommon implements IAdBannerBaseListener
// AdBanner.OnAdBannerListener

{
    private static final String TAG = AdBannerCommon.class.getSimpleName();

    private static Activity sActivity = null;
    private static AdBannerCommon pthis;

    private static String sStrAppId;
    private static String sStrAppKey;
    private boolean isAdInsertInit;
    private boolean isAdWallInit;
    private boolean isAdBannerInit = false;

    private static boolean sInited = false;
    FrameLayout framelayout;
    private GLSurfaceView gameSurfaceView;

    IAdBannerBase adBanner;
    int adBannerW;
    int adBannerH;

    private String strAdSource;
    private String strAppIdBanner;
    private String strAppKeyBanner;

    public void init(final Activity activity, FrameLayout layout) {

        pthis = this;
        sActivity = activity;
        framelayout = layout;

        // ad

    }

    public void createAdBanner(String source) {
        if (source.equals(Source.ADMOB)) {
            adBanner = new AdBannerAdmob();
        } else if (source.equals(Source.XIAOMI)) {
            adBanner = new AdBannerXiaomi();
        } else if (source.equals(Source.GDT)) {
            adBanner = new AdBannerGdt();
        } else if (source.equals(Source.ADVIEW)) {
            adBanner = new AdBannerAdView();
        }else if (source.equals(Source.MobVista)) {
            adBanner = new AdBannerMobVista();
        }
       // adBanner = new AdBannerMobVista();
        if (adBanner != null) {
            adBanner.init(sActivity, framelayout);
            adBanner.setListener(this);
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

    public void setBannerId(final String strAppId, final String strAppKey) {
        strAppIdBanner = strAppId;
        strAppKeyBanner = strAppKey;

    }

    public static void adBanner_setAd(final String source) {

        Log.d(TAG, "adBanner_setAd strSource=" + source);
        final  String str = source;
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pthis.adBanner_setAppIdOrKey_nonstatic(str);
            }
        });

    }

    private void adBanner_setAppIdOrKey_nonstatic(String source) {

        if (isAdBannerInit) {
          //  return;
        }
        final  String str = source;
        strAdSource = source;
        
        isAdBannerInit = true;
        createAdBanner(str);
        adBanner.setAd();

    }

    public static void adBanner_setAlpha(float alpha) {
        if (pthis.adBanner == null) {

            return;
        }
        pthis.adBanner.setAlpha(alpha);
    }

    public static void adBanner_layoutSubView(int w, int h) {
        if (pthis.adBanner == null) {
            return;
        }
        pthis.adBanner.layoutSubView(w, h);
    }

    public static void adBanner_show(boolean isShow, int offsety) {
        if (pthis.adBanner == null) {

            return;
        }

        pthis.adBanner.show(isShow);
        pthis.adBanner.setOffsetY(offsety);
    }

    @Override
    public void onReceiveAd(int w, int h) {
        adBannerW = w;
        adBannerH = h;

        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int w = adBannerW;
                int h = adBannerH;

                // int screen_w = mGLSurfaceView.getWidth();
                //
                // int screen_h = mGLSurfaceView.getHeight();

                // nativeAdBannerDidReceiveAd(w, h);
                String str = Integer.toString(w) + ":" + Integer.toString(h);
                UnityPlayer.UnitySendMessage("Scene", "AdBannerDidReceiveAd", str);
            }
        });

    }

    @Override
    public void onLoadAdFail() {
        isAdBannerInit = false;
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String str = strAdSource;
                UnityPlayer.UnitySendMessage("Scene", "AdBannerDidReceiveAdFail", str);
            }
        });
    }
    @Override
    public void onLoadAdClick() {
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String str = strAdSource;
                Log.d(TAG,"onLoadAdClick source="+str);
                UnityPlayer.UnitySendMessage("Scene", "AdBannerDidClick", str);

            }
        });
    }
}
