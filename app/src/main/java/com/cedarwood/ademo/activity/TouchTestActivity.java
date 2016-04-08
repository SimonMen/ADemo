package com.cedarwood.ademo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.cedarwood.ademo.R;

/**
 * Created by wentongmen on 2016/4/5.
 */
public class TouchTestActivity extends BaseActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_test);

        initView();
        initData();

//        ViewPager

    }

    private void initData() {


    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.global_title_toolbar);
        toolbar.setTitle("Picker");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);


    }
}
