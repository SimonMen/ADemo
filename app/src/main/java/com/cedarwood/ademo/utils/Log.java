package com.cedarwood.ademo.utils;

public class Log {

	private static boolean openState = true; //发布版本的时候，请置为false
    private final static boolean logFlag=true; //显示哪种log
	private final static String TAG = "log";

    public final static int V=1;
    public final static int D=2;
    public final static int I=3;
    public final static int W=4;
    public final static int E=5;

    private static int level=V;

    public static void setLevel(int l){
        level=l;
    }

    public static void showLog(boolean s){
        openState=s;
    }

	public static void d(String subTag, String info){
		if (!openState || level>=D) {
			return;
		}
        if(logFlag){
            android.util.Log.d(TAG, subTag + ":" + info);
        }else{
            android.util.Log.d(subTag, subTag + ":" + info);
        }


	}
	
	public static void i(String subTag, String info){
		if (!openState || level>=I) {
			return;
		}
        if(logFlag){
            android.util.Log.i(TAG, subTag + ":" + info);
        }else{
            android.util.Log.i(subTag, subTag + ":" + info);
        }
	}
	
	public static void w(String subTag, String info){
		if (!openState || level>=W) {
			return;
		}
        if(logFlag){
            android.util.Log.w(TAG, subTag + ":" + info);
        }else{
            android.util.Log.w(subTag, subTag + ":" + info);
        }
	}
	
	public static void e(String subTag, String info){
		if (!openState || level>=E) {
			return;
		}
        if(logFlag){
            android.util.Log.e(TAG, subTag + ":" + info);
        }else{
            android.util.Log.e(subTag, subTag + ":" + info);
        }
	}
	
	public static void v(String subTag, String info){
		if (!openState || level>=V) {
			return;
		}
        if(logFlag){
            android.util.Log.v(TAG, subTag + ":" + info);
        }else{
            android.util.Log.v(subTag, subTag + ":" + info);
        }
	}
	
}
