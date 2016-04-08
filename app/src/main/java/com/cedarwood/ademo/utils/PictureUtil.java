package com.cedarwood.ademo.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by men on 2015/1/5.
 */
public class PictureUtil {

    private static final String TAG = PictureUtil.class.getSimpleName();

    /**
     * 缩放图片
     * @param imagePath
     * @param width
     * @param height
     * @return
     */
    public Bitmap getNewImage(String imagePath, int width, int height) {

        if(TextUtils.isEmpty(imagePath)){
            return null;
        }
        if(!new File(imagePath).exists()){
            return null;
        }

        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        //newbitmap = bitmap.copy(bitmap.getConfig(), false);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        Log.v("getImageThumbnail", "be>>>" + be);
        options.inSampleSize = be;
        Bitmap newbitmap = null;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        try {
			newbitmap = BitmapFactory.decodeFile(imagePath, options);
            return newbitmap;
        } catch (OutOfMemoryError e) {
            if(newbitmap!=null) {
                newbitmap.recycle();
            }
            e.printStackTrace();
        } catch(Exception e){
            if(newbitmap!=null){
                newbitmap.recycle();
            }
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 获取图片缩略图
     * @param imagePath
     * @param width
     * @param height
     * @return
     */
    public Bitmap getThumbnailImage(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        //newbitmap = bitmap.copy(bitmap.getConfig(), false);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        Log.v("getImageThumbnail", "be>>>" + be);
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        try {
            bitmap = BitmapFactory.decodeFile(imagePath, options);
//			bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(Uri.parse(imagePath)), null, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//		bitmap.recycle();
        return bitmap;
    }

    public Bitmap getNewImage(Bitmap bitmap, int width, int height) {
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newBitmap;
    }

    /**
     * 图片的旋转处理
     */
    private Bitmap getRotateImage(Bitmap bitmap, int degrees){
        if (bitmap == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		bitmap.recycle();
        return rotateBitmap;
    }

    public Bitmap getRotateImage(Bitmap bitmap, final File fileWithExifInfo) {
        if (bitmap == null) {
            return null;
        }
        Bitmap rotatedBitmap = bitmap;
        try {
            int orientation = getImageOrientation(fileWithExifInfo.getAbsolutePath());
            Log.d(TAG, "rotateImage orientation>>>>" + orientation);
            if (orientation != 0) {
                return getRotateImage(bitmap,orientation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotatedBitmap;
    }

    public int getImageOrientation(final String file) throws IOException {
        ExifInterface exif = new ExifInterface(file);
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return 0;
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;
        }
    }



    /**
     * 图片uri 转换为 Bitmap
     *
     * @param  uri
     * @return bitmap
     */
    public Bitmap decodeUriAsBitmap(Context context,Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
    /**
     * 图片path 转换为 Bitmap
     *
     * @param
     * @return bitmap
     */
    public Bitmap decodeUriAsBitmap(Context context,String path) {
        Bitmap bitmap = null;
        Uri uri = Uri.parse(path.replace("file://", ""));
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }



    /***************************************************************/

    // And to convert the image URI to the direct file system path of the image file
    public String getRealPathFromURI(Activity act, Uri contentUri) {

        // can post image
        String[] proj={MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = act.managedQuery( contentUri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }


    @SuppressWarnings("deprecation")
    public Uri changeContentUri(Context context,Uri uri) {
        Log.d("PictureUtils", "changeContentUri uri is " + uri);
        if (null == uri) {
            return null;
        }
        if (uri.getScheme().equals("file")) {
            String path = uri.getEncodedPath();
            Log.d("PictureUtils", "changeContentUri path1 is " + path);
            if (path != null) {
                path = Uri.decode(path);
                Log.d("PictureUtils", "changeContentUri path2 is " + path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[] { MediaStore.Images.ImageColumns._ID },
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    Log.d("PictureUtils", "changeContentUri uri_temp is " + uri_temp);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
                cur.close(); // close
            }
        }
        return uri;
    }


    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


}
