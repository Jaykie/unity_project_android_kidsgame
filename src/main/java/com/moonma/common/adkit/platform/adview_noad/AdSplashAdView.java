package com.moonma.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.moonma.common.IAdSplashBase;

/**
 * Created by jaykie on 2017/5/4.
 */

public class AdSplashAdView implements IAdSplashBase {

    private static String TAG = "AdSplash";

    FrameLayout framelayout;
    Activity mainActivity; 
    FrameLayout frameLayoutAd;
 

    public void init(Activity activity, FrameLayout layout) {
        mainActivity = activity;
        framelayout = layout;

    }

    public void showAdSplash() {
        

    }

}
