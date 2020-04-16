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
public class AdConfig {
    private static final String TAG = AdConfig.class.getSimpleName();

    public static final int SOURCE_TYPE_SPLASH = 0;
    public static final int SOURCE_TYPE_BANNER = 1;
    public static final int SOURCE_TYPE_INSERT = 2;
    public static final int SOURCE_TYPE_SPLASH_INSERT = 3;//开机插屏
    public static final int SOURCE_TYPE_NATIVE = 4;
    public static final int SOURCE_TYPE_VIDEO = 5;
    public static final int SOURCE_TYPE_INSERT_VIDEO = 6;

    Context sActivity = null; 
    private static AdConfig _mian = null;
    JSONObject jsonRoot;
    JSONArray jsonPlatform;
    FileUtil fileUtil;
    public static AdConfig Main(Context ctx) {
        if(_mian==null){
            _mian = new AdConfig(); 
            _mian.Init(ctx);
        }
        return _mian;
    }

    void Init(Context ac )
    {
        sActivity = ac;
        fileUtil = new FileUtil();
        fileUtil.init(ac);
        LoadJson();
    }
    void LoadJson()
    {
        boolean isHd = false;
        if(sActivity!=null){
            isHd = Common.isAppForPad(sActivity);
        }
        
        String jsonFile = "GameData/adconfig/ad_config_android.json";
        if(isHd){
            jsonFile = "GameData/adconfig/ad_config_android_hd.json";
        }
        Log.v(TAG, "LoadJson jsonFile="+jsonFile);
        String json = fileUtil.ReadStringAsset(jsonFile);
        try {
            jsonRoot = new JSONObject(json);  
            jsonPlatform = jsonRoot.getJSONArray("platform");

        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG, "LoadJson fail");
        }
    }

   public String GetAppId(String src)
    { 
        return GetKey(src,"appid");
    }
    public String GetAppKey(String src)
    { 
        return GetKey(src,"appkey");
    }

    public String GetSplashId(String src)
    { 
        return GetKey(src,"key_splash");
    }

    String GetKey(String src,String key)
    {
        String str = "0";
        if(jsonPlatform==null){
            Log.v(TAG, "jsonPlatform is null");
            return str;
        }
        for (int i=0; i<jsonPlatform.length(); i++){

            try {
                JSONObject object = jsonPlatform.getJSONObject(i);
                String jsonsrc = object.getString("source");
                //Log.v(TAG, "jsonPlatform jsonsrc="+jsonsrc);
                if(jsonsrc.equals(src)){
                    str = object.getString(key);
                    Log.v(TAG, "jsonPlatform key_v="+str);
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.v(TAG, "json appkey tongji error");
            }


        }  
        return str;
    }

}
