package com.moonma.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.widget.FrameLayout;
import android.util.Log;
//ad
import com.moonma.common.AdBannerCommon;
import com.moonma.common.AdInsertCommon;
import com.moonma.common.AdNativeCommon;
import com.moonma.common.AdVideoCommon;
import com.moonma.common.AdConfig;
import com.moonma.common.AdConfigBase;
import com.moonma.common.AdConfigAdmob;
import com.moonma.common.AdConfigXiaomi;
import com.moonma.common.AdConfigGdt;
import com.moonma.common.AdConfigMobVista;
import com.moonma.common.AdConfigUnity;
import com.moonma.common.AdConfigAdView;
import com.moonma.common.AdConfigVungle;
/**
 * Created by jaykie on 16/5/24.
 */
public class CommonAd {
 
    private static final String TAG = CommonAd.class.getSimpleName();

    private static Activity sActivity = null;
    private static CommonAd pthis;

    private static String sStrAppId;
    private static String sStrAppKey;
    private boolean isAdInsertInit;
    private boolean isAdWallInit;
    private boolean isAdBannerInit;

    private static boolean sInited = false;
    FrameLayout framelayout;
    private GLSurfaceView gameSurfaceView;

    AdBannerCommon aBannerCommon;
    AdInsertCommon adInsertCommon;
    AdNativeCommon adNativeCommon;
    AdVideoCommon adVideoCommon;


    private String strAppIdSplash;
    private String strAppKeySplash;
    //navive callback
//    private static native void nativeDidGetPoint(int point);

    public void init(final Activity activity, FrameLayout layout) {

        pthis = this;
        sActivity = activity;

//ad
        aBannerCommon = new AdBannerCommon();
        aBannerCommon.init(activity, layout);
        //aBannerCommon.setListener(this);


        adInsertCommon = new AdInsertCommon();
        adInsertCommon.init(activity, layout);
        //adInsertCommon.setListener(this);

        adNativeCommon = new AdNativeCommon();
        adNativeCommon.init(activity, layout);
        //adNativeCommon.setListener(this);


        adVideoCommon = new AdVideoCommon();
        adVideoCommon.init(activity, layout);




    }

    public static Activity getActivity() {

        return sActivity;
    }

    public void setFrameLayout(FrameLayout layout) {
        framelayout = layout;

    }

    public void setGameSurfaceView(GLSurfaceView view) {
        gameSurfaceView = view;

    }

    public void setSplashId(final String strAppId, final String strAppKey) {
        strAppIdSplash = strAppId;
        strAppKeySplash = strAppKey;

    }


    public void startSplashAd() {
        //  adInsert.startSplashAd(strAppIdSplash,strAppKeySplash);

    }

    private static void adSplash_start() {
        //  pthis.startSplashAd();

    }

    
    static void InitAdConfigKey(AdConfigBase config ,int type,String appKey)
    { 
        if(type==AdConfig.SOURCE_TYPE_SPLASH){ 
            config.appKeySplash= appKey;
        }
        if(type==AdConfig.SOURCE_TYPE_BANNER){
            config.appKeyBanner= appKey;
        }
        if(type==AdConfig.SOURCE_TYPE_INSERT){
            config.appKeyInsert= appKey;
        }
        if(type==AdConfig.SOURCE_TYPE_SPLASH_INSERT){
            config.appKeySplashInsert= appKey;
        }
        if(type==AdConfig.SOURCE_TYPE_NATIVE){
            config.appKeyNative= appKey;
        }
        if(type==AdConfig.SOURCE_TYPE_VIDEO){
            config.appKeyVideo= appKey;
        }
        if(type==AdConfig.SOURCE_TYPE_INSERT_VIDEO){
            Log.d(TAG,"appKeyInsertVideo="+appKey);
            config.appKeyInsertVideo= appKey;
        }

    }
    public static void InitPlatform(final String source,int type,final String appId,final String appKey,final String adKey)
    {
        AdConfigBase config = null;
        if(Common.isBlankString(appKey)){
            return;
        }
        if(source.equals(Source.ADMOB))
        {
             config = AdConfigAdmob.main(); 
        }
 
         if(source.equals(Source.XIAOMI))
        {
             config = AdConfigXiaomi.main(); 
           
        }
         if(source.equals(Source.GDT))
        {
             config = AdConfigGdt.main();
            
        }

        if(source.equals(Source.UNITY))
        {
             config = AdConfigUnity.main(); 
        }
        if(source.equals(Source.ADVIEW))
        {
             config = AdConfigAdView.main(); 
        }

        if(source.equals(Source.MobVista))
        {
             config = AdConfigMobVista.main(); 
        }
        if(source.equals(Source.VUNGLE))
        {
            config = AdConfigVungle.main();
        }


        if(config!=null){
            config.appId = appId; 
            config.appKey = appKey;
            InitAdConfigKey(config,type,adKey);
            config.onInit(sActivity.getApplicationContext(), appId,appKey);
        }
        
        
    }

    public static void SetEnableAdSplash(boolean enable) {
        String pkey = "key_enable_splash";
        SharedPreferences prefs = sActivity.getSharedPreferences(
                "cocos2dx_app", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(pkey, enable);
        editor.commit();
    }

    public static void SetNoAd() {
        String pkey = "key_app_no_ad";
        SharedPreferences prefs = sActivity.getSharedPreferences(
                "cocos2dx_app", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(pkey, true);
        editor.commit();
    }

    public static void SetNoAdDay(int day) {
        String pkey = "key_no_ad_day";
        SharedPreferences prefs = sActivity.getSharedPreferences(
                "cocos2dx_app", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(pkey, day);
        editor.commit();
    }

    public static void AdSplash_SetConfig(final String strSource, final String strAppId, final String strAppKey) {
        String pkey_splash_souce = "key_splash_souce";
        String pkey_splash_appid = "key_splash_appid";
        String pkey_splash_appkey = "key_splash_appkey";
        SharedPreferences prefs = sActivity.getSharedPreferences(
                "cocos2dx_app", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(pkey_splash_souce, strSource);
        editor.putString(pkey_splash_appid, strAppId);
        editor.putString(pkey_splash_appkey, strAppKey);
        editor.commit();


    }


}
