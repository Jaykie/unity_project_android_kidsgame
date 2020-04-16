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

public class AdBannerXiaomi implements IAdBannerBase {

    // 自定义单一平台广告视图对象
    private static String TAG = "AdBanner";

    FrameLayout framelayout;
    Activity mainActivity;
    private boolean sIsShow;
    private int bannerOffsety;
    private float bannerAlhpha; 
    // AdView adView;
    FrameLayout framelayoutAd;
    IAdBannerBaseListener adBannerBaseListener;
    boolean isAdInit;

    public void init(Activity activity, FrameLayout layout) {
        mainActivity = activity;
        framelayout = layout;
        isAdInit = false;
        sIsShow = false;
        bannerOffsety = 0;

    }

    public void setAd() {

        if (adBannerBaseListener != null) {
            adBannerBaseListener.onLoadAdFail();
            return;
        }

    }

    public void setAlpha(float alpha) {

    }

    public void show(boolean isShow) {

    }

    public void layoutSubView(int w, int h) {
        setOffsetY(bannerOffsety);

    }

    public void setOffsetY(int y) {

        bannerOffsety = y;
        //

    }

    public void setListener(IAdBannerBaseListener listener) {
        adBannerBaseListener = listener;
    }

}
