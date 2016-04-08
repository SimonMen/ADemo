package com.cedarwood.ademo.view.drag;


import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cedarwood.ademo.R;

public class DragMark extends FrameLayout {

	
	
	
	
	private TextView text;
	private int screenWidth;
	private int screenHeight;



	public DragMark(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public DragMark(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		
//		screenWidth = CommonUtil.getScreenWidth(getContext());
//		screenHeight = CommonUtil.getScreenHeight(getContext());
		
		
//		text = new TextView(getContext());
//		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		params.leftMargin= dp2px(getContext(),10);
//		params.rightMargin = dp2px(getContext(),10);
//		params.topMargin = dp2px(getContext(),7);
////		params.bottomMargin = dp2px(getContext(),5);
////		params.gravity = Gravity.CENTER;
//		text.setLayoutParams(params);
//		
//		
//		text.setTextColor(Color.WHITE);
//		text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
//		
//		addView(text);
//		
//		FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(dp2px(getContext(), 35), dp2px(getContext(), 22));
//		params2.gravity = Gravity.CENTER_HORIZONTAL;
//		setLayoutParams(params2);
//		
//		
//		setBackgroundResource(R.mipmap.drag_mark_bg);

		RelativeLayout layout = new RelativeLayout(getContext());
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(dp2px(getContext(),53), dp2px(getContext(),33));
		layout.setLayoutParams(params);
		layout.setBackgroundResource(R.mipmap.drag_mark_bg);
		addView(layout,params);


		text = new TextView(getContext());
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params2.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params2.topMargin = dp2px(getContext(),8);
		text.setLayoutParams(params2);
		layout.addView(text,params2);

		text.setTextSize(TypedValue.COMPLEX_UNIT_SP,9);
		text.setTextColor(getResources().getColor(R.color.co_text_white));

		
//		View view = View.inflate(getContext(), R.layout.view_drag_mark, this);
//		text = (TextView) view.findViewById(R.id.house_mark_text);

	}
	
	
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		
//		int startX=0;
//		int startY=0;
//		
//		switch(event.getAction()){
//		case MotionEvent.ACTION_DOWN:
//			startX=(int) event.getRawX();
//			startY=(int) event.getRawY();
//			
//			
//			break;
//		case MotionEvent.ACTION_MOVE:
//			int newX=(int) event.getRawX();
//			int newY=(int) event.getRawY();
//			
//			int dx = newX - startX;
//			int dy = newY - startY;
//			
//			int l= getLeft();
//			int t = getTop();
//			int r = getRight();
//			int b = getBottom();
//			
//			l=l+dx;
//			t=t+dy;
//			r=r+dx;
//			b=b+dy;
//			
//			if(l<0 || r>screenWidth*3/4 || t <0
//					|| b > screenHeight){
//				break;
//			}
//			
//			
//			
//			layout(l, t, r, b);
//			startX=(int) event.getRawX();
//			startY=(int) event.getRawY();
//			
//			break;
//		case MotionEvent.ACTION_UP:
//			int lastx = getLeft();
//			int lasty = getTop();
////			location.setLeft(lastx);
////			location.setTop(lasty);
//
//			break;
//		}
//		return false;
//	}
	
	
	
	
	public void setText(String s){
		text.setText(s);
	}
	
	
	public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
	
	

}
