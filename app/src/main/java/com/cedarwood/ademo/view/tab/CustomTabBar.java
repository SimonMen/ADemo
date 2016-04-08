package com.cedarwood.ademo.view.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cedarwood.ademo.R;
import com.cedarwood.ademo.utils.Log;

import java.util.ArrayList;

/**
 * Created by Administrator on 2014/10/16.
 */
public class CustomTabBar extends RelativeLayout {

    private static final String TAG = CustomTabBar.class.getSimpleName();

    private static final int ANIM_NORMAL = 0;
    private static final int ANIM_TYPE_1 = 1;

    //默认动画时长
    private int animDuration = 300;
    //默认动画bar的高度 单位px
    private int barHeight = 3;
    //默认动画bar的最小高度 单位px  animType 为ANIM_TYPE_1 时有用
    private int barMinHeight =1;
    //默认动画bar的宽度 单位px
    private int barWidth = 70;

    //默认动画bar的宽度 和 每个item的比例
    private float barWidthScale=0.8f;

    //默认起始位置
    private int curTabIndex = 2;
    //默认选中的字体大小 单位sp
    private int selectedTextSize=18;
    //默认没有选中的字体大小 单位sp
    private int normalTextSize=15;
    //默认选中的字体颜色
    private int selectedTextColor= Color.parseColor("#12aaeb");
    //默认没有选中的字体颜色
    private int normalTextColor= Color.parseColor("#b2b2b2");
    //默认动画bar的颜色
    private int barColor= Color.parseColor("#12aaeb");
    //默认执行动画效果
    private boolean hasAnim=true;
    //tabBar 滑动效果 类型
    private int animType = ANIM_NORMAL;

    //分隔线宽度 px
    private int lineWidth = 2;
    //分隔线宽度 px
    private int lineHeight = 60;
    //分隔线颜色
    private int lineColor = Color.parseColor("#ff666c");


    private View bottomBar;
    private LinearLayout tabLayout;
    private ArrayList<View> tabList;
    private OnTabChangeListener onTabChangeListener;
    private boolean measured = true;
    private boolean toRight = false; //是否向右滑


    public CustomTabBar(Context context) {
        super(context);
        init(context);
    }


    public CustomTabBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        isInEditMode();

        initAttr(context, attrs);

