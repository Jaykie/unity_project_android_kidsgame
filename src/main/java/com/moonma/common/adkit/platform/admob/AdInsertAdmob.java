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

import com.moonma.common.IAdInsertBase;
import com.moonma.common.IAdInsertBaseListener;
import com.moonma.common.AdConfigAdmob;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

public class AdInsertAdmob implements IAdInsertBase  {
    private InterstitialAd mInterstitialAd;
	// 自定义单一平台广告视图对象
	//private AdView adView;

    private static String TAG = "AdInsert";


    boolean isAdInit;
   // FrameLayout framelayout;
    Activity mainActivity;
    private   boolean sIsShow;
    private   int bannerOffsety;
    private   float bannerAlhpha;

   // InterstitialAd interAd;

    private IAdInsertBaseListener adInsertBaseListener;

    public   void init(  Activity activity,FrameLayout layout)
    {
        mainActivity = activity;
        //framelayout = layout;
        isAdInit = false;
    }


     public void setAd()
    {

        if(isAdInit==false)
        {
            isAdInit = true;
            String strAppId = AdConfigAdmob.main().appId;
            String strAppKey = AdConfigAdmob.main().appKeyInsert; 

            //        // Initialize the Mobile Ads SDK.
            //儿童游戏：ca-app-pub-9172948412628142~3290229713
            //MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

            // Create the InterstitialAd and set the adUnitId.
            mInterstitialAd = new InterstitialAd(mainActivity);
            // Defined in res/values/strings.xml

            //ca-app-pub-9172948412628142/3150628914 儿童游戏
            //demo:ca-app-pub-3940256099942544/1033173712
            Log.v(TAG,strAppKey);
            mInterstitialAd.setAdUnitId(strAppKey);

            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    //startGame();
                    if (adInsertBaseListener!=null){
                        adInsertBaseListener.adInsertDidClose();

                    }
                    //提前加载下个广告
                    loadAd();
                }

                public void onAdFailedToLoad(int var1) {
                    int test;
                    test=0;
                    if (adInsertBaseListener!=null){
                        adInsertBaseListener.adInsertDidFail();
                    }
                }

                public void onAdLeftApplication() {
                }

                public void onAdOpened() {
                    if (adInsertBaseListener!=null){
                        adInsertBaseListener.adInsertWillShow();
                    }
                }

                public void onAdLoaded() {
                  //  showInterstitial();
                }
            });


            loadAd();


        }

    }
    
    public void show( )
    {
        showInterstitial();
    }

    public void setListener(IAdInsertBaseListener listener)
    {
        adInsertBaseListener = listener;
    }


    public void startSplashAd(final String strAppId, final String strAppKey) {


    }


    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {

            loadAd();
        }
    }

    private void loadAd() {
        // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.
        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }

//        mRetryButton.setVisibility(View.INVISIBLE);
//        resumeGame(GAME_LENGTH_MILLISECONDS);
    }



}
