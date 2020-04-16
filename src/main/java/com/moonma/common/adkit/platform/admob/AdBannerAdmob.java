package com.moonma.common;

import android.app.Activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.moonma.common.IAdBannerBase;
import com.moonma.common.IAdBannerBaseListener;
import com.moonma.common.AdConfigAdmob;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


public class AdBannerAdmob implements IAdBannerBase {

    // 自定义单一平台广告视图对象
    //private AdView adView;
    private AdView mAdView;

    private static String TAG = "AdBanner";


    IAdBannerBaseListener adBannerBaseListener;
    FrameLayout framelayout;
    Activity mainActivity;
    private   boolean sIsShow;
    private   int bannerOffsety;
    private   float bannerAlhpha;

    //  AdView adView;
    FrameLayout framelayoutAd;

    boolean isAdInit;
    public   void init(  Activity activity,FrameLayout layout)
    {
        mainActivity = activity;
        framelayout = layout;
        isAdInit = false;
        sIsShow = false;

    }
    public void setListener(IAdBannerBaseListener listener)
    {
        adBannerBaseListener = listener;

    }

    public void setAd()
    {
        if(isAdInit==false)
        {
            isAdInit = true;
             String strAppId = AdConfigAdmob.main().appId;
             String strAppKey = AdConfigAdmob.main().appKeyBanner;
            Log.v(TAG,strAppKey);

            // Initialize the Mobile Ads SDK.
            //MobileAds.initialize(mainActivity, "ca-app-pub-3940256099942544~3347511713");

            // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
            // values/strings.xml.
            mAdView = new AdView(mainActivity);
            mAdView.setAdSize(AdSize.BANNER);
            //test demo :ca-app-pub-3940256099942544/6300978111
            //儿童游戏android：ca-app-pub-9172948412628142/4766962919
            mAdView.setAdUnitId(strAppKey);


            // Create an ad request. Check your logcat output for the hashed device ID to
            // get test ads on a physical device. e.g.
            // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();

            // Start loading the ad in the background.
            mAdView.loadAd(adRequest);



            mAdView.setAdListener(new

                                          AdListener() {
                                              @Override
                                              public void onAdClosed () {
                                                  //startGame();
//                        if (mOnAdInsertListener != null) {
//                            mOnAdInsertListener.adInsertDidClose();
//
//                        }
                                              }

                                              public void onAdFailedToLoad(int var1) {
                                                  int test;
                                                  test = 0;
                                                  if (adBannerBaseListener != null) {
                                                      adBannerBaseListener.onLoadAdFail();
                                                  }
                                              }

                                              public void onAdLeftApplication() {
                                              }

                                              public void onAdOpened() {

                                              }


                                              public void onAdLoaded()
                                              {
                                                  //showInterstitial();
                                                  if (adBannerBaseListener != null) {
                                                      int w, h;
                                                      w = mAdView.getWidth();
                                                      h = mAdView.getHeight();
                                                      adBannerBaseListener.onReceiveAd(w, h);

                                                      setOffsetY(bannerOffsety);
                                                  }
                                              }
                                          });


            //将adView添加到父控件中(注：该父控件不一定为您的根控件，只要该控件能通过addView能添加广告视图即可)
            RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            rllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            //rllp.addRule(RelativeLayout.CENTER_HORIZONTAL);


            // FrameLayout
            ViewGroup.LayoutParams framelayout_params =
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            framelayoutAd = new FrameLayout(mainActivity);
            framelayoutAd.setLayoutParams(framelayout_params);

            framelayoutAd.addView(mAdView);
            framelayout.addView(framelayoutAd, rllp);


            // adView.setVisibility(View.GONE);
            framelayoutAd.setVisibility(View.GONE);

        }

    }



    public     void setAlpha(float alpha )
    {

        bannerAlhpha = alpha;
        if (mainActivity == null)
        {
            return;
        }
        mainActivity.runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {
                if(bannerAlhpha<1.0)
                {
                    //  bannerAlhpha =0;
                }
                framelayoutAd.setAlpha(bannerAlhpha);


            }
        } );

    }

    public     void show(boolean isShow )
    {
        sIsShow = isShow;

        if (mainActivity == null)
        {
            return;
        }


        mainActivity.runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {

                if (mAdView == null)
                {
                    return;
                }
                if (sIsShow) {


                    framelayoutAd.setVisibility(View.VISIBLE);

                } else {

                    framelayoutAd.setVisibility(View.GONE);
                }
            }
        } );


    }

    public void layoutSubView(int w,int h )
    {
        setOffsetY(bannerOffsety);

    }
    public void setOffsetY(int y )
    {

        bannerOffsety = y;
//
//
        if ((mAdView == null)||(framelayout == null)||(mainActivity == null))
        {
            return;
        }
        mainActivity.runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {



                int screen_w = framelayout.getWidth();
                int w = mAdView.getWidth();
                int screen_h = framelayout.getHeight();
                int h = mAdView.getHeight();

                if(h==0){
                    h=168;
                }

                Log.v(TAG,"admob banner w="+w+" h="+h);
                int x = (screen_w - w)/2;
                if(w==0){
                    x=0;
                }
                int oft_bottom = com.moonma.common.ScreenUtil.getBottomNavigationBarHeight();
                if (Device.isLandscape()) {
                    // 华为全面屏 底部导航栏 横屏时候现在在右边
                    oft_bottom = 0;
                } 

                int y = screen_h - h -bannerOffsety-oft_bottom;

                if (h==0){
                    //显示到屏幕外
                    // y =  screen_h;

                }
                //adView.setX(x);
                // adView.setY(y);

                framelayoutAd.setX(x);
                framelayoutAd.setY(y);


                // FrameLayout.LayoutParams cameraFL = new FrameLayout.LayoutParams(w, h,Gravity.TOP); // set size
                // cameraFL.setMargins(x, y, 0, 0);  // set position
                //adsMogoLayoutCode.setLayoutParams(cameraFL);


                mAdView.setY(0);
                mAdView.bringToFront();
                framelayoutAd.bringToFront();
            }
        } );

    }


}
