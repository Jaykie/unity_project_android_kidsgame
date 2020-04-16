package com.moonma.unity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.moonma.common.ChannelUtil;
import com.moonma.common.CommonAd;
import com.moonma.common.CommonUtils;
import com.moonma.common.FileUtil;
import com.moonma.common.IAPCommon;
import com.moonma.common.ImageUtil;
import com.moonma.common.MyApplication;
import com.moonma.common.PermissionManager;
import com.moonma.common.Share;
import com.moonma.tts.TTSBaidu;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends UnityPlayerActivity  implements ViewTreeObserver.OnGlobalLayoutListener{//com.moonma.kidsgame.

    //@moon
 //

    private static String TAG = "UnityPlayerActivity";
    private static MainActivity sActivity = null;
    CommonAd commonAd;
    CommonUtils commonUtils;
    ImageUtil imageUtil;
    FileUtil fileUtil;
    TTSBaidu ttsBaidu;
    //IAdSplashBase adSplash;
    Share share;
    IAPCommon iapCommon;

    RelativeLayout layout;
    ImageView imageViewStartUp;

    PermissionManager pmger;
    private boolean DOUBLECLICK_EXIT = true;
    boolean isGameRun;
    //@moon
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

//@moon
        isGameRun = false;
        mUnityPlayer.getViewTreeObserver().addOnGlobalLayoutListener(this);
        // 如果targetSDKVersion >= 23，就要申请好权限。如果您的App没有适配到Android6.0（即targetSDKVersion < 23），那么只需要在这里直接调用fetchSplashAD接口。
        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermission();
        }
       RunApp();

        //@moon
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        if(pmger!=null)
        {
            pmger.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }

      void RunApp() {
        MyApplication.main().setMainActivity(this);
      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        // 如果api >= 23 需要显式申请权限
//	        if (Build.VERSION.SDK_INT >= 23) {
//	            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
//	                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//	                    || ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
//	                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
//	                        Manifest.permission.READ_PHONE_STATE, Manifest.permission.INTERNET}, 0);
//	            }
//	        }
        //@moon
        sActivity = this;

          pmger = PermissionManager.Main();
          pmger.Init(this);

        commonAd = new CommonAd();
        commonAd.init(this, mUnityPlayer);


        String strAdSplashSource = "";

        String pkey_splash_souce = "key_splash_souce";
        SharedPreferences prefs = this.getSharedPreferences("cocos2dx_app", Context.MODE_PRIVATE);

        //strAdSplashSource = prefs.getString(pkey_splash_souce, "0");
        // adSplash = new AdSplashXiaomi();
        // adSplash.init(this, mUnityPlayer);


          imageUtil = new ImageUtil();
          imageUtil.init(this);

        commonUtils = new CommonUtils();
        commonUtils.init(this);

        fileUtil = new FileUtil();
        fileUtil.init(this);


        ttsBaidu = new TTSBaidu();
        ttsBaidu.init(this);


        //统计频道
        String strPackage = getPackageName();
        String strTmp = strPackage.substring(4);//"com."
        String strTmp1 = strTmp.substring(strTmp.indexOf('.') + 1);
        String channelName = ChannelUtil.getChannel(this);
        if (isBlankString(channelName)) {
            //默认设置为小米
            channelName = "xiaomi";
        }
        commonUtils.setChannelName(channelName);
        String strChannel = strTmp1 + "_" + channelName;
        if (strChannel != null) {
            // AnalyticsConfig.setChannel(strChannel);
        }
        String appkey = getUmengAppKey();
        UMConfigure.init(this, appkey, strChannel, UMConfigure.DEVICE_TYPE_PHONE, null);


        //share
        share = new Share();
        share.init(this);

        //IAP

        iapCommon = new IAPCommon();
        iapCommon.init(this);

        for (int i = 0; i < mUnityPlayer.getChildCount(); i++) {
            View child = mUnityPlayer.getChildAt(i);
            if (child instanceof SurfaceView) {
                SurfaceView game = (SurfaceView) child;
                //@moon 解决游戏遮挡在他上面的其他view的问题，如banner等
                game.setZOrderMediaOverlay(true);
            }

        }

        setStartUpLogo();

        //@moon

    }


    //@moon


    /**
     * ----------非常重要----------
     * <p>
     * Android6.0以上的权限适配简单示例：
     * <p>
     * 如果targetSDKVersion >= 23，那么必须要申请到所需要的权限，再调用广点通SDK，否则广点通SDK不会工作。
     * <p>
     * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
     * 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用广点通SDK即可。
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (lackedPermission.size() != 0) {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }


    public String getUmengAppKey() {
        String appkey = "";
        String jsonFile = "ConfigData/config/config_android.json";
        String json = fileUtil.ReadStringAsset(jsonFile);
        try {
            JSONObject object = new JSONObject(json);
            appkey = object.getString("APPTONGJI_ID");
            Log.v(TAG, "json appkey tongji:" + appkey);
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG, "json appkey tongji error");
        }

        if (isBlankString(appkey)) {
            try {
                ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                Bundle bundle = ai.metaData;
                appkey = bundle.getString("UMENG_APPKEY");
                Log.v(TAG, "xml appkey tongji:" + appkey);

            } catch (Exception e) {
                Log.v(TAG, "xml appkey tongji error");
                e.printStackTrace();
            }
        }

        Log.v(TAG, "last appkey tongji = " + appkey);

        return appkey;
    }

    public boolean isBlankString(String str) {

        if (str == null) {
            //为null;
            return true;
        }
        if (str.length() == 0) {
            //为空字符串;
            return true;
        }
        if (str.equals("")) {
            //为空字符串;
            return true;
        }

        return false;
    }


    public static void unityStartUpFinish() {
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                sActivity.unityStartUpFinishOnMainThread();
            }
        });

    }

    private void unityStartUpFinishOnMainThread() {
        isGameRun = true;
        if(imageViewStartUp!=null){
            imageViewStartUp.setVisibility(View.GONE);
        }
    }

    void setStartUpLogo() {
        imageViewStartUp = new ImageView(this);
        // imageView.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT));
        //imageView.setImageResource(R.drawable.app_icon);

        AssetManager assetManager = null;
        assetManager = this.getAssets();
        int image_w = 0;
        int image_h = 0;
        try {
            String fileName = "GameData/startup";
            String filePath = fileName + ".png";
            if (!FileUtil.FileIsExistAsset(filePath)) {
                filePath = fileName + ".jpg";
            }
            InputStream in = assetManager.open(filePath);
            Bitmap bmp = BitmapFactory.decodeStream(in);
            image_w = bmp.getWidth();
            image_h = bmp.getHeight();
            imageViewStartUp.setImageBitmap(bmp);
            in.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        float scale = 1.0f;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;

        //          image_w = imageView.getWidth();
        //          image_h = imageView.getHeight();
        float scale_x = width * 1.0f / image_w;
        float scale_y = height * 1.0f / image_h;
        scale = Math.max(scale_x, scale_y);
        int disp_w = (int) (image_w * scale);
        int disp_h = (int) (image_h * scale);
        // imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //imageView.setScaleX(scale_x);
        //imageView.setScaleY(scale_y);
        //   imageView.set
        //imageView.setMaxHeight(disp_h);
        //imageView.setMaxWidth(disp_w);
        //FrameLayout  layoutImage = new FrameLayout(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(disp_w, disp_h);
        int x, y;
        x = (width - disp_w) / 2;
        y = (height - disp_h) / 2;
        imageViewStartUp.setX(x);
        imageViewStartUp.setY(y);
        //imageView.setLayoutParams(params);
        mUnityPlayer.addView(imageViewStartUp, params);
        //mUnityPlayer.addView(imageView);
        // mUnityPlayer.addView(layoutImage);
    }

    void OnSysImageLibDidOpenFinish(String file)
    {
//        Bitmap bmp = imageUtil.DecodeImage(file);
//        ImageUtil.dataRGB = ImageUtil.GetRGBDataOfBmp(bmp);
        final String file_f = file;
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UnityPlayer.UnitySendMessage(ImageUtil.unityObjName, ImageUtil.unityObjMethod, file_f);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImageUtil.REQUEST_CODE_IMAGE && resultCode == RESULT_OK) {
            Uri mPath = data.getData();
           final String file = ImageUtil.getPath(mPath);
           // Bitmap bmp = imageUtil.DecodeImage(file);
//            if (bmp == null || bmp.getWidth() <= 0 || bmp.getHeight() <= 0) {
//                Log.e(TAG, "error");
//            } else {
//                Log.i(TAG, "bmp [" + bmp.getWidth() + "," + bmp.getHeight());
//            }
          //  /storage/emulated/0/Pictures/1552563657100.jpg
            OnSysImageLibDidOpenFinish(file);
        }
        if (requestCode == ImageUtil.REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            //Uri mPath = ((MyApplication) (this.getApplicationContext())).getCaptureImage();
           Uri mPath = ImageUtil.imageCamera;
            String file = ImageUtil.getPath(mPath);
//            Bitmap bmp = ImageUtil.DecodeImage(file);
            OnSysImageLibDidOpenFinish(file);
        }
    }

    //@moon
    // Pause Unity
    @Override
    protected void onPause() {
        super.onPause();
        //@moon
        MobclickAgent.onPause(this);
    }

    // Resume Unity
    @Override
    protected void onResume() {
        super.onResume();
        //@moon
      MobclickAgent.onResume(this);
    }

    @Override
    public void onGlobalLayout() {
        Log.d(TAG, "onGlobalLayout");
        onNavigationBarStatusChanged();
    }

    protected void onNavigationBarStatusChanged() { // 子类重写该方法，实现自己的逻辑即可。
        if (!isGameRun) return;
        UnityPlayer.UnitySendMessage("Scene", "OnAndroidGlobalLayout", "");
    }

        //@moon
    /**
     * 双击退出函数
     */
    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (DOUBLECLICK_EXIT) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (System.currentTimeMillis() - firstTime > 2000) {
                   // Toast.makeText(mContext, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = System.currentTimeMillis();
                } else {
                    finish();
                    System.exit(0);
                    return true;
                }

                //这里会拦截KEYCODE_BACK 单次点击 所以不能return
                //return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    //@moon
}
