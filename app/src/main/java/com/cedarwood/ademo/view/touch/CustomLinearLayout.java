package com.cedarwood.ademo.view.touch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.cedarwood.ademo.utils.Log;

/**
 * Created by wentongmen on 2016/4/5.
 */
public class CustomLinearLayout extends LinearLayout {


    public CustomLinearLayout(Context context) {
        super(context);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int action = ev.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {

            case MotionEvent.ACTION_DOWN:

                Log.i("touch", "CustomLinearLayout --dispatchTouchEvent action:ACTION_DOWN");

                break;

            case MotionEvent.ACTION_MOVE:

                Log.i("touch", "CustomLinearLayout --dispatchTouchEvent action:ACTION_MOVE");
//                return false;
                break;

            case MotionEvent.ACTION_UP:

                Log.i("touch", "CustomLinearLayout --dispatchTouchEvent action:ACTION_UP");

                break;

            case MotionEvent.ACTION_CANCEL:

                Log.i("touch", "CustomLinearLayout --dispatchTouchEvent action:ACTION_CANCEL");

                break;

        }


        boolean b = false;
//        boolean b = true;
        b = super.dispatchTouchEvent(ev);

        Log.i("touch","CustomLinearLayout --dispatchTouchEvent : "+b);

        return b;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int action = ev.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {

            case MotionEvent.ACTION_DOWN:

                Log.i("touch", "CustomLinearLayout --onInterceptTouchEvent action:ACTION_DOWN");

                break;

            case MotionEvent.ACTION_MOVE:

                Log.i("touch", "CustomLinearLayout --onInterceptTouchEvent action:ACTION_MOVE");
//                return false;
                break;

            case MotionEvent.ACTION_UP:

                Log.i("touch", "CustomLinearLayout --onInterceptTouchEvent action:ACTION_UP");

                break;

            case MotionEvent.ACTION_CANCEL:

                Log.i("touch", "CustomLinearLayout --onInterceptTouchEvent action:ACTION_CANCEL");

                break;

        }



        boolean b = false;
        b = super.onInterceptTouchEvent(ev);

        Log.i("touch","CustomLinearLayout --onInterceptTouchEvent : "+b);

        return b;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {

            case MotionEvent.ACTION_DOWN:

                Log.i("touch", "CustomLinearLayout --onTouchEvent action:ACTION_DOWN");

                break;

            case MotionEvent.ACTION_MOVE:

                Log.i("touch", "CustomLinearLayout --onTouchEvent action:ACTION_MOVE");
//                return false;
                break;

            case MotionEvent.ACTION_UP:

                Log.i("touch", "CustomLinearLayout --onTouchEvent action:ACTION_UP");

                break;

            case MotionEvent.ACTION_CANCEL:

                Log.i("touch", "CustomLinearLayout --onTouchEvent action:ACTION_CANCEL");

                break;

        }



        boolean b = false;
        b = super.onTouchEvent(event);
        Log.i("touch","CustomLinearLayout --onTouchEvent : "+b);

        return b;
    }














}
