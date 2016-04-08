package com.cedarwood.ademo.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cedarwood.ademo.R;
import com.cedarwood.ademo.adapter.AnimationGridAdapter;
import com.cedarwood.ademo.adapter.AnimationListAdapter;
import com.cedarwood.ademo.adapter.SimpleAnimatorAdapter;

/**
 * Created by wentongmen on 2016/3/11.
 */
public class RecyclerAnimatorsTestActivity extends BaseActivity {


    RecyclerView mRecyclerView;
    protected AnimationListAdapter mListAdapter;
    protected AnimationGridAdapter mGridAdapter;
    protected RecyclerView.Adapter mAdapter;

    private SimpleAnimatorAdapter animatorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_animators_test);

        //Setup Spinner
        setupSpinner();

        //Setup RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_animators_test_list);
        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mListAdapter = new AnimationListAdapter(this);
        mGridAdapter = new AnimationGridAdapter(this);
        mAdapter = mListAdapter;
        animatorAdapter = new SimpleAnimatorAdapter(mAdapter, mRecyclerView);
        animatorAdapter.getViewAnimator().setAnimationDelayMillis(1000);
        mRecyclerView.setAdapter(animatorAdapter);
    }


    private void setupSpinner() {

        Spinner tSpinner = (Spinner) findViewById(R.id.recycler_animators_test_type_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> tAdapter = ArrayAdapter.createFromResource(this,
                R.array.recycler_animators_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        tAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        tSpinner.setAdapter(tAdapter);

        tSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        mAdapter = mListAdapter;
                        break;
                    case 1:
                        mAdapter = mGridAdapter;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        Spinner cSpinner = (Spinner) findViewById(R.id.recycler_animators_test_category_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> cAdapter = ArrayAdapter.createFromResource(this,
                R.array.recycler_animators_adapters, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        cSpinner.setAdapter(cAdapter);

        cSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if(mAdapter == mListAdapter){
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(RecyclerAnimatorsTestActivity.this));
                }else{
                    mRecyclerView.setLayoutManager(new GridLayoutManager(RecyclerAnimatorsTestActivity.this,5));
                }
                animatorAdapter = new SimpleAnimatorAdapter(mAdapter,mRecyclerView);
//                animatorAdapter.setType(SimpleAnimatorAdapter.SLIDE_IN_SWING_BOTTOM);
                animatorAdapter.setType(position);
                animatorAdapter.getViewAnimator().setAnimationDelayMillis(100);
                mRecyclerView.setAdapter(animatorAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


}
