package com.cedarwood.ademo.view.onoff;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cedarwood.ademo.R;
import com.cedarwood.ademo.utils.CommonUtil;
import com.cedarwood.ademo.utils.Log;

/**
 * Created by wentongmen on 2016/5/13.
 */
public class OnOffTwo extends LinearLayout{


    private static final String TAG = OnOffTwo.class.getSimpleName();

    private ImageView dotImg;
    private ShapeDrawable drawable;

    private boolean isOn = false;
    public float startX;
    public long startTime;

    private float leftMargin = 0;
    private int maxLeftMargin;

    public OnOffTwo(Context context) {
        this(context,null);

    }

    public OnOffTwo(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        maxLeftMargin = CommonUtil.dip2px(getContext(), 30);


        View view = View.inflate(getContext(), R.layout.view_on_off_one, this);
        dotImg = (ImageView) view.findViewById(R.id.on_off_one_img);

        dotImg.setOnTouchListener(touchListener);

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isOn = !isOn;
                ScrollAnimation animation = new ScrollAnimation(leftMargin);
                animation.setDuration(200);
                dotImg.startAnimation(animation);
            }
        });

        dotImg.setImageDrawable(getImgDrawable());
    }




    private void moveImage(float left){

        if(left<0){
            left = 0;
        }

        if(left>maxLeftMargin ){
            left = maxLeftMargin;
        }
        leftMargin = left;

        LayoutParams params = (LayoutParams) dotImg.getLayoutParams();
        params.leftMargin = (int)left;
        dotImg.setLayoutParams(params);

        dotImg.setImageDrawable(getImgDrawable());
    }


    private Drawable getImgDrawable(){


        int rRed = Integer.parseInt("ff", 16);
        int rGreen = Integer.parseInt("66", 16);
        int rBlue = Integer.parseInt("6c", 16);
        int lColor = Integer.parseInt("b2", 16);

//        Log.i(TAG,"getImgDrawable leftMargin : " +leftMargin+" l : "+l);
//        Log.i(TAG,"getImgDrawable rRed : " +rRed+" rGreen : "+rGreen+" rBlue : "+rBlue +" lColor : "+lColor);

        String cRed = Integer.toHexString(lColor+ (int)((rRed - lColor)*(leftMargin/maxLeftMargin)));
        String cGreen = Integer.toHexString(lColor+ (int)((rGreen - lColor)*(leftMargin/maxLeftMargin)));
        String cBlue = Integer.toHexString(lColor+ (int)((rBlue - lColor)*(leftMargin/maxLeftMargin)));

//        Log.i(TAG,"getImgDrawable cRed : " +cRed+" cGreen : "+cGreen+" cBlue : "+cBlue);

        if(drawable == null){
            drawable = new ShapeDrawable(new OvalShape());
            int width = CommonUtil.dip2px(getContext(), 20);
            drawable.setIntrinsicWidth(width);
            drawable.setIntrinsicHeight(width);
        }


        drawable.getPaint().setColor(Color.parseColor("#"+cRed+cGreen+cBlue));

        return  drawable;


    }


    OnTouchListener touchListener = new OnTouchListener() {


        @Override
        public boolean onTouch(View v, MotionEvent event) {

            int action = event.getAction() & MotionEvent.ACTION_MASK;
            switch (action) {

                case MotionEvent.ACTION_DOWN:

                    startX = event.getRawX();
                    startTime = System.currentTimeMillis();
                    break;

                case MotionEvent.ACTION_MOVE:
                    float curX = event.getRawX();
                    float v1 = curX - startX;

                    if(!isOn){
                        leftMargin = v1;
                    }else{
                        leftMargin = maxLeftMargin + v1;
                    }

                    Log.i(TAG,"getImgDrawable isOn : " +isOn+" curX : "+curX+" startX : "+startX+" leftMargin : "+leftMargin );

                    moveImage(leftMargin);

                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:

                    long endTime = System.currentTimeMillis();
                    if(endTime - startTime<1000){
                        isOn = !isOn;
                    }else {
                        if(leftMargin<=maxLeftMargin/2){
                            isOn = false;
                        }else{
                            isOn = true;
                        }
                    }

                    ScrollAnimation animation = new ScrollAnimation(leftMargin);
                    animation.setDuration(200);
                    dotImg.startAnimation(animation);

                    break;

            }

            return true;
        }
    };


    private class ScrollAnimation extends Animation {

        private float left;

        public ScrollAnimation(final float leftMargin) {

            left = leftMargin;

            this.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationEnd(Animation arg0) {
                    LayoutParams params = (LayoutParams) dotImg.getLayoutParams();
                    if(!isOn){
                        params.leftMargin = 0;
                    }else {
                        params.leftMargin = maxLeftMargin;
                    }
                    dotImg.setLayoutParams(params);

                }

                @Override
                public void onAnimationRepeat(Animation arg0) {

                }

                @Override
                public void onAnimationStart(Animation arg0) {

                }
            });
        }

        @Override
        final protected void applyTransformation(float interpolatedTime, Transformation t) {

            if(!isOn){
                leftMargin = left - left* interpolatedTime;
            }else{
                leftMargin = left + (maxLeftMargin - left) * interpolatedTime;
            }

            moveImage(leftMargin);
        }
    }


}
