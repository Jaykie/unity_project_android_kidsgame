package com.moonma.common;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.moonma.common.IAdInsertBaseListener;

/**
 * Created by jaykie on 16/5/27.
 */
public interface IAdInsertBase {

    public void init(final Activity activity,FrameLayout layout);
    public void setListener(IAdInsertBaseListener listener);
    public void setAd();
    public void show();
}
