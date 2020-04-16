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

import com.moonma.common.IAdVideoBase;
import com.moonma.common.IAdVideoBaseListener;
 
import com.moonma.common.AdVideoAdmob;
import com.moonma.common.AdVideoXiaomi;
import com.moonma.common.AdVideoUnity;
import com.moonma.common.AdVideoGdt;
import com.moonma.common.AdVideoAdView;
import com.moonma.common.AdVideoMobVista;
import com.moonma.common.AdVideoVungle;;

/**
 * Created by jaykie on 16/5/24.
 */
public class AdVideoCommon implements IAdVideoBaseListener {
    private static final String TAG = AdVideoCommon.class.getSimpleName();

    private static Activity sActivity = null;
    private static AdVideoCommon pthis;
    private boolean isAdInsertInit;
    private boolean isAdWallInit;
    private boolean isAdBannerInit;

    private static boolean sInited = false;
    FrameLayout framelayout;
    private GLSurfaceView gameSurfaceView;

    IAdVideoBase adVideo;
    private static String unityObjNameNative;
    private static String unityObjMethodNative; 
    static int adType;

    public void init(final Activity activity, FrameLayout layout) {

        pthis = this;
        sActivity = activity;
        framelayout = layout;

    }

    public IAdVideoBase getAd(String source) {
        IAdVideoBase ad = null;
        if (source.equals(Source.ADMOB)) {
            ad = new AdVideoAdmob();
        } else if (source.equals(Source.UNITY)) {
            ad = new AdVideoUnity();
        } else if (source.equals(Source.XIAOMI)) {
            ad = new AdVideoXiaomi();
        } else if (source.equals(Source.GDT)) {
            ad = new AdVideoGdt();
        } else if (source.equals(Source.ADVIEW)) {
            ad = new AdVideoAdView();
        }else if (source.equals(Source.MobVista)) {
            ad = new AdVideoMobVista();
        }else if (source.equals(Source.VUNGLE)) {
            ad = new  AdVideoVungle();
        }
        return ad;

    }

    public void createAd(String source) {

        adVideo = getAd(source);
      //  adVideo = new AdVideoMobVista();
        if (adVideo != null) {
            adVideo.init(sActivity, framelayout);
            adVideo.setListener(this);
        }
    }

    public static Activity getActivity() {

        return sActivity;
    }

    public void setFrameLayout(FrameLayout layout) {
        framelayout = layout;

    }

    public static void setType(int type) {
        adType = type;
    }


    public static void PreLoad(final String source) {
        final  String str = source;
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                IAdVideoBase  ad= pthis.getAd(str);
                Log.d(TAG,"Ad Video PreLoad:"+str);
                if (ad != null) {
                    ad.init(sActivity, pthis.framelayout);
                    ad.setListener(null);
                    ad.setType(adType);
                    ad.setAd();
                }
            }
        });

        }
        public static void setAd(final String source) { 
        final  String str = source;
        Log.d(TAG,"video source:"+source);
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pthis.setAppIdOrKey_nonstatic(str);
            }
        });

    }

    public void setAppIdOrKey_nonstatic(String source) {
        // L.i("AdsMoGo", "adIndsert_setAppIdOrKey_nonstatic start");
        // L.i("AdsMoGo", "=====adIndsert_setAppIdOrKey_nonstatic=====:" + strAppId);
        // gdt
        {
            if (isAdInsertInit) {
                return;
            }

            {
                isAdInsertInit = true;
                createAd(source);
                adVideo.setType(adType);
                adVideo.setAd();
            }
        }

    }

    public static void show() {

        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pthis.adVideo.setType(adType);
                pthis.adVideo.show();
            }
        });

    }

    @Override
    public void adVideoDidFail() {
        isAdInsertInit = false;
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
Log.d(TAG,"adVideoDidFail");
               UnityPlayer.UnitySendMessage("Scene", "AdVideoDidFail", "fail");
            }
        });
    }

    @Override
    public void adVideoDidStart() {

        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show_GLView(false);
                // gameSurfaceView.setZOrderOnTop(false);
                // gameSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
                // @moon
                // nativeAdInsertWillShow();
                UnityPlayer.UnitySendMessage("Scene", "AdVideoDidStart", "start");
            }
        });
    }

    @Override
    public void adVideoDidFinish() {
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show_GLView(true);
                // gameSurfaceView.setZOrderOnTop(true);
                // gameSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
                // @moon
                // nativeAdInsertDidClose();
                UnityPlayer.UnitySendMessage("Scene", "AdVideoDidFinish", "finish");
            }
        });

    }

}
