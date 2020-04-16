package com.moonma.common;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;

import java.util.ArrayList;
import java.util.List;



public class PermissionManager {
    static private final String TAG = "PermissionManager";
    public static int PERMISSION_REQ = 0x123456;

    private String[] mPermission = new String[] {
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private List<String> mRequestPermission = new ArrayList<String>();

    Activity acMain;
    public IPermissionManagerDelegate iDelegate;

    public interface IPermissionManagerDelegate {
        public void OnRequestPermissionDidFinish();
    }

    private static PermissionManager _mian = null;
    public static PermissionManager Main() {
        if(_mian==null){
            _mian = new PermissionManager();
        }
        return _mian;
    }
    public  void Init(final Activity ac ) {
        acMain = ac;
    }
      public void RequestPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            for (String one : mPermission) {
                if (PackageManager.PERMISSION_GRANTED != acMain.checkPermission(one, Process.myPid(), Process.myUid())) {
                    mRequestPermission.add(one);
                }
            }
            if (!mRequestPermission.isEmpty()) {
                acMain.requestPermissions(mRequestPermission.toArray(new String[mRequestPermission.size()]), PERMISSION_REQ);
                return ;
            }
        }
          OnRequestFinish();
    }

      void OnRequestFinish()
    {
    if(iDelegate!=null){
        iDelegate.OnRequestPermissionDidFinish();
    }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        // 版本兼容
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            return;
        }
        if (requestCode == PERMISSION_REQ) {
            for (int i = 0; i < grantResults.length; i++) {
                for (String one : mPermission) {
                    if (permissions[i].equals(one) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        mRequestPermission.remove(one);
                    }
                }
            }
           // RunApp();
            OnRequestFinish();
        }
    }


}
