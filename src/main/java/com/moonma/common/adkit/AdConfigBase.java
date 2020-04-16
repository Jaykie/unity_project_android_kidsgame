package com.moonma.common;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.InputStream;


import java.io.File;
import com.moonma.common.FileUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by jaykie on 16/5/24.
 */
public class AdConfigBase { 
    public  String appId;
    public  String appKey;
    public  String appKeyBanner;
    public  String appKeyInsert;
    public  String appKeySplash;
    public  String appKeySplashInsert;
    public  String appKeyNative;
    public  String appKeyVideo;
    public  String appKeyInsertVideo;
    public  void  onInit(Context context, String appId, String appKey)
    {

    }


}
