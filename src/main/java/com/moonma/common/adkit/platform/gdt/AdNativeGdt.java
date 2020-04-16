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
import com.qq.e.ads.interstitial.AbstractInterstitialADListener;
import com.qq.e.ads.interstitial.InterstitialAD;
import com.qq.e.ads.nativ.NativeAD;
import com.qq.e.ads.nativ.NativeADDataRef;

import org.json.JSONObject;

import java.util.List;

import com.moonma.common.AdConfigGdt;
import com.qq.e.comm.util.AdError;

public class AdNativeGdt implements NativeAD.NativeAdListener {

    private NativeADDataRef adItem;
    private NativeAD nativeAD;

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
        String strAppId = AdConfigGdt.main().appId;
        String strAppKey = AdConfigGdt.main().appKeyNative;
        if (nativeAD == null) {
            this.nativeAD = new NativeAD(mainActivity, strAppId, strAppKey, this);
        }
        int count = 1; // 一次拉取的广告条数：范围1-10
        nativeAD.loadAD(count);
    }

     public void setAd()
    {

       // if(isAdInit==false)
        {
            isAdInit = true;

            String strAppId = AdConfigGdt.main().appId;
            String strAppKey = AdConfigGdt.main().appKeyNative;

            // strAppId = "1101152570";
            // strAppKey = "5000709048439488";
            Log.i("AD_DEMO", "native id="+strAppId+ " key="+strAppKey);


            loadAD();


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
        if (adItem!=null) {
            adItem.onExposured(framelayout); // 需要先调用曝光接口
            adItem.onClicked(framelayout); // 点击接口
        }
    }

    public void setListener(OnAdNativeListener listener)
    {
        mOnAdNativeListener = listener;
    }



    @Override
    public void onADLoaded(List<NativeADDataRef> arg0) {
        if (arg0.size() > 0) {
            adItem = arg0.get(0);
          //  $.id(R.id.showNative).enabled(true);
           // Toast.makeText(this, "原生广告加载成功", Toast.LENGTH_LONG).show();
            if (mOnAdNativeListener!=null){
                String str = adItem.getImgUrl();
                Log.i("AD_DEMO", "AdNative:onADLoaded:"+str);
                mOnAdNativeListener.adNativeDidFinish(str);
            }
        } else {
            Log.i("AD_DEMO", "NOADReturn");
            if (mOnAdNativeListener!=null){
                mOnAdNativeListener.adNativeDidFail();
            }
        }
    }

    @Override
    public void onADStatusChanged(NativeADDataRef arg0) {
       // $.id(R.id.btn_download).text(getADButtonText());
    }

    @Override
    public void onADError(NativeADDataRef adData, AdError var2) {
       // Log.i("AD_DEMO", "onADError:" + errorCode);
        if (mOnAdNativeListener!=null){
            mOnAdNativeListener.adNativeDidFail();
        }
    }
    @Override
    public void onNoAD(AdError var1) {

       // Log.i("AD_DEMO", "ONNoAD:" + arg0);
        if (mOnAdNativeListener!=null){
            mOnAdNativeListener.adNativeDidFail();
        }
    }



    public interface OnAdNativeListener {

        void adNativeDidFinish(String str);
        void adNativeDidFail();
    }
}
