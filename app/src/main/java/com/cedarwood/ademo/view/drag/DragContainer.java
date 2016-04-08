package com.cedarwood.ademo.view.drag;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.cedarwood.ademo.model.data.DragMarkInfoLocation;
import com.cedarwood.ademo.utils.Log;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by wentongmen on 2016/3/9.
 */
public class DragContainer extends LinearLayout {

    private static final String TAG = DragContainer.class.getSimpleName();
    private DragView drag;
    private int height;
    private int photoWidth;
    private int photoHeight;


    public DragContainer(Context context) {
        super(context);

        isInEditMode();

//        init();

    }



    public DragContainer(Context context, AttributeSet attrs) {
        super(context, attrs);

//        init();
    }

    public void init(int temp,int photoWidth,int photoHeight) {

        height = temp;
        this.photoWidth = photoWidth;
        this.photoHeight = photoHeight;

        drag = new DragView(getContext());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        params.height = (int) (height * 2.5);
        params.width = (int) ((height * (photoWidth / (double) photoHeight)) * 2.5);
        drag.setLayoutParams(params);
        addView(drag,params);

        drag.setPhotoSize(photoWidth, photoHeight);
        drag.setOnTouchListener(new DragOnTouchListener());
    }

    public void setBg(Drawable drawable){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            drag.setBackground(drawable);
        } else {
            drag.setBackgroundDrawable(drawable);
        }

    }

    public void removeMark(int position){
        drag.removeMark(position);
    }

    public void addMark(int position,DragMarkInfoLocation location){
        drag.addMark(position,location);
    }



    public void setOnMoveUpListener(OnMoveUpListener l){

        this.listener = l;

        drag.setOnMoveUpListener(new DragView.OnMoveUpListener() {

            @Override
            public void onMoveUp(int positon, DragMarkInfoLocation location) {

                listener.onMoveUp(positon,location);

            }

        });

    }



    public class DragOnTouchListener implements View.OnTouchListener {

        int startX = 0;
        int startY = 0;

        /**
         * 记录是聚焦照片模式还是放大缩小照片模式
         */

        private static final int MODE_INIT = 0;
        /**
         * 放大缩小照片模式
         */
        private static final int MODE_ZOOM = 1;
        private int mode = MODE_INIT;// 初始状态

        /**
         * 用于记录拖拉图片移动的坐标位置
         */

        private float startDis;



        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case ACTION_DOWN:

                    mode = MODE_INIT;

                    startX = (int) event.getRawX();
                    startY = (int) event.getRawY();
                    Log.i(TAG, ">>>  startX " + startX + "  startY " + startY);


                    break;
                case MotionEvent.ACTION_POINTER_DOWN:


                    mode = MODE_ZOOM;
                    drag.removeAllMark();
                    /** 计算两个手指间的距离 */
                    startDis = distance(event);
                    break;
                case ACTION_MOVE:
                    Log.i("onTouch", "onTouch ACTION_MOVE mode : " + mode);
                    if (mode == MODE_ZOOM) {
                        //只有同时触屏两个点的时候才执行
                        Log.i("onTouch", "onTouch event.getPointerCount() : " + event.getPointerCount());
                        if (event.getPointerCount() < 2)
                            return true;
                        float endDis = distance(event);// 结束距离
                        //每变化10f zoom变1
                        int scale = (int) ((endDis - startDis) / 10f);

                        Log.i("onTouch", "onTouch scale : " + scale + "  startDis :　" + startDis + " endDis : " + endDis);

                        if (scale >= 1 || scale <= -1) {
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) drag.getLayoutParams();
                            float ss = params.height / (float) height;
                            float s = ss + scale / (float) 50;

                            Log.i("onTouch", "onTouch t : " + ss + "  s :　" + s);

                            if (s < 1) {
                                s = 1;
                            }

                            if (s > 2.5) {
                                s = 2.5f;
                            }

                            int h = (int) (height * s);
                            int w = (int) ((height * (photoWidth / (double) photoHeight)) * s);

//							double sy = (params.height/2+params.topMargin-temp/2)/ss*(ss-s);
//
//							double sx = (params.width/2+params.leftMargin-(temp*(photoWidth/(double)photoHeight))/2)/ss*(ss-s);

                            int left = (int) (params.leftMargin + (params.width - w) / 2);
                            int top = (int) (params.topMargin + (params.height - h) / 2);


//							int left = (int)(params.leftMargin/ss*s);
//							int top = (int) (params.topMargin/ss*s);


                            int l = left;
                            int t = top;
                            int r = l + w;
                            int b = t + h;

                            if (l > 0) {
                                l = 0;
                            }
                            if (t > 0) {
                                t = 0;
                            }

                            if (r < getWidth()) {
                                l = getWidth() - w;
                                r = l + w;
                            }

                            if (b < getHeight()) {
                                t = getHeight() - h;
                                b = t + h;
                            }


                            if (l > 0 || r < getWidth() || t > 0
                                    || b < getHeight()) {
                                break;
                            }

                            params.width = w;
                            params.height = h;
                            params.leftMargin = l;
                            params.topMargin = t;

                            drag.setLayoutParams(params);

                            //将最后一次的距离设为当前距离
                            startDis = endDis;
                        }

                        break;
                    }


                    int newX = (int) event.getRawX();
                    int newY = (int) event.getRawY();

                    Log.i(TAG, ">>>  newX " + newX + "  newY " + newY);

                    int dx = newX - startX;
                    int dy = newY - startY;

                    int l = view.getLeft();
                    int t = view.getTop();
                    int r = view.getRight();
                    int b = view.getBottom();

                    Log.i(TAG, ">>>  l " + l + "  t " + t + " r " + r + "  b" + b);
                    Log.i(TAG, ">>>  dx " + dx + "  dy " + dy);

                    l = l + dx;
                    t = t + dy;
                    r = r + dx;
                    b = b + dy;

                    Log.i(TAG, ">>>  drag " + drag.getWidth() + "  drag " + drag.getHeight());


                    if (l > 0 || r < getWidth() || t > 0
                            || b < getHeight()) {
                        break;
                    }


                    Log.i(TAG, ">>>  drag " + drag.getWidth() + "  drag " + drag.getHeight());


                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) drag.getLayoutParams();
                    params.leftMargin = l;
                    params.topMargin = t;
                    drag.setLayoutParams(params);

