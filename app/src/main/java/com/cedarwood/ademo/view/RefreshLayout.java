package com.cedarwood.ademo.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;

import com.cedarwood.ademo.utils.Log;
import com.cedarwood.ademo.view.pull.PullListView;

/**
 * Created by wentongmen on 2016/3/8.
 */
public class RefreshLayout extends SwipeRefreshLayout {


    private static final String TAG = RefreshLayout.class.getSimpleName();
    /**
     * 滑动到最下面时的上拉操作
     */

    private int mTouchSlop;
    /**
     * listview实例
     */
    private PullListView mListView;

    /**
     * 上拉监听器, 到了最底部的上拉加载操作
     */
    private OnLoadListener mOnLoadListener;
    private boolean isLoading;
    private PullListView.MoreListener moreListener;


    /**
     * @param context
     */
    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        isInEditMode();

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // 初始化ListView对象
        if (mListView == null) {
            getListView();
        }
    }

    /**
     * 获取ListView对象
     */
    private void getListView() {
        int childs = getChildCount();
        if (childs > 0 && mListView==null) {
            for(int i=0;i<childs;i++){
                View childView = getChildAt(i);
//                Log.i(TAG,"getListView : "+childView);
                if (childView instanceof PullListView) {
                    mListView = (PullListView) childView;
                    Log.d(TAG, "### 找到listview");
                    break;
                }
            }

//            View childView = getChildAt(0);
//            if (childView instanceof PullListView) {
//                mListView = (PullListView) childView;
//                Log.d(TAG, "### 找到listview");
//            }

            if(mListView!=null){
                mListView.setMoreListener(moreListener);
            }

        }
    }



    /**
     * @param loading
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
        if (isLoading) {
            mListView.startMore();
        } else {
            mListView.successMore();
        }
    }

    /**
     * @param loadListener
     */
    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;
        moreListener = new PullListView.MoreListener() {
            @Override
            public void more() {
                mOnLoadListener.onLoad();
            }
        };
        if(mListView!=null){
            mListView.setMoreListener(moreListener);
        }


    }



    /**
     * 加载更多的监听器
     *
     * @author mrsimple
     */
    public static interface OnLoadListener {
        public void onLoad();
    }
}
