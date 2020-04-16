package com.moonma.common;

import android.app.Activity;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;

public class Device {
    static private final String TAG = "Device";

    static public Size getScreenSize() {
        Activity ac = Common.getMainActivity();
        DisplayMetrics dm = new DisplayMetrics();
        ac.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return new Size(dm.widthPixels, dm.heightPixels);
    }

    static public boolean isLandscape() {
        Size sz = getScreenSize();
        if (sz.getWidth() > sz.getHeight()) {
            return true;
        }
        return false;

    }

    public static boolean isEmulator() {
//MODEL=BKL-AL20 FINGERPRINT= MANUFACTURER=HUAWEI BRAND=HUAWEI
        Log.d(TAG, "MODEL=" + Build.MODEL + " FINGERPRINT=" + " MANUFACTURER=" + Build.MANUFACTURER + " BRAND=" + Build.BRAND);
        //  return false;

        return (Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("BKL-AL20")
                || Build.MODEL.contains("Android SDK built for x86"));

//
//        return Build.FINGERPRINT.startsWith("generic")
//                || Build.FINGERPRINT.toLowerCase().contains("vbox")
//                || Build.FINGERPRINT.toLowerCase().contains("test-keys")
//                || Build.MODEL.contains("google_sdk")
//                || Build.MODEL.contains("Emulator")
//                || Build.MODEL.contains("BKL-AL20")
//                || Build.MODEL.contains("Android SDK built for x86")
//                || Build.MANUFACTURER.contains("Genymotion")
//                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
//                || "google_sdk".equals(Build.PRODUCT);
    }
}
