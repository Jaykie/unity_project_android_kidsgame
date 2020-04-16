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

import com.moonma.common.IAdInsertBase;
import com.moonma.common.IAdInsertBaseListener;
import com.moonma.common.AdConfigVungle;
 

public class AdInsertVungle implements IAdInsertBase  {

    private   String TAG = this.getClass().getSimpleName();
 
    private IAdInsertBaseListener adInsertBaseListener;

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

        if (adInsertBaseListener != null) {
            adInsertBaseListener.adInsertDidFail();

        }

    }

     public void setListener(IAdInsertBaseListener listener) {
        adInsertBaseListener = listener;
    }

 

}
