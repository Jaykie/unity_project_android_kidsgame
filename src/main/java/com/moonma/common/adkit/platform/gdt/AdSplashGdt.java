package com.moonma.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;

import com.moonma.common.IAdSplashBase;
import com.moonma.common.AdConfigGdt;
import com.qq.e.comm.util.AdError;

/**
 * Created by jaykie on 2017/5/4.
 */

public class AdSplashGdt implements IAdSplashBase,SplashADListener {

    private static String TAG = "AdSplash";

    FrameLayout framelayout;
    Activity mainActivity;
    private SplashAD splashAD;
    public boolean canJump = false;
    FrameLayout frameLayoutAd;


    //获取当前使用第几天 从1开始
    int getDayIndexOfUse()
    {
        int index =1;
        long second_first_use =0;
        String pkey_firstuse_second = "key_app_first_use_second";
        String pkeyfirstuse = "key_app_first_use";

        SharedPreferences prefs = mainActivity.getSharedPreferences(
                "cocos2dx_app", Context.MODE_PRIVATE);
        boolean  isFirstUse = !prefs.getBoolean(pkeyfirstuse, false);
        if(isFirstUse){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(pkeyfirstuse, true);
            editor.commit();

            second_first_use =  System.currentTimeMillis()/1000;
//

            //默认设置为软件解码
//            int mode = 2;//DecoderMode.UM;
            editor.putLong(pkey_firstuse_second, second_first_use);
            editor.commit();


        }


        second_first_use = prefs.getLong(pkey_firstuse_second, 0);
        long second_cur = System.currentTimeMillis()/1000;
        index = (int)((second_cur - second_first_use)/(3600*24));
        if (index<0) {
            index=0;
        }
        index++;

        return index;
    }

    int getNoAdDay()
    {
        int index =1; 
        String pkey = "key_no_ad_day";
        SharedPreferences prefs = mainActivity.getSharedPreferences(
                "cocos2dx_app", Context.MODE_PRIVATE); 

        index = prefs.getInt(pkey, 1);
        return index;
    }

    boolean isEnableAd()
    {
        
        String pkey = "key_enable_splash";
        SharedPreferences prefs = mainActivity.getSharedPreferences(
                                                                    "cocos2dx_app", Context.MODE_PRIVATE);
        
        boolean ret = prefs.getBoolean(pkey, true);
        return ret;
    }
    
    public boolean isNoAd()
    {
        String pkey = "key_app_no_ad";
        SharedPreferences prefs = mainActivity.getSharedPreferences(
                "cocos2dx_app", Context.MODE_PRIVATE);

        boolean ret = prefs.getBoolean(pkey, false);
        return ret;
    }

    public   void init(  Activity activity,FrameLayout layout)
    {
        mainActivity = activity;
        framelayout = layout;


    }

    public void showAdSplash()
    {
        if(!isEnableAd())
        {
            return;
        }

        if((getDayIndexOfUse()<=getNoAdDay())||isNoAd()) {
            return;
        }
 
        ViewGroup.LayoutParams framelayout_params =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);

        frameLayoutAd = new FrameLayout(mainActivity);

        frameLayoutAd.setLayoutParams(framelayout_params);

        framelayout.addView(frameLayoutAd);

        String strAppId = "";
        String strAppKeyBanner = "";
        String strAppKeyInsert = "";
        String strAppKeySplash = "";
//        try {
//            ApplicationInfo ai = mainActivity.getPackageManager().getApplicationInfo(mainActivity.getPackageName(), PackageManager.GET_META_DATA);
//            Bundle bundle = ai.metaData;
//
//
//            {
//                int value = bundle.getInt("GDT_AD_APPID");
//                strAppId = String.valueOf(value);
//            }
//
//            {
//                Object value = bundle.get("GDT_AD_BANNER");
//                strAppKeyBanner = value.toString();
//            }
//
//            {
//                Object value = bundle.get("GDT_AD_INSERT");
//                strAppKeyInsert = value.toString();
//            }
//            {
//                Object value = bundle.get("GDT_AD_SPLASH");
//                strAppKeySplash = value.toString();
//            }
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        String pkey_splash_souce = "key_splash_souce";
        String pkey_splash_appid = "key_splash_appid";
        String pkey_splash_appkey = "key_splash_appkey";
        SharedPreferences prefs = mainActivity.getSharedPreferences(
                "cocos2dx_app", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        //editor.putString(pkey_splash_souce, strSource);
        strAppId = prefs.getString(pkey_splash_appid, "0");
        strAppKeySplash = prefs.getString(pkey_splash_appkey, "0");

        //fetchSplashAD(this, container, skipView, strAppId, strAppKeySplash, this, 0);
        Log.i("AD_DEMO", "splash id="+strAppId+ " key="+strAppKeySplash);
     //   fetchSplashAD(mainActivity, frameLayoutAd, null, strAppId, strAppKeySplash, this, 0);

    }

