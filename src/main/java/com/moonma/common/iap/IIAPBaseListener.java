package com.moonma.common;

/**
 * Created by jaykie on 16/5/27.
 */
public interface IIAPBaseListener {
    public void onBuyDidFinish();
    public void onBuyDidFail();
    public void onBuyDidBuy();
    public void onBuyDidRestore();

}
