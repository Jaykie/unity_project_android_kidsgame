package com.moonma.common;
import android.content.Context;
import android.util.Log;

import com.moonma.common.AdConfigBase; 

/**
 * Created by jaykie on 16/5/24.
 */
public class AdConfigVungle extends AdConfigBase{

    private  String TAG = "AdConfigVungle";
    static boolean isInitedSDK = false;

    private static AdConfigVungle _mian = null;
    public static AdConfigVungle main() {
        if(_mian==null){
            _mian = new AdConfigVungle();
        }
        return _mian;
    }

    public void onInit(Context context, String appId, String appKey)
    {
      
    }
}



