package com.cedarwood.ademo.view.picker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


public class PickerView extends RelativeLayout {

    public int ITEM_HEIGHT = 43;
    
    private float paintWidth=0;

    private PickerScrollView pickerScrollView;

    // scroll position
    private int totalHeight = 0;

    public PickerView(Context context) {
        super(context);
        init();
    }

    public PickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public void init() {
    	paintWidth = dp2px(getContext(), 0.5f);
        ITEM_HEIGHT = dp2px(getContext(), ITEM_HEIGHT);
        pickerScrollView = new PickerScrollView(getContext());
        pickerScrollView.init(ITEM_HEIGHT);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(pickerScrollView, params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (totalHeight == 0) {
            totalHeight = getHeight();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Rect rect = new Rect(-5, (int) (totalHeight / 2 - ITEM_HEIGHT / 2), getWidth() + 5, (int) (totalHeight / 2 + ITEM_HEIGHT / 2));
        Paint paint = new Paint();

//        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(Color.argb(32, 0, 0, 192));
//        canvas.drawRect(rect, paint);

//        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(Color.argb(192, 0, 0, 192));
//        canvas.drawRect(rect, paint);

        paint.setColor(Color.rgb(231, 231, 231));
        paint.setStrokeWidth(paintWidth);
        canvas.drawLine(-5,(int) (totalHeight / 2 - ITEM_HEIGHT / 2),getWidth() + 5,(int) (totalHeight / 2 - ITEM_HEIGHT / 2),paint);
        canvas.drawLine(-5,(int) (totalHeight / 2 + ITEM_HEIGHT / 2),getWidth() + 5,(int) (totalHeight / 2 + ITEM_HEIGHT / 2),paint);

    }

    public PickerScrollView getPickerScrollView() {
        return pickerScrollView;
    }
}
