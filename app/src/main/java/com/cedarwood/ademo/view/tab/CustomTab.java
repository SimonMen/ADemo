package com.cedarwood.ademo.view.tab;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by wentongmen on 2016/2/26.
 */
public class CustomTab extends LinearLayout {


    private TextView text;
    private View line;
    private int lineWidth = 2;
    private int lineHeight = 30;

    private int lineColor= Color.parseColor("#12aaeb");

    public CustomTab(Context context) {
        super(context);
        init(context);
    }



    public CustomTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        line = new View(context);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(lineWidth, lineHeight);
        line.setLayoutParams(params1);
        line.setBackgroundColor(lineColor);
        addView(line, params1);


        text = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        text.setLayoutParams(params);
        text.setFocusable(false);
        text.setBackgroundResource(0);
        text.setGravity(Gravity.CENTER);
        addView(text, params);
    }


    public void setLineInvisible(){
        line.setVisibility(View.INVISIBLE);
    }

    public void setLineVisible(){
        line.setVisibility(View.VISIBLE);
    }

    public void setLineColor(int color){
        lineColor = color;
        line.setBackgroundColor(color);
    }

    public void setLineSize(int width,int height){
        lineWidth = width;
        lineHeight = height;
        LinearLayout.LayoutParams params = (LayoutParams) line.getLayoutParams();
        params.width = lineWidth;
        params.height = lineHeight;
        line.setLayoutParams(params);
    }

    public float getLineWidth(){
        return lineWidth;
    }

    public TextView getTextView(){
        return text;
    }


    public void setText(String tab){
        text.setText(tab);
    }
    public void setTextColor(int color){
        text.setTextColor(color);
    }

    public void setTextSize(int size){
        text.setTextSize(size);
    }



    private int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);

    }

}
