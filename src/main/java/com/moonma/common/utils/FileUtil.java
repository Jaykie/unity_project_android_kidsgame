package com.moonma.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.FrameLayout;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.moonma.common.CommonUtils;
import com.moonma.common.ImageUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * Created by jaykie on 16/5/24.
 */
public class FileUtil {
    private static final String TAG = CommonUtils.class.getSimpleName();

    private static Context sActivity = null;
    private static FileUtil pthis;
    private static boolean sInited = false;

    private boolean isShowStatusBar;

    private static int bmpWidth;
    private static int bmpHeigt;

    public void init(final Context activity) {
        sActivity = activity;
        pthis = this;
    }

    public static Context getActivity() {

        return sActivity;
    }


    static public String ReadStringAsset(String file) {
        byte[] data = ReadDataAsset(file);
        String ret = new String(data);
        return ret;
    }


    public ExifInterface GetExifInterface(InputStream inputStream) {
        ExifInterface exif = null;
        // exif = new ExifInterface();

        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream cannot be null");
        }
//        mFilename = null;
//        if (inputStream instanceof AssetManager.AssetInputStream) {
//            mAssetInputStream = (AssetManager.AssetInputStream) inputStream;
//            mSeekableFileDescriptor = null;
//        } else if (inputStream instanceof FileInputStream
//                && isSeekableFD(((FileInputStream) inputStream).getFD())) {
//            mAssetInputStream = null;
//            mSeekableFileDescriptor = ((FileInputStream) inputStream).getFD();
//        } else {
//            mAssetInputStream = null;
//            mSeekableFileDescriptor = null;
//        }
//        mIsInputStream = true;
//        loadAttributes(inputStream);
        return exif;
    }

    // 将byte[]转换成InputStream
    static  public InputStream Byte2InputStream(byte[] b) {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        return bais;
    }
    static public byte[] ReadRGBDataFromByte(byte[] data) {
        try {
            InputStream in =Byte2InputStream(data);
            return ReadRGBDataFromInputStream(in);
        } catch (Exception e) {
            return null;
        }
    }
    static public byte[] ReadRGBDataAsset(String file) {
        try {
            InputStream in = sActivity.getResources().getAssets().open(file);
            return ReadRGBDataFromInputStream(in);
        } catch (Exception e) {
            return null;
        }
    }

    static public byte[] ReadRGBDataFromInputStream(InputStream in) {
        if (sActivity == null) {
            return null;
        }
        try {
            Bitmap temp = BitmapFactory.decodeStream(in);

            int orientation = ExifInterface.ORIENTATION_ROTATE_180;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //Android 7
                ExifInterface exif = new ExifInterface(in);
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            }


            Matrix matrix = new Matrix();
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                matrix.postRotate(90);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                matrix.postRotate(180);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                matrix.postRotate(270);
            }else if (orientation == ExifInterface.ORIENTATION_UNDEFINED) {
                matrix.postRotate(180);
            }

            Bitmap bmp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), matrix, true);
            Log.d("com.arcsoft", "check target Image:" + temp.getWidth() + "X" + temp.getHeight());

            if (!temp.equals(bmp)) {
                temp.recycle();
            }

            int bytes = bmp.getByteCount();
            bmpWidth = bmp.getWidth();
            bmpHeigt = bmp.getHeight();

            int size = bmp.getWidth() * bmp.getHeight();
            int bit = bytes / size;
            ByteBuffer buf = ByteBuffer.allocate(bytes);
            bmp.copyPixelsToBuffer(buf);

            byte[] data = buf.array();

            // byte[] data = ImageUtil.Bmp2Data(bmp);
            return data;
        } catch (Exception e) {
            return null;
        }

    }

    static public int GetRGBDataHeight() {
        return bmpHeigt;
    }


    static public int GetRGBDataWidth() {
        return bmpWidth;
    }


    static public byte[] ReadDataAsset(String file) {
        if (sActivity == null) {
            return null;
        }

        AssetManager assetManager = null;
        assetManager = sActivity.getAssets();

        try {
            InputStream is = assetManager.open(file);
            if (is == null) {
                return null;
            }
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return buffer;
        } catch (Exception e) {
            return null;
        }

    }

    static public boolean FileIsExistAsset(String file) {
        if (sActivity == null) {
            return false;
        }
        AssetManager assetManager = null;
        assetManager = sActivity.getAssets();

        try {
            InputStream is = assetManager.open(file);
            if (is == null) {
                return false;
            }

            is.close();
            return true;
        } catch (Exception e) {
            return false;
        }

        // return  false;
    }


}
