package com.cedarwood.ademo.view.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Area;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.cedarwood.ademo.utils.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 自定义相机视图
 * Created by hangzhang209526 on 2015/8/17.
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback, Camera.PictureCallback {
    /**
     * 保存照片文件格式
     */
    public static final String FILE_FORMAT = ".jpg";
	private static final String TAG = CameraView.class.getSimpleName();
    private SurfaceHolder mHolder;

	private String TAKING_PIC_SAVE_LOCATION = "";
	private String HISTORY_PIC_SAVE_LOCATION = "";

    /**
     * 相机API
     */
    private Camera mCamera;

    /**
     * 保存图片的文件列表
     */
    private File[] imageFiles;

    /**
     * 存放照片的目录
     */
    private File root;
    
    /**
     * 手机朝向
     */
    private int mOrientation;
    
    /**
     * 照片最大尺寸
     */
    private int maxSize = 1024;

    /**
     * 基本文件名
     */
    private String mBaseFileName;
    /**
     * 完成拍照回调接口
     */
    private CameraContainer.CameraUtilListener mFinishTakeListener;
    private Context mContext;
    /**
     * 是否设置过相机预览的参数
     */
    private boolean isSetPreViewParams = false;
	private int mZoom;
	
	 public CameraView(Context context) {
	        super(context);
	        mContext = context;
	        mHolder = getHolder();
//	        mHolder.addCallback(this);
//	        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	        initData();
	        
	    }
	
	
    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mHolder = getHolder();
//        mHolder.addCallback(this);
//        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        initData();
        
    }
    private void initData() {
		TAKING_PIC_SAVE_LOCATION = getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+File.separator+"camera";
		HISTORY_PIC_SAVE_LOCATION = getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+File.separator+"c";

        String imageRoot = TAKING_PIC_SAVE_LOCATION;
        root = new File(imageRoot);
        if (!root.exists()) root.mkdirs();
        
        imageFiles = root.listFiles();
        mBaseFileName = SystemClock.elapsedRealtime() + "";
        
//        setOnTouchListener(new TouchListener());
    }
    
    public void init(int from){
    	String imageRoot = TAKING_PIC_SAVE_LOCATION;
//    	if(from == 1){
//    		imageRoot = Constants.HOUSE_COVER_PIC_SAVE_LOCATION;
//    	}else if(from==2){
//    		imageRoot = Constants.HOUSE_PIC_SAVE_LOCATION;
//    	}else if(from==3){
//    		imageRoot = Constants.RELEASE_PIC_SAVE_LOCATION;
//    	}
    	
    	
        root = new File(imageRoot);
        if (!root.exists()) root.mkdirs();
        
        imageFiles = root.listFiles();
    	
    }
    
    
    public void refreshPhotos(){
    	imageFiles = root.listFiles();
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera == null) {
            try {
                mCamera = Camera.open();//开启相机,不能放在构造函数中，不然不会显示画面.
                setCameraParams();
                mCamera.setDisplayOrientation(90);
                mCamera.setPreviewDisplay(mHolder); 
                mCamera.startPreview();//开始预览
                mCamera.autoFocus(focusCallback);
            } catch (IOException e) {
            }catch (Exception e){
                if (mFinishTakeListener != null) mFinishTakeListener.onFailedOpenCamera();//回调接口
            }
            
            
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera != null) {
//        	isSetPreViewParams=false;
//            setCameraParams();
        	mCamera.startPreview();//开始预览
            mCamera.autoFocus(focusCallback);
        }
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();//停止预览
            mCamera.release();//释放相机资源
        }
        mCamera = null;
        isSetPreViewParams = false;
    }
    /**
     * 设置完成拍照回调接口
     */
    public void setFinishTakeListener(CameraContainer.CameraUtilListener finishTakeListener) {
        mFinishTakeListener = finishTakeListener;
//        isSetPreViewParams = false;
//        setCameraParams();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        try {// 获得图片
        	
        	
        	saveBitmap(data);
        	camera.startPreview();
        	onFocus(new Point(getWidth()/2, getHeight()/2), focusCallback);
            if (mFinishTakeListener != null) 
            	mFinishTakeListener.onFinishTakPicture();//回调接口
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void saveBitmap(byte[] data){
    	
    	Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
		//newbitmap = bitmap.copy(bitmap.getConfig(), false);
		options.inJustDecodeBounds = false; // 设为 false
		
		int h = options.outHeight;
		int w = options.outWidth;
		
		Log.i(TAG, "saveBitmap h : "+h+", w : "+w);
		int scale = 1;
		for( ;h>=600*2 && w >=900*2;){
			Log.i(TAG, "saveBitmap a h : "+h+", w : "+w);
			scale=scale*2;
			h=h/2;
			w=w/2;
		}
    	
		options.inSampleSize = scale;
		try {
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		bitmap = bitmapRotate(bitmap,mOrientation+90==360?0:90+mOrientation);
		
		try {
			
			String fileName = mBaseFileName + imageFiles.length;
	        File file = new File(root.getAbsolutePath() + File.separator + fileName + CameraView.FILE_FORMAT);
	        if (!file.exists()){
	        	file.createNewFile();
	        } 
	        
	        File file2 = new File(HISTORY_PIC_SAVE_LOCATION + File.separator + fileName + CameraView.FILE_FORMAT);
	        if(!file2.getParentFile().exists()){
	        	file2.getParentFile().mkdirs();
	        }
	        if(!file2.exists()){
	        	file2.createNewFile();
	        }
	        
	        imageFiles = root.listFiles();
	        
	        byte[] array = compress(bitmap).toByteArray();
	        
	        FileOutputStream fos=new FileOutputStream(file);
			fos.write(array);
			fos.flush();
			fos.close();
			
			FileOutputStream fos2=new FileOutputStream(file2);
			fos2.write(array);
			fos2.flush();
			fos2.close();
			
			
			try {
		        MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
						file2.getAbsolutePath(), fileName, null);
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    }
		    // 最后通知图库更新
			mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file2.getAbsolutePath())));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    /**
	 * 旋转图片
	 * @param bitmap
	 * @param degrees
	 * @return
	 */
	private Bitmap bitmapRotate(Bitmap bitmap, int degrees){
		
		Matrix matrix = new Matrix();
		matrix.postRotate(degrees);
		Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//		bitmap.recycle();
		return rotateBitmap;
	}


	
	/**
	 * 图片压缩方法
	 * @param bitmap 图片文件
	 * @return 压缩后的字节流
	 * @throws Exception
	 */
	public ByteArrayOutputStream compress(Bitmap bitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 99;
		while ( baos.toByteArray().length / 1024 > maxSize) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			options -= 3;// 每次都减少10
			//压缩比小于0，不再压缩
			if (options<0) {
				break;
			}
			Log.i(TAG,baos.toByteArray().length / 1024+"");
			baos.reset();// 重置baos即清空baos
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
		}
		return baos;
	}
    
    
    /**
     * 获取照片列表
     */
    public File[] getPictures() {
        return imageFiles;
    }
    /**
     * 照相操作
     */
    public void takePicture() {
        try {
            mCamera.takePicture(null, null, this);
        }catch (Exception e){

        }
    }
    /**
     * 设置相机的相关参数
     */
    private void setCameraParams() {
  
        if (!isSetPreViewParams) {
            //设置参数
            Camera.Parameters params = mCamera.getParameters();
            params.setPictureFormat(ImageFormat.JPEG);
            
            Size picSize = getFitPictureSize(params.getSupportedPictureSizes());
            Size preSize = getFitPreviewSize(params.getSupportedPreviewSizes());

            if (picSize != null) 
            	params.setPictureSize(picSize.width, picSize.height); //设置拍摄照片的大小
            if (preSize != null) 
            	params.setPreviewSize(preSize.width, preSize.height);//设置预览区域的大小
            params.setPictureFormat(ImageFormat.JPEG);//设置照片输出的格式
            params.set("jpeg-quality", 90);//设置照片质量
            //聚焦模式
            List<String> allFocus = params.getSupportedFocusModes();
            if (allFocus.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }else if (allFocus.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
            mCamera.setParameters(params);//把上面的设置 赋给摄像头
            
            startOrientationChangeListener();
            
            isSetPreViewParams = true;
            
            if(picSize==null && mFinishTakeListener!=null){
            	mFinishTakeListener.onFailedSetResolutionRatio();
            }
        }

        
    }
    
    private Size getFitPreviewSize(List<Size> sizes) {
    	
    	if (sizes == null || sizes.size() <= 0) 
    		return null;
    	
    	if (sizes.size() >= 1) {
            Collections.sort(sizes, new Comparator<Size>() {
                @Override
                public int compare(Size lhs, Size rhs) {
                    int lvalue = lhs.height*lhs.width;
                    int rvalue = rhs.height*rhs.width;
                    return lvalue==rvalue?0:(lvalue<rvalue?1:-1);
                }
            });
        }
    	
//    	for(Camera.Size s : sizes){
//    		Log.i("CameraView", "CameraView getFitPreviewSize Preview width : "+s.width+" , height : "+s.height);
//    	}
    	
    	
    	Size size = null;
    	for(Size s : sizes){
    		if(s.width==1280 && s.height == 960){
    			return s;
    		}
    	}
    	
    	for(Size s : sizes){
    		if((int)(s.width*0.75) == s.height){
    			return s;
    		}
    	}
    	
    	size = sizes.get(0);
    	
    	
    	
    	return size;
    }
    
    
    private Size getFitPictureSize(List<Size> sizes) {
    	
    	if (sizes == null || sizes.size() <= 0) 
    		return null;
    	
    	if (sizes.size() >= 1) {
            Collections.sort(sizes, new Comparator<Size>() {
                @Override
                public int compare(Size lhs, Size rhs) {
                    int lvalue = lhs.height*lhs.width;
                    int rvalue = rhs.height*rhs.width;
                    return lvalue==rvalue?0:(lvalue<rvalue?-1:1);
                }
            });
        }
    	
//    	for(Camera.Size s : sizes){
//    		Log.i("CameraView", "CameraView getFitPreviewSize Picture width : "+s.width+" , height : "+s.height);
//    	}
    	
    	
    	Size size = null;
    	for(Size s : sizes){
    		if(s.width==1280 && s.height == 960){
    			return s;
    		}
    	}
    	
  
    	for(Size s : sizes){
    		
    		if(s.width<900 || s.height<600){
    			continue;
    		}
    		
    		if((int)(s.width*0.75) == s.height){
    			return s;
    		}
    	}
    	
    	for(Size s : sizes){
    		
    		if(s.width<900 || s.height<600){
    			continue;
    		}
    		
    		size = s;
    	}
    	
    	return size;
    }
    
    
    /**
     * 获取合适的照片大小
     */
//    private Camera.Size getFitPictureSize(List<Camera.Size> supportedPictureSizes) {
//        int suportSize = supportedPictureSizes.size();
//        if (supportedPictureSizes == null || suportSize <= 0) return null;
//
//        int baseWidth = getMeasuredWidth();//标准宽度
//        int baseHeight = getMeasuredHeight();//标准高度
//        if (baseWidth == 0 || baseHeight == 0) {
//            measure(0, 0);
//            baseWidth = getMeasuredWidth();
//            baseHeight = getMeasuredHeight();
//        }
//        // 如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择
//        if (suportSize >= 1) {
//            Collections.sort(supportedPictureSizes, new Comparator<Camera.Size>() {
//                @Override
//                public int compare(Camera.Size lhs, Camera.Size rhs) {
//                    int lvalue = lhs.height*lhs.width;
//                    int rvalue = rhs.height*rhs.width;
//                    return lvalue==rvalue?0:(lvalue<rvalue?-1:1);
//                }
//            });
//            Iterator<Camera.Size> itor = supportedPictureSizes.iterator();
//            while (itor.hasNext()) {
//                Camera.Size cur = itor.next();
//                if (cur.width > baseWidth
//                        && cur.height > baseHeight) {
//                    return cur;
//                }
//            }
//        }
//        //如果没有合适的就取第一个可用的尺寸
//        Camera.Size size = (Camera.Size) supportedPictureSizes.get(suportSize/2);
//        return size;
//    }
    
    /**  
	 *   启动屏幕朝向改变监听函数 用于在屏幕横竖屏切换时改变保存的图片的方向  
	 */
	private  void startOrientationChangeListener() {  
		OrientationEventListener mOrEventListener = new OrientationEventListener(getContext()) {  
			

			@Override  
			public void onOrientationChanged(int rotation) { 

//				Log.i("CameraView", "CameraView startOrientationChangeListener rotation : "+rotation+" , mOrientation : "+mOrientation);
				
				
				if (((rotation >= 0) && (rotation <= 45)) || (rotation > 315)){
					rotation=0;
				}else if ((rotation > 45) && (rotation <= 135))  {
					rotation=90;
				}
				else if ((rotation > 135) && (rotation <= 225)) {
					rotation=180;
				} 
				else if((rotation > 225) && (rotation <= 315)) { 
					rotation=270;
				}else {
					rotation=0;
				}
				if(rotation==mOrientation)
					return;
				mOrientation=rotation;
			}  
		};  
		mOrEventListener.enable();  
	}  
	
	
	private AutoFocusCallback focusCallback = new AutoFocusCallback() {
		
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			
			
		}
	};
    
    
