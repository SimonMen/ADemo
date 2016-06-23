package com.cedarwood.ademo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.cedarwood.ademo.R;
import com.cedarwood.ademo.fragment.DoubleDragInfoFragment;
import com.cedarwood.ademo.fragment.DoubleDragPicFragment;
import com.cedarwood.ademo.utils.Log;
import com.cedarwood.ademo.view.DoubleDragLayout;
import com.cedarwood.ademo.view.OverOpenScrollView;

/**
 * Created by wentongmen on 2016/4/11.
 */
public class DoubleDragTest2Activity extends BaseActivity {


    private static final String TAG = DoubleDragTest2Activity.class.getSimpleName();
    private DoubleDragLayout mLayout;
    private ScrollView mHeader;
    private RelativeLayout mFooter;
    private LinearLayout mHeaderContent;
    private DoubleDragPicFragment picFragment;
    private DoubleDragInfoFragment infoFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_drag_2);

        initView();
        initData();


    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.global_title_toolbar);
        toolbar.setTitle("DoubleDrag");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);

        mLayout = (DoubleDragLayout) findViewById(R.id.double_drag_ddl);
        mLayout.setOnPullListener(pullListener);
        mLayout.setOnContentChangeListener(pageChangedListener);
        mHeader = (ScrollView) findViewById(R.id.double_drag_header);
        mFooter = (RelativeLayout) findViewById(R.id.double_drag_footer);
        mHeaderContent = (LinearLayout) mHeader.getChildAt(0);
//        mHeader.setListener(headStateListenter);
//        setupJazziness(TransitionEffect.Standard);
//        mHeader.setHeadImage(R.id.double_drag_header_image_layout);

//        initPicFragment();
//        initInfoFragment();
    }

    private void initPicFragment() {

        picFragment = new DoubleDragPicFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.double_drag_header_image_container, picFragment);
        fragmentTransaction.commit();
        picFragment.setListener(new DoubleDragPicFragment.StateListener() {

            @Override
            public void onStateclick() {
//                mHeader.setStateChanger();
            }
        });


    }

    private void initInfoFragment() {
        infoFragment = new DoubleDragInfoFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.double_drag_header_info_layout, infoFragment).commit();
    }

    private void initData() {


    }


    DoubleDragLayout.OnPullListener pullListener = new DoubleDragLayout.OnPullListener() {
        @Override
        public boolean headerFootReached(MotionEvent event) {
            Log.i(TAG,"pullListener mHeader.getScrollY() : "+mHeader.getScrollY()+"  mHeader.getHeight() : "+mHeader.getHeight()+" mHeaderContent.getHeight() : "+mHeaderContent.getHeight());
            if (mHeader.getScrollY() + mHeader.getHeight() >= mHeaderContent.getHeight()) {
                return true;
            }
            return false;
        }

        @Override
        public boolean footerHeadReached(MotionEvent event) {
            if (mFooter.getScrollY() == 0) {
                return true;
            }
            return false;
        }
    };

    DoubleDragLayout.OnPageChangedListener pageChangedListener = new DoubleDragLayout.OnPageChangedListener() {
        @Override
        public void onPageChanged(int stub) {

            switch (stub) {
                case DoubleDragLayout.SCREEN_HEADER:
                    Log.d("tag", "SCREEN_HEADER");
                    showToast("SCREEN_HEADER");
                    break;
                case DoubleDragLayout.SCREEN_FOOTER:
                    Log.d("tag", "SCREEN_FOOTER");
                    showToast("SCREEN_FOOTER");
                    break;
            }
        }
    };


    OverOpenScrollView.HeadStateListener headStateListenter = new OverOpenScrollView.HeadStateListener() {
        @Override
        public void headTouch() {
            picFragment.setTouch();

        }

        @Override
        public void headOpen() {
            picFragment.setOpen();
        }

        @Override
        public void headClosed() {
            picFragment.setClosed();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    mHeader.smoothScrollTo(0, 0);
                }
            }, 200);
        }
    };








}
