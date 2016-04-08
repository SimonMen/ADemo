package com.cedarwood.ademo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cedarwood.ademo.Cheeses;
import com.cedarwood.ademo.R;

/**
 * Created by wentongmen on 2016/3/10.
 */
public class CheeseDetailTestActivity extends BaseActivity {


    public static final String EXTRA_NAME = "cheese_name";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheese_detail_test);

        initView();
        initData();

    }

    private void initView() {

        Intent intent = getIntent();
        final String cheeseName = intent.getStringExtra(EXTRA_NAME);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.cheese_detail_test_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.cheese_detail_test_collapsing_toolbar);
        collapsingToolbar.setTitle(cheeseName);

        loadBackdrop();

    }

    private void initData() {


    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.cheese_detail_test_backdrop);
        Glide.with(this).load(Cheeses.getRandomCheeseDrawable()).centerCrop().into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }




}
