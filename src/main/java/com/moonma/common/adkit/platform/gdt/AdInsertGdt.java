package com.moonma.common;

import android.app.Activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
//import com.baidu.mobads.*;

import org.json.JSONObject;

import com.moonma.common.IAdInsertBase;
import com.moonma.common.IAdInsertBaseListener;
import com.moonma.common.AdConfigGdt;

import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.comm.util.AdError;

import java.util.Locale;

public class AdInsertGdt implements IAdInsertBase ,
        UnifiedInterstitialADListener, CompoundButton.OnCheckedChangeListener{

	// 自定义单一平台广告视图对象
	//private AdView adView;
    UnifiedInterstitialAD iad;
    private static String TAG = "AdInsert";

    public boolean isUseActivity = false;//true
    boolean isAdInit;
    FrameLayout framelayout;
    Activity mainActivity;
    private   boolean sIsShow;
    private   int bannerOffsety;
    private   float bannerAlhpha;

   // InterstitialAd interAd;

    private IAdInsertBaseListener adInsertBaseListener;

    public   void init(  Activity activity,FrameLayout layout)
    {
        mainActivity = activity;
        framelayout = layout;
        isAdInit = false;
    }


     public void setAd()
    {

        if(isAdInit==false)
        {
            isAdInit = true;
            String strAppId = AdConfigGdt.main().appId;
            String strAppKey = AdConfigGdt.main().appKeyInsert;
            Log.i(TAG, "insert id="+strAppId+ " key="+strAppKey);

            if (iad == null) {
                iad = new UnifiedInterstitialAD(mainActivity, strAppId, strAppKey, this);
            }


        }

    }
    
    public void show( )
    {


        mainActivity.runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {

                iad.loadAD();
                //iad.show();

            }
        } );
    }

    public void setListener(IAdInsertBaseListener listener)
    {
        adInsertBaseListener = listener;
    }

    public void startSplashAd(final String strAppId, final String strAppKey) {


    }



    public interface OnAdInsertListener {

        void adInsertWillShow();
        void adInsertDidClose();
    }



    @Override
    public void onADReceive() {

       // Toast.makeText(this, "广告加载成功 ！ ", Toast.LENGTH_LONG).show();
        Log.i(TAG, "onADReceive");
        iad.show();
    }

    @Override
    public void onNoAD(AdError error) {
       String msg = String.format(Locale.getDefault(), "onNoAD, error code: %d, error msg: %s",
               error.getErrorCode(), error.getErrorMsg());
        Log.i(TAG, msg);
     //   Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        if (adInsertBaseListener!=null){
            adInsertBaseListener.adInsertDidFail();

        }
    }

    @Override
    public void onADOpened() {
        Log.i(TAG, "onADOpened");
        if (adInsertBaseListener!=null){
            adInsertBaseListener.adInsertWillShow();
        }
    }

    @Override
    public void onADExposure() {
        Log.i(TAG, "onADExposure");
    }

    @Override
    public void onADClicked() {
        Log.i(TAG, "onADClicked");
    }

    @Override
    public void onADLeftApplication() {
        Log.i(TAG, "onADLeftApplication");
    }

    @Override
    public void onADClosed() {
        Log.i(TAG, "onADClosed");
        if (adInsertBaseListener!=null){
            adInsertBaseListener.adInsertDidClose();

        }
    }
    @Override
    public void onVideoCached() {
        // 视频素材加载完成，在此时调用iad.show()或iad.showAsPopupWindow()视频广告不会有进度条。
        Log.i(TAG, "onVideoCached");
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
           // ((EditText) findViewById(R.id.posId)).setText(Constants.UNIFIED_INTERSTITIAL_ID_LARGE_SMALL);
        } else {
          //  ((EditText) findViewById(R.id.posId)).setText(Constants.UNIFIED_INTERSTITIAL_ID_ONLY_SMALL);
        }
    }
}
