package com.cedarwood.ademo.view.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cedarwood.ademo.R;
import com.cedarwood.ademo.utils.Log;

import java.util.ArrayList;

/**
 * Created by Administrator on 2014/10/16.
 */
public class CustomScrollTabBar2 extends HorizontalScrollView {

    private static final String TAG = CustomScrollTabBar2.class.getSimpleName();
    //默认动画时长
    private int animDuration = 300;
    //默认动画bar的高度 单位px
    private int barHeight = 3;
    //默认动画bar的宽度 单位px
    private int barWidth = 70;

    //默认动画bar的宽度 和 每个item的比例
//    private float barWidthScale=0.8f;

    //默认起始位置
    private int curTabIndex = 2;
    //默认选中的字体大小 单位sp
    private int selectedTextSize=18;
    //默认没有选中的字体大小 单位sp
    private int normalTextSize=15;
    //默认选中的字体颜色
    private int selectedTextColor= Color.parseColor("#ff666c");
    //默认没有选中的字体颜色
    private int normalTextColor= Color.parseColor("#b2b2b2");
    //默认动画bar的颜色
    private int barColor= Color.parseColor("#ff666c");
    //默认执行动画效果
    private boolean hasAnim=true;
    //分隔线宽度 px
    private int lineWidth = 2;
    //分隔线宽度 px
    private int lineHeight = 60;
    //分隔线颜色
    private int lineColor = Color.parseColor("#ff666c");

    //默认距离左边边界的距离 px
    private int leftMargin=25;

    private LinearLayout tabLayout;
    private ArrayList<CustomTab> tabList;
    private OnTabChangeListener onTabChangeListener;
	private View bottomBar;
	private RelativeLayout titleLayout;
    private ArrayList<String> titles;
    private boolean measured;

    public CustomScrollTabBar2(Context context) {
        super(context);
        init(context);
    }


    public CustomScrollTabBar2(Context context, AttributeSet attrs) {
        super(context, attrs);

        isInEditMode();

        setHorizontalScrollBarEnabled(false);

        initAttr(context, attrs);

        init(context);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTabBar);

        barHeight = a.getDimensionPixelSize(R.styleable.CustomTabBar_barHeight, barHeight);
//        barWidthScale=a.getFloat(R.styleable.CustomTabBar_barWidthScale, barWidthScale);
        barColor=a.getColor(R.styleable.CustomTabBar_barColor, barColor);
        leftMargin =  a.getDimensionPixelSize(R.styleable.CustomTabBar_leftMargin, leftMargin);
        curTabIndex = a.getInt(R.styleable.CustomTabBar_curTabIndex, curTabIndex);

        float selectedSize=a.getDimension(R.styleable.CustomTabBar_selectedTextSize, 0);
        if(selectedSize!=0){
            selectedTextSize = px2sp(context, selectedSize);
        }

        float normalSize= a.getDimension(R.styleable.CustomTabBar_normalTextSize,0);
        if(normalSize!=0){
            normalTextSize = px2sp(context, normalSize);
        }
        selectedTextColor=a.getColor(R.styleable.CustomTabBar_selectedTextColor,selectedTextColor);
        normalTextColor=a.getColor(R.styleable.CustomTabBar_normalTextColor,normalTextColor);

        hasAnim=a.getBoolean(R.styleable.CustomTabBar_hasAnim, hasAnim);
        animDuration=a.getInt(R.styleable.CustomTabBar_animDuration, animDuration);

        lineWidth= a.getDimensionPixelSize(R.styleable.CustomTabBar_lineWidth, lineWidth);
        lineHeight =  a.getDimensionPixelSize(R.styleable.CustomTabBar_lineHeight, lineHeight);
        lineColor=a.getColor(R.styleable.CustomTabBar_lineColor, lineColor);
    }

    private void init(Context context) {
        tabList = new ArrayList<CustomTab>();

        titleLayout = new RelativeLayout(context);
        LayoutParams params  = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        addView(titleLayout,params);

        tabLayout = new LinearLayout(context);
        tabLayout.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tabLayout.setLayoutParams(params2);
        tabLayout.setGravity(Gravity.CENTER_VERTICAL);
        titleLayout.addView(tabLayout, params2);


        // tab bottom animation bar
        bottomBar = new View(context);
        bottomBar.setBackgroundColor(barColor);
        params2 = new RelativeLayout.LayoutParams(barWidth, barHeight);
        params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params2.leftMargin = leftMargin;
        params2.rightMargin = leftMargin;
        titleLayout.addView(bottomBar, params2);


    }

    public void setAnimDuration(int animDuration) {
        this.animDuration = animDuration;
    }

    public void setBarHeight(float barHeight) {
        this.barHeight = dp2px(getContext(),barHeight);
    }