//    @Override
//    public void onAutoFocus(boolean success, Camera camera) {
////        Log.i("zhanghang","聚焦"+success);
//    }
    
    
//    private final class TouchListener implements OnTouchListener {
//
//		/** 记录是聚焦照片模式还是放大缩小照片模式 */
//
//		private static final int MODE_INIT = 0;
//		/** 放大缩小照片模式 */
//		private static final int MODE_ZOOM = 1;
//		private int mode = MODE_INIT;// 初始状态 
//
//		/** 用于记录拖拉图片移动的坐标位置 */
//
//		private float startDis;
//
//
//		@Override
//		public boolean onTouch(View v, MotionEvent event) {
//			/** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */
//			switch (event.getAction() & MotionEvent.ACTION_MASK) {
//			// 手指压下屏幕
//			case MotionEvent.ACTION_DOWN:
//				mode = MODE_INIT;
//				break;
//			case MotionEvent.ACTION_POINTER_DOWN:
//				
//				if(getMaxZoom()==-1){
//					return true;
//				}
//				
//				mode = MODE_ZOOM;
//				/** 计算两个手指间的距离 */
//				startDis = distance(event);
//				break;
//			case MotionEvent.ACTION_MOVE:
//				if (mode == MODE_ZOOM) {
//					//只有同时触屏两个点的时候才执行
//					if(event.getPointerCount()<2) return true;
//					float endDis = distance(event);// 结束距离
//					//每变化10f zoom变1
//					int scale=(int) ((endDis-startDis)/10f);
//					if(scale>=1||scale<=-1){
//						int zoom= getZoom()+scale;
//						//zoom不能超出范围
//						if(zoom> getMaxZoom()) 
//							zoom= getMaxZoom();
//						if(zoom<0) 
//							zoom=0;
//						setZoom(zoom);
//						//将最后一次的距离设为当前距离
//						startDis=endDis;
//					}
//				}
//				break;
//				// 手指离开屏幕
//			case MotionEvent.ACTION_UP:
//				if(mode!=MODE_ZOOM){
//					//设置聚焦
//					Point point=new Point((int)event.getX(), (int)event.getY());
//					onFocus(point,SohuCameraView.this);
//				}
//				break;
//			}
//			return true;
//		}
//		/** 计算两个手指间的距离 */
//		private float distance(MotionEvent event) {
//			float dx = event.getX(1) - event.getX(0);
//			float dy = event.getY(1) - event.getY(0);
//			/** 使用勾股定理返回两点之间的距离 */
//			return (float) Math.sqrt(dx * dx + dy * dy);
//		}
//
//	}
    
    
    
    /**  
	 * 手动聚焦 
	 *  @param point 触屏坐标
	 */
	@SuppressLint("NewApi")
	protected void onFocus(Point point,AutoFocusCallback callback){
		
		if(VERSION.SDK_INT<VERSION_CODES.ICE_CREAM_SANDWICH){
			mCamera.autoFocus(callback);
			return;
		}
		
		Camera.Parameters parameters=mCamera.getParameters();
		//不支持设置自定义聚焦，则使用自动聚焦，返回
		if (parameters.getMaxNumFocusAreas()<=0) {
			mCamera.autoFocus(callback);
			return;
		}
		List<Area> areas=new ArrayList<Area>();
		int left=point.x-300;
		int top=point.y-300;
		int right=point.x+300;
		int bottom=point.y+300;
		left=left<-1000?-1000:left;
		top=top<-1000?-1000:top;
		right=right>1000?1000:right;
		bottom=bottom>1000?1000:bottom;
		areas.add(new Area(new Rect(left,top,right,bottom), 100));
		parameters.setFocusAreas(areas);
		try {
			mCamera.setParameters(parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mCamera.autoFocus(callback);
	}

	/**  
	 *  获取最大缩放级别，最大为40
	 *  @return   
	 */

	public int getMaxZoom(){
		if(mCamera==null) return -1;		
		Camera.Parameters parameters=mCamera.getParameters();
		if(!parameters.isZoomSupported()) return -1;
		return parameters.getMaxZoom()>40?40:parameters.getMaxZoom();
	}
	/**  
	 *  设置相机缩放级别
	 *  @param zoom   
	 */

	public void setZoom(int zoom){
		if(mCamera==null) return;
		
		Camera.Parameters parameters=mCamera.getParameters();

		if(!parameters.isZoomSupported()) return;
		parameters.setZoom(zoom);
		mCamera.setParameters(parameters);
		mZoom=zoom;
	}

	public int getZoom(){
		return mZoom;
	}
    


}
