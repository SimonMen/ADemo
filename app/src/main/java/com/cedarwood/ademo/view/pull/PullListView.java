package com.cedarwood.ademo.view.pull;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.cedarwood.ademo.R;
import com.cedarwood.ademo.utils.Log;


/**
 * Created by men on 2015/1/13.
 */
public class PullListView extends ListView implements AbsListView.OnScrollListener {
    private static final String TAG = "PullListView";


    public PullListView(Context context) {
        super(context);
        init(context);
        mContext = context;
    }

    public PullListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
        mContext = context;
    }

    public PullListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        mContext = context;
    }

    private final static int DONE				= 0;
    private final static int PULL_TO_REFRESH	= 1;
    private final static int RELEASE_TO_REFRESH = 2;
    private final static int REFRESHING			= 3;

    // 实际的padding的距离与界面上偏移距离的比例
    private final static int RATIO = 3;
    private int HEADER_THRESHOLD = 0; //刷新的临界值
    private int FOOTER_THRESHOLD = 0; //刷新的临界值
    private int ROLLBACK_TIME = 500; //动画过渡时间

    private boolean mHeaderEnable=false; //下拉刷新和上拉加载是否可用  设置监听即可用
    private boolean mFooterEnable=false;

    private Context mContext;

    private RefreshListener mRefreshListener; //监听
    private MoreListener mMoreListener;

    private View mHeaderView;//布局设置
    private View mFooterView;
    private RefreshImageView mHeaderArrowImg;
    private ProgressBar mHeaderProgress;
    private RefreshImageView mFooterArrowImg;
    private ProgressBar mFooterProgress;

    private RotateAnimation animation;  //动画效果
    private RotateAnimation reverseAnimation;
    private Animation loadingAnimation;


    private int mHeaderState;   //当前状态
    private boolean mHeaderBack; //进入PULL_TO_REFRESH 的方法 当从DONE开始  为false  当从RELEASE_TO_REFRESH 开始  为true
    private int mFooterState;
    private boolean mFooterBack;

    private int mHeaderStartY;  //刷新开始时的值
    private int mFooterStartY;
    private boolean mHeaderRecording;  //记录刷新开始
    private boolean mFooterRecording;

    private boolean showRefreshFirst = true;
    private boolean isFirstItemShow; //那种刷新可用 由onScroll 判断
    private boolean isLastItemShow;

    private int lastVisibleItem; //最后一个可见item的position
    
    private OnPullListScrollListener srollListener; //滑动监听
   
    private UpOrDownListener upOrDownListener; //上下活动的监听
    
    private int oldTotalItemCount=0; //总条目不变 不再自动加载

    private boolean autoMoring =false;

    private boolean autoMore = true;

    private void init(Context context) {
        //setCacheColorHint(context.getResources().getColor(R.color.transparent));

        addHeaderView(context);
        addFooterView(context);
        setOnScrollListener(this);

        animation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(250);
        animation.setFillAfter(true);

        reverseAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(250);
        reverseAnimation.setFillAfter(true);

        loadingAnimation = new RotateAnimation(0, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        loadingAnimation.setInterpolator(new LinearInterpolator());
        loadingAnimation.setDuration(800);
        loadingAnimation.setFillAfter(true);
        loadingAnimation.setRepeatCount(-1);

        mHeaderState = DONE;
        mFooterState = DONE;

        mHeaderEnable = false;
        mFooterEnable = false;
    }


    protected void addHeaderView(Context context) {
        mHeaderView =  View.inflate(context, R.layout.view_pull_list_view_header, null);
        mHeaderArrowImg = (RefreshImageView) mHeaderView.findViewById(R.id.view_list_view_header_arrow);
        mHeaderProgress = (ProgressBar) mHeaderView.findViewById(R.id.view_list_view_header_progress);
        mHeaderArrowImg.setVisibility(View.GONE);
        mHeaderProgress.setVisibility(View.GONE);

        measureView(mHeaderView);
        HEADER_THRESHOLD = mHeaderView.getMeasuredHeight();
//        Log.i(TAG,"addHeaderView HEADER_THRESHOLD : "+HEADER_THRESHOLD);

        mHeaderView.setPadding(0, -1 * HEADER_THRESHOLD, 0, 0);
        mHeaderView.invalidate();

        addHeaderView(mHeaderView);
    }

    protected void addFooterView(Context context) {
        mFooterView =  View.inflate(context,R.layout.view_pull_list_view_footer, null);
        mFooterArrowImg = (RefreshImageView) mFooterView.findViewById(R.id.view_list_view_footer_arrow);
        mFooterProgress = (ProgressBar) mFooterView.findViewById(R.id.view_list_view_footer_progress);
        mFooterArrowImg.setVisibility(View.VISIBLE);
        mFooterProgress.setVisibility(View.GONE);

        measureView(mFooterView);
        FOOTER_THRESHOLD = mFooterView.getMeasuredHeight();
//        Log.i(TAG,"addFooterView FOOTER_THRESHOLD : "+FOOTER_THRESHOLD);

        mFooterView.setPadding(0, 0, 0, -FOOTER_THRESHOLD);
        mFooterView.invalidate();
        addFooterView(mFooterView);
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
//            Log.i(TAG, "measureView p is null ");
            p = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0,0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    //滑动
    public void onScroll(AbsListView arg0, int firstVisiableItem, int visiableItemCount,
                         int totalItemCount) {
        lastVisibleItem = firstVisiableItem + visiableItemCount;

        // update state for is header show or is footer show
        if (totalItemCount > 0) {
            if (firstVisiableItem == 0) {
                isFirstItemShow = true;
            } else {
                isFirstItemShow = false;

            }
            if (firstVisiableItem + visiableItemCount >= totalItemCount) {
                isLastItemShow = true;
            } else {
                isLastItemShow = false;
            }

        } else {
            isFirstItemShow = true;
            isLastItemShow = true;
        }
        if (isFirstItemShow && isLastItemShow) {
            if (showRefreshFirst) {
                isLastItemShow = false;
            } else {
                isFirstItemShow = false;
            }
        }
        
        if (autoMore && totalItemCount>=10 && totalItemCount>oldTotalItemCount &&
                firstVisiableItem + visiableItemCount >= (totalItemCount-3)  && !autoMoring && mFooterEnable){
        	oldTotalItemCount=totalItemCount;
        	autoMoring =true;
        	startMore();
        }
        
        if(srollListener!=null){
        	srollListener.onScroll(firstVisiableItem,visiableItemCount,totalItemCount);
        }
        
    }

    //滑动状态改变
    public void onScrollStateChanged(AbsListView arg0, int scrollState) {
    	
//    	if(scrollState==OnScrollListener.SCROLL_STATE_IDLE){
//    		int count = getAdapter().getCount();
//            if(isLastItemShow || lastVisibleItem>=count){
//                startMore();
//            }
//    	}
    }


    private int mScrollStartY;
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        Log.i(TAG, "onTouchEvent");
        if(mHeaderEnable){
            onHeaderTouchEvent(ev);
        }

        if(mFooterEnable){
            onFooterTouchEvent(ev);
        }
        
        int curY = (int) ev.getY();
        switch (ev.getAction()) {
        
		case MotionEvent.ACTION_DOWN:
			mScrollStartY=curY;
			break;
		case MotionEvent.ACTION_MOVE:
			if(upOrDownListener!=null){
				if(curY-mScrollStartY>50){
					upOrDownListener.upOrDown(false);
					mScrollStartY=curY;
				}else if(mScrollStartY-curY>50){
					upOrDownListener.upOrDown(true);
					mScrollStartY=curY;
				}
			}
			
			break;
		case MotionEvent.ACTION_UP:
			mScrollStartY=0;
			break;
		}
        

        return super.onTouchEvent(ev);
    }

    //以header或footer的状态为主导
//    private void onHeaderTouchEvent(MotionEvent event) {
//        int action=event.getAction();
//        int curY = (int) event.getY();
//        Log.d(TAG,"onHeaderTouchEvent mHeaderState : "+mHeaderState+"  isFirstItemShow : "+isFirstItemShow+" mHeaderStartY : "+mHeaderStartY+" curY : "+curY+"  mHeaderRecording : "+mHeaderRecording);
//        switch (mHeaderState){
//            case DONE:
//                if(action==MotionEvent.ACTION_UP){
//                    mHeaderRecording=false;
//                }else{
//                    if(isFirstItemShow && !mHeaderRecording){
//                        mHeaderStartY=curY;
//                        mHeaderRecording=true;
//                    }
//                    if(isFirstItemShow && curY>mHeaderStartY){
//                        mHeaderState=PULL_TO_REFRESH;
//                        changeHeaderViewByState();
//                        setHeaderHeight((curY - mHeaderStartY) / RATIO,true);
//                    }
//                }
//                break;
//            case PULL_TO_REFRESH:
//                if(action==MotionEvent.ACTION_UP){
//                    if(curY>mHeaderStartY){
//                        Log.d(TAG, "onHeaderTouchEvent PULL_TO_REFRESH ACTION_UP");
//                        HeaderRollAnimation ra = new HeaderRollAnimation((curY - mHeaderStartY) / RATIO, true);
//                        ra.setDuration(ROLLBACK_TIME);
//                        this.startAnimation(ra);
//                    }
//                    mHeaderRecording=false;
//                }else {
//                    if ((curY - mHeaderStartY) / RATIO >= HEADER_THRESHOLD) {
//                        mHeaderState = RELEASE_TO_REFRESH;
//                        mHeaderBack = true;
//                        changeHeaderViewByState();
//                        Log.d(TAG, "onHeaderTouchEvent 由done或者下拉刷新状态转变到松开刷新");
//                    }
//                    // 上推到顶了
//                    else if (curY - mHeaderStartY <= 0) {
//                        mHeaderState = DONE;
//                        changeHeaderViewByState();
//                        Log.d(TAG, "onHeaderTouchEvent 由DOne或者下拉刷新状态转变到done状态");
//                    }
//
//                    setHeaderHeight((curY - mHeaderStartY) / RATIO,true);
//                }
//                break;
//            case RELEASE_TO_REFRESH:
//                if(action==MotionEvent.ACTION_UP){
//                    Log.d(TAG, "onHeaderTouchEvent RELEASE_TO_REFRESH ACTION_UP");
//                    HeaderRollAnimation ra = new HeaderRollAnimation((curY - mHeaderStartY) / RATIO, true);
//                    ra.setDuration(ROLLBACK_TIME);
//                    this.startAnimation(ra);
//                    mHeaderRecording=false;
//                }else {
//                    if (((curY - mHeaderStartY) / RATIO < HEADER_THRESHOLD)
//                            && (curY - mHeaderStartY) > 0) {
//                        mHeaderState = PULL_TO_REFRESH;
//                        changeHeaderViewByState();
//
//                        Log.d(TAG, "onHeaderTouchEvent 由松开刷新状态转变到下拉刷新状态");
//                    }
//                    // 一下子推到顶了
//                    else if (curY - mHeaderStartY <= 0) {
//                        mHeaderState = DONE;
//                        changeHeaderViewByState();
//
//                        Log.d(TAG, "onHeaderTouchEvent 由松开刷新状态转变到done状态");
//                    }
//
//                    setHeaderHeight((curY - mHeaderStartY) / RATIO,true);
//                }
//                break;
//        }
//    }

    //以手势状态为主导
    private void onHeaderTouchEvent(MotionEvent event) {
//        int action=event.getAction();
        int curY = (int) event.getY();

//        Log.d(TAG,"onHeaderTouchEvent mHeaderState : "+mHeaderState+"  isFirstItemShow : "+isFirstItemShow+" mHeaderStartY : "+mHeaderStartY+" curY : "+curY+"  mHeaderRecording : "+mHeaderRecording);

        switch (event.getAction()){
        case MotionEvent.ACTION_DOWN:
            if(mHeaderState==DONE && isFirstItemShow && !mHeaderRecording){
                mHeaderStartY=curY;
                mHeaderRecording=true;
            }
            break;
        case MotionEvent.ACTION_MOVE:
            if(mHeaderState==DONE && isFirstItemShow && !mHeaderRecording){
                mHeaderStartY=curY;
                mHeaderRecording=true;
            }

            if(mHeaderState==DONE && mHeaderRecording && curY>mHeaderStartY){
                mHeaderState=PULL_TO_REFRESH;
                changeHeaderViewByState();
                setHeaderHeight((curY - mHeaderStartY) / RATIO,true);
                return ;
            }

            if(mHeaderState== PULL_TO_REFRESH){
                if ((curY - mHeaderStartY) / RATIO >= HEADER_THRESHOLD) {
                    mHeaderState = RELEASE_TO_REFRESH;
                    mHeaderBack = true;
                    changeHeaderViewByState();
//                    Log.d(TAG, "onHeaderTouchEvent 由done或者下拉刷新状态转变到松开刷新");
                }
                // 上推到顶了
                else if (curY - mHeaderStartY <= 0) {
                    mHeaderState = DONE;
                    changeHeaderViewByState();
//                    Log.d(TAG, "onHeaderTouchEvent 由DOne或者下拉刷新状态转变到done状态");
                }
                
                //下面两行是用来当mHeaderArrowImg初始化后 设置前需要通过setImageResource 再次初始化  才能实现动态变化  否则第一次下拉没有变化  和footer不同。
                mHeaderArrowImg.setImageResource(R.mipmap.refresh_red);
                mHeaderArrowImg.setImageResource(R.mipmap.refresh_grey);
                setHeaderHeight((curY - mHeaderStartY) / RATIO,true);
                return;
            }

            if(mHeaderState == RELEASE_TO_REFRESH){
                if (((curY - mHeaderStartY) / RATIO < HEADER_THRESHOLD)&& (curY - mHeaderStartY) > 0) {
                    mHeaderState = PULL_TO_REFRESH;
                    changeHeaderViewByState();

                    Log.i(TAG, "onHeaderTouchEvent 由松开刷新状态转变到下拉刷新状态");
                }
                // 一下子推到顶了
                else if (curY - mHeaderStartY <= 0) {
                    mHeaderState = DONE;
                    changeHeaderViewByState();

                    Log.i(TAG, "onHeaderTouchEvent 由松开刷新状态转变到done状态");
                }

                setHeaderHeight((curY - mHeaderStartY) / RATIO,true);
                return ;
            }
            break;
        case MotionEvent.ACTION_UP:
            mHeaderRecording=false;
            if(mHeaderState== PULL_TO_REFRESH){
                Log.i(TAG, "onHeaderTouchEvent PULL_TO_REFRESH ACTION_UP");
                HeaderRollAnimation ra = new HeaderRollAnimation((curY - mHeaderStartY) / RATIO, true);
                ra.setDuration(ROLLBACK_TIME);
                this.startAnimation(ra);
                return ;
            }
            if(mHeaderState== RELEASE_TO_REFRESH){
                Log.i(TAG, "onHeaderTouchEvent RELEASE_TO_REFRESH ACTION_UP");
                HeaderRollAnimation ra = new HeaderRollAnimation((curY - mHeaderStartY) / RATIO, true);
                ra.setDuration(ROLLBACK_TIME);
                this.startAnimation(ra);
                return ;
            }
            break;
        }
    }
//以header或footer的状态为主导
//    private void onFooterTouchEvent(MotionEvent event) {
//        int action=event.getAction();
//        int curY = (int) event.getY();
//        Log.d(TAG,"onFooterTouchEvent mFooterState : "+mFooterState+"  isLastItemShow : "+isLastItemShow+" mFooterStartY : "+mFooterStartY+" curY : "+curY+"  mFooterRecording : "+mFooterRecording);
//        switch (mFooterState){
//            case DONE:
//                if(action==MotionEvent.ACTION_UP){
//                    mFooterRecording=false;
//                }else{
//                    if(isLastItemShow && !mFooterRecording){
//                        mFooterStartY=curY;
//                        mFooterRecording=true;
//                    }
//
//                    if(isLastItemShow && curY<mFooterStartY){
//                        mFooterState=PULL_TO_REFRESH;
//                        changeFooterViewByState();
//                        setFooterHeight((mFooterStartY - curY) / RATIO, true);
//                    }
//                }
//
//                break;
//            case PULL_TO_REFRESH:
//                if(action==MotionEvent.ACTION_UP){
//                    if(mFooterStartY>curY){
//                        Log.d(TAG, "onFooterTouchEvent PULL_TO_REFRESH ACTION_UP");
//                        FooterRollAnimation ra = new FooterRollAnimation((mFooterStartY - curY) / RATIO, false);
//                        ra.setDuration(ROLLBACK_TIME);
//                        this.startAnimation(ra);
//                    }
//                    mFooterRecording=false;
//                }else {
//                    if ((mFooterStartY - curY) / RATIO >= FOOTER_THRESHOLD) {
//                        mFooterState = RELEASE_TO_REFRESH;
//                        mFooterBack = true;
//                        changeFooterViewByState();
//
//                        Log.d(TAG, "onFooterTouchEvent 由done或者下拉刷新状态转变到松开刷新");
//                    }
//                    else if (mFooterStartY - curY <= 0) {
//                        mFooterState = DONE;
//                        changeFooterViewByState();
//                        Log.d(TAG, "onFooterTouchEvent 由DOne或者下拉刷新状态转变到done状态");
//                    }
//
//                    setFooterHeight((mFooterStartY - curY) / RATIO, true);
//                }
//                break;
//            case RELEASE_TO_REFRESH:
//                if(action==MotionEvent.ACTION_UP){
//
//                    Log.d(TAG, "onFooterTouchEvent RELEASE_TO_REFRESH ACTION_UP");
//                    FooterRollAnimation ra = new FooterRollAnimation((mFooterStartY - curY) / RATIO, true);
//                    ra.setDuration(ROLLBACK_TIME);
//                    this.startAnimation(ra);
//                    mFooterRecording=false;
//                }else {
//                    if (((mFooterStartY - curY) / RATIO < FOOTER_THRESHOLD)
//                            && (mFooterStartY - curY) > 0) {
//                        mFooterState = PULL_TO_REFRESH;
//                        changeFooterViewByState();
//
//                        Log.d(TAG, "onFooterTouchEvent 由松开刷新状态转变到下拉刷新状态");
//                    }
//                    // 一下子推到顶了
//                    else if (mFooterStartY - curY <= 0) {
//                        mFooterState = DONE;
//                        changeFooterViewByState();
//
//                        Log.d(TAG, "onFooterTouchEvent 由松开刷新状态转变到done状态");
//                    }
//
//                    setFooterHeight((mFooterStartY - curY) / RATIO, true);
//                }
//                break;
//        }
//    }

    //以手势状态为主导
    private void onFooterTouchEvent(MotionEvent event) {
        int curY = (int) event.getY();
        Log.i(TAG,"onFooterTouchEvent mFooterState : "+mFooterState+"  isLastItemShow : "+isLastItemShow+" mFooterStartY : "+mFooterStartY+" curY : "+curY+"  mFooterRecording : "+mFooterRecording);
        switch (event.getAction()){
        case MotionEvent.ACTION_DOWN:
            if(mFooterState==DONE && isLastItemShow && !mFooterRecording){
                mFooterStartY=curY;
                mFooterRecording=true;
            }
            break;
        case MotionEvent.ACTION_MOVE:
            if(mFooterState==DONE && isLastItemShow && !mFooterRecording){
                mFooterStartY=curY;
                mFooterRecording=true;
            }

            if(mFooterState==DONE && mFooterRecording && curY<mFooterStartY){
                mFooterState=PULL_TO_REFRESH;
                changeFooterViewByState();
                setFooterHeight((curY - mHeaderStartY) / RATIO,true);
                return ;
            }

            if(mFooterState== PULL_TO_REFRESH){
                if ((mFooterStartY - curY) / RATIO >= FOOTER_THRESHOLD) {
                    mFooterState = RELEASE_TO_REFRESH;
                    mFooterBack = true;
                    changeFooterViewByState();

                    Log.i(TAG, "onFooterTouchEvent 由done或者下拉刷新状态转变到松开刷新");
                }
                else if (mFooterStartY - curY <= 0) {
                    mFooterState = DONE;
                    changeFooterViewByState();
                    Log.i(TAG, "onFooterTouchEvent 由DOne或者下拉刷新状态转变到done状态");
                }
                
                setFooterHeight((mFooterStartY - curY) / RATIO, true);
                return;
            }

            if(mFooterState == RELEASE_TO_REFRESH){
                if (((mFooterStartY - curY) / RATIO < FOOTER_THRESHOLD)
                        && (mFooterStartY - curY) > 0) {
                    mFooterState = PULL_TO_REFRESH;
                    changeFooterViewByState();

                    Log.i(TAG, "onFooterTouchEvent 由松开刷新状态转变到下拉刷新状态");
                }
                // 一下子推到顶了
                else if (mFooterStartY - curY <= 0) {
                    mFooterState = DONE;
                    changeFooterViewByState();

                    Log.i(TAG, "onFooterTouchEvent 由松开刷新状态转变到done状态");
                }

                setFooterHeight((mFooterStartY - curY) / RATIO, true);
                return ;
            }


            break;
        case MotionEvent.ACTION_UP:
            mFooterRecording=false;
            if(mFooterState== PULL_TO_REFRESH){
                Log.i(TAG, "onFooterTouchEvent PULL_TO_REFRESH ACTION_UP");
                FooterRollAnimation ra = new FooterRollAnimation((mFooterStartY - curY) / RATIO, false);
                ra.setDuration(ROLLBACK_TIME);
                this.startAnimation(ra);
                return;
            }

            if(mFooterState == RELEASE_TO_REFRESH){
                Log.i(TAG, "onFooterTouchEvent RELEASE_TO_REFRESH ACTION_UP");
                FooterRollAnimation ra = new FooterRollAnimation((mFooterStartY - curY) / RATIO, true);
                ra.setDuration(ROLLBACK_TIME);
                this.startAnimation(ra);
                return ;
            }
            break;
        }
    }



    // 当状态改变时候，调用该方法，以更新界面
    protected void changeHeaderViewByState() {
        switch (mHeaderState) {
            case DONE:
                mHeaderProgress.setVisibility(View.GONE);
//                mHeaderProgress.clearAnimation();

                mHeaderArrowImg.setVisibility(View.GONE);
//                mHeaderArrowImg.clearAnimation();

                Log.i(TAG, "当前状态，done");
                break;
            case PULL_TO_REFRESH:

//            	mHeaderProgress.clearAnimation();
                mHeaderProgress.setVisibility(View.GONE);
                
//                mHeaderArrowImg.clearAnimation();
                mHeaderArrowImg.setVisibility(View.VISIBLE);

                // 是由RELEASE_To_REFRESH状态转变来的
                if (mHeaderBack) {
                    mHeaderBack = false;
//                    mHeaderArrowImg.clearAnimation();
//                    mHeaderArrowImg.startAnimation(reverseAnimation);
                    mHeaderArrowImg.setImageResource(R.mipmap.refresh_grey);
                }
                Log.i(TAG, "当前状态，下拉刷新");
                break;
            case RELEASE_TO_REFRESH:
//                mHeaderProgress.clearAnimation();
                mHeaderProgress.setVisibility(View.GONE);

                mHeaderArrowImg.setVisibility(View.VISIBLE);
//                mHeaderArrowImg.clearAnimation();
//                mHeaderArrowImg.startAnimation(animation);
                mHeaderArrowImg.setImageResource(R.mipmap.refresh_red);

                Log.i(TAG, "当前状态，松开刷新");
                break;

            case REFRESHING:
                mHeaderProgress.setVisibility(View.VISIBLE);
//                mHeaderProgress.clearAnimation();
//                mHeaderProgress.startAnimation(loadingAnimation);

//                mHeaderArrowImg.clearAnimation();
                mHeaderArrowImg.setVisibility(View.GONE);

                Log.i(TAG, "当前状态,正在刷新...");
                break;

        }
    }


    // 当状态改变时候，调用该方法，以更新界面
    protected void changeFooterViewByState() {
        switch (mFooterState) {
            case DONE:
//                mFooterProgress.clearAnimation();
                mFooterProgress.setVisibility(View.GONE);
//                mFooterArrowImg.clearAnimation();
                mFooterArrowImg.setVisibility(View.GONE);

                Log.i(TAG, "当前状态，done");
                break;
            case PULL_TO_REFRESH:
//                mFooterProgress.clearAnimation();
                mFooterProgress.setVisibility(View.GONE);
                
//                mFooterArrowImg.clearAnimation();
                mFooterArrowImg.setVisibility(View.VISIBLE);
              //下面这行是用来当mFooterArrowImg初始化后 设置前需要通过setImageResource 再次初始化  才能实现动态变化  否则第一次下拉没有变化  和header设置不同。
                mFooterArrowImg.setImageResource(R.mipmap.refresh_grey);
                // 是由RELEASE_To_REFRESH状态转变来的
                if (mFooterBack) {
                    mFooterBack = false;
//                    mFooterArrowImg.clearAnimation();
//                    mFooterArrowImg.startAnimation(reverseAnimation);
                    mFooterArrowImg.setImageResource(R.mipmap.refresh_grey);
                }
                Log.i(TAG, "当前状态，上拉加载");
                break;
            case RELEASE_TO_REFRESH:
//                mFooterProgress.clearAnimation();
                mFooterProgress.setVisibility(View.GONE);
                mFooterArrowImg.setVisibility(View.VISIBLE);

//                mFooterArrowImg.clearAnimation();
//                mFooterArrowImg.startAnimation(animation);
                mFooterArrowImg.setImageResource(R.mipmap.refresh_red);

                Log.i(TAG, "当前状态，松开刷新");
                break;


            case REFRESHING:
                mFooterProgress.setVisibility(View.VISIBLE);
//                mFooterProgress.clearAnimation();
//                mFooterProgress.startAnimation(loadingAnimation);

//                mFooterArrowImg.clearAnimation();
                mFooterArrowImg.setVisibility(View.GONE);

                Log.i(TAG, "当前状态,正在刷新...");
                break;

        }
    }

    //更新header和footer的显示区域
    private void setHeaderHeight(int height, boolean alwaysTop) {
        mHeaderView.setPadding(0,height-1 * HEADER_THRESHOLD, 0, 0);
        Log.i(TAG," getHeight HEADER_THRESHOLD : "+HEADER_THRESHOLD+" height : "+height);
        if(height>(HEADER_THRESHOLD/2)){
        	mHeaderArrowImg.setProgress(height*2*100/HEADER_THRESHOLD-100,true);
        }
        
        if (alwaysTop)
            this.setSelection(0);
    }
    //更新header和footer的显示区域
    private void setFooterHeight(int height, boolean alwaysBottom) {
        mFooterView.setPadding(0,0, 0, height-FOOTER_THRESHOLD);
        Log.i(TAG," getHeight FOOTER_THRESHOLD : "+FOOTER_THRESHOLD+" height : "+height);
        if(height>(FOOTER_THRESHOLD/2)){
        	mFooterArrowImg.setProgress(height*2*100/FOOTER_THRESHOLD-100,false);
        }
        if (alwaysBottom && this.getAdapter().getCount() > 0) {
            this.setSelection(this.getAdapter().getCount());
        }
    }

    //header和footer的动画 1 从RELEASE_TO_REFRESH 到 REFRESHING 2 从REFRESHING到DONE 3 从 PULL_TO_REFRESH 到 DONE
    private class HeaderRollAnimation extends Animation {
        private int mStartY;
        private int mEndY;
        private int mDeltaY;
        private int mCurY;

        private boolean alwaysTop;

        public HeaderRollAnimation(int y, final boolean alwaysTop) {
            mStartY = y;
            this.alwaysTop = alwaysTop;
            mCurY = mStartY;
            if (mStartY <= HEADER_THRESHOLD)
                mEndY = 0;
            else
                mEndY = HEADER_THRESHOLD;
            mDeltaY = mEndY - mStartY;
            this.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation arg0) {
                }

                @Override
                public void onAnimationEnd(Animation arg0) {
                    PullListView.this.clearAnimation();
                    if (mCurY > 0) {
                        mHeaderState=REFRESHING;
                        changeHeaderViewByState();
                        if (mRefreshListener != null) {
                            mRefreshListener.refresh();
                        } else {
                            Handler h = new Handler();
                            h.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    successRefresh();
                                }
                            }, 1000);
                        }
                    } else {
                        mHeaderState=DONE;
                        changeHeaderViewByState();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation arg0) {

                }


            });
        }

        @Override
        final protected void applyTransformation(float interpolatedTime, Transformation t) {
            mCurY = (int) (mStartY + interpolatedTime * mDeltaY);
            setHeaderHeight(mCurY, alwaysTop);
        }
    }

    //header和footer的动画 1 从RELEASE_TO_REFRESH 到 REFRESHING 2 从REFRESHING到DONE 3 从 PULL_TO_REFRESH 到 DONE
    private class FooterRollAnimation extends Animation {
        private int mStartY;
        private int mEndY;
        private int mDeltaY;
        private int mCurY;
        private boolean alwaysBottom;

        public FooterRollAnimation(int y, final boolean alwaysBottom) {
            mStartY = y;
            this.alwaysBottom = alwaysBottom;
            mCurY = mStartY;
            if (mStartY <= FOOTER_THRESHOLD)
                mEndY = 0;
            else
                mEndY = FOOTER_THRESHOLD;
            mDeltaY = mEndY - mStartY;
            this.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationEnd(Animation arg0) {
                    PullListView.this.clearAnimation();
                    if (mCurY > 0) {
                        mFooterState = REFRESHING;
                        changeFooterViewByState();
                        if (mMoreListener != null) {
                            mMoreListener.more();
                        } else {
                            Handler h = new Handler();
                            h.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    successMore();
                                }
                            }, 1000);
                        }
                    } else {
                        mFooterState = DONE;
                        changeFooterViewByState();

                    }
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
            mCurY = (int) (mStartY + interpolatedTime * mDeltaY);
            setFooterHeight(mCurY, alwaysBottom);
        }
    }
    
    public void success(boolean isRefresh){
    	if(isRefresh){
    		successRefresh();
    	}else{
    		successMore();
    	}
    }
    
    public void fail(boolean isRefresh){
    	if(isRefresh){
    		failRefresh();
    	}else{
    		failMore();
    	}
    }

    //自动刷新
    public void startRefresh(){
        setHeaderHeight((int)(HEADER_THRESHOLD*1.2),true);
        HeaderRollAnimation ra = new HeaderRollAnimation((int)(HEADER_THRESHOLD*1.2),true);
        ra.setDuration(ROLLBACK_TIME);
        startAnimation(ra);
    }
    //自动加载
    public void startMore(){
        setFooterHeight((int)(FOOTER_THRESHOLD*1.2),false);
        FooterRollAnimation ra = new FooterRollAnimation((int)(FOOTER_THRESHOLD*1.2),false);
        ra.setDuration(ROLLBACK_TIME);
        startAnimation(ra);
    }

    //刷新成功
    public void successRefresh() {
        if (mHeaderState == REFRESHING) {
            HeaderRollAnimation ra = new HeaderRollAnimation(HEADER_THRESHOLD, false);
            ra.setDuration(ROLLBACK_TIME);
            startAnimation(ra);
        }
    }
    //刷新失败
    public void failRefresh() {
        if (mHeaderState == REFRESHING) {
            HeaderRollAnimation ra = new HeaderRollAnimation(HEADER_THRESHOLD, false);
            ra.setDuration(ROLLBACK_TIME);
            startAnimation(ra);
        }
    }
    //加载成功
    public void successMore() {
    	autoMoring =false;
        if (mFooterState == REFRESHING) {
            FooterRollAnimation ra = new FooterRollAnimation(FOOTER_THRESHOLD, false);
            ra.setDuration(ROLLBACK_TIME);
            startAnimation(ra);
        }
    }
    //加载失败
    public void failMore() {
    	autoMoring =false;
        if (mFooterState == REFRESHING) {
            FooterRollAnimation ra = new FooterRollAnimation(FOOTER_THRESHOLD, false);
            ra.setDuration(ROLLBACK_TIME);
            startAnimation(ra);
        }
    }

    //设置刷新监听
    public void setRefreshListener(RefreshListener listener){
        this.mRefreshListener=listener;
        mHeaderEnable=true;
    }
    //设置加载监听
    public void setMoreListener(MoreListener listener){
        this.mMoreListener=listener;
        mFooterEnable=true;
    }

    
    public interface UpOrDownListener {
        public void upOrDown(boolean isUp);
    }
    
    public void  setUpOrDownListener(UpOrDownListener upOrDownListener){
    	this.upOrDownListener=upOrDownListener;
    }
    

    public interface RefreshListener {
        public void refresh();
    }

    public interface MoreListener {
        public void more();
    }
    
    public void  setOnPullListSrollListener(OnPullListScrollListener srollListener){
    	this.srollListener=srollListener;
    }
    
    public interface OnPullListScrollListener{
    	void onScroll(int firstVisiableItem, int visiableItemCount, int totalItemCount);
    }

    public void setAutoMore(boolean autoMore) {
        this.autoMore = autoMore;
    }
}