//    public void setBarWidthScale(float barWidthScale) {
//        this.barWidthScale = barWidthScale;
//    }

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

	public void setLeftMargin(int leftMargin) {
		this.leftMargin = leftMargin;
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

        titles = tabs;

        for(int i=0;i<tabs.size();i++){

            String tab = tabs.get(i);

            CustomTab item = new CustomTab(getContext());
            item.setFocusable(false);
            item.setBackgroundResource(0);
            item.setTextColor(normalTextColor);
            item.setTextSize(selectedTextSize);
            item.setText(tab);

            item.setLineColor(lineColor);
            item.setLineSize(lineWidth, lineHeight);
//            Log.i(TAG, "addTabs lineHeight : " + lineHeight);
            if(i==0){
                item.setLineInvisible();
            }

            int width = (int)item.getTextView().getPaint().measureText(tab);
            item.setTextSize(normalTextSize);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.width = width+leftMargin*2+lineWidth;

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

            if(curTabIndex < 0 || curTabIndex >= tabList.size()){
                curTabIndex=0;
            }


            CustomTab b= (CustomTab) tabList.get(curTabIndex);
            b.setTextSize(selectedTextSize);
            b.setTextColor(selectedTextColor);

            int itemWidth = (int) b.getTextView().getPaint().measureText(titles.get(curTabIndex));
            RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams) bottomBar.getLayoutParams();
            params.width=itemWidth;
            params.height=barHeight;

            int left = 0;

            for(int i=0;i<curTabIndex;i++){
                left+= ((CustomTab) tabList.get(i)).getMeasuredWidth();
            }
            left += leftMargin+lineWidth;


            params.setMargins(left, params.topMargin, params.rightMargin, params.bottomMargin);
            bottomBar.setLayoutParams(params);

            Log.i(TAG, "params.width : " + params.width + "  params.height : " + params.height+"b.getLeft() : "+b.getLeft());

            measured = true;
        }


    }


    public void goToIndex(int index){
        changeTabTo(index,hasAnim);
    }
    
    public void goToIndex(int index,boolean hasAnim){
        changeTabTo(index,hasAnim);
    }


    public void setOnTabChangeListener(OnTabChangeListener onTabChangeListener) {
        this.onTabChangeListener = onTabChangeListener;
    }


    private void changeTabTo(int newIndex, boolean hasAnimation) {

        if (newIndex >= 0 && newIndex < tabList.size() && curTabIndex != newIndex) {

            CustomTab newBtn = (CustomTab) tabList.get(newIndex);
            CustomTab oldBtn= (CustomTab) tabList.get(curTabIndex);
            boolean change = true;
            if (onTabChangeListener != null) {
                change = onTabChangeListener.onTabChanged(newIndex);
            }
            if (change) {

                oldBtn.setTextSize(normalTextSize);
                oldBtn.setTextColor(normalTextColor);
                newBtn.setTextSize(selectedTextSize);
                newBtn.setTextColor(selectedTextColor);

                Log.i(TAG, "oldBtn : " + oldBtn.getLeft() + " .. " + oldBtn.getRight() + "newBtn : " + newBtn.getLeft() + " .. " + newBtn.getRight());

                int startLeft =oldBtn.getLeft()+leftMargin+lineWidth;
                int endLeft =newBtn.getLeft()+leftMargin+lineWidth;

                int oldLen = oldBtn.getRight()-oldBtn.getLeft()-leftMargin*2-lineWidth;
                int newLen = newBtn.getRight()-newBtn.getLeft()-leftMargin*2-lineWidth;

                curTabIndex = newIndex;
                if (hasAnimation) {
                    startBottomAnimation(oldLen,newLen,startLeft,endLeft, animDuration);
                } else {
                    startBottomAnimation(oldLen,newLen,startLeft,endLeft, 0);
                }


                smoothScrollTo(newBtn.getLeft()-leftMargin*4, 0);
            }
        }
    }
    


    public interface OnTabChangeListener {
        public boolean onTabChanged(int newIndex);
    }



    private void startBottomAnimation(int oldLen, final int newLen,int startLeft,int endLeft, int duration) {


        SwitchTabAnimation animation = new SwitchTabAnimation(oldLen,newLen,startLeft,endLeft);
        animation.setDuration(duration);
        startAnimation(animation);

    }


    public final class SwitchTabAnimation extends Animation {
        private int tabIndex;
        private int startLeftMargin;
        private int endLeftMargin;

        private int startLen =0;
        private int endLen =0;

        public SwitchTabAnimation(int oldLen, final int newLen,int startLeft,int endLeft) {
            setRepeatCount(0);
            setInterpolator(new AccelerateDecelerateInterpolator());
            startLeftMargin = startLeft;
            endLeftMargin = endLeft;

            startLen = oldLen;
            endLen = newLen;

            this.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {


                    RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams) bottomBar.getLayoutParams();
                    params.width= newLen ;
                    params.height=barHeight;
                    params.setMargins(endLeftMargin, params.topMargin, params.rightMargin, params.bottomMargin);
                    bottomBar.setLayoutParams(params);

                    CustomScrollTabBar2.this.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        @Override
        final protected void applyTransformation(float interpolatedTime, Transformation t) {
            RelativeLayout.LayoutParams bottomParams = (RelativeLayout.LayoutParams) bottomBar.getLayoutParams();
            bottomParams.width= (int)(startLen+(endLen-startLen)*interpolatedTime);
            bottomParams.height=barHeight;
            bottomParams.setMargins((int) (startLeftMargin + (endLeftMargin - startLeftMargin) * interpolatedTime),
                    bottomParams.topMargin,bottomParams.rightMargin , bottomParams.bottomMargin);
            bottomBar.setLayoutParams(bottomParams);
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
