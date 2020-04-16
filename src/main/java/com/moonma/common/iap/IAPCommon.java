package com.moonma.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.unity3d.player.UnityPlayer;

import java.io.File;


//import com.moonma.common.IAPXiaomi;
import com.moonma.common.IAPGoogle;
import com.moonma.common.IIAPBase;
import com.moonma.common.IIAPBaseListener;


/**
 * Created by jaykie on 16/5/24.
 */
public class IAPCommon implements IIAPBaseListener

{
    public static final String CALLBACK_BUY_DID_FINISH = "BuyDidFinish";//购买成功
    public static final String CALLBACK_BUY_DID_FAIL = "BuyDidFail";//购买失败
    public static final String CALLBACK_DID_BUY = "DidBuy";//已经购买项目
    public static final String CALLBACK_BUY_DID_RESTORE = "BuyDidRestore";//恢复购买

    private static final String TAG = IAPCommon.class.getSimpleName();

    private static Activity sActivity = null;
    private static IAPCommon pthis;

    private boolean isAdBannerInit = false;

    private static boolean sInited = false;
    FrameLayout framelayout;
    IIAPBase iapBase;

    private  String strSource;
    private static String strProduct;
    private static boolean isConsumeProduct;
    private static String strAppKey;

    private static String unityObjName;
    private static String unityObjMethod;

    public  void init(final Activity activity) {

        pthis = this;
        sActivity = activity;
    }

    public void createIAP(String source) {
      if(source.equals(Source.XIAOMI))
        {
//            iapBase = new IAPXiaomi();
        }

        if(source.equals(Source.GOOGLE)||(source.equals(Source.GP)))
        {
          iapBase = new IAPGoogle();
        }

        if(iapBase!=null){
            iapBase.init(sActivity);
            iapBase.setListener(this);
        }

    }

    public static void SetSource(String source)
    {
        pthis.strSource = source;
        pthis.createIAP(pthis.strSource);
    }

    public static void SetObjectInfo(String objName, String objMethod)
{
    unityObjName = objName;
    unityObjMethod = objMethod;
}

    public static void SetAppKey(String key)
    {
        strAppKey = key;
        if (strAppKey.equals("0")) {
            return;
        }
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pthis.SetAppKey_nonstatic(pthis.strAppKey);
            }
        });
    }

    private void SetAppKey_nonstatic(final String key)
    {
        if(iapBase!=null){
            iapBase.SetAppKey(key);
        }
    }

    public static void StartBuy(String product, boolean isConsume)
{
    strProduct = product;
    isConsumeProduct = isConsume;
    sActivity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
            pthis.startBuy_nonstatic(pthis.strProduct,pthis.isConsumeProduct);
        }
    });

}


    private void startBuy_nonstatic(final String product, boolean isConsume)
    {

        if(iapBase!=null){
            iapBase.StartBuy(product,isConsume);
        }
    }

    public static void RestoreBuy(String product)
{
    strProduct = product;
    sActivity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
            pthis.restoreBuy_nonstatic(pthis.strProduct);
        }
    });
}

    private void restoreBuy_nonstatic(final String product)
    {

        if(iapBase!=null){
            iapBase.RestoreBuy(product);
        }
    }


    @Override
    public void onBuyDidFinish() {
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UnityPlayer.UnitySendMessage(unityObjName,unityObjMethod,CALLBACK_BUY_DID_FINISH);
            }
        });
    }

    @Override
    public void onBuyDidFail() {
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UnityPlayer.UnitySendMessage(unityObjName,unityObjMethod,CALLBACK_BUY_DID_FAIL);
            }
        });
    }
    @Override
    public void onBuyDidBuy() {
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UnityPlayer.UnitySendMessage(unityObjName, unityObjMethod, CALLBACK_DID_BUY);
            }
        });
    }

    @Override
    public void onBuyDidRestore() {
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UnityPlayer.UnitySendMessage(unityObjName,unityObjMethod,CALLBACK_BUY_DID_RESTORE);
            }
        });


    }



}
