package com.cedarwood.ademo.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.cedarwood.ademo.view.ChoiceDialog;
import com.cedarwood.ademo.view.SimpleToast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by wentongmen on 2016/2/23.
 */
public class CommonUtil {

    /**
     * dp sp px 转换
     */
    // dp转换成pix
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue / scale + 0.5f);
    }

    //暂不用
    private static float getScale(Context context) {
        float widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        return widthPixels / 320;
    }

    //暂不用
    private static int getScreenType(Context context) {
        float height = context.getResources().getDisplayMetrics().heightPixels;
        if (height >= 800) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将输入float值转为dip值
     */
    public static float valueToDp(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                context.getResources().getDisplayMetrics());
    }

    /**
     * screen
     */

    /**
     * 得到设备宽度
     *
     * @param context
     * @return 当前设备屏幕宽度
     */
    public static int getServiceScreenWidth(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        if (context != null) {
            metrics = context.getResources().getDisplayMetrics();
            return metrics.widthPixels;
        }
        return 480;
    }

    /**
     * 得到设备宽度
     *
     * @param context
     * @return 当前设备屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        if (context != null) {
            ((Activity) context).getWindowManager().getDefaultDisplay()
                    .getMetrics(metrics);
            return metrics.widthPixels;
        }
        return 480;
    }

    /**
     * 得到设备高度
     *
     * @param context
     * @return 设备高度
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        if (context != null) {
            ((Activity) context).getWindowManager().getDefaultDisplay()
                    .getMetrics(metrics);
            return metrics.heightPixels;
        }
        return 800;
    }


    /**
     * system
     */
    public static String getPackageVersion(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return info.versionName;
    }

    public static String getImei(Context context) {
        String imei = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imei;
    }

    public static String getMetaValueStr(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return apiKey;
    }

    public static boolean getMetaValueBoo(Context context, String metaKey) {
        Bundle metaData = null;
        boolean apiKey = false;
        if (context == null || metaKey == null) {
            return false;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getBoolean(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return apiKey;
    }


    /**
     * net
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static boolean isWifiOnLine(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * md5
     */
    public static String string2MD5(String inStr) {
        StringBuilder sb = new StringBuilder();
        try {
            byte[] md5ByteArray = MessageDigest.getInstance("MD5").digest(inStr.getBytes("utf-8"));
            for (int i = 0; i < md5ByteArray.length; i++) {
                sb.append(String.format("%02x", md5ByteArray[i]));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return sb.toString();
    }


    /**
     * string
     */
    public static boolean notEmpty(String str) {
        return (str != null && !str.equals("") && !str.equals("null") && !str.equals("　") && !str
                .equals(" "));
    }

    public static boolean isEmpty(String str) {
        return str == null || str.equals("") || str.equals("　") || str.equals(" ")
                || str.equals("null");
    }

    /**计算中英文个数
     * @param s 需要校验的字符串
     * @param limit 需要校验的字符串的长度的最大值
     * @return  true表示指定的字符串长度超出最大值
     */
    public static boolean computCharsNum(CharSequence s,int limit){
        try {
            String context = s.toString();
            byte[] contextByte = context.getBytes("GBK");
            if (!TextUtils.isEmpty(context) && contextByte.length > limit) {
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }


    /**
     * 判断是否含有emoji表情
     * @param source
     * @return
     */
    public static boolean hasEmojiCharacter(String source){

        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }

        return false;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    //字符串src中find的个数
    public static int getCharCount(String src,String find){

        int i = 0;
        int index=-1;
        while((index=src.indexOf(find,index))>-1){
            ++index;
            ++i;
        }
        return i;
    }

    //输入流转字符串
    public static String inputStream2String(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is, "utf-8"));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }


    /**
     * File
     */
    public static ArrayList<File> getPhotoFiles(String path){
        ArrayList<File> fileArrayList = new ArrayList<File>();
        File rootFile = new File(path);
        if(!rootFile.exists()||!rootFile.isDirectory()) return fileArrayList;

        File[] files = rootFile.listFiles();
        if(files!=null){
            fileArrayList.addAll(Arrays.asList(files));
        }
        return fileArrayList;
    }

    /**
     * 移动文件到本地目录之中
     */
    public static String moveFileToLocalDir(final File file,String localDir) {
        if (file == null || !file.exists()) {
            return "";
        }

        try {
            final File failedFile = new File(localDir + File.separator + file.getName());
            if(!failedFile.getParentFile().exists()){
                failedFile.getParentFile().mkdirs();
            }
            failedFile.createNewFile();

            FileInputStream input = null;
            FileOutputStream output = null;
            try {
                input = new FileInputStream(file);
                output = new FileOutputStream(failedFile);
                byte[] buffer = new byte[1024];
                int start = 0;
                int len = 1024;
                while ((len = input.read(buffer)) != -1) {
                    output.write(buffer);
                }
            } catch (Exception e) {
//                e.printStackTrace();
                return "";
            } finally {
                try {
                    if (input != null) input.close();
                    if (input != null) output.close();
                } catch (Exception e) {
                }

            }

            file.delete();

            return failedFile.getAbsolutePath();

        } catch (Exception e) {
            return "";
        }
    }



    /**
     * 子线程回调
     */
    public static void runInBackground(final Callback callback) {


        new AsyncTask<Void, Object, Object>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (callback != null) {
                    callback.callBefore();
                }
            }


            @Override
            protected Object doInBackground(Void... params) {
                Object obj = callback.callInBackground();
                return obj;
            }

            protected void onPostExecute(Object obj) {
                if (callback != null) {
                    callback.callAfter(obj);
                }
            }

            ;

        }.execute();
    }

    public abstract class Callback {
        public abstract void callAfter(Object obj);

        public abstract Object callInBackground();

        public void callBefore() {};
    }

    /**
     * dialog
     */
    public static void showDialog(Context context,String description, Runnable sureCallback) {
        showDialog(context,null,description,null,null,sureCallback,null,0);
    }

    public static void showDialog(Context context,String title,CharSequence description,String sure,String cancel, final Runnable sureCallback,final Runnable cancelCallback,int mp) {
        if(TextUtils.isEmpty(title)){
            title="提示";
        }

        if(TextUtils.isEmpty(sure)){
            sure="确定";
        }
        if(TextUtils.isEmpty(cancel)){
            cancel="取消";
        }

        ChoiceDialog dialog = new ChoiceDialog.Builder(context).setTitle(title).setMessage(description)
                .setNegativeButton(cancel, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(cancelCallback!=null){
                            cancelCallback.run();
                        }

                    }
                }).setPositiveButton(sure, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(sureCallback!=null){
                            sureCallback.run();
                        }

                    }
                }).setCancelable(false).setMessagePosition(mp).create();

        if (context instanceof Service) {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (CommonUtil.getServiceScreenWidth(context)); // 设置宽度
        dialog.getWindow().setAttributes(lp);

        dialog.show();
    }

    /**
     * toast
     */
    private static Toast getSimpleToast(Context context,int textId,int duration) {
        return SimpleToast.getSimpleToast(context, -1, context.getResources().getString(textId), duration);
    }

    private static Toast getSimpleToast(Context context,String text,int duration) {
        return SimpleToast.getSimpleToast(context, -1, text, duration);
    }



}