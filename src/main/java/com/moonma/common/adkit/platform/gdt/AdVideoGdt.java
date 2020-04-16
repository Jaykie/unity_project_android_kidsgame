package com.moonma.common;

import android.app.Activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

import com.moonma.common.IAdVideoBase;
import com.moonma.common.IAdVideoBaseListener;
import com.moonma.common.AdConfigGdt;

import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.comm.util.AdError;

import java.util.Date;
import java.util.Locale;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class AdVideoGdt implements IAdVideoBase, RewardVideoADListener {

  private static String TAG = "AdVideo";

  private RewardVideoAD rewardVideoAD;
  private boolean adLoaded;// 广告加载成功标志
  private boolean videoCached;// 视频素材文件下载完成标志

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

    if (isAdInit == false) {
      isAdInit = true;
      String strAppId = AdConfigGdt.main().appId;
      String strAppKey = AdConfigGdt.main().appKeyVideo;
      // 1. 初始化激励视频广告
      rewardVideoAD = new RewardVideoAD(mainActivity, strAppId, strAppKey, this);
      adLoaded = false;
      videoCached = false;
    }

    // 2. 加载激励视频广告
   // rewardVideoAD.loadAD();
  }

  public void show() {
    if ( rewardVideoAD != null) {
      rewardVideoAD.loadAD();
    }
  }

  public void showInternal() {
    // 3. 展示激励视频广告
    if (adLoaded && rewardVideoAD != null) {// 广告展示检查1：广告成功加载，此处也可以使用videoCached来实现视频预加载完成后再展示激励视频广告的逻辑
      if (!rewardVideoAD.hasShown()) {// 广告展示检查2：当前广告数据还没有展示过
        long delta = 1000;// 建议给广告过期时间加个buffer，单位ms，这里demo采用1000ms的buffer
        // 广告展示检查3：展示广告前判断广告数据未过期
        if (SystemClock.elapsedRealtime() < (rewardVideoAD.getExpireTimestamp() - delta)) {
          rewardVideoAD.showAD();
        } else {
          // Toast.makeText(this, "激励视频广告已过期，请再次请求广告后进行广告展示！", Toast.LENGTH_LONG).show();
        }
      } else {
        // Toast.makeText(this, "此条广告已经展示过，请再次请求广告后进行广告展示！", Toast.LENGTH_LONG).show();
      }
    }

  }

  public void setListener(IAdVideoBaseListener listener) {
    adVideoBaseListener = listener;
  }

  /**
   * 广告加载成功，可在此回调后进行广告展示
   **/
  @Override
  public void onADLoad() {
    adLoaded = true;
    String msg = "load ad success ! expireTime = "
        + new Date(System.currentTimeMillis() + rewardVideoAD.getExpireTimestamp() - SystemClock.elapsedRealtime());
    // Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    showInternal();
  }

  /**
   * 视频素材缓存成功，可在此回调后进行广告展示
   */
  @Override
  public void onVideoCached() {
    videoCached = true;
    Log.i(TAG, "onVideoCached");
  }

  /**
   * 激励视频广告页面展示
   */
  @Override
  public void onADShow() {
    Log.i(TAG, "onADShow");
    if (adVideoBaseListener != null) {
      adVideoBaseListener.adVideoDidStart();
    }
  }

  /**
   * 激励视频广告曝光
   */
  @Override
  public void onADExpose() {
    Log.i(TAG, "onADExpose");
  }

  /**
   * 激励视频触发激励（观看视频大于一定时长或者视频播放完毕）
   */
  @Override
  public void onReward() {
    Log.i(TAG, "onReward");
  }

  /**
   * 激励视频广告被点击
   */
  @Override
  public void onADClick() {
    Log.i(TAG, "onADClick");
  }

  /**
   * 激励视频播放完毕
   */
  @Override
  public void onVideoComplete() {
    Log.i(TAG, "onVideoComplete");
    if (adVideoBaseListener != null) {
      adVideoBaseListener.adVideoDidFinish();
    }
  }

  /**
   * 激励视频广告被关闭
   */
  @Override
  public void onADClose() {
    Log.i(TAG, "onADClose");
  }

  /**
   * 广告流程出错
   */
  @Override
  public void onError(AdError adError) {
    String msg = String.format(Locale.getDefault(), "onError, error code: %d, error msg: %s", adError.getErrorCode(),
        adError.getErrorMsg());
    // Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    if (adVideoBaseListener != null) {
      adVideoBaseListener.adVideoDidFail();
    }
  }

}
