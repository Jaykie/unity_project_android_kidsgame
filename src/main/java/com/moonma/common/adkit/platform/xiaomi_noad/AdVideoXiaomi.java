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
 
public class AdVideoXiaomi implements IAdVideoBase {

    private static String TAG = "AdVideo";

    String strAdAppId;
    boolean isAdInit;
    // FrameLayout framelayout;
    Activity mainActivity;
    private boolean sIsShow;
    private int bannerOffsety;
    private float bannerAlhpha;

    int adType;
    private IAdVideoBaseListener adVideoBaseListener;

    public void init(Activity activity, FrameLayout layout) {
        mainActivity = activity;
        // framelayout = layout;
        isAdInit = false;
    }

    public void setType(int type) {
        adType = type;
    }

    public void setAd() {
  
    }

    public void show() {
        if (adVideoBaseListener != null) {
            adVideoBaseListener.adVideoDidFail();
        }
    }

    public void setListener(IAdVideoBaseListener listener) {
        adVideoBaseListener = listener;
    } 

}
