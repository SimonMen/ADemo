package com.cedarwood.ademo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.cedarwood.ademo.R;
import com.cedarwood.ademo.fragment.BaseFragment;
import com.cedarwood.ademo.view.JazzyViewPager;
import com.cedarwood.ademo.view.OutlineContainer;

import java.util.ArrayList;

/**
 * Created by wentongmen on 2016/4/12.
 */
public class JazzyViewPagerTestActivity extends BaseActivity {


    private String[] transitions;
    private JazzyViewPager pager;
    private ArrayList<String> tabs;
    private ArrayList<BaseFragment> fragments = new ArrayList<BaseFragment>();
    private JazzyAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jazzy_view_pager);

        transitions = getResources().getStringArray(R.array.jazzy_effects);

        initView();
        initData();


    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.global_title_toolbar);
        toolbar.setTitle("JazzyViewPager");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);

        setupSpinner();

        pager = (JazzyViewPager) findViewById(R.id.jazzy_view_pager_viewpager);
        adapter = new JazzyAdapter();
        pager.setAdapter(adapter);

    }

    private void initData() {
        tabs = new ArrayList<String>();
        tabs.add("貂蝉");
        tabs.add("落雁昭君");
        tabs.add("西施");
        tabs.add("羞花玉环");
        tabs.add("貂蝉");
        tabs.add("落雁昭君");
        tabs.add("西施");
        tabs.add("羞花玉环");
        tabs.add("貂蝉");
        tabs.add("落雁昭君");
        tabs.add("西施");
        tabs.add("羞花玉环");


//        for(int i =0;i<tabs.size();i++){
//
//            TextView textView = new TextView(this);
//            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
//            params.gravity = Gravity.CENTER;
//            textView.setLayoutParams(params);
//            textView.setText(tabs.get(i));
//
//            pager.addView(textView,params);
//
//        }


//        for(String name : tabs){
//            MessageFragment fragment = MessageFragment.newInstance(name);
//            fragments.add(fragment);
//        }

        adapter.notifyDataSetChanged();


    }

    private void setupSpinner() {

        Spinner tSpinner = (Spinner) findViewById(R.id.jazzy_view_pager_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> tAdapter = ArrayAdapter.createFromResource(this,
                R.array.jazzy_effects, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        tAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        tSpinner.setAdapter(tAdapter);

        tSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {


                if(position ==6 || position == 8){
                    if(!pager.getFadeEnabled()){
                        pager.setFadeEnabled(true);
                    }
                }

                pager.setTransitionEffect(JazzyViewPager.TransitionEffect.valueOf(transitions[position]));


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner cSpinner = (Spinner) findViewById(R.id.jazzy_view_pager_type_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> cAdapter = ArrayAdapter.createFromResource(this,
                R.array.jazzy_fade_outline, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        cSpinner.setAdapter(cAdapter);

        cSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                switch (position){
                    case 0:
                        pager.setFadeEnabled(true);

                        break;
                    case 1:

                        pager.setFadeEnabled(false);
                        break;
                    case 2:
                        pager.setOutlineEnabled(true);

                        break;
                    case 3:
                        pager.setOutlineEnabled(false);

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    private class JazzyAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            TextView text = new TextView(JazzyViewPagerTestActivity.this);
            text.setGravity(Gravity.CENTER);
            text.setTextSize(30);
            text.setTextColor(Color.WHITE);
            text.setText(tabs.get(position));
            text.setPadding(30, 30, 30, 30);
            int bg = Color.rgb((int) Math.floor(Math.random()*128)+64,
                    (int) Math.floor(Math.random()*128)+64,
                    (int) Math.floor(Math.random()*128)+64);
            text.setBackgroundColor(bg);
            container.addView(text, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            pager.setObjectForPosition(text, position);
            return text;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object obj) {
            container.removeView(pager.findViewFromObject(position));
        }
        @Override
        public int getCount() {
            return tabs==null?0:tabs.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            if (view instanceof OutlineContainer) {
                return ((OutlineContainer) view).getChildAt(0) == obj;
            } else {
                return view == obj;
            }
        }
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
