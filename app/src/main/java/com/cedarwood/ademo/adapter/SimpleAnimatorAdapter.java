package com.cedarwood.ademo.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by wentongmen on 2016/3/11.
 */
public class SimpleAnimatorAdapter<T extends RecyclerView.ViewHolder> extends AnimatorAdapter<T>{


    public static final int SLIDE_IN_LEFT = 1;
    public static final int SLIDE_IN_RIGHT = 2;
    public static final int SLIDE_IN_BOTTOM = 3;
    public static final int SLIDE_IN_SWING_BOTTOM = 5;
    public static final int SLIDE_IN_SCALE = 4;
    public static final int SLIDE_IN_ALPHA = 0;

    private static final float DEFAULT_SCALE_FROM = 0.6f;


    private int type = SLIDE_IN_ALPHA;

    private static final String TRANSLATION_Y = "translationY";
    private static final String TRANSLATION_X = "translationX";

    private static final String SCALE_X = "scaleX";
    private static final String SCALE_Y = "scaleY";

    private float mScaleFrom ;

    public SimpleAnimatorAdapter(RecyclerView.Adapter<T> adapter,
                                        RecyclerView recyclerView) {

        this(adapter, recyclerView,DEFAULT_SCALE_FROM);
    }

    public SimpleAnimatorAdapter(RecyclerView.Adapter<T> adapter,
                                  RecyclerView recyclerView,float mScaleFrom) {
        super(adapter, recyclerView);
        this.mScaleFrom = mScaleFrom;
    }

    public SimpleAnimatorAdapter(RecyclerView.Adapter<T> adapter,
                                 RecyclerView recyclerView,int type) {
        super(adapter, recyclerView);
        this.type = type;
    }

    public SimpleAnimatorAdapter(RecyclerView.Adapter<T> adapter,
                                 RecyclerView recyclerView,float mScaleFrom,int type) {
        super(adapter, recyclerView);
        this.mScaleFrom = mScaleFrom;
        this.type = type;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @NonNull
    @Override
    public Animator[] getAnimators(@NonNull View view) {

        if(type==SLIDE_IN_LEFT){
            return new Animator[]{ObjectAnimator.ofFloat(view, TRANSLATION_X, 0 - mRecyclerView.getLayoutManager().getWidth(), 0)};
        }
        if(type==SLIDE_IN_RIGHT){
            return new Animator[]{ObjectAnimator.ofFloat(view, TRANSLATION_X, mRecyclerView.getLayoutManager().getWidth(), 0)};
        }
        if(type==SLIDE_IN_BOTTOM){
            return new Animator[]{ObjectAnimator.ofFloat(view, TRANSLATION_Y, mRecyclerView.getMeasuredHeight() >> 1, 0)};
        }
        if(type==SLIDE_IN_SWING_BOTTOM){
            float mOriginalY = mRecyclerView.getLayoutManager().getDecoratedTop(view);
            float mDeltaY = mRecyclerView.getHeight() - mOriginalY;

            return new Animator[]{ObjectAnimator.ofFloat(view, TRANSLATION_Y, mDeltaY, 0),ObjectAnimator.ofFloat(view, TRANSLATION_X, mRecyclerView.getLayoutManager().getWidth(), 0)};
        }
        if(type==SLIDE_IN_SCALE){
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, SCALE_X, mScaleFrom, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, SCALE_Y, mScaleFrom, 1f);
            return new ObjectAnimator[]{scaleX, scaleY};
        }

        return new Animator[0];

    }

    public void setType(int type) {
        this.type = type;
    }

    public void setScaleFrom(float mScaleFrom) {
        this.mScaleFrom = mScaleFrom;
    }
}
