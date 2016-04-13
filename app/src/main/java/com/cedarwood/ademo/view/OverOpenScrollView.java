package com.cedarwood.ademo.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.cedarwood.ademo.R;
import com.cedarwood.ademo.utils.Log;


public class OverOpenScrollView extends ScrollView {
    private static final String TAG = OverOpenScrollView.class.getSimpleName();
    private View headView;
    private Scroller scroller;
    private Context context;

    private float currentX;
    private float currentY;
    private float startX;
    private float startY;

    private int mViewHeight;
    private static int TITLE_HEIGHT = 60;
    private static int SCROLL_HEIGHT = 150;
    private boolean couldScroll;
    private boolean isScrolling;
    private boolean isHeadOpening = false;
    private HeadStateListener listener;
    private int statusBarHeight;
    private int startHeight;
    private int endHeight;
    private int titleHeight;
    private final int ANIMATIONTIME = 600;

    private boolean isTouch =false;
    private long startTime;
    private float dx;
    private float dy;


    public OverOpenScrollView(Context context) {
        super(context);
        this.context = context;
        scroller = new Scroller(context);
    }

    public OverOpenScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        scroller = new Scroller(context);
    }

    public OverOpenScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        scroller = new Scroller(context);
    }

    public void setListener(HeadStateListener listener) {
        this.listener = listener;
    }

    public int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 =
                        Integer.parseInt(localClass.getField("status_bar_height").get(localObject)
                                .toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

//        int contentTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
//        //statusBarHeight是上面所求的状态栏的高度
//        titleHeight = contentTop - statusBarHeight;

        return statusHeight;
    }

    @SuppressWarnings("deprecation")
    public void setHeadImage(int resId) {
        headView = (View) ((Activity) context).findViewById(resId);
        startHeight = context.getResources().getDimensionPixelSize(R.dimen.dimen_double_drag_image_height);
//        titleHeight = context.getResources().getDimensionPixelSize(R.dimen.dimen_global_title);
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            titleHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        statusBarHeight = getStatusHeight((Activity) context);

        Log.i(TAG,"setHeadImage titleHeight : "+titleHeight+"  statusBarHeight : "+statusBarHeight);

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        endHeight = wm.getDefaultDisplay().getHeight() - statusBarHeight - titleHeight;
    }

    public void setStateChanger(){
        if (!isHeadOpening) {
            scroller.startScroll(headView.getLeft(), headView.getBottom(), 0,
                    endHeight - (headView.getBottom() - headView.getTop()), ANIMATIONTIME);
        } else {
            scroller.startScroll(headView.getLeft(), headView.getBottom(), 0,
                    startHeight - (headView.getBottom() - headView.getTop()), ANIMATIONTIME);
        }
        invalidate();   
    }





    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        currentY = ev.getY();
        currentX = ev.getX();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("touch", "OverOpenScrollView --dispatchTouchEvent action:ACTION_DOWN");
                startY = currentY;
                startX = currentX;
                startTime = SystemClock.uptimeMillis();
                mViewHeight = headView.getBottom() - headView.getTop();
                couldScroll = (getScrollY() == 0 || isHeadOpening);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("touch", "OverOpenScrollView --dispatchTouchEvent action:ACTION_MOVE");
                if (!couldScroll)
                    break;
                dx = Math.abs(currentX - startX);
                dy = Math.abs(currentY - startY);
                if ((couldScroll && currentY - startY > SCROLL_HEIGHT && !isHeadOpening && dx < dy)
                        || (couldScroll && isHeadOpening && startY - currentY > SCROLL_HEIGHT && dx < dy)) {
                    isScrolling = true;
                    int targetY = 0;
                    if(!isHeadOpening){
                        targetY = (int) (mViewHeight + ((currentY - startY-SCROLL_HEIGHT)) / 1.5F);
                    }else{
                        targetY = (int) (mViewHeight + ((currentY - startY+SCROLL_HEIGHT)) / 1.5F);
                    }
                    if(targetY<startHeight){
                        targetY = startHeight;
                    }
                    if(targetY>endHeight){
                        targetY = endHeight;
                    }

                    LinearLayout.LayoutParams lp =
                            new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT, targetY);
                    headView.setLayoutParams(lp);

                    invalidate();
                    if(listener!=null && !isTouch){
                        isTouch = true;
                        listener.headTouch();
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                Log.i("touch", "OverOpenScrollView --dispatchTouchEvent action:ACTION_UP");
                currentY = ev.getY();
                if (couldScroll && isScrolling) {
                    couldScroll =false;
                    isScrolling = false;
                    isTouch =false;
                    if (!isHeadOpening) {
                        if ((currentY - startY > SCROLL_HEIGHT)) {
                            isHeadOpening = true;
                            scroller.startScroll(headView.getLeft(), headView.getBottom(), 0,
                                    endHeight - (headView.getBottom() - headView.getTop()), ANIMATIONTIME);
                        } else {
                            scroller.startScroll(headView.getLeft(), headView.getBottom(), 0,
                                    startHeight - (headView.getBottom() - headView.getTop()), ANIMATIONTIME);
                        }
                    } else {
                        if (startY - currentY > SCROLL_HEIGHT) {
                            isHeadOpening = false;
                            scroller.startScroll(headView.getLeft(), headView.getBottom(), 0,
                                    startHeight - (headView.getBottom() - headView.getTop()), ANIMATIONTIME);
                        } else {
                            scroller.startScroll(headView.getLeft(), headView.getBottom(), 0,
                                    endHeight - (headView.getBottom() - headView.getTop()), ANIMATIONTIME);
                        }
                    }
                }
                invalidate();
                break;
            default:
                break;
        }

        Log.i("touch", "OverOpenScrollView --dispatchTouchEvent isScrolling : "+isScrolling);
        if(isScrolling){
            return false;
        }

