package com.moonma.common;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import com.moonma.common.IAdBannerBaseListener;

/**
 * Created by jaykie on 16/5/27.
 */
public interface IAdBannerBase {

    public void init(final Activity activity,FrameLayout layout);
    public void setListener(IAdBannerBaseListener listener);
    public void setAd();
    public void setAlpha(float alpha);
    public void show(boolean isShow);
    public void layoutSubView(int w,int h );
    public void setOffsetY(int y);
}
