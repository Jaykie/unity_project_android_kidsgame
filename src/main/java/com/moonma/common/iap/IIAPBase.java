package com.moonma.common;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import com.moonma.common.IIAPBaseListener;

/**
 * Created by jaykie on 16/5/27.
 */
public interface IIAPBase {
    public void setListener(IIAPBaseListener listener);
    public void init(Activity activity);
    public  void StartBuy(String product, boolean isConsume);
    public  void RestoreBuy(String product);
    public  void SetAppKey(String key);
}
