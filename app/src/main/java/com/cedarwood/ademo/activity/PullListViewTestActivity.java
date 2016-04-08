package com.cedarwood.ademo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;

import com.cedarwood.ademo.R;
import com.cedarwood.ademo.view.pull.PullListView;


/**
 * Created by wentongmen on 2016/3/3.
 */
public class PullListViewTestActivity extends BaseActivity {


    private View loading;
    private View fail;
    private View noData;
    private PullListView list;

    private String[] lvs2 = {"List Item 01", "List Item 02", "List Item 03", "List Item 04","List Item 05","List Item 06",
            "List Item 07","List Item 08","List Item 09","List Item 10","List Item 11","List Item 12","List Item 13",
            "List Item 14","List Item 15","List Item 16","List Item 17","List Item 18","List Item 19","List Item 20"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_list_test);

        initView();
        initData();


    }



    private void initView() {

        loading = findViewById(R.id.global_loading);
        fail = findViewById(R.id.global_fail);
        noData = findViewById(R.id.global_no_data);


        Toolbar toolbar = (Toolbar) findViewById(R.id.pull_list_test_toolbar);
        toolbar.setTitle("SwipeRefreshTestActivity");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);


        list = (PullListView) findViewById(R.id.pull_list_test_list);

        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs2);
        list.setAdapter(dataAdapter);
        list.setAutoMore(false);

        list.setRefreshListener(new PullListView.RefreshListener() {
            @Override
            public void refresh() {
                showToast("refreshing");
                list.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.successRefresh();
                    }
                },5000);
            }
        });


        list.setMoreListener(new PullListView.MoreListener() {
            @Override
            public void more() {
                showToast("loading");
                list.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.successMore();
                    }
                },5000);
            }
        });


    }

    private void initData() {
        showProgress();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showData();
            }
        },3000);
    }


    private void showProgress(){
        list.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        fail.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
    }

    private void showFail(){
        list.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
        fail.setVisibility(View.VISIBLE);
        noData.setVisibility(View.GONE);
    }

    private void showNoData(){
        list.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
        fail.setVisibility(View.GONE);
        noData.setVisibility(View.VISIBLE);

    }

    private void showData(){
        list.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        fail.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
    }
}
