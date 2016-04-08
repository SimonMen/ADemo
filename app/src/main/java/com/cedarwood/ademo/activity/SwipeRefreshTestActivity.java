package com.cedarwood.ademo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cedarwood.ademo.R;
import com.cedarwood.ademo.view.RefreshLayout;
import com.cedarwood.ademo.view.pull.PullListView;

/**
 * Created by wentongmen on 2016/3/3.
 */
public class SwipeRefreshTestActivity extends BaseActivity {



    private String[] lvs = {"List Item 01", "List Item 02", "List Item 03", "List Item 04"};
    private String[] lvs2 = {"List Item 01", "List Item 02", "List Item 03", "List Item 04","List Item 05","List Item 06",
            "List Item 07","List Item 08","List Item 09","List Item 10","List Item 11","List Item 12","List Item 13",
            "List Item 14","List Item 15","List Item 16","List Item 17","List Item 18","List Item 19","List Item 20"};
    private PullListView list;
    private RefreshLayout refreshLayout;
    private View loading;
    private View fail;
    private View noData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_refresh_test);

        initView();
        initData();


    }



    private void initView() {

        loading = findViewById(R.id.global_loading);
        fail = findViewById(R.id.global_fail);
        noData = findViewById(R.id.global_no_data);


        Toolbar toolbar = (Toolbar) findViewById(R.id.swipe_refresh_test_toolbar);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.swipe_refresh_test_drawer_layout);
        ListView drawerList = (ListView) findViewById(R.id.swipe_refresh_test_drawer_list);

        toolbar.setTitle("SwipeRefreshTestActivity");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //创建返回键，并实现打开关/闭监听
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);

        //设置菜单列表
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        drawerList.setAdapter(arrayAdapter);


        refreshLayout = (RefreshLayout) findViewById(R.id.swipe_refresh_test_refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.co_global_blue,R.color.co_global_bg,R.color.co_global_red,R.color.co_global_white);
        refreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorAccent);
        refreshLayout.setProgressViewEndTarget(false,300);
        list = (PullListView) findViewById(R.id.swipe_refresh_test_list);

        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs2);
        list.setAdapter(dataAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                showToast("refreshing");
                list.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                },5000);
            }
        });

        refreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                showToast("loading");
                list.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setLoading(false);
                    }
                },5000);
            }
        });

//        list.setMoreListener(new PullListView.MoreListener() {
//            @Override
//            public void more() {
//                showToast("more");
//                list.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        list.successMore();
//                    }
//                },5000);
//
//            }
//        });



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
