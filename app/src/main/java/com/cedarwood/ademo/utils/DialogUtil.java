package com.cedarwood.ademo.utils;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.cedarwood.ademo.R;

public class DialogUtil {

    private static final String TAG = DialogUtil.class.getSimpleName();
    private Context mContext;
	private DialogUtil(){};
	
	private static DialogUtil du;
	
	public static DialogUtil getInstance(){
		
		if(du==null){
			du=new DialogUtil();
		}
		return du;
	}
	private Dialog loadingDialog;
	private long startTime;
	
	
	public Dialog getSystemLoadingDialogShow(Context context){
		return getLoadingDialogShow(context,-1,null,true);
	}

	public Dialog getLoadingDialogShow(Context context){
		
		if(context instanceof Service){
			return getSystemLoadingDialogShow(context);
		}

		return getLoadingDialogShow(context,-1,null,false);
	}
	
	public Dialog getSystemLoadingDialogShow(Context context,String title){
		return getLoadingDialogShow(context,-1,title,true);
	}
	
	public Dialog getLoadingDialogShow(Context context,String title){
		
		if(context instanceof Service){
			return getSystemLoadingDialogShow(context,title);
		}
		
		return getLoadingDialogShow(context,-1,title,false);
	}
	
	
	/**
	 * 该方法用于显示一个loading的dialog，默认：原dialog的背景透明，采用view的背景;后退键不可撤销。如有需要可自行设置重载函数。
	 * @param context 上下文
	 * @param resId dialog样式的资源ID,如使用默认可传负数
	 * @return 返回该dialog，因为该dialog直接显示可以不用该返回值。如需自行处理则需要。
	 */
	public Dialog getLoadingDialogShow(Context context,int resId,String title,boolean system){
		if(loadingDialog!=null && loadingDialog.isShowing()){
			loadingDialog.dismiss();
			loadingDialog=null;
		}
		
		View view = null;
		if(resId<0){
			resId= R.layout.view_loading_dialog;
		}

		view=View.inflate(context, resId,null);
		
		TextView titleText=(TextView) view.findViewById(R.id.simple_dialog_text);
		if(!TextUtils.isEmpty(title)){
			titleText.setText(title);
		}

		loadingDialog=new Dialog(context, R.style.simple_dialog);
		loadingDialog.setContentView(view);
		loadingDialog.setCancelable(false);
		if(system){
			loadingDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		}
		
        try {
            loadingDialog.show();
        }catch (Exception e){
            loadingDialog=null;
            return null;
        }
		startTime=System.currentTimeMillis();

		return loadingDialog;
	}

	
	
	
	public void setLoadingDialogDismiss(){
		setLoadingDialogDismiss(null,-1,2000);
	}
	
	
	/**
	 * 该方法用于dismiss  LoadingDialog，并控制dialog显示的时长。
	 * @param dialog  如为null，则必须使用getLoadingDialogShow()方法获取dialog并显示。
	 * @param startTime dialog显示时的时间，如为负数，则同上。
	 * @param delayTime 显示时长。
	 */
	public void setLoadingDialogDismiss(Dialog dialog,long startTime,long delayTime){
		
		if(dialog==null && loadingDialog==null){
			return;
		}
		if(dialog==null && loadingDialog!=null){
			dialog=loadingDialog;
		}
		
		if(startTime<0){
			startTime=this.startTime;
		}
		
		if(dialog!=null && dialog.isShowing()) {
            long time = 0;
            long endTime = 0;
//			do{
//				endTime = System.currentTimeMillis();
//				time=endTime-startTime;
//			}while(time<2000);

            endTime = System.currentTimeMillis();
            time = endTime - startTime;
//            Log.i(TAG, "TIME :" + time);
//            final Dialog d = dialog;
//            if (time < delayTime)
//                SystemClock.sleep(delayTime - time);

            dialog.dismiss();


        }
		loadingDialog=null;
		this.startTime=0;
	}
	
	
	
	
//	public Dialog getLoadingDialog(Context context){
//		
//		View view = View.inflate(context, R.layout.ss_widget_loading_dialog,null);
//		final Dialog dialog=new Dialog(context, R.style.simple_dialog);
//		dialog.setContentView(view);
//		dialog.setCancelable(false);
//		
//		return  dialog;
//	}
	
	

	
//	public Dialog getSimpleDialog(Context context,String title,String text,String sure,String cancel){
//		return getSimpleDialog(context,title,text,sure,cancel,true);
//	}
//	
//	
//	public Dialog getSimpleDialog(Context context,String title,String text,String sure,String cancel,boolean showCancel){
//
//		View view = View.inflate(context, R.layout.ss_widget_simple_dialog,null);
//		TextView titelText = (TextView) view.findViewById(R.id.simple_dialog_title);
//		TextView contentText = (TextView) view.findViewById(R.id.simple_dialog_context);
//        TextView sureBtn = (TextView) view.findViewById(R.id.simple_dialog_sureBtn);
//        TextView cancleBtn = (TextView) view.findViewById(R.id.simple_dialog_cancleBtn);
//        LinearLayout sureBtnLayout = (LinearLayout) view.findViewById(R.id.simple_dialog_sureBtn_layout);
//        LinearLayout cancleBtnLayout = (LinearLayout) view.findViewById(R.id.simple_dialog_cancleBtn_layout);
//		
//		if(!TextUtils.isEmpty(title)){
//			titelText.setText(title);
//		}
//		
//		if(!TextUtils.isEmpty(text)){
//			contentText.setText(text);
//		}
//		if(!TextUtils.isEmpty(sure)){
//			sureBtn.setText(sure);
//		}
//		if(!TextUtils.isEmpty(cancel)){
//			cancleBtn.setText(cancel);
//		}
//		
//		if(!showCancel){
//			cancleBtn.setVisibility(View.GONE);
//		}
//		
//		final Dialog dialog=new Dialog(context, R.style.simple_dialog);
//		
//		dialog.setContentView(view);
//
//        sureBtnLayout.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//				listener.surePress();
//			}
//		});
//
//        cancleBtnLayout.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {				
//				dialog.dismiss();
//			}
//		});
//		return  dialog;
//	}
	
	
	
	
	private OnSureClickListener listener;
	
