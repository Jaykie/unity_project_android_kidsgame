package com.moonma.common;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;

import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;

//import com.guo.android_extend.image.ImageConverter;

public class ImageUtil implements PermissionManager.IPermissionManagerDelegate {
    static private final String TAG = "ImageUtil";

    public static final int REQUEST_CODE_CAMERA = 1;
    public static final int REQUEST_CODE_IMAGE = 2;
    public static final int REQUEST_CODE_OP = 3;

    public static String unityObjName;
    public static String unityObjMethod;

    static public byte [] dataRGB;
    private static int bmpWidth;
    private static int bmpHeigt;

    static public Uri imageCamera;

    private static ImageUtil pthis;
    private static Activity sActivity = null;

    int requestCode;
    public  void init(final Activity activity)
    {
        sActivity = activity;
        pthis = this;
    }
    /**
     * @param path
     * @return
     */
    public static Bitmap DecodeImage(String path) {
        Bitmap res;
        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            BitmapFactory.Options op = new BitmapFactory.Options();
            op.inSampleSize = 1;
            op.inJustDecodeBounds = false;
            //op.inMutable = true;
            //res = BitmapFactory.decodeFile(path, op);
            res = BitmapFactory.decodeFile(path);
            //rotate and scale.
            Matrix matrix = new Matrix();

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                matrix.postRotate(90);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                matrix.postRotate(180);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                matrix.postRotate(270);
            }if (orientation == ExifInterface.ORIENTATION_UNDEFINED) {
                matrix.postRotate(180);
            }
            if(orientation == ExifInterface.ORIENTATION_NORMAL){
               // return res;
            }
            Bitmap temp = Bitmap.createBitmap(res, 0, 0, res.getWidth(), res.getHeight(), matrix, true);
            Log.d("com.arcsoft", "check target Image:" + temp.getWidth() + "X" + temp.getHeight());

            if (!temp.equals(res)) {
                res.recycle();
            }
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void SetObjectInfo(String objName, String objMethod)
    {
        unityObjName = objName;
        unityObjMethod = objMethod;
    }

    static public void OpenSystemImageLib() {
        pthis.OpenSystemLib_notstatic(REQUEST_CODE_IMAGE);
    }
    static public void OpenSystemCameraApp() {
        pthis.OpenSystemLib_notstatic(REQUEST_CODE_CAMERA);
    }

      public void OpenSystemLib_notstatic(int code) {
        requestCode =code;
        PermissionManager pmger = PermissionManager.Main();
        pmger.iDelegate = this;
        pmger.RequestPermission();
    }

    public void OpenSystemImageLibInternal() {
        Activity ac = Common.getMainActivity();
        Intent getImageByalbum = new Intent(Intent.ACTION_GET_CONTENT);
        getImageByalbum.addCategory(Intent.CATEGORY_OPENABLE);
        //getImageByalbum.setType("image/jpeg");
        getImageByalbum.setType("image/*");
        ac.startActivityForResult(getImageByalbum, REQUEST_CODE_IMAGE);
    }

      public void OpenSystemCameraAppInternal() {
        Activity ac = Common.getMainActivity();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues(1);
       // values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
          values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
        Uri uri = ac.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //((Application)(MainActivity.this.getApplicationContext())).setCaptureImage(uri);
        imageCamera = uri;
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        ac.startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    //https://blog.csdn.net/ak4100/article/details/52933923
    //调用相机
    public void takephoto() {
        Activity ac = Common.getMainActivity();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
        ac.startActivityForResult(intent, REQUEST_CODE_CAMERA);

    }

    //调用相册
    public void OpenGallery() {
        Activity ac = Common.getMainActivity();
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        ac.startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }
    public void OnRequestPermissionDidFinish()
    {
        if(REQUEST_CODE_IMAGE==requestCode)
        {
            OpenSystemImageLibInternal();
        }
        if(REQUEST_CODE_CAMERA==requestCode)
        {
            OpenSystemCameraAppInternal();
        }
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri
     * @return
     */
    static public String getPath(Uri uri) {
        Activity ac = Common.getMainActivity();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            if (DocumentsContract.isDocumentUri(ac, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(ac, contentUri, null, null);
                } else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(ac, contentUri, selection, selectionArgs);
                }
            }
        }
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = ac.getContentResolver().query(uri, proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor.getString(actual_image_column_index);
        String end = img_path.substring(img_path.length() - 4);
        if (0 != end.compareToIgnoreCase(".jpg") && 0 != end.compareToIgnoreCase(".png")) {
            return null;
        }
        return img_path;
    }


    static public byte[] Bmp2Data(Bitmap bmp) {
        //    implementation 'com.guo.android_extend:android-extend:1.0.6'
        byte[] data = new byte[bmp.getWidth() * bmp.getHeight() * 2];//3 / 2
//        try {
//            ImageConverter convert = new ImageConverter();
//            convert.initial(bmp.getWidth(), bmp.getHeight(), ImageConverter.CP_RGBA8888);
//            if (convert.convert(bmp, data)) {
//                Log.d(TAG, "convert ok!");
//            }
//            convert.destroy();
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
        return data;
    }

    static public byte[]  GetRGBData() {
        return dataRGB;
    }

    static public int GetRGBDataHeight() {
        return bmpHeigt;
    }


    static public int GetRGBDataWidth() {
        return bmpWidth;
    }

    static public byte[] GetRGBDataOfBmp(Bitmap bmp) {
        try {
            int bytes = bmp.getByteCount();
            bmpWidth = bmp.getWidth();
            bmpHeigt = bmp.getHeight();

            int size = bmp.getWidth() * bmp.getHeight();
            int bit = bytes / size;
            ByteBuffer buf = ByteBuffer.allocate(bytes);
            bmp.copyPixelsToBuffer(buf);

            byte[] data = buf.array();
            return data;
        } catch (Exception e) {
            return null;
        }

    }

}
