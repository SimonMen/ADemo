package com.cedarwood.ademo.view.touch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.cedarwood.ademo.utils.Log;

/**
 * Created by wentongmen on 2016/4/5.
 */
public class CustomTextView extends TextView {
    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {

            case MotionEvent.ACTION_DOWN:

                Log.i("touch", "CustomTextView --dispatchTouchEvent action:ACTION_DOWN");

                break;

            case MotionEvent.ACTION_MOVE:

                Log.i("touch", "CustomTextView --dispatchTouchEvent action:ACTION_MOVE");
//                return false;
                break;

            case MotionEvent.ACTION_UP:

                Log.i("touch", "CustomTextView --dispatchTouchEvent action:ACTION_UP");

                break;

            case MotionEvent.ACTION_CANCEL:

                Log.i("touch", "CustomTextView --dispatchTouchEvent action:ACTION_CANCEL");

                break;

        }
        boolean b = false;
//        b = super.dispatchTouchEvent(ev);

        Log.i("touch","CustomTextView --dispatchTouchEvent : "+b);

        return b;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {

            case MotionEvent.ACTION_DOWN:

                Log.i("touch", "CustomTextView --onTouchEvent action:ACTION_DOWN");

                break;

            case MotionEvent.ACTION_MOVE:

                Log.i("touch", "CustomTextView --onTouchEvent action:ACTION_MOVE");
//                return false;
                break;

            case MotionEvent.ACTION_UP:

                Log.i("touch", "CustomTextView --onTouchEvent action:ACTION_UP");

                break;

            case MotionEvent.ACTION_CANCEL:

                Log.i("touch", "CustomTextView --onTouchEvent action:ACTION_CANCEL");

                break;

        }

        boolean b = true;
        b = super.onTouchEvent(event);
        Log.i("touch","CustomTextView --onTouchEvent : "+b);

        return b;
    }



}
