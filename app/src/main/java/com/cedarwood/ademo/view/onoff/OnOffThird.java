package com.cedarwood.ademo.view.onoff;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cedarwood.ademo.R;
import com.cedarwood.ademo.utils.CommonUtil;
import com.cedarwood.ademo.utils.Log;

/**
 * Created by wentongmen on 2016/5/13.
 */
public class OnOffThird extends RelativeLayout{


    private static final String TAG = OnOffThird.class.getSimpleName();

    private ImageView dotImg;
    private ShapeDrawable drawable;
    private View lineView;

    private boolean isOn = false;
    public float startX;
    public long startTime;


    private float leftMargin = 0;
    private int maxLeftMargin;
    private int imgWidth;

    private int defaultWidth;
    private int defaultHeight;

    private int padding;

    private int bgColor;
    private int bgStrokeColor;
    private int bgStrokeWidth;

    private int dotOffColor;
    private int dotOnColor;

    private int animDuration = 200;



    public OnOffThird(Context context) {
        this(context,null);

    }

    public OnOffThird(Context context, AttributeSet attrs) {
        super(context, attrs);

        isInEditMode();

        initAttr(context, attrs);

        init();
    }

    private void initAttr(Context context, AttributeSet attrs) {

        defaultWidth = CommonUtil.dip2px(getContext(),56);
        defaultHeight = CommonUtil.dip2px(getContext(),26);
        padding = CommonUtil.dip2px(getContext(), 3);
        bgColor = getResources().getColor(R.color.co_global_white);
        bgStrokeColor = getResources().getColor(R.color.co_global_gray_deep);
        bgStrokeWidth = CommonUtil.dip2px(getContext(),5);
        dotOffColor = getResources().getColor(R.color.co_global_gray);
        dotOnColor = getResources().getColor(R.color.co_global_red);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OnOff);

        padding = a.getDimensionPixelSize(R.styleable.OnOff_padding, padding);
        bgColor=a.getColor(R.styleable.OnOff_bgColor, bgColor);
        bgStrokeColor=a.getColor(R.styleable.OnOff_bgStrokeColor, bgStrokeColor);
        bgStrokeWidth = a.getDimensionPixelSize(R.styleable.OnOff_bgStrokeWidth, bgStrokeWidth);
        dotOffColor=a.getColor(R.styleable.OnOff_dotOffColor, dotOffColor);
        dotOnColor=a.getColor(R.styleable.OnOff_dotOnColor, dotOnColor);
        animDuration=a.getInt(R.styleable.OnOff_animScrollDuration, animDuration);

        a.recycle();
    }

    private void init() {




        lineView = new View(getContext());
        LayoutParams params1 = new LayoutParams(LayoutParams.MATCH_PARENT, bgStrokeWidth);
        params1.addRule(RelativeLayout.CENTER_VERTICAL);
        lineView.setLayoutParams(params1);
        lineView.setBackgroundColor(getBgColor());


        addView(lineView,params1);

        dotImg = new ImageView(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        dotImg.setLayoutParams(params);
        addView(dotImg,params);

        dotImg.setOnTouchListener(touchListener);







        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isOn = !isOn;
                ScrollAnimation animation = new ScrollAnimation(leftMargin);
                animation.setDuration(200);
                dotImg.startAnimation(animation);
            }
        });





    }


    private boolean isOpen(){
        return isOn;
    }

    private void setOpen(boolean isOn){
        this.isOn = isOn;
        startAnim(0);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if(maxLeftMargin == 0){
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();

            Log.i(TAG,"onMeasure width : "+width+" height : "+height );

            if(width<=0){
                width = defaultWidth;
            }

            if(height<=0){
                height = defaultHeight;
            }

            setPadding(padding,padding,padding,padding);

            ViewGroup.LayoutParams layoutParams = lineView.getLayoutParams();
            layoutParams.width = width-padding*2;
            lineView.setLayoutParams(layoutParams);


            imgWidth = (height-padding*2);
            LayoutParams params = (LayoutParams) dotImg.getLayoutParams();
            params.height = imgWidth;
            params.width = imgWidth;
            dotImg.setLayoutParams(params);
            dotImg.setImageDrawable(getImgDrawable());

            maxLeftMargin = width-padding*2-imgWidth;
        }
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
        lineView.setBackgroundColor(getBgColor());
    }


    private Drawable getImgDrawable(){

        int rRed = Color.red(dotOnColor);
        int rGreen = Color.green(dotOnColor);
        int rBlue = Color.blue(dotOnColor);
        int lRed = Color.red(dotOffColor);
        int lGreen = Color.green(dotOffColor);
        int lBlue = Color.blue(dotOffColor);

//        Log.i(TAG,"getImgDrawable leftMargin : " +leftMargin+" l : "+l);
//        Log.i(TAG,"getImgDrawable rRed : " +rRed+" rGreen : "+rGreen+" rBlue : "+rBlue +" lColor : "+lColor);

        String cRed = Integer.toHexString(lRed+ (int)((rRed - lRed)*(leftMargin/maxLeftMargin)));
        String cGreen = Integer.toHexString(lGreen+ (int)((rGreen - lGreen)*(leftMargin/maxLeftMargin)));
        String cBlue = Integer.toHexString(lBlue+ (int)((rBlue - lBlue)*(leftMargin/maxLeftMargin)));

//        Log.i(TAG,"getImgDrawable cRed : " +cRed+" cGreen : "+cGreen+" cBlue : "+cBlue);

        if(drawable == null){
            drawable = new ShapeDrawable(new OvalShape());
            drawable.setIntrinsicWidth(imgWidth);
            drawable.setIntrinsicHeight(imgWidth);
        }


        drawable.getPaint().setColor(Color.parseColor("#"+cRed+cGreen+cBlue));

        return  drawable;


    }

    private int getBgColor(){


        int rRed = Color.red(dotOnColor);
        int rGreen = Color.green(dotOnColor);
        int rBlue = Color.blue(dotOnColor);
        int lRed = Color.red(dotOffColor);
        int lGreen = Color.green(dotOffColor);
        int lBlue = Color.blue(dotOffColor);

//        Log.i(TAG,"getImgDrawable leftMargin : " +leftMargin+" l : "+l);
//        Log.i(TAG,"getImgDrawable rRed : " +rRed+" rGreen : "+rGreen+" rBlue : "+rBlue +" lColor : "+lColor);

        String cRed = Integer.toHexString(lRed+ (int)((rRed - lRed)*(leftMargin/maxLeftMargin)));
        String cGreen = Integer.toHexString(lGreen+ (int)((rGreen - lGreen)*(leftMargin/maxLeftMargin)));
        String cBlue = Integer.toHexString(lBlue+ (int)((rBlue - lBlue)*(leftMargin/maxLeftMargin)));

//        Log.i(TAG,"getImgDrawable cRed : " +cRed+" cGreen : "+cGreen+" cBlue : "+cBlue);

//        if(drawable == null){
//            drawable = new ShapeDrawable(new RectShape());
//            drawable.setIntrinsicWidth(imgWidth);
//            drawable.setIntrinsicHeight(imgWidth);
//        }


        return Color.parseColor("#"+cRed+cGreen+cBlue);




    }

    private void startAnim(int duration){
        ScrollAnimation animation = new ScrollAnimation(leftMargin);
        animation.setDuration(duration);
        dotImg.startAnimation(animation);
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

                    startAnim(animDuration);

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
