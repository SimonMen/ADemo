package com.cedarwood.ademo.view.pull;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.cedarwood.ademo.R;
import com.cedarwood.ademo.utils.Log;

public class RefreshImageView extends ImageView{

	private static final String TAG = RefreshImageView.class.getSimpleName();
	private Drawable maskDraw;
	private boolean isRefresh=true;
	
	/**
	 * 进度条
	 * 0-100
	 */
	private float mRefreshProcess = 0;
	private float mMoreProcess = 0;
	
	public RefreshImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		maskDraw = context.getResources().getDrawable(R.mipmap.refresh_red);
	}

	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();
		if(isRefresh){
			Log.i(TAG, " getHeight() : " + getHeight() + "   getRefreshProcess() : " + getRefreshProcess());
			int topline = (int) (getHeight() - getHeight()*getRefreshProcess()/100);
			canvas.clipRect(0, topline , getWidth(), getHeight());
		}else{
			Log.i(TAG, " getHeight() : "+getHeight()+"   getMoreProcess() : "+getMoreProcess());
			int bottomline = (int) (getHeight()*getMoreProcess()/100);
			canvas.clipRect(0, 0 , getWidth(), bottomline);
		}
		maskDraw.draw(canvas);
		canvas.restore();
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		maskDraw.setBounds(0, 0, getWidth(), getHeight());
	}



	public float getRefreshProcess() {
		return mRefreshProcess;
	}
	
	public float getMoreProcess() {
		return mMoreProcess;
	}

	/**
	 * 设置新的进度以后，自动刷新
	 */
	public void setProgress(int process,boolean isRefresh) {
		if(process>100 ){
			process = 100;
		}
		if(process<0 ){
			process = 0;
		}
		
		this.isRefresh=isRefresh;
		
		if(isRefresh){
			mRefreshProcess=process;
		}else{
			mMoreProcess=process;
		}
		
		invalidate();
	}
	
//	public void setProgress(int process) {
//		if(process>100 ){
//			process = 100;
//		}
//		if(process<0 ){
//			process = 0;
//		}
//		
////		this.isRefresh=isRefresh;
//		
//
//		mRefreshProcess=process;
//		
//		
//		invalidate();
//	}
	
}
