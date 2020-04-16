package com.moonma.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.FrameLayout;

import java.io.File;

/**
 * Created by jaykie on 16/5/24.
 */
public class CommonUtils  {
    private static final String TAG = CommonUtils.class.getSimpleName();

    private static Activity sActivity = null;
    private static CommonUtils pthis;
    private static String sStrUrl;
    private static String strChannelName;
    private static boolean sInited = false;
    FrameLayout framelayout;



    private static int UIORIENTATION_ALL= 0;//all 任意方向
    private static int UIORIENTATION_PortraitUp=1;
    private static int UIORIENTATION_PortraitDown=2;
    private static int UIORIENTATION_LandscapeLeft=3;
    private static int UIORIENTATION_LandscapeRight=4;
    private static int UIORIENTATION_Portrait=5;
    private static int UIORIENTATION_Landscape=6;
    private static int orientaionActivity;

    private boolean isShowStatusBar;

    public  void init(final Activity activity)
    {
        sActivity = activity;
        pthis = this;
    }

    public static Activity getActivity() {

        return sActivity;
    }
    public   void setFrameLayout(FrameLayout layout)
    {
        framelayout = layout;

    }

    public  void setChannelName(String name)
    {
        strChannelName = name;
    }
    public static String getChannelName()
    {
        return strChannelName;
    }


    public static float getPathMemorySizeTotal_GB(String path) {
        long total = pthis.getPathMemorySizeTotal(path);
        float total_f = total;
        float ret = total_f / 1024 / 1024 / 1024;
        return ret;
    }

    //获取总的容量
    public  long getPathMemorySizeTotal(String path) {
        //File path = Environment.getDataDirectory();
        File file = new File(path);
        if (!file.exists()){
            return 0;
        }
        StatFs stat = new StatFs(path);

        //文件系统的块的大小（byte）
        long blockSize1 = stat.getBlockSize();
        //文件系统的总的块数
        long totalBlocks1 = stat.getBlockCount();
        //文件系统上空闲的可用于程序的存储块数
        long availableBlocks1 = stat.getAvailableBlocks();

        //总的容量
        long totalSize1 = blockSize1 * totalBlocks1;
        long availableSize1 = blockSize1 * availableBlocks1;

        // String totalStr1 = Formatter.formatFileSize(Cocos2dxActivity.cocos2dxActivity, totalSize1);
        //  String availableStr1 = Formatter.formatFileSize(Cocos2dxActivity.cocos2dxActivity, availableSize1);

        // internalTv.setText("内部存储的大小"+"\n"+"总大小："+totalStr1+"\n"+"可用大小："+availableStr1)
        return totalSize1;

    }

    public static float getPathMemorySizeAvailable_MB(String path) {
        long total = pthis.getPathMemorySizeAvailable(path);
        float total_f = total;
        float ret = total_f / 1024 / 1024;
        return ret;
    }
    //获取可用的容量
    public  long getPathMemorySizeAvailable(String path) {
        //File path = Environment.getDataDirectory();
        File file = new File(path);
        if (!file.exists()){
            return 0;
        }
        StatFs stat = new StatFs(path);

        //文件系统的块的大小（byte）
        long blockSize1 = stat.getBlockSize();
        //文件系统的总的块数
        long totalBlocks1 = stat.getBlockCount();
        //文件系统上空闲的可用于程序的存储块数
        long availableBlocks1 = stat.getAvailableBlocks();

        //总的容量
        long totalSize1 = blockSize1 * totalBlocks1;
        long availableSize1 = blockSize1 * availableBlocks1;
//        availableSize1 = stat.getFreeBytes();
//
//        long test = stat.getAvailableBytes();

        // String totalStr1 = Formatter.formatFileSize(Cocos2dxActivity.cocos2dxActivity, totalSize1);
        //  String availableStr1 = Formatter.formatFileSize(Cocos2dxActivity.cocos2dxActivity, availableSize1);

        // internalTv.setText("内部存储的大小"+"\n"+"总大小："+totalStr1+"\n"+"可用大小："+availableStr1)
        return availableSize1;

    }

    public static boolean isSDCardExist() {
        // return false;

        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
        // sdCardExist = false;

        return sdCardExist;

    }

