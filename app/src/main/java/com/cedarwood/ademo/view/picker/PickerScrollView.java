package com.cedarwood.ademo.view.picker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;


public class PickerScrollView extends ScrollView {

    public int ITEM_HEIGHT = 43;
    public int EXTRA_HEIGHT = 40;
    public int MAX_SPEED = 1000;
    public int TEXT_NORMAL_SIZE = 16;
    public int TEXT_SELECTED_SIZE = 18;
    public int TEXT_NORMAL_COLOR = Color.rgb(182, 182, 182);
    public int TEXT_SELECTED_COLOR = Color.rgb(119, 119, 119);
    public int TEXT_PADDING = 5;

    public int CLICK_RADIUS_SQUARE = 100;

    private ArrayList<TextView> itemViewList;
    // scroll position
    private int totalHeight = 0;
    private LinearLayout container;
    private boolean isFly;
    private int selectedIndex;
    private OnItemChangedListener onItemChangedListener;
    private OnClickListener onClickListener;
    private float startX;
    private float startY;

    private boolean isClick = false;

    public PickerScrollView(Context context) {
        super(context);
    }

    public PickerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (isFly) {
            if (Math.abs(oldt - t) <= 1) {
                isFly = false;
                scrollToNearestItem();
            } else if (t < EXTRA_HEIGHT - totalHeight - ITEM_HEIGHT || t > EXTRA_HEIGHT + ITEM_HEIGHT * itemViewList.size() + ITEM_HEIGHT) {
                isFly = false;
                scrollToNearestItem();
            }
        }
        int curIndex = getCurIndex();
        if (selectedIndex != curIndex) {
            if (selectedIndex < itemViewList.size() && selectedIndex >= 0) {
                TextView lastTextView = itemViewList.get(selectedIndex);
                TextView curTextView = itemViewList.get(curIndex);
                updateOtherTextView(lastTextView);
                updateCurTextView(curTextView);
                if (onItemChangedListener != null) {
                    onItemChangedListener.OnItemChanged(curTextView, selectedIndex, curIndex);
                }
            }
            selectedIndex = curIndex;
        }
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        TextView lastTextView = itemViewList.get(this.selectedIndex);
        TextView curTextView = itemViewList.get(selectedIndex);
        updateOtherTextView(lastTextView);
        updateCurTextView(curTextView);
        this.selectedIndex = selectedIndex;
        scrollTo(0, EXTRA_HEIGHT - totalHeight / 2 + ITEM_HEIGHT / 2 + selectedIndex * ITEM_HEIGHT);
        postInvalidate();
    }

    public void setOnItemChangedListener(OnItemChangedListener onItemChangedListener) {
        this.onItemChangedListener = onItemChangedListener;
    }

    public LinearLayout getContainer() {
        return container;
    }

    public void init(int itemHeight) {
        isClick = false;
        isFly = false;
        ITEM_HEIGHT = itemHeight;
        EXTRA_HEIGHT = ITEM_HEIGHT * 50;
        TEXT_PADDING = dp2px(getContext(), TEXT_PADDING);
        CLICK_RADIUS_SQUARE = dp2px(getContext(), 100);

        container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(container, params);
        setVerticalScrollBarEnabled(false);
        itemViewList = new ArrayList<TextView>();
        setSmoothScrollingEnabled(true);
    }

    public void updateData(ArrayList<String> strings) {
        selectedIndex = 0;
        container.removeAllViews();
        itemViewList.clear();
        View header = new View(getContext());
        LinearLayout.LayoutParams hparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, EXTRA_HEIGHT);
        header.setLayoutParams(hparams);
        container.addView(header, hparams);
        for (int i = 0; i < strings.size(); i++) {
            addItem(strings.get(i), i);
        }

        View footer = new View(getContext());
        LinearLayout.LayoutParams fparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, EXTRA_HEIGHT);
        header.setLayoutParams(fparams);
        container.addView(footer, fparams);
    }
    
    public void updateData(String[] strings) {
        selectedIndex = 0;
        container.removeAllViews();
        itemViewList.clear();
        View header = new View(getContext());
        LinearLayout.LayoutParams hparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, EXTRA_HEIGHT);
        header.setLayoutParams(hparams);
        container.addView(header, hparams);
        for (int i = 0; i < strings.length; i++) {
            addItem(strings[i], i);
        }

        View footer = new View(getContext());
        LinearLayout.LayoutParams fparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, EXTRA_HEIGHT);
        header.setLayoutParams(fparams);
        container.addView(footer, fparams);
    }

    public void addItem(String str, int position) {
        TextView textView = new TextView(getContext());
        textView.setPadding(TEXT_PADDING, TEXT_PADDING, TEXT_PADDING, TEXT_PADDING);
        textView.setText(str);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_NORMAL_SIZE);
        textView.setGravity(Gravity.CENTER);
        updateOtherTextView(textView);
        LinearLayout.LayoutParams fparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ITEM_HEIGHT);
        textView.setLayoutParams(fparams);
        itemViewList.add(textView);
        container.addView(textView, fparams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 初始化自己的大小和开始的位置
        if (totalHeight == 0) {
            totalHeight = getHeight();
            if (selectedIndex < itemViewList.size() && selectedIndex >= 0) {
                TextView textView = itemViewList.get(selectedIndex);
                updateCurTextView(textView);
            }
            scrollTo(0, EXTRA_HEIGHT - totalHeight / 2 + ITEM_HEIGHT / 2 + selectedIndex * ITEM_HEIGHT);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            if (!isFly) {
                startX = ev.getX();
                startY = ev.getY();
                isClick = true;
            }
        } else if (action == MotionEvent.ACTION_UP) {
            if (isClick) {
                isClick = false;
                boolean ret = super.onTouchEvent(ev);
                handleClickByTouch();
                return ret;
            } else {
                isFly = false;
                boolean ret = super.onTouchEvent(ev);
                // 如果不是scroll fly
                if (!isFly) {
                    scrollToNearestItem();
                }
                return ret;
            }
        }
        if (isClick) {
            int dx = (int) (ev.getX() - startX);
            int dy = (int) (ev.getY() - startY);
            if (dx * dx + dy * dy > CLICK_RADIUS_SQUARE) {
                isClick = false;
            }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void fling(int velocityY) {

        super.fling(Math.max(Math.min(velocityY, MAX_SPEED), -MAX_SPEED));
        if (Math.abs(velocityY) > 0) isFly = true;
    }

    private void updateCurTextView(TextView textView) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SELECTED_SIZE);
//        textView.setTextColor(Color.BLACK);
//        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(TEXT_SELECTED_COLOR);
        textView.setTypeface(null, Typeface.NORMAL);
    }

    private void updateOtherTextView(TextView textView) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_NORMAL_SIZE);
        textView.setTextColor(TEXT_NORMAL_COLOR);
        textView.setTypeface(null, Typeface.NORMAL);
    }

    private int getCurIndex() {
        int index = Math.round((getScrollY() - (EXTRA_HEIGHT - totalHeight / 2)) / ITEM_HEIGHT);
        return Math.max(Math.min(index, itemViewList.size() - 1), 0);

    }

    private void scrollToNearestItem() {
        if (itemViewList.size() <= 0) {
            smoothScrollTo(0, EXTRA_HEIGHT - totalHeight / 2 + ITEM_HEIGHT / 2);
            selectedIndex = 0;
        } else {
            int nearestIndex = getCurIndex();
            smoothScrollTo(0, EXTRA_HEIGHT - totalHeight / 2 + ITEM_HEIGHT / 2 + nearestIndex * ITEM_HEIGHT);
        }
    }

    private void handleClickByTouch() {
//        Log.d("onclick", startX + "," + startY);
        if (startY >= (totalHeight - ITEM_HEIGHT) / 2 && startY <= (totalHeight + ITEM_HEIGHT) / 2) {

//            Log.d("onclick", startX + "," + startY);
            if (onClickListener != null && itemViewList.size() > 0) {
                onClickListener.onClick(itemViewList.get(selectedIndex));
            }
            return;
        }
        int scrollY = (int) (getScrollY() + startY);
        int index = Math.round((scrollY - EXTRA_HEIGHT) / ITEM_HEIGHT);
        index = Math.max(Math.min(index, itemViewList.size() - 1), 0);

//        Log.d("onclick", getScrollY() + "," + index);
        smoothScrollTo(0, EXTRA_HEIGHT - totalHeight / 2 + ITEM_HEIGHT / 2 + index * ITEM_HEIGHT);
    }

    public interface OnItemChangedListener {
        public void OnItemChanged(View TextView, int lastIndex, int newIndex);
    }
}
