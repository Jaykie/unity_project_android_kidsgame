package com.moonma.common;

import android.app.Activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

import com.moonma.common.IAdVideoBase;
import com.moonma.common.IAdVideoBaseListener;
import com.moonma.common.AdConfigVungle;
 

public class AdVideoVungle implements IAdVideoBase  {
 private   String TAG = this.getClass().getSimpleName();
 
    private IAdVideoBaseListener adVideoBaseListener;

    public   void init(  Activity activity,FrameLayout layout)
    {
       
    }

    public void setType(int type)
    {
       
    }
     public void setAd()
    { 
    }

    public void show() {
        if (adVideoBaseListener != null) {
            adVideoBaseListener.adVideoDidFail();
        }
    }

    public void setListener(IAdVideoBaseListener listener)
    {
        adVideoBaseListener = listener;
    }

 

}