    public static String getSDCardPath() {
        File sdDir = null;
        boolean sdCardExist = isSDCardExist();   //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        if (sdDir!=null){
            return sdDir.toString();
        }
        return "";

    }


    /*

    getCacheDir()方法用于获取/data/data/<application package>/cache目录
getFilesDir()方法用于获取/data/data/<application package>/files目录

     */
    public static String getDownloadPath()//String key
    {
        String ret = "1.0";
        ret = Environment.getDataDirectory().toString() + Environment.getDownloadCacheDirectory().toString();
        // getApplicationContext().getFilesDir().getAbsolutePath();
        ret = sActivity.getFilesDir().getAbsolutePath();

        return ret;
    }

    public static String getCachePath()//String key
    {
        String ret = "1.0";
        ret = Environment.getRootDirectory().toString() + Environment.getDownloadCacheDirectory().toString();
        ret = sActivity.getCacheDir().getAbsolutePath();

        return ret;
    }


    public static String getPackage()//String key
    {
        String ret = "1.0";
        ret = sActivity.getPackageName();
        return ret;
    }


    public static String getAppName()//String key
    {
        String ret = "1.0";

        PackageManager pm = sActivity.getPackageManager();
        String packageName = sActivity.getPackageName();


        try {

            ret = pm.getApplicationLabel(

                    pm.getApplicationInfo(packageName,

                            PackageManager.GET_META_DATA)).toString();

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

        }

        return ret;
    }



    public static String getAppVersion()//String key
    {
        String ret = "1.0.0";
        // String pkName = this.getPackageName();
        //  String versionName =  this.getPackageManager().getPackageInfo(pkName,0).versionName;

        //  String versionName = this.getPackageManager().getPackageInfo(
        //      pkName, 0).versionName;


        try {
            PackageManager manager = sActivity.getPackageManager();
            PackageInfo info = manager.getPackageInfo(sActivity.getPackageName(), 0);
            ret = info.versionName;

        } catch (Exception e) {
            e.printStackTrace();

        }

        return ret;
    }




    public static void exitapp()//String key
    {
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sActivity.finish();
            }
        });
    }


    public static void SetIpInChina(boolean isIn)
    {

    }


    public static void showOrientation(boolean isHeng)
    {

        if(isHeng) {
            setOrientation(UIORIENTATION_Landscape);
            //sActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);//SCREEN_ORIENTATION_LANDSCAPE
        }else{
            setOrientation(UIORIENTATION_Portrait);
            //sActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);//SCREEN_ORIENTATION_PORTRAIT
        }
    }


    public  void setOrientationOnMainthread (int orientaion)
    {

//
//
        if(UIORIENTATION_ALL == orientaion)
        {
            sActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
        if(UIORIENTATION_Portrait == orientaion)
        {
            sActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }

        if(UIORIENTATION_PortraitUp == orientaion)
        {
            sActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }

        if(UIORIENTATION_PortraitDown == orientaion)
        {
            sActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }


        if(UIORIENTATION_Landscape == orientaion)
        {
            sActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }

        if(UIORIENTATION_LandscapeLeft == orientaion)
        {
            sActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }


        if(UIORIENTATION_LandscapeRight == orientaion)
        {
            sActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
    }
    public static void setOrientation (int orientaion)
    {

        pthis.orientaionActivity = orientaion;
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pthis.setOrientationOnMainthread(pthis.orientaionActivity);
            }
        });

    }


    public static int getHeightOfStatusBar()
    {
        int ret = 0;
//        Rect frame = new Rect();
//        sActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//        ret = frame.top;
        return ret;
    }

    public  void showStatusBar_nonstatic(boolean isShow)
    {
                if(!isShow)
        {
            sActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            sActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
        else
        {
            sActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            sActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
    public static void showStatusBar(boolean isShow)
    {

        pthis.isShowStatusBar = isShow;

        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pthis.showStatusBar_nonstatic(pthis.isShowStatusBar);
            }
        });


    }

    public static boolean isStatusBarShow()
    {
        boolean ret = false;
        WindowManager.LayoutParams attrs = sActivity.getWindow().getAttributes();

        if((attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN){
            ret = false;
        }else{
            ret = true;
        }

        return ret;
    }


    private static void openurl(final String strUrl )
    {



       pthis.sStrUrl = strUrl;

        sActivity.runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {
                pthis.openurl_nonstatic( pthis.sStrUrl);
            }
        } );


    }

    private   void openurl_nonstatic(final String strUrl ) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(strUrl));
        intent.setAction(Intent.ACTION_VIEW);
        sActivity.startActivity(intent); //启动浏览器
    }



}
