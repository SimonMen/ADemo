package com.cedarwood.ademo.view.drag;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cedarwood.ademo.model.data.DragMarkInfoLocation;
import com.cedarwood.ademo.utils.Log;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by wentongmen on 2015/8/27.
 */
public class DragView extends RelativeLayout {


	public static final String TAG = DragView.class.getSimpleName();

	private Context context;
	private ImageView image;
	private int mMinimumVelocity;
	private int mTouchSlop;

	private VelocityTracker mVelocityTracker;
	private boolean mIsDragging;
	private float mLastTouchX;
	private float mLastTouchY;
	private RelativeLayout layout;

	private int photoWidth;
	private int photoHeight;

	private HashMap<Integer, DragMark> markMap = new HashMap<Integer, DragMark>();
	private HashMap<Integer, DragMarkInfoLocation> locationMap = new HashMap<Integer, DragMarkInfoLocation>();

	public DragView(Context context) {
		super(context);
		init(context);
	}

	public DragView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}


	private void init(Context context) {

		final ViewConfiguration configuration = ViewConfiguration.get(context);
		mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
		mTouchSlop = configuration.getScaledTouchSlop();

		this.context = context;



		layout = new RelativeLayout(context);
		// layout.setBackgroundColor(Color.parseColor("#33ff0000"));
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);
		addView(layout, params);

	}

	public void addMark(int position, DragMarkInfoLocation location) {

		int height = layout.getHeight();
		int width = layout.getWidth();


		DragMark mark = new DragMark(getContext());
		mark.setText(location.getTitle());


		if (mark.getMeasuredWidth() == 0) {
			measureView(mark);
		}

		markMap.put(position, mark);
		locationMap.put(position, location);

		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		int left = (int) (width * location.getLeft() / (float) photoWidth);
		int top = (int) (height * location.getTop() / (float) photoHeight);

		params.leftMargin = left - mark.getMeasuredWidth() / 2;
		params.topMargin = top - mark.getMeasuredHeight();
		mark.setLayoutParams(params);

		Log.i("BuildingPartMarkInfo",
				"width : " + width + "  mark.getMeasuredWidth() : " + mark.getMeasuredWidth());
		Log.i("BuildingPartMarkInfo",
				"height : " + height + "  mark.getMeasuredHeight() : " + mark.getMeasuredHeight());


		mark.setOnTouchListener(new MarkOnTouchListener());

		addView(mark, params);

	}

	public void addAllMark() {

		for (Entry<Integer, DragMarkInfoLocation> entry : locationMap.entrySet()) {
			addMark(entry.getKey(), entry.getValue());
		}
	}


	public void removeAllMark() {

		for (Entry<Integer, DragMark> entry : markMap.entrySet()) {
			DragMark mark = entry.getValue();
			removeView(mark);
		}

	}

	private void measureView(View view) {
		int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

		int height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

		view.measure(width, height);

		// int height=view.getMeasuredHeight();
		//
		// int width=view.getMeasuredWidth();
	}


	public void removeMark(int position) {

		DragMark mark = markMap.get(position);

		removeView(mark);
		markMap.remove(position);
		locationMap.remove(position);
	}


	public class MarkOnTouchListener implements OnTouchListener {

		int startX = 0;
		int startY = 0;

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			switch (event.getAction()) {
				case ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					Log.i(TAG, ">>>  startX " + startX + "  startY " + startY);


					break;
				case ACTION_MOVE:
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();

					Log.i(TAG, ">>>  newX " + newX + "  newY " + newY);

					int dx = newX - startX;
					int dy = newY - startY;

					int l = view.getLeft();
					int t = view.getTop();
					int r = view.getRight();
					int b = view.getBottom();

					l = l + dx;
					t = t + dy;
					r = r + dx;
					b = b + dy;

					if (l < 0 || r > layout.getWidth() || t < 0 || b > layout.getHeight()) {
						break;
					}

					LayoutParams params = (LayoutParams) view.getLayoutParams();
					params.leftMargin = l;
					params.topMargin = t;
					view.setLayoutParams(params);

					// view.layout(l, t, r, b);
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();



					break;
				case ACTION_UP:
					int lastx = view.getLeft();
					int lasty = view.getTop();

					int height = layout.getHeight();
					int width = layout.getWidth();

					int locationX = (int) ((lastx + view.getMeasuredWidth() / 2) * photoWidth / (float) width);
					int locationY = (int) ((lasty + view.getMeasuredHeight()) * photoHeight / (float) height);

					DragMarkInfoLocation location = null;
					int position = 0;
					Set<Entry<Integer, DragMark>> entrySet = markMap.entrySet();
					for (Entry<Integer, DragMark> markEntry : entrySet) {
						if (markEntry.getValue() == view) {
							position = markEntry.getKey();
							location = locationMap.get(markEntry.getKey());
							break;
						}
					}
					if (location != null) {
						location.setLeft(locationX);
						location.setTop(locationY);
						listener.onMoveUp(position, location);
					}


					break;
			}
			return true;
		}

	};


	public void setPhotoSize(int photoWidth, int photoHeight) {
		this.photoWidth = photoWidth;
		this.photoHeight = photoHeight;
	}



	private OnMoveUpListener listener;


	public interface OnMoveUpListener {
		void onMoveUp(int positon, DragMarkInfoLocation location);
	}

	public void setOnMoveUpListener(OnMoveUpListener listener) {
		this.listener = listener;
	}


	private int dp2px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);

	}

}
