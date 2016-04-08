package com.cedarwood.ademo;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.cedarwood.ademo.utils.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wentongmen on 2016/2/23.
 */
public class DemoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        Log.showLog(true);

//        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));

    }




    public static class CrashHandler implements Thread.UncaughtExceptionHandler {

        private final String TAG = CrashHandler.class.getSimpleName();

        private String DIR_FIRST = "ademo";
        private String DIR_SECOND = "";
        private String DIR_THIRD = "";


        private Context context;
        public CrashHandler(Context context){
            this.context=context;
        }


        @Override
        public void uncaughtException(Thread thread, Throwable ex) {

//            MobclickAgent.reportError(context,ex);

            if(!Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)){
                return;
            }


            byte[] info = null;

            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                PrintStream ps = new PrintStream(baos);

                ex.printStackTrace(ps);

                info = baos.toByteArray();
                baos.flush();
                baos.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            //把系统异常存放到本地
//          String rkDic = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"sohu"+File.separator+"focus"+File.separator+"EditingAssistant";
//			String rkDic =  context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            String rkDic = Environment.getExternalStorageDirectory().getAbsolutePath();
            if(!TextUtils.isEmpty(DIR_FIRST)){
                rkDic += File.separator+DIR_FIRST;
            }
            if(!TextUtils.isEmpty(DIR_SECOND)){
                rkDic += File.separator+DIR_SECOND;
            }
            if(!TextUtils.isEmpty(DIR_THIRD)){
                rkDic += File.separator+DIR_THIRD;
            }

            File dicFile = new File(rkDic);
            if (!dicFile.exists()) {
                dicFile.mkdirs();
            }

            if (!dicFile.exists()) {
                return;
            }

            String errLogPath = rkDic +File.separator +"errlog.txt";
            File errLogFile = new File(errLogPath);

            if (!errLogFile.exists()) {
                try {
                    errLogFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                String newLine = System.getProperty("line.separator");
                FileOutputStream fos = new FileOutputStream(errLogFile,true);
                fos.write((new SimpleDateFormat().format(new Date())+newLine).getBytes());
                fos.write(info);
                fos.flush();
                fos.close();

            } catch (Exception e) {
            }

            try {
                System.exit(0);
            } catch (Exception e) {
            }

            Log.i(TAG, "Time: " + new Date());

        }

    }



}
