package com.moonma.common;

/**
 * Created by jaykie on 16/5/27.
 */
public interface IAdBannerBaseListener {
    public void onReceiveAd(int w, int h);
    public void onLoadAdFail();
    public void onLoadAdClick();
}
