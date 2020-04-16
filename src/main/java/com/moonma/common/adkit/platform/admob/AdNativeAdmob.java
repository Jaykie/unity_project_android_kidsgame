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
//import com.baidu.mobads.*;

import org.json.JSONObject;

import java.util.List;

import com.moonma.common.AdConfigAdmob;

public class AdNativeAdmob   {

//    private NativeADDataRef adItem;
//    private NativeAD nativeAD;

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

    public void loadAD()
    {
        String strAppId = AdConfigAdmob.main().appId;
             String strAppKey = AdConfigAdmob.main().appKeyNative;

//        if (nativeAD == null) {
//            this.nativeAD = new NativeAD(mainActivity, strAppId, strAppKey, this);
//        }
//        int count = 1; // 一次拉取的广告条数：范围1-10
//        nativeAD.loadAD(count);
    }

     public void setAd(final String strAppId, final String strAppKey)
    {

       // if(isAdInit==false)
        {
            isAdInit = true;

            String strAppIdInsert = "";
            String strAppKeyInsert = "";
//
//            try {
//                ApplicationInfo ai = mainActivity.getPackageManager().getApplicationInfo(mainActivity.getPackageName(), PackageManager.GET_META_DATA);
//                Bundle bundle = ai.metaData;
//
//
//                {
//                    int value = bundle.getInt("GDT_AD_APPID");
//                    strAppIdInsert = String.valueOf(value);
//                }
//
//                {
//                    Object value = bundle.get("GDT_AD_NATIVE");
//                    strAppKeyInsert = value.toString();
//                }
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            strAppIdInsert = strAppId;
            strAppKeyInsert = strAppKey;


        }

    }
    
    public void show( )
    {
        mainActivity.runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {



            }
        } );
    }
    public void onAdClick()
    {
//        if (adItem!=null) {
//            adItem.onExposured(framelayout); // 需要先调用曝光接口
//            adItem.onClicked(framelayout); // 点击接口
//        }
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