//        if(isHeadOpening && startY - currentY < SCROLL_HEIGHT && dx < dy){
//            return  false;
//        }
        Log.i("touch", "OverOpenScrollView --dispatchTouchEvent startY : "+startY+" currentY :"+currentY+"  dx : "+dx+"  dy : "+dy);
        if(isHeadOpening && startY > currentY && dx < dy){
            return  false;
        }

        boolean b = super.dispatchTouchEvent(ev);
        Log.i("touch", "OverOpenScrollView --dispatchTouchEvent : "+b);
        return b;
    }




    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        currentY = ev.getY();
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                startY = currentY;
//                mViewHeight = headView.getBottom() - headView.getTop();
//                couldScroll = (getScrollY() == 0 || isHeadOpening);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (!couldScroll)
//                    break;
//                currentY = ev.getY();
//                if ((couldScroll && currentY - startY > 60 && !isHeadOpening)
//                        || (couldScroll && isHeadOpening && startY - currentY > 60)) {
//                    isScrolling = true;
//                    int targetY = (int) (mViewHeight + ((currentY - startY)) / 1.5F);
//                    RelativeLayout.LayoutParams lp =
//                            new RelativeLayout.LayoutParams(
//                                    RelativeLayout.LayoutParams.MATCH_PARENT, targetY);
//                    headView.setLayoutParams(lp);
//                    invalidate();
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                currentY = ev.getY();
//                if (couldScroll && isScrolling) {
//                    isScrolling = false;
//                    if (!isHeadOpening) {
//                        if ((currentY - startY > TITLE_HEIGHT)) {
//                            scroller.startScroll(headView.getLeft(), headView.getBottom(), 0,
//                                    endHeight - (headView.getBottom() - headView.getTop()), 300);
//                        } else {
//                            scroller.startScroll(headView.getLeft(), headView.getBottom(), 0,
//                                    startHeight - (headView.getBottom() - headView.getTop()), 300);
//                        }
//                    } else {
//                        if (startY - currentY > TITLE_HEIGHT) {
//                            scroller.startScroll(headView.getLeft(), headView.getBottom(), 0,
//                                    startHeight - (headView.getBottom() - headView.getTop()), 300);
//                        } else {
//                            scroller.startScroll(headView.getLeft(), headView.getBottom(), 0,
//                                    endHeight - (headView.getBottom() - headView.getTop()), 300);
//                        }
//                    }
//                }
//                invalidate();
//                break;
//            default:
//                break;
//        }
//        if(isHeadOpening&&!isScrolling){
//            return true;
//        }
        return super.onTouchEvent(ev);
    }
    
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            int x = scroller.getCurrX();
            int y = scroller.getCurrY();
            LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, y);
            headView.setLayoutParams(lp);
            headView.requestLayout();
            invalidate();
            if (y == endHeight) {
                scroller.abortAnimation();
                if (listener != null) {
                    listener.headOpen();
                }
            } else if (y == startHeight) {
                scroller.abortAnimation();
                if (listener != null) {
                    listener.headClosed();
                }
            }

        }
    }

    public interface HeadStateListener {
        void headOpen();

        void headClosed();
        
        void headTouch();
    }
}