        init(context);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTabBar);

        barHeight = a.getDimensionPixelSize(R.styleable.CustomTabBar_barHeight, barHeight);
        barMinHeight = a.getDimensionPixelSize(R.styleable.CustomTabBar_barMiniHeight, barMinHeight);
        barWidthScale=a.getFloat(R.styleable.CustomTabBar_barWidthScale, barWidthScale);
        barColor=a.getColor(R.styleable.CustomTabBar_barColor, barColor);
        curTabIndex = a.getInt(R.styleable.CustomTabBar_curTabIndex, curTabIndex);

        float selectedSize=a.getDimension(R.styleable.CustomTabBar_selectedTextSize, 0);
        if(selectedSize!=0){
            selectedTextSize = px2sp(context, selectedSize);
        }

        float normalSize= a.getDimension(R.styleable.CustomTabBar_normalTextSize,0);
        if(normalSize!=0){
            normalTextSize = px2sp(context, normalSize);
        }
        selectedTextColor=a.getColor(R.styleable.CustomTabBar_selectedTextColor, selectedTextColor);
        normalTextColor=a.getColor(R.styleable.CustomTabBar_normalTextColor,normalTextColor);


        hasAnim=a.getBoolean(R.styleable.CustomTabBar_hasAnim, hasAnim);
        animDuration=a.getInt(R.styleable.CustomTabBar_animDuration, animDuration);
        animType=a.getInt(R.styleable.CustomTabBar_animType, animType);

        lineWidth= a.getDimensionPixelSize(R.styleable.CustomTabBar_lineWidth, lineWidth);
        lineHeight =  a.getDimensionPixelSize(R.styleable.CustomTabBar_lineHeight, lineHeight);
        lineColor=a.getColor(R.styleable.CustomTabBar_lineColor, lineColor);

        Log.i(TAG, "initAttr selectedTextSize : " + selectedTextSize+"  normalTextSize : "+normalTextSize);
    }

    private void init(Context context) {
        tabList = new ArrayList<View>();
        // tab layout
        tabLayout = new LinearLayout(context);
        tabLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tabLayout.setLayoutParams(params);
        tabLayout.setGravity(Gravity.CENTER_VERTICAL);
        addView(tabLayout, params);

        // tab bottom animation bar
        bottomBar = new View(context);
        bottomBar.setBackgroundColor(barColor);
        params = new LayoutParams(barWidth, barHeight);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(bottomBar, params);

    }


    public void setAnimDuration(int animDuration) {
        this.animDuration = animDuration;
    }


    public void setBarHeight(float barHeight) {
        this.barHeight = dp2px(getContext(),barHeight);
    }


    public void setBarWidthScale(float barWidthScale) {
        this.barWidthScale = barWidthScale;
    }


    public int getCurTabIndex() {
        return curTabIndex;
    }

    public void setCurTabIndex(int curTabIndex) {
        this.curTabIndex = curTabIndex;
    }


    public void setSelectedTextSize(int selectedTextSize) {
        this.selectedTextSize = selectedTextSize;
    }

    public void setNormalTextSize(int normalTextSize) {
        this.normalTextSize = normalTextSize;
    }

    public void setSelectedTextColor(int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
    }


    public void setNormalTextColor(int normalTextColor) {
        this.normalTextColor = normalTextColor;
    }


    public void setBarColor(int barColor) {
        this.barColor = barColor;
    }

    public boolean isHasAnim() {
        return hasAnim;
    }

    public void setHasAnim(boolean hasAnim) {
        this.hasAnim = hasAnim;
    }

    //lineWidth  dp
    public void setLineWidth(float lineWidth) {
        this.lineWidth = dp2px(getContext(),lineWidth);
    }
    //lineHeight  dp
    public void setLineHeight(float lineHeight) {
        this.lineHeight = dp2px(getContext(),lineHeight);
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public void addTabs(ArrayList<String> tabs){

        if(tabList.size()>0){
            tabList.clear();
            tabLayout.removeAllViews();
        }

        for(int i=0;i<tabs.size();i++){

            String tab = tabs.get(i);

            CustomTab item = new CustomTab(getContext());
            item.setFocusable(false);
            item.setBackgroundResource(0);
            item.setTextColor(normalTextColor);
            item.setTextSize(normalTextSize);
            item.setText(tab);

            item.setLineColor(lineColor);
            item.setLineSize(lineWidth, lineHeight);
            Log.i(TAG, "addTabs lineHeight : " + lineHeight);
            if(i==0){
                item.setLineInvisible();
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            item.setGravity(Gravity.CENTER);
            tabLayout.addView(item, params);
            tabList.add(item);
            final int pos=i;
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeTabTo(pos, hasAnim);
                }
            });
        }

        measured=false;
        requestLayout();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        updateAnimationBar();
    }

    private void updateAnimationBar() {
        if (tabList.size() > 0 && !measured) {
//            int tabWidth = getMeasuredWidth() / tabList.size();

            if(curTabIndex < 0 || curTabIndex >= tabList.size()){
                curTabIndex=0;
            }

            CustomTab b= (CustomTab) tabList.get(curTabIndex);
            b.setTextSize(selectedTextSize);
            b.setTextColor(selectedTextColor);
            int width = b.getMeasuredWidth();

            LayoutParams bottomParams = (LayoutParams) bottomBar.getLayoutParams();
            bottomParams.height = barHeight;

            bottomParams.width = (int) (width * barWidthScale);

            int leftDiff =(int) (width*(1-barWidthScale)/2);
            int left = 0;

            for(int i=0;i<curTabIndex;i++){
                left+= ((CustomTab) tabList.get(i)).getMeasuredWidth();
            }
            left += leftDiff+lineWidth;

            Log.i(TAG,"updateAnimationBar width : "+width+"  b.getRight() : "+b.getRight()+"  b.getLeft() : "+b.getLeft()+"  left : "+left);

            bottomParams.setMargins(left, bottomParams.topMargin, bottomParams.rightMargin, bottomParams.bottomMargin);
            bottomBar.setLayoutParams(bottomParams);
            measured = true;


        }
    }

    public void goToIndex(int index){
        changeTabTo(index,hasAnim);
    }

    public void goToIndex(int index,boolean hasAnim){
        changeTabTo(index, hasAnim);
    }

    public void setOnTabChangeListener(OnTabChangeListener onTabChangeListener) {
        this.onTabChangeListener = onTabChangeListener;
    }


    private void changeTabTo(int newIndex, boolean hasAnimation) {

        if (newIndex >= 0 && newIndex < tabList.size() && curTabIndex != newIndex) {

            boolean change = true;
            if (onTabChangeListener != null) {
                change = onTabChangeListener.onTabChanged(newIndex);
            }


            if (false) {
                CustomTab newBtn = (CustomTab) tabList.get(newIndex);
                CustomTab oldBtn= (CustomTab) tabList.get(curTabIndex);

                oldBtn.setTextSize(normalTextSize);
                oldBtn.setTextColor(normalTextColor);
                newBtn.setTextSize(selectedTextSize);
                newBtn.setTextColor(selectedTextColor);


                int startLeftDiff =(int) ((oldBtn.getRight()-oldBtn.getLeft())*(1-barWidthScale)/2);
                int endLeftDiff =(int) ((newBtn.getRight()-newBtn.getLeft())*(1-barWidthScale)/2);
                int startLeft =oldBtn.getLeft()+startLeftDiff+lineWidth;
                int endLeft =newBtn.getLeft()+endLeftDiff+lineWidth;

                int oldLen = (oldBtn.getRight()-oldBtn.getLeft())-startLeftDiff*2-lineWidth;
                int newLen = (newBtn.getRight()-newBtn.getLeft())-endLeftDiff*2-lineWidth;

                int diff = startLeftDiff+endLeftDiff+lineWidth;

                curTabIndex = newIndex;
                if (hasAnimation) {
                    startBottomAnimation(oldLen,newLen,startLeft,endLeft,diff, animDuration);
                } else {
                    startBottomAnimation(oldLen,newLen,startLeft,endLeft,diff, 0);
                }
            }
        }
    }


    public interface OnTabChangeListener {
        public boolean onTabChanged(int newIndex);
    }


    private void startBottomAnimation(int oldLen, final int newLen,int startLeft,int endLeft,int diff, int duration) {
        SwitchTabAnimation animation = new SwitchTabAnimation(oldLen,newLen,startLeft,endLeft,diff);
        animation.setDuration(duration);
        startAnimation(animation);
    }

    public final class SwitchTabAnimation extends Animation {

        private int startLeftMargin;
        private int endLeftMargin;
        private int startLen =0;
        private int endLen =0;
        private int diff =0;

        public SwitchTabAnimation(int oldLen, final int newLen,int startLeft,int endLeft,int diff) {
            setRepeatCount(0);
            setInterpolator(new AccelerateDecelerateInterpolator());
            startLeftMargin = startLeft;
            endLeftMargin = endLeft;
            this.diff= diff;

            startLen = oldLen;
            endLen = newLen;

            this.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    LayoutParams bottomParams = (LayoutParams) bottomBar.getLayoutParams();
                    bottomParams.width= endLen ;
                    bottomParams.height=barHeight;
                    bottomParams.setMargins((int) endLeftMargin, bottomParams.topMargin, bottomParams.rightMargin, bottomParams.bottomMargin);
                    bottomBar.setLayoutParams(bottomParams);
                    CustomTabBar.this.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        @Override
        final protected void applyTransformation(float interpolatedTime, Transformation t) {
//            LayoutParams bottomParams = (LayoutParams) bottomBar.getLayoutParams();
//            bottomParams.width= (int)(startLen+(endLen-startLen)*interpolatedTime);
//            bottomParams.height=barHeight;
//            bottomParams.setMargins((int) (startLeftMargin + (endLeftMargin - startLeftMargin) * interpolatedTime), bottomParams.topMargin, bottomParams.rightMargin, bottomParams.bottomMargin);
//            bottomBar.setLayoutParams(bottomParams);

            setAnimType(startLen,endLen,startLeftMargin,endLeftMargin,diff,interpolatedTime);
        }
    }




    private void moveBar(int position, float positionOffset){

        CustomTab oldBtn = (CustomTab) tabList.get(curTabIndex);
        CustomTab newBtn = (CustomTab) tabList.get(curTabIndex);
        float interpolatedTime = positionOffset;

        Log.i(TAG,"PageChangeListener moveBar position : "+position+"  curTabIndex : "+curTabIndex);

        if(position==curTabIndex && curTabIndex< tabList.size()-1){
            newBtn = (CustomTab) tabList.get(curTabIndex+1);
            toRight = true;
        }else if(position!=curTabIndex){
            newBtn = (CustomTab) tabList.get(position);
            interpolatedTime = 1-positionOffset;
            if(position>curTabIndex){
                toRight = true;
            }else{
                if(position== curTabIndex-2){  //处理连续滑动过程中出现的特殊情况
                    oldBtn = (CustomTab) tabList.get(curTabIndex);
                    newBtn = (CustomTab) tabList.get(curTabIndex-1);
                    interpolatedTime = positionOffset;
                }
                toRight = false;
            }
        }

        int startLeftDiff =(int) ((oldBtn.getRight()-oldBtn.getLeft())*(1-barWidthScale)/2);
        int endLeftDiff =(int) ((newBtn.getRight()-newBtn.getLeft())*(1-barWidthScale)/2);
        int startLeftMargin =oldBtn.getLeft()+startLeftDiff+lineWidth;
        int endLeftMargin =newBtn.getLeft()+endLeftDiff+lineWidth;

        int startLen = (oldBtn.getRight()-oldBtn.getLeft())-startLeftDiff*2-lineWidth;
        int endLen = (newBtn.getRight()-newBtn.getLeft())-endLeftDiff*2-lineWidth;

        int diff = startLeftDiff+endLeftDiff+lineWidth;
        setAnimType(startLen,endLen,startLeftMargin,endLeftMargin,diff,interpolatedTime);

        if((toRight && position==curTabIndex+1 ) || (!toRight && (position== curTabIndex-1 && positionOffset==0)) ){
            curTabIndex = position;
        }else if(!toRight && position== curTabIndex-2 ){  //处理连续滑动过程中出现的特殊情况
            curTabIndex = curTabIndex-1;
        }

//        if((toRight && position==curTabIndex+1 ) || (!toRight && (position== curTabIndex-1 && positionOffset==0)) ){
//            oldBtn.setTextSize(normalTextSize);
//            oldBtn.setTextColor(normalTextColor);
//            newBtn.setTextSize(selectedTextSize);
//            newBtn.setTextColor(selectedTextColor);
//            curTabIndex = position;
//        }else if(!toRight && position== curTabIndex-2 ){  //处理连续滑动过程中出现的特殊情况
//            oldBtn.setTextSize(normalTextSize);
//            oldBtn.setTextColor(normalTextColor);
//            newBtn.setTextSize(selectedTextSize);
//            newBtn.setTextColor(selectedTextColor);
//            curTabIndex = curTabIndex-1;
//        }
    }

    private void setAnimType(int oldLen, final int newLen,int startLeft,int endLeft,int diff,float interpolatedTime){

        LayoutParams bottomParams = (LayoutParams) bottomBar.getLayoutParams();
        int width = barWidth;
        int height = barHeight;

        int left = 0;
        int bottom = bottomParams.bottomMargin;

        if(animType == ANIM_NORMAL){

            width = (int)(oldLen+(newLen-oldLen)*interpolatedTime);
            height = barHeight;
            left = (int) (startLeft + (endLeft - startLeft) * interpolatedTime);
            bottom = bottomParams.bottomMargin;

        }else if(animType == ANIM_TYPE_1){
            width = (int)(-1*(2*newLen+2*oldLen+4*diff)*interpolatedTime*interpolatedTime+(3*newLen+oldLen+4*diff)*interpolatedTime+oldLen);
            height = (int)(4*(barHeight-barMinHeight)*interpolatedTime*interpolatedTime-4*(barHeight-barMinHeight)*interpolatedTime+barHeight);
            if(toRight){
                int r = endLeft +newLen;
                left = (int)(interpolatedTime<0.5?startLeft : r-width);
            }else{
                int r = startLeft + oldLen;
                left = (int)(interpolatedTime<0.5? r-width : endLeft);
            }
            bottom =  (barHeight-height)/2;
        }

        bottomParams.width= width;
        bottomParams.height=height;
        bottomParams.setMargins(left, bottomParams.topMargin, bottomParams.rightMargin, bottom);
        bottomBar.setLayoutParams(bottomParams);
    }


    private void setTextChange(int pos){

        for(int i=0;i<tabList.size();i++){
            CustomTab tab = (CustomTab) tabList.get(i);
            if(i==pos){
                tab.setTextSize(selectedTextSize);
                tab.setTextColor(selectedTextColor);
            }else {
                tab.setTextSize(normalTextSize);
                tab.setTextColor(normalTextColor);
            }
        }
    }



    public static class PageChangeListener implements ViewPager.OnPageChangeListener {


        private CustomTabBar bar;
        public PageChangeListener(CustomTabBar bar){
            this.bar = bar;
        }


        private int s = ViewPager.SCROLL_STATE_IDLE;
//        private int s = ViewPager.SCROLL_STATE_DRAGGING;
//        private int s = ViewPager.SCROLL_STATE_SETTLING;
        private int pos;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

//            Log.i(TAG,"PageChangeListener onPageScrolled position : "+position+"  positionOffset : "+positionOffset+"  positionOffsetPixels : "+positionOffsetPixels);
            if(bar!=null){
                bar.moveBar(position,positionOffset);
            }


        }

        @Override
        public void onPageSelected(int position) {
//            Log.i(TAG,"PageChangeListener onPageSelected position : "+position);
            pos = position;
            if(bar!=null){
                bar.setTextChange(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
//            Log.i(TAG,"PageChangeListener onPageScrollStateChanged state : "+state);
            s= state;
        }
    }






    private int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);

    }

    public  int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

}
