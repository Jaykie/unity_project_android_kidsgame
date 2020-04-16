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

import com.moonma.common.IAdInsertBase;
import com.moonma.common.IAdInsertBaseListener; 
 
public class AdInsertXiaomi implements IAdInsertBase {

    // 自定义单一平台广告视图对象
    // private AdView adView;

    private static String TAG = "AdInsert";

    public boolean isUseActivity = false;// true
    boolean isAdInit;
    FrameLayout framelayout;
    Activity mainActivity;
    private boolean sIsShow;
    private int bannerOffsety;
    private float bannerAlhpha;
    String strAdAppId;
    // InterstitialAd interAd;

    private IAdInsertBaseListener adInsertBaseListener;

    public void init(Activity activity, FrameLayout layout) {
        mainActivity = activity;
        framelayout = layout;
        isAdInit = false;
    }

    public void setAd() {
  
    }

    public void show() {

        if (adInsertBaseListener != null) {
            adInsertBaseListener.adInsertDidFail();

        }
    } 
 

    public void setListener(IAdInsertBaseListener listener) {
        adInsertBaseListener = listener;
    }
 
}
