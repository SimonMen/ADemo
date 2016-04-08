package com.cedarwood.ademo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;

import com.cedarwood.ademo.R;
import com.cedarwood.ademo.view.tab.CustomScrollTabBar;
import com.cedarwood.ademo.view.tab.CustomTabBar;

import java.util.ArrayList;

/**
 * Created by wentongmen on 2016/2/25.
 */
public class TabBarTestActivity extends BaseActivity {


    private CustomTabBar bar1;
    private CustomTabBar bar2;
    private CustomScrollTabBar sbar1;
    private CustomScrollTabBar sbar2;
    private ArrayList<String> tabs;
    private ArrayList<String> tabs2;

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tab_bar_test);
//
//        initView();
//        initData();
//    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_tab_bar_test);

        initView();
        initData();

    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.tab_bar_test_toolbar);
        toolbar.setTitle("SwipeRefreshTestActivity");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bar1 = (CustomTabBar) findViewById(R.id.tab_bar_test_tab_1);
        bar2 = (CustomTabBar)findViewById(R.id.tab_bar_test_tab_2);
        sbar1 = (CustomScrollTabBar) findViewById(R.id.tab_bar_test_scroll_tab_1);
        sbar2 = (CustomScrollTabBar) findViewById(R.id.tab_bar_test_scroll_tab_2);

    }


    private void initData() {

        tabs = new ArrayList<String>();
        tabs.add("貂蝉");
        tabs.add("落雁昭君");
        tabs.add("西施");
        tabs.add("羞花玉环");

        bar1.addTabs(tabs);
        bar1.setOnTabChangeListener(new CustomTabBar.OnTabChangeListener() {
            @Override
            public boolean onTabChanged(int newIndex) {
                showToast(tabs.get(newIndex));
                return true;
            }
        });

        bar2.addTabs(tabs);
        bar2.setOnTabChangeListener(new CustomTabBar.OnTabChangeListener() {
            @Override
            public boolean onTabChanged(int newIndex) {
                showToast(tabs.get(newIndex));
                return true;
            }
        });

        tabs2 = new ArrayList<String>();
        tabs2.add("貂蝉");
        tabs2.add("落雁昭君");
        tabs2.add("西施");
        tabs2.add("羞花玉环");
        tabs2.add("李师师");
        tabs2.add("陈圆圆~~");
        tabs2.add("小凤仙@@@@@@");
        tabs2.add("哈");
        tabs2.add("哈哈哈哈哈");

        sbar1.addTabs(tabs2);
        sbar1.setOnTabChangeListener(new CustomScrollTabBar.OnTabChangeListener() {
            @Override
            public boolean onTabChanged(int newIndex) {
                showToast(tabs2.get(newIndex));
                return true;
            }
        });

        sbar2.addTabs(tabs2);
        sbar2.setOnTabChangeListener(new CustomScrollTabBar.OnTabChangeListener() {
            @Override
            public boolean onTabChanged(int newIndex) {
                showToast(tabs2.get(newIndex));
                return true;
            }
        });


    }




































}
