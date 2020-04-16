package com.moonma.common;

import android.app.Activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.moonma.common.IAdBannerBase;
import com.moonma.common.IAdBannerBaseListener;
import com.moonma.common.AdConfigGdt;

import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;

import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.comm.util.AdError;

import org.json.JSONObject;

import java.util.Locale;

public class AdBannerGdt implements IAdBannerBase, UnifiedBannerADListener {

    // 自定义单一平台广告视图对象
    // private AdView adView;
    UnifiedBannerView bv;
    private static String TAG = "AdBanner";

    FrameLayout framelayout;
    Activity mainActivity;
    private boolean sIsShow;
    private int bannerOffsety;
    static int heightAd;
    int widthAd;
    int heigtAdMin = 8;
    float heightRatioMin = 0.15f;
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

    }

    public void setAd() {
        if (isAdInit == false) {
            isAdInit = true;

            String strAppId = AdConfigGdt.main().appId;
            String strAppKey = AdConfigGdt.main().appKeyBanner;

            Log.i(TAG, "banner id=" + strAppId + " key=" + strAppKey);

            // this.bv = new BannerView(mainActivity, ADSize.BANNER, strAppId, strAppKey);

            // 2.0：
            this.bv = new UnifiedBannerView(mainActivity, strAppId, strAppKey, this);

            // 注意：如果开发者的banner不是始终展示在屏幕中的话，请关闭自动刷新，否则将导致曝光率过低。
            // 并且应该自行处理：当banner广告区域出现在屏幕后，再手动loadAD。
            // bv.setRefresh(30);

            // 将adView添加到父控件中(注：该父控件不一定为您的根控件，只要该控件能通过addView能添加广告视图即可)
            RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            // rllp.addRule(RelativeLayout.CENTER_HORIZONTAL);

            // FrameLayout
            ViewGroup.LayoutParams framelayout_params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            if (Device.isLandscape()) {
                int screen_h = framelayout.getHeight();
                // 横屏 显示面积过大 缩小一些
                int h_adbanner = (int) (heightRatioMin * screen_h);
                framelayout_params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, h_adbanner);
            }
            framelayoutAd = new FrameLayout(mainActivity);
            framelayoutAd.setLayoutParams(framelayout_params);

            framelayoutAd.addView(bv);
            framelayout.addView(framelayoutAd);

            // adView.setVisibility(View.GONE);
            // framelayoutAd.setVisibility(View.GONE);

            bv.loadAD();
        }

    }

    public void setAlpha(float alpha) {

        bannerAlhpha = alpha;
        if (mainActivity == null) {
            return;
        }
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bannerAlhpha < 1.0) {
                    // bannerAlhpha =0;
                }
                framelayoutAd.setAlpha(bannerAlhpha);

            }
        });

    }

    private void doCloseBanner() {
        framelayoutAd.removeAllViews();
        if (bv != null) {
            bv.destroy();
            bv = null;
        }
    }

    public void show(boolean isShow) {
        sIsShow = isShow;

        if (mainActivity == null) {
            return;
        }

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (bv == null) {
                    return;
                }
                if (sIsShow) {

                    // bv.loadAD();
                    bv.setVisibility(View.VISIBLE);
                    framelayoutAd.setVisibility(View.VISIBLE);

                } else {
                    bv.setVisibility(View.INVISIBLE);
                    framelayoutAd.setVisibility(View.INVISIBLE);
                    // framelayoutAd.removeAllViews();
                    // doCloseBanner();
                }
            }
        });

    }

    public void layoutSubView(int w, int h) {
        // setOffsetY(bannerOffsety);

    }

    public void setOffsetYInternal() {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int screen_w = framelayout.getWidth();
                int screen_h = framelayout.getHeight();
                int h_adbanner = heightAd;
                if (h_adbanner < heigtAdMin) {// 0 ,1,126
                    // 显示到屏幕外
                    return;
                }
                // h = 128;
                float ratio_h = h_adbanner * 1.0f / screen_h;
                float scale = 1f;
                if ((ratio_h > heightRatioMin) && (screen_w > screen_h)) {
                    // 横屏 显示面积过大 缩小一些
                    ratio_h = heightRatioMin;
                    // h_adbanner = (int)(ratio_h*screen_h);
                    scale = h_adbanner * 1.0f / heightAd;
                    // framelayoutAd.setScaleX(scale);
                    // framelayoutAd.setScaleY(scale);
                }
                Log.i(TAG, "setOffsetYInternal h_adbanner=" + h_adbanner + " scale=" + scale + " heightAd=" + heightAd);
                int x = (screen_w - widthAd) / 2;
                int oft_bottom = com.moonma.common.ScreenUtil.getBottomNavigationBarHeight();
                if (Device.isLandscape()) {
                    // 华为全面屏 底部导航栏 横屏时候现在在右边
                    oft_bottom = 0;
                }
                int y = screen_h - h_adbanner - bannerOffsety
                        - oft_bottom;

                // y = 128;
                // framelayoutAd.setX(x);
                framelayoutAd.setY(y);

                bv.setY(0);
                // bv.bringToFront();
                // framelayoutAd.bringToFront();

                if (adBannerBaseListener != null) {
                    adBannerBaseListener.onReceiveAd(widthAd, h_adbanner);
                }
            }
        });

    }

    public void setOffsetY(int y) {

        bannerOffsety = y;
        //
        //
        if ((bv == null) || (framelayout == null) || (mainActivity == null)) {
            return;
        }
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bv == null) {
                    return;
                }
                if (framelayoutAd.getVisibility() != View.VISIBLE) {
                    return;
                }
                widthAd = bv.getWidth();
                // heightAd = bv.getHeight();

                if (heightAd <= heigtAdMin) {
                    // 显示到屏幕外 解决banner可能不显示的问题
                    heightAd = 126;
                }
                setOffsetYInternal();

            }

        });

        ViewTreeObserver vto2 = bv.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // bv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                widthAd = bv.getWidth();
                int htmp = bv.getHeight();
                if (htmp > heigtAdMin) {
                    // 显示到屏幕外 解决banner可能不显示的问题
                    int heightAdPre = heightAd;
                    heightAd = htmp;

                    bv.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                    // 定时更新
                    // Handler hd = new Handler();
                    // Runnable r = new Runnable() {
                    // @Override
                    // public void run() {
                    // setOffsetYInternal();
                    // }
                    // };
                    //
                    // hd.postDelayed(r, 3000);//ms
                    // setOffsetYInternal();
                    // 重新加载
                    if (heightAdPre != heightAd) {
                        bv.loadAD();
                    }
                }
            }
        });

    }

    public void setListener(IAdBannerBaseListener listener) {
        adBannerBaseListener = listener;

    }

    public interface OnAdBannerListener {

        void onReceiveAd(int w, int h);
    }

    @Override
    public void onNoAD(AdError adError) {
        String msg = String.format(Locale.getDefault(), "onNoAD, error code: %d, error msg: %s", adError.getErrorCode(),
                adError.getErrorMsg());
        Log.i(TAG, msg);
        // Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        if (adBannerBaseListener != null) {
            adBannerBaseListener.onLoadAdFail();
        }
    }

    @Override
    public void onADReceive() {
        Log.i(TAG, "onADReceive");
        setOffsetY(bannerOffsety);
    }

    @Override
    public void onADExposure() {
        Log.i(TAG, "onADExposure");
        // setOffsetYInternal();
    }

    @Override
    public void onADClosed() {
        Log.i(TAG, "onADClosed");
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
    public void onADOpenOverlay() {
        Log.i(TAG, "onADOpenOverlay");
    }

    @Override
    public void onADCloseOverlay() {
        Log.i(TAG, "onADCloseOverlay");
    }
}