//					drag.invalidate();

//                    view.layout(l, t, r, b);
                    startX = (int) event.getRawX();
                    startY = (int) event.getRawY();


                    break;
                case ACTION_UP:

                    if (mode == MODE_ZOOM) {
                        drag.addAllMark();
                    }

//					int lastx = view.getLeft();
//					int lasty = view.getTop();
//
//					int height = layout.getHeight();
//					int width = layout.getWidth();
//
//					int locationX =  (int)((lastx + view.getMeasuredWidth()/2)*photoWidth/(float)width);
//					int locationY =  (int)((lasty + view.getMeasuredHeight())*photoHeight/(float)height);
//
//					DragMarkInfoLocation location = null;
//					int position = 0;
//					Set<Map.Entry<Integer,HouseMark>> entrySet = markMap.entrySet();
//					for(Map.Entry<Integer,HouseMark> markEntry : entrySet){
//						if(markEntry.getValue() == view){
//							position = markEntry.getKey();
//							location = locationMap.get(markEntry.getKey());
//							break;
//						}
//					}
//					if(location!=null){
//						location.setLeft(locationX);
//						location.setTop(locationY);
//						listener.onMoveUp(position ,location);
//					}
//

                    break;
            }
            return true;
        }

        /**
         * 计算两个手指间的距离
         */
        private float distance(MotionEvent event) {
            float dx = event.getX(1) - event.getX(0);
            float dy = event.getY(1) - event.getY(0);
            /** 使用勾股定理返回两点之间的距离 */
            return (float) Math.sqrt(dx * dx + dy * dy);
        }

    }


    private OnMoveUpListener listener;


    public interface OnMoveUpListener {
        void onMoveUp(int positon, DragMarkInfoLocation location);
    }




}