	public interface OnSureClickListener{
		void surePress();
	}
	
	public void setOnSureClickListener(OnSureClickListener listener){
		this.listener=listener;
	}
	
	
	private OnEidtSureClickListener editListener;
	
	public interface OnEidtSureClickListener{
		void editSurePress(String str);
	}
	
	public void setOnEidtSureClickListener(OnEidtSureClickListener editListener){
		this.editListener=editListener;
		
	}
	
	
	
	
	/**
	 * 该方法用于获取一个带edittext的dialog
	 * @param context 上下文
	 * @param content hint内容
	 * @param title  如“提示”
	 * @param sure	如“确定”
	 * @param cancel 如“取消”
	 * @param type 1，显示数据键盘    !=1 显示英文键盘
	 * @return
	 *//*
	public Dialog getEditDialog(final Context context,String content,String title,String sure,String cancel,int type){
		
		return getEditDialog(context,content,null,title,sure,cancel,type);

	}
	
	
	public Dialog getEditDialog(final Context context,String content,String hint,String title,String sure,String cancel,int type){
		View view = View.inflate(context, R.layout.cx_fa_widget_mate_user_dialog,null);
		
		TextView titelText = (TextView) view.findViewById(R.id.cx_fa_mate_family_info_user_title_tv);
		final EditText contentText = (EditText) view.findViewById(R.id.cx_fa_mate_family_info_user_content_et);
		Button sureBtn = (Button) view.findViewById(R.id.cx_fa_mate_family_info_user_ok_btn);
		Button cancleBtn = (Button) view.findViewById(R.id.cx_fa_mate_family_info_user_cancel_btn);
		
		if(type==1){//设置电话号码需要数字键盘
			contentText.setInputType(InputType.TYPE_CLASS_PHONE);
		}
		
		if(!TextUtils.isEmpty(title)){
			titelText.setText(title);
		}
		
		if(!TextUtils.isEmpty(content)){
			contentText.setText(content);
			contentText.setSelection(content.length());
		}
		if(!TextUtils.isEmpty(hint)){
			contentText.setHint(hint);		
		}
		
		if(!TextUtils.isEmpty(sure)){
			sureBtn.setText(sure);
		}
		if(!TextUtils.isEmpty(cancel)){
			cancleBtn.setText(cancel);
		}
		
		contentText.setFocusable(true);
		contentText.requestFocus();		
		contentText.requestFocusFromTouch();
		final InputMethodManager input = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		input.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);//弹出键盘
		
		
		final Dialog dialog=new Dialog(context, R.style.simple_dialog);
		
		dialog.setContentView(view);
		
		contentText.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					input.hideSoftInputFromWindow(contentText.getWindowToken(), 0);
				}
				
			}
		});
		
		
		sureBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String text = contentText.getText().toString();
				if(TextUtils.isEmpty(text)){
					ToastUtil.getSimpleToast(context, -3, "你尚未输入内容！", 0).show();
					return;
				}
				
				editListener.editSurePress(text);
//				contentText.setFocusable(false);
				input.hideSoftInputFromWindow(contentText.getWindowToken(), 0);
				dialog.dismiss();
				
				
			}
		});
		
		cancleBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {		
				input.hideSoftInputFromWindow(contentText.getWindowToken(), 0);
				dialog.dismiss();
				
			}
		});
		return  dialog;
	}*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
