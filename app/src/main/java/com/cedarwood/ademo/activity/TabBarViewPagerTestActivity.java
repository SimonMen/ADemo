package com.cedarwood.ademo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.cedarwood.ademo.R;
import com.cedarwood.ademo.fragment.BaseFragment;
import com.cedarwood.ademo.fragment.MessageFragment;
import com.cedarwood.ademo.view.tab.CustomScrollTabBar;
import com.cedarwood.ademo.view.tab.CustomTabBar;

import java.util.ArrayList;

/**
 * Created by wentongmen on 2016/3/29.
 */
public class TabBarViewPagerTestActivity extends BaseActivity {


    private static final String TAG = TabBarViewPagerTestActivity.class.getSimpleName();

    private CustomTabBar bar;
    private ViewPager viewPager;
    private ArrayList<String> tabs;
    private ArrayList<BaseFragment> fragments = new ArrayList<BaseFragment>();
    private TabPagerAdapter adapter;
    private CustomTabBar.PageChangeListener pageChangeListener;
    private CustomScrollTabBar bar2;
    private CustomScrollTabBar.PageChangeListener pageChangeListener2;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_tab_bar_view_pager_test);

        initView();
        initData();

    }

    private void initData() {

        tabs = new ArrayList<String>();
        tabs.add("貂蝉");
        tabs.add("落雁昭君");
        tabs.add("西施");
        tabs.add("羞花玉环");
//        tabs.add("貂蝉");
//        tabs.add("落雁昭君");
//        tabs.add("西施");
//        tabs.add("羞花玉环");
//        tabs.add("貂蝉");
//        tabs.add("落雁昭君");
//        tabs.add("西施");
//        tabs.add("羞花玉环");

        bar.setCurTabIndex(0);
        bar.addTabs(tabs);
        bar.setOnTabChangeListener(new CustomTabBar.OnTabChangeListener() {
            @Override
            public boolean onTabChanged(int newIndex) {
                viewPager.setCurrentItem(newIndex);
                return true;
            }
        });

        bar2.setCurTabIndex(0);
        bar2.addTabs(tabs);
        bar2.setOnTabChangeListener(new CustomScrollTabBar.OnTabChangeListener() {
            @Override
            public boolean onTabChanged(int newIndex) {
//                viewPager.setCurrentItem(newIndex);
                return true;
            }
        });

        for(String name : tabs){
            MessageFragment fragment = MessageFragment.newInstance(name);
            fragments.add(fragment);
        }


        adapter.notifyDataSetChanged();

    }


    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.tab_bar_pager_test_toolbar);
        toolbar.setTitle("TabAndPager");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bar = (CustomTabBar) findViewById(R.id.tab_bar_pager_test_tab);
        pageChangeListener = new CustomTabBar.PageChangeListener(bar){

            @Override
            public void onPageSelected(int i) {
                super.onPageSelected(i);
//            showToast(""+i);


            }
        };

        bar2 = (CustomScrollTabBar) findViewById(R.id.tab_bar_pager_test_tab2);
        pageChangeListener2 = new CustomScrollTabBar.PageChangeListener(bar2){

            @Override
            public void onPageSelected(int i) {
                super.onPageSelected(i);
//            showToast(""+i);


            }
        };

        viewPager = (ViewPager) findViewById(R.id.tab_bar_pager_test_pager);
        adapter = new TabPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(pageChangeListener);


    }


    private class TabPagerAdapter extends FragmentStatePagerAdapter {

        ArrayList<BaseFragment> fragments;

        public TabPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> list) {
            super(fm);
            fragments = list;
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragments.get(arg0);
        }

        @Override
        public int getCount() {
            return fragments==null?0:fragments.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE; // 这个返回值和方法必须复写
        }

    }





}