    /**
     * 拉取开屏广告，开屏广告的构造方法有3种，详细说明请参考开发者文档。
     *
     * @param activity        展示广告的activity
     * @param adContainer     展示广告的大容器
     * @param skipContainer   自定义的跳过按钮：传入该view给SDK后，SDK会自动给它绑定点击跳过事件。SkipView的样式可以由开发者自由定制，其尺寸限制请参考activity_splash.xml或者接入文档中的说明。
     * @param appId           应用ID
     * @param posId           广告位ID
     * @param adListener      广告状态监听器
     * @param fetchDelay      拉取广告的超时时长：取值范围[3000, 5000]，设为0表示使用广点通SDK默认的超时时长。
     */
    private void fetchSplashAD(Activity activity, ViewGroup adContainer, View skipContainer,
                               String appId, String posId, SplashADListener adListener, int fetchDelay) {
      //  splashAD = new SplashAD(activity, adContainer, skipContainer, appId, posId, adListener, fetchDelay);
    }

    @Override
    public void onADPresent() {
        Log.i("AD_DEMO", "SplashADPresent");
        //splashHolder.setVisibility(View.INVISIBLE); // 广告展示后一定要把预设的开屏图片隐藏起来
    }

    @Override
    public void onADClicked() {
        Log.i("AD_DEMO", "SplashADClicked");
        next();
    }


    /**
     * 倒计时回调，返回广告还将被展示的剩余时间。
     * 通过这个接口，开发者可以自行决定是否显示倒计时提示，或者还剩几秒的时候显示倒计时
     *
     * @param millisUntilFinished 剩余毫秒数
     */
    @Override
    public void onADTick(long millisUntilFinished) {
        Log.i("AD_DEMO", "SplashADTick " + millisUntilFinished + "ms");

        //skipView.setText(String.format(SKIP_TEXT, Math.round(millisUntilFinished / 1000f)));
    }

    @Override
    public void onADDismissed() {
        Log.i("AD_DEMO", "SplashADDismissed");
        next();
    }
    public void onADExposure()
    {

    }

    @Override
    public void onNoAD(AdError var1){
     //   Log.i("AD_DEMO", "LoadSplashADFail, eCode=" + errorCode);
        /** 如果加载广告失败，则直接跳转 */

        // this.startActivity(new Intent(this, UnityPlayerActivity.class));
        // this.finish();
        // mUnityPlayer.removeView(frameLayoutAd);
        next();
    }
    @Override
    public void onADLoaded(long expireTimestamp) {
        Log.i("AD_DEMO", "SplashADFetch expireTimestamp:"+expireTimestamp);

//        if(loadAdOnly) {
//            loadAdOnlyDisplayButton.setEnabled(true);
//            long timeIntervalSec = (expireTimestamp- SystemClock.elapsedRealtime())/1000;
//            long min = timeIntervalSec/60;
//            long second = timeIntervalSec-(min*60);
//            loadAdOnlyStatusTextView.setText("加载成功,广告将在:"+min+"分"+second+"秒后过期，请在此之前展示(showAd)");
//        }
    }

    /**
     * 设置一个变量来控制当前开屏页面是否可以跳转，当开屏广告为普链类广告时，点击会打开一个广告落地页，此时开发者还不能打开自己的App主页。当从广告落地页返回以后，
     * 才可以跳转到开发者自己的App主页；当开屏广告是App类广告时只会下载App。
     */
    private void next() {
        framelayout.removeView(frameLayoutAd);

        if (canJump) {
//            this.startActivity(new Intent(this,UnityPlayerActivity.class));//UnityPlayerActivity MainActivity
//            this.finish();

        } else {
            canJump = true;
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        canJump = false;
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (canJump) {
//            next();
//        }
//        canJump = true;
//    }

    //@moon
}
