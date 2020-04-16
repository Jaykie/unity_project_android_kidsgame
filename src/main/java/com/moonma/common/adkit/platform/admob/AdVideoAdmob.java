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
import com.moonma.common.AdConfigAdmob;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class AdVideoAdmob implements IAdVideoBase, RewardedVideoAdListener  {

    private static String TAG = "AdVideoAdmob";
    private RewardedVideoAd mRewardedVideoAd;

    static boolean isAdInit;
   // FrameLayout framelayout;
    Activity mainActivity;
    private   boolean sIsShow;
    private   int bannerOffsety;
    private   float bannerAlhpha;

 

    private IAdVideoBaseListener adVideoBaseListener;
    int adType;
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
            String strAppId = AdConfigAdmob.main().appId; 
            // Initialize the Mobile Ads SDK.
            MobileAds.initialize(mainActivity, strAppId);
            mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mainActivity);
            mRewardedVideoAd.setRewardedVideoAdListener(this);
            loadRewardedVideoAd();
        }

    }
    
    public void show( )
    {
        showRewardedVideo();
    }

    public void setListener(IAdVideoBaseListener listener)
    {
        adVideoBaseListener = listener;
    }

    private void loadRewardedVideoAd() {
        if (!mRewardedVideoAd.isLoaded()) { 
             String strAppKey = AdConfigAdmob.main().appKeyVideo;
            mRewardedVideoAd.loadAd(strAppKey, new AdRequest.Builder().build());
        }
    }

    private void showRewardedVideo() {

        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }else{
            loadRewardedVideoAd();
        }
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
       // Toast.makeText(this, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
       // Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
        // Preload the next video ad.
        loadRewardedVideoAd();
        if(adVideoBaseListener!=null){
            adVideoBaseListener.adVideoDidFinish();
        }
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
       // Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
        Log.v(TAG,"onRewardedVideoAdFailedToLoad:errorCode="+errorCode);
        if(adVideoBaseListener!=null){
            adVideoBaseListener.adVideoDidFail();
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {
       // Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
       // Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewarded(RewardItem reward) {
       // Toast.makeText(this,
//                String.format(" onRewarded! currency: %s amount: %d", reward.getType(),
//                        reward.getAmount()),
//                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRewardedVideoStarted() {
       // Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();

        if(adVideoBaseListener!=null){
            adVideoBaseListener.adVideoDidStart();
        }
    }
}
