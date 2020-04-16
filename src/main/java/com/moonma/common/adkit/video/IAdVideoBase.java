package com.moonma.common;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.moonma.common.IAdVideoBaseListener;

/**
 * Created by jaykie on 16/5/27.
 */
public interface IAdVideoBase {
    public  int ADVIDEO_TYPE_INSERT = 0;
    public  int ADVIDEO_TYPE_REWARD = 1;
    public void init(final Activity activity,FrameLayout layout);
    public void setListener(IAdVideoBaseListener listener);
    public void setAd();
    public void show();
    public void setType(int type);
}
