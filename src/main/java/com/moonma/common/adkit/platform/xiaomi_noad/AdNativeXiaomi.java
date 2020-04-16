package com.moonma.common;

import android.app.Activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast; 
import org.json.JSONObject;

import java.util.List;


public class AdNativeXiaomi {

   
    private static String TAG = "AdNative";

    public boolean isUseActivity = true;//true
    boolean isAdInit;
    FrameLayout framelayout;
    Activity mainActivity;
    private   boolean sIsShow;
    private   int bannerOffsety;
    private   float bannerAlhpha;

   // InterstitialAd interAd;

    private OnAdNativeListener mOnAdNativeListener;

    public   void init(  Activity activity,FrameLayout layout)
    {
        mainActivity = activity;
        framelayout = layout;
        isAdInit = false;
    }

    public void loadAD(final String strAppId, final String strAppKey)
    {
    
    }

     public void setAd(final String strAppId, final String strAppKey)
    { 

    }
    
    public void show( )
    {
        if (mOnAdNativeListener!=null){
            mOnAdNativeListener.adNativeDidFail();
        }
    } 

    public void setListener(OnAdNativeListener listener)
    {
        mOnAdNativeListener = listener;
    }
    
    public interface OnAdNativeListener {

        void adNativeDidFinish(String str);
        void adNativeDidFail();
    }
 
}
