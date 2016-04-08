package com.cedarwood.ademo.view.camera;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.File;

public class CameraContainer extends RelativeLayout {

	private CameraView mCameraView;
	private FocusImageView mFocusImg;





	public CameraContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initView(context);
		setOnTouchListener(new TouchListener());
		
		
	}

	private void initView(Context context) {
		
		mCameraView = new CameraView(context);
		mFocusImg = new FocusImageView(context);
		
		LayoutParams cameraParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mCameraView.setLayoutParams(cameraParams);
		
//		int w = CommonUtil.dip2px(context, 120);
		int w = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,120, new DisplayMetrics());
		LayoutParams focusParams = new LayoutParams(w, w);
		mFocusImg.setLayoutParams(focusParams);
		mFocusImg.setPadding(w/4, w/4, w/4, w/4);
		
		addView(mCameraView,cameraParams);
		addView(mFocusImg,focusParams);
		
	}
	
	
	
	
	
	private final class TouchListener implements OnTouchListener {

		/** 记录是聚焦照片模式还是放大缩小照片模式 */

		private static final int MODE_INIT = 0;
		/** 放大缩小照片模式 */
		private static final int MODE_ZOOM = 1;
		private int mode = MODE_INIT;// 初始状态 

		/** 用于记录拖拉图片移动的坐标位置 */

		private float startDis;


		@Override
		public boolean onTouch(View v, MotionEvent event) {
			/** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			// 手指压下屏幕
			case MotionEvent.ACTION_DOWN:
				mode = MODE_INIT;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				
				if(getMaxZoom()==-1){
					return true;
				}
				
				mode = MODE_ZOOM;
				/** 计算两个手指间的距离 */
				startDis = distance(event);
				break;
			case MotionEvent.ACTION_MOVE:
				if (mode == MODE_ZOOM) {
					//只有同时触屏两个点的时候才执行
					if(event.getPointerCount()<2) return true;
					float endDis = distance(event);// 结束距离
					//每变化10f zoom变1
					int scale=(int) ((endDis-startDis)/10f);
					if(scale>=1||scale<=-1){
						int zoom= getZoom()+scale;
						//zoom不能超出范围
						if(zoom> getMaxZoom()) 
							zoom= getMaxZoom();
						if(zoom<0) 
							zoom=0;
						setZoom(zoom);
						//将最后一次的距离设为当前距离
						startDis=endDis;
					}
				}
				break;
				// 手指离开屏幕
			case MotionEvent.ACTION_UP:
				if(mode!=MODE_ZOOM){
					//设置聚焦
					Point point=new Point((int)event.getX(), (int)event.getY());
					mCameraView.onFocus(point,focusCallback);
					mFocusImg.startFocus(point);
				}
				break;
			}
			return true;
		}
		/** 计算两个手指间的距离 */
		private float distance(MotionEvent event) {
			float dx = event.getX(1) - event.getX(0);
			float dy = event.getY(1) - event.getY(0);
			/** 使用勾股定理返回两点之间的距离 */
			return (float) Math.sqrt(dx * dx + dy * dy);
		}

	}


	private AutoFocusCallback focusCallback = new AutoFocusCallback() {
		
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			if (success) {
				mFocusImg.onFocusSuccess();
			}else {
				//聚焦失败显示的图片，由于未找到合适的资源，这里仍显示同一张图片
				mFocusImg.onFocusFailed();

			}
			
		}
	};


	public int getMaxZoom() {
		
		return mCameraView.getMaxZoom();
	}

	public void setZoom(int zoom) {
		mCameraView.setZoom(zoom);
	}

	public int getZoom() {
	
		return mCameraView.getZoom();
	}
	
	public void init(int from){
		mCameraView.init(from);
	}
	
	public void refreshPhotos(){
		mCameraView.refreshPhotos();
	}
	
	public void takePicture(){
		mCameraView.takePicture();
	}
	
	public File[] getPictures(){
		return mCameraView.getPictures();
	}
	
	
	public void setFinishTakeListener(CameraUtilListener finishTakeListener) {
		mCameraView.setFinishTakeListener(finishTakeListener);
	}
	
	
	/**
     * 完成拍照后的回调接口
     */
    public interface CameraUtilListener {
        /**
         * 完成拍照后的回调方法
         */
        public void onFinishTakPicture();
        /**
         * 打开照相机失败方法
         */
        public void onFailedOpenCamera();
        /**
         * 打开照相机失败方法
         */
        public void onFailedSetResolutionRatio();
    }

}
