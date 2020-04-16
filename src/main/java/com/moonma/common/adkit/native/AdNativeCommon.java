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

 
/**
 * Created by jaykie on 16/5/24.
 */
public class AdNativeCommon  {
    private static final String TAG = CommonAd.class.getSimpleName();

    private static Activity sActivity = null;
    private static AdNativeCommon pthis;
 
    private boolean isAdInsertInit;
    private boolean isAdWallInit;
    private boolean isAdBannerInit;

    private static boolean sInited = false;
    FrameLayout framelayout;
   
    private static String unityObjNameNative;
    private static String unityObjMethodNative;
 


    public  void init(final Activity activity,FrameLayout layout) {

            pthis = this;
            sActivity = activity;

//ad


        // adNative = new AdNative();
        // adNative.init(activity, layout);
        // adNative.setListener(this);

        
    }

    public static Activity getActivity() {

        return sActivity;
    }
    public   void setFrameLayout(FrameLayout layout)
    {
        framelayout = layout;

    } 
 
     public static void adNative_SetObjectInfo(final String objName, final String objMethod)
        {
            unityObjNameNative = objName;
            unityObjMethodNative = objMethod;
        }
 
    public static void adNative_setAd(final String strSource) {
 
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pthis.adNative_setAd_nonstatic();
            }
        });


    }

    public void adNative_setAd_nonstatic()
     {
        
    }

    public static void adNative_show() {
     
    }
    public static void adNative_onClick() {
     
    }



    public void adNativeDidFinish(String str)
    {
        UnityPlayer.UnitySendMessage(unityObjNameNative,"AdNativeDidLoad",str);
    }


    public void adNativeDidFail()
    {
        UnityPlayer.UnitySendMessage(unityObjNameNative,"AdNativeDidFail","");
    }



}
