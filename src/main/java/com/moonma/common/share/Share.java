package com.moonma.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.unity3d.player.UnityPlayer;

import java.io.File;

import com.umeng.social.tool.UMImageMark;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMEmoji;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.utils.SocializeUtils;
import com.unity3d.player.UnityPlayerActivity;

/**
 * Created by jaykie on 16/5/24.
 */
public class Share  {
    public final static String SOURCE_WEIXIN = "weixin";
    public final static String SOURCE_WEIXINFRIEND = "weixinfriend";
    public final static String SOURCE_QQ = "qq";
    public final static String SOURCE_QQZONE = "qqzone";
    public final static String SOURCE_WEIBO = "weibo";
    public final static String SOURCE_EMAIL = "email";
    public final static String SOURCE_SMS = "sms";

    private static final String TAG = Share.class.getSimpleName();

    private static Activity sActivity = null;
    private static Share pthis;


    private static boolean sInited = false;

    private static String unityObjName;
    private static String unityObjMethod;

     String strSource;
     String strAppId;
     String strAppKey;
    String strTitle;
    String strDetail;
    String strUrl;

    String strShareResult;


    public void init(final Activity activity) {

        pthis = this;
        sActivity = activity;
        UMShareAPI.get(sActivity);

    }


      SHARE_MEDIA getPlatform(final String source) {
        SHARE_MEDIA ret= SHARE_MEDIA.WEIXIN;
        if(source.equals(SOURCE_WEIXIN))
        {
            ret = SHARE_MEDIA.WEIXIN;
        }

        if(source.equals(SOURCE_WEIXINFRIEND))
        {
            ret = SHARE_MEDIA.WEIXIN_CIRCLE;
        }

        if(source.equals(SOURCE_QQ))
        {
            ret = SHARE_MEDIA.QQ;
        }

        if(source.equals(SOURCE_QQZONE))
        {
            ret = SHARE_MEDIA.QZONE;
        }
        if(source.equals(SOURCE_WEIBO))
        {
            ret = SHARE_MEDIA.SINA;
        }
        if(source.equals(SOURCE_EMAIL))
        {
            ret = SHARE_MEDIA.EMAIL;
        }
        if(source.equals(SOURCE_SMS))
        {
            ret = SHARE_MEDIA.SMS;
        }

        return ret;
    }
    public static void ShareInit(final String source,final String appId,final String appKey)
    {
        pthis.strSource = source;
        pthis.strAppId = appId;
        pthis.strAppKey = appKey;
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pthis.ShareInit_nonstatic(pthis.strSource, pthis.strAppId,pthis.strAppKey);
            }
        });
    }
    public  void ShareInit_nonstatic(final String source,final String appId,final String appKey)
    {

    }

    public static void SetObjectInfo(final String objName) {
        pthis.unityObjName = objName;
    }

    public static void InitPlatform(final String source,final String appId,final String appKey)
    {
        pthis.strSource = source;
        pthis.strAppId = appId;
        pthis.strAppKey = appKey;
        pthis.InitPlatform_nonstatic(pthis.strSource, pthis.strAppId,pthis.strAppKey);
    }
    public  void InitPlatform_nonstatic(final String source,final String appId,final String appKey)
    {
        if((source.equals(SOURCE_WEIXIN))||(source.equals(SOURCE_WEIXINFRIEND)))
        {
            PlatformConfig.setWeixin(appId,appKey);
        }
        if((source.equals(SOURCE_QQZONE))||(source.equals(SOURCE_QQ)))
        {
            PlatformConfig.setQQZone(appId,appKey);
        }
        if(source.equals(SOURCE_WEIBO))
        {
           // PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad","http://sns.whalecloud.com");
           PlatformConfig.setSinaWeibo(appId,appKey,"http://sns.whalecloud.com");
        }

//        如有任何错误，请开启debug模式:在设置各平台APPID的地方添加代码：Config.DEBUG = true
//        所有编译问题或者设置问题，请参考文档：https://at.umeng.com/0fqeCy?cid=476
        Config.DEBUG = true;
       // PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad","http://sns.whalecloud.com");
    }

    public static void ShareImage(final String source,final String pic,final String url)
    {

    }
    
    public static void ShareImageText(final String source,final String title,final String pic,final String url)
    {
        
    }

    public static void ShareWeb(final String source,final String title,final String detail,final String url)
    {
        pthis.strSource = source;
        pthis.strTitle = title;
        pthis.strDetail = detail;
        pthis.strUrl = url;
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pthis.ShareWeb_nonstatic(pthis.strSource, pthis.strTitle,pthis.strDetail,pthis.strUrl);
            }
        });
    }

    public  void ShareWeb_nonstatic(final String source,final String title,final String detail,final String url)
    {
        String UMS_THUMB_IMAGE = "https://mobile.umeng.com/images/pic/home/social/img-1.png";
        //@moon res_id = R.mipmap.ic_launcher
        //用代码获取资源id 解除R.java对包名的依赖
        int  res_id= sActivity.getResources().getIdentifier("ic_launcher", "mipmap", sActivity.getPackageName());
    Log.d(TAG,"res_id="+res_id+" title="+title+" detail="+detail+" url="+url);
        UMImage thumb = new UMImage(sActivity, res_id);
        //UMImage thumb = new UMImage(sActivity, UMS_THUMB_IMAGE);
        UMWeb  web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(detail);//描述


        new ShareAction(sActivity)
                .withMedia(web)
                .setPlatform(getPlatform(source))
                .setCallback(shareListener).share();
    }


    public void ShareDidFinish(String str) {
        Log.e(TAG,str);
        strShareResult = str;
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UnityPlayer.UnitySendMessage(unityObjName, "ShareDidFinish", strShareResult);
            }
        });

    }

    public void ShareDidFail(String str) {
        strShareResult = str;
        Log.e(TAG,str);
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UnityPlayer.UnitySendMessage(unityObjName, "ShareDidFail", strShareResult);
            }
        });

    }


    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {


        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            ShareDidFinish("成功了");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {

            ShareDidFail("失败:"+t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

           // Toast.makeText(ShareDetailActivity.this,"取消了", Toast.LENGTH_LONG).show();

        }
    };



}
