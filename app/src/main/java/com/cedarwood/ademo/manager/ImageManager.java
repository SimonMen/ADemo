package com.cedarwood.ademo.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.cedarwood.ademo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageManager {

	private static ImageManager instance = null;

    private ImageManager() {
        super();
    }

    public static ImageManager getInstance() {
        if (instance == null) {
            synchronized (ImageManager.class) {
                if (instance == null) {
                     instance = new ImageManager();
                }
            }
        }
        return instance;
    }

    private Context context;
    private ImageLoader imageLoader;
    private DisplayImageOptions defaultOptions;

    public void init(Context context) {
        this.context = context;

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageForEmptyUri(R.mipmap.drag_photo_fail);
        builder.showImageOnFail(R.mipmap.drag_photo_fail);
        builder.showImageOnLoading(R.mipmap.drag_photo_fail);
        builder.bitmapConfig(Bitmap.Config.RGB_565);
        builder.cacheInMemory(true);
        builder.cacheOnDisk(true);
        builder.imageScaleType(ImageScaleType.EXACTLY);
        defaultOptions=builder.build();

    }

    public void displayImage(String imageUri, ImageView imageView) {
        imageLoader.displayImage(imageUri, imageView, defaultOptions);
    }
    
    public void loadImage(String imageUri) {
        imageLoader.loadImage(imageUri, defaultOptions,new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				
			}
		});
    }
    
    
    
    
    
    public void loadImage(String imageUri, ImageLoadingListener loadingListener) {
        imageLoader.loadImage(imageUri, defaultOptions,loadingListener);
    }
    
    public void loadImage(String imageUri, int defaultRes, ImageLoadingListener loadingListener) {
    	DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageForEmptyUri(defaultRes);
        builder.showImageOnFail(defaultRes);
        builder.showImageForEmptyUri(defaultRes);
        builder.showImageOnLoading(defaultRes);
        builder.bitmapConfig(Bitmap.Config.RGB_565);
        builder.cacheInMemory(true);
        builder.cacheOnDisk(true);
        builder.imageScaleType(ImageScaleType.EXACTLY);
        imageLoader.loadImage(imageUri, builder.build(),loadingListener);
    }
    
    
    
    public void clearCache() {
        imageLoader.clearDiskCache();
    }

    public void displayImage(String imageUri, int defaultRes, ImageView imageView) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageForEmptyUri(defaultRes);
        builder.showImageOnFail(defaultRes);
        builder.showImageForEmptyUri(defaultRes);
        builder.showImageOnLoading(defaultRes);
        builder.bitmapConfig(Bitmap.Config.RGB_565);
        builder.cacheInMemory(true);
        builder.cacheOnDisk(true);
        builder.imageScaleType(ImageScaleType.EXACTLY);

        imageLoader.displayImage(imageUri, imageView, builder.build());
    }


}
