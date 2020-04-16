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

import com.moonma.common.IAdVideoBase;
import com.moonma.common.IAdVideoBaseListener;
import com.moonma.common.AdConfigUnity;


import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.ads.metadata.MediationMetaData;
import com.unity3d.ads.metadata.MetaData;
import com.unity3d.ads.metadata.PlayerMetaData;
import com.unity3d.services.core.misc.Utilities;
import com.unity3d.services.core.properties.SdkProperties;
import com.unity3d.services.core.webview.WebView;

//https://github.com/Unity-Technologies/unity-ads-android

public class AdVideoUnity implements IAdVideoBase  {

    private static String TAG = "AdVideoUnity";


    public String interstitialPlacementId = "video";
    public String incentivizedPlacementId = "rewardedVideo";

    private static int ordinal = 1;

   static boolean isAdInit;
   // FrameLayout framelayout;
    Activity mainActivity;
    private   boolean sIsShow;
    private   int bannerOffsety;
    private   float bannerAlhpha;
    int adType;

   // InterstitialAd interAd;

    private IAdVideoBaseListener adVideoBaseListener;

    public   void init(  Activity activity,FrameLayout layout)
    {
        mainActivity = activity;
        //framelayout = layout;
        isAdInit = false;
    }

    public void setType(int type)
    {
        adType = type;
    }
     public void setAd()
    {

        if(isAdInit==false)
        {
            isAdInit = true;
            String strAppId = AdConfigUnity.main().appId;
            String strAppKey = AdConfigUnity.main().appKeyVideo;
            final UnityAdsListener unityAdsListener = new UnityAdsListener();

//            if(Build.VERSION.SDK_INT >= 19) {
//                WebView.setWebContentsDebuggingEnabled(true);
//            }

            UnityAds.setListener(unityAdsListener);
            UnityAds.setDebugMode(true);

            MediationMetaData mediationMetaData = new MediationMetaData(mainActivity);
            mediationMetaData.setName("mediationPartner");
            mediationMetaData.setVersion("v12345");
            mediationMetaData.commit();

            MetaData debugMetaData = new MetaData(mainActivity);
            debugMetaData.set("test.debugOverlayEnabled", true);
            debugMetaData.commit();
            String gameId = strAppId;
            boolean testmode = false;
            UnityAds.initialize(mainActivity, gameId, unityAdsListener, testmode);

        }

    }
    
    public void show( )
    {
        if(adType == ADVIDEO_TYPE_INSERT){
            showInterstitial();
        }else{
            showIncentivized();
        }


    }

    //普通视频
    public void showInterstitial( )
    {
        PlayerMetaData playerMetaData = new PlayerMetaData(mainActivity);
        playerMetaData.setServerId("rikshot");
        playerMetaData.commit();

        MediationMetaData ordinalMetaData = new MediationMetaData(mainActivity);
        ordinalMetaData.setOrdinal(ordinal++);
        ordinalMetaData.commit();

        UnityAds.show(mainActivity, interstitialPlacementId);
    }
//激励视频
    public void showIncentivized( )
    {

                PlayerMetaData playerMetaData = new PlayerMetaData(mainActivity);
                playerMetaData.setServerId("rikshot");
                playerMetaData.commit();

                MediationMetaData ordinalMetaData = new MediationMetaData(mainActivity);
                ordinalMetaData.setOrdinal(ordinal++);
                ordinalMetaData.commit();

                UnityAds.show(mainActivity, incentivizedPlacementId);

    }

    public void setListener(IAdVideoBaseListener listener)
    {
        adVideoBaseListener = listener;
    }



    /* LISTENER */

    private class UnityAdsListener implements IUnityAdsListener {

        @Override
        public void onUnityAdsReady(final String zoneId) {

            DeviceLog.debug("onUnityAdsReady: " + zoneId);
            Utilities.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // look for various default placement ids over time
                    switch (zoneId) {
                        case "video":
                        case "defaultZone":
                        case "defaultVideoAndPictureZone":
                            interstitialPlacementId = zoneId;

                            break;

                        case "rewardedVideo":
                        case "rewardedVideoZone":
                        case "incentivizedZone":
                            incentivizedPlacementId = zoneId;

                            break;
                    }
                }
            });

            toast("Ready", zoneId);
        }

        @Override
        public void onUnityAdsStart(String zoneId) {
            DeviceLog.debug("onUnityAdsStart: " + zoneId);
            toast("Start", zoneId);
            if(adVideoBaseListener!=null){
                adVideoBaseListener.adVideoDidStart();
            }
        }

        @Override
        public void onUnityAdsFinish(String zoneId, UnityAds.FinishState result) {
            DeviceLog.debug("onUnityAdsFinish: " + zoneId + " - " + result);
            toast("Finish", zoneId + " " + result);
            if(adVideoBaseListener!=null){
                adVideoBaseListener.adVideoDidFinish();
            }
        }

        @Override
        public void onUnityAdsError(UnityAds.UnityAdsError error, String message) {
            DeviceLog.debug("onUnityAdsError: " + error + " - " + message);
            toast("Error", error + " " + message);

            if(adVideoBaseListener!=null){
                adVideoBaseListener.adVideoDidFail();
            }
        }

        private void toast(String callback, String msg) {
            Log.v(TAG,msg);
        }
    }

}
