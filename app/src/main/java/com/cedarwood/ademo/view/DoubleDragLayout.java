package com.cedarwood.ademo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.cedarwood.ademo.R;
import com.cedarwood.ademo.utils.Log;


public class DoubleDragLayout extends LinearLayout {
    private OnPullListener pullListener;
    private OnPageChangedListener ctListener;

    public void setOnPullListener(OnPullListener listener) {
        this.pullListener = listener;
    }

    public void setOnContentChangeListener(OnPageChangedListener ler) {
        this.ctListener = ler;
    }

    private View mHeader;
    private View mFooter;
    private Scroller scroller;
    private int mTouchSlop = 0;

    private int mLastY;
    private int mLastInterceptY;
    private int mHeaderHeight;

    public final static int SCREEN_HEADER = 11;
    public final static int SCREEN_FOOTER = 12;

    private int screen = SCREEN_HEADER;
    
    private static final float SCROLL_RATIO = 0.5f;

    @SuppressLint("NewApi")
    public DoubleDragLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DoubleDragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DoubleDragLayout(Context context) {
        super(context);
        init();
    }

    private void init() {
        scroller = new Scroller(getContext());
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus && mHeader == null && mFooter == null) {
            initData();
        }
    }

    public void initData() {
        mHeader = findViewById(R.id.double_drag_header);
        mFooter = findViewById(R.id.double_drag_footer);
        ViewGroup.LayoutParams lps = mFooter.getLayoutParams();
        mHeaderHeight = mHeader.getMeasuredHeight();
        lps.height = mHeaderHeight;
        mFooter.setLayoutParams(lps);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = false;
        final int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("touch", "DoubleDragLayout --onInterceptTouchEvent action:ACTION_DOWN");
                mLastInterceptY = mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("touch", "DoubleDragLayout --onInterceptTouchEvent action:ACTION_MOVE");
                int dy = y - mLastInterceptY;
                if (dy > mTouchSlop && screen == SCREEN_FOOTER) {// pull down
                    result = (pullListener != null && pullListener
                            .footerHeadReached(ev));
//                    return false;
                } else if (dy < -mTouchSlop && screen == SCREEN_HEADER) { // pull up
                    result = (pullListener != null && pullListener
                            .headerFootReached(ev));
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.i("touch", "DoubleDragLayout --onInterceptTouchEvent action:ACTION_UP");
                mLastInterceptY = 0;
                break;
        }

        Log.i("touch", "DoubleDragLayout --onInterceptTouchEvent : "+result);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("touch", "DoubleDragLayout --onTouchEvent action:ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("touch", "DoubleDragLayout --onTouchEvent action:ACTION_MOVE");
                int dy = y - mLastY;
                switch (screen) {
                    case SCREEN_HEADER:
                        int sy = -dy;
                        if (sy < 0) {
                            sy = 0;
                        } else if (sy > getHeight()) {
                            sy = getHeight();
                        }
//                        System.out.println("滑动距离" + sy);
                        scrollTo(0, (int)(sy * SCROLL_RATIO));
//                        System.out.println("加上阻尼系数后的滑动距离" + sy * SCROLL_RATIO);
                        break;
                    case SCREEN_FOOTER:
                        if (dy > 0) {
                            scrollTo(0, mHeaderHeight - dy);
                        } else { // dy < 0
                        }
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.i("touch", "DoubleDragLayout --onTouchEvent action:ACTION_UP");
                int t = 0;
                switch (screen) {
                    case SCREEN_HEADER:
                        t = mHeaderHeight / 6;
                        break;
                    case SCREEN_FOOTER:
                        t = mHeaderHeight * 3 / 4;
                        break;
                }
                int sy = getScrollY();
                if (sy > t) { // scroll to footer
                    scroller.startScroll(0, sy, 0, mHeaderHeight - sy, 500);
                    screen = SCREEN_FOOTER;
                    if (ctListener != null) {
                        ctListener.onPageChanged(SCREEN_FOOTER);
                    }
                    invalidate();
                } else { // scroll to header
                    scroller.startScroll(0, sy, 0, -sy, 500);
                    screen = SCREEN_HEADER;
                    if (ctListener != null) {
                        ctListener.onPageChanged(SCREEN_HEADER);
                    }
                    invalidate();
                }
                mLastY = 0;
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }
    
    public void scrollToHeader(){
        if (screen == SCREEN_FOOTER) {
            scroller.startScroll(0, getScrollY(), 0, -getScrollY(), 200);
            screen = SCREEN_HEADER;
            if (ctListener != null) {
                ctListener.onPageChanged(SCREEN_HEADER);
            }
        }
    }
    
    public void scrollToFooter(){
        if (screen == SCREEN_HEADER) {
            scrollBy(0, mHeaderHeight);
            screen = SCREEN_FOOTER;
            if (ctListener != null) {
                ctListener.onPageChanged(SCREEN_FOOTER);
            }
        }
    }
    
    public void setHeaderHeight(){
        if (mHeaderHeight == 0) {
            mHeaderHeight = mHeader.getMeasuredHeight();
        }
    }
    
    public interface OnPullListener {
        // 向上拉到达第二页
        public boolean headerFootReached(MotionEvent event);

        // 向下拉到达第一页
        public boolean footerHeadReached(MotionEvent event);
    }

    public interface OnPageChangedListener {
        // 页面变化时Listener
        public void onPageChanged(int stub);
    }
}
