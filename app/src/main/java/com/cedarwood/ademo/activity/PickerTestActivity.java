package com.cedarwood.ademo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cedarwood.ademo.R;
import com.cedarwood.ademo.view.picker.PickerScrollView;
import com.cedarwood.ademo.view.picker.PickerView;

/**
 * Created by wentongmen on 2016/3/9.
 */
public class PickerTestActivity extends BaseActivity {


    private int typeOne;
    private int typeTwo;
    private int typeThree;
    private View typeLayout;

    private String[] names = {"零","一","二","三","四","五","六","七","八","九","十"};

    private String type = "110";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_test);

        initView();
        initData();


    }

    private void initData() {


    }

    private void initView() {


        Toolbar toolbar = (Toolbar) findViewById(R.id.global_title_toolbar);
        toolbar.setTitle("Picker");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);


        TextView cancelText = (TextView) findViewById(R.id.picker_test_cancel_text);
        TextView sureText = (TextView) findViewById(R.id.picker_test_sure_text);

        typeLayout = findViewById(R.id.picker_test_type_layout);


        String houseType = type;
        initTypePicker(houseType);


        cancelText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        sureText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(typeOne==0 && typeTwo==0 && typeThree==0){
                    showToast("请选择分类");
                    return;
                }

                showToast(""+typeOne+typeTwo+typeThree);
            }
        });


        showPicker();

    }

    private void initOperationUnitListPicker(String[] arrays,int selected) {

        findViewById(R.id.picker_test_type_one_picker).setVisibility(View.GONE);
        PickerView houseTypeTwoPicker = (PickerView) findViewById(R.id.picker_test_type_two_picker);
        findViewById(R.id.picker_test_type_three_picker).setVisibility(View.GONE);

        houseTypeTwoPicker.getPickerScrollView().updateData(arrays);
        houseTypeTwoPicker.getPickerScrollView().setSelectedIndex(selected);
        houseTypeTwoPicker.getPickerScrollView().setOnItemChangedListener(new PickerScrollView.OnItemChangedListener() {

            @Override
            public void OnItemChanged(View TextView, int lastIndex, int newIndex) {

                typeTwo=newIndex;
            }
        });
    }

    private void initTypePicker(String type) {

        int one=0;
        int two=0;
        int three=0;

        String typeStr = type+"";
        if(typeStr.length()==3){
            one = Integer.parseInt(typeStr.substring(0,1));
            two = Integer.parseInt(typeStr.substring(1,2));
            three = Integer.parseInt(typeStr.substring(2,3));
        }

        typeOne = one;
        typeTwo = two;
        typeThree = three;


        String[] oneArray = names;
        String[] twoArray = names;
        String[] threeArray = names;


        PickerView houseTypeOnePicker = (PickerView) findViewById(R.id.picker_test_type_one_picker);
        PickerView houseTypeTwoPicker = (PickerView) findViewById(R.id.picker_test_type_two_picker);
        PickerView houseTypeThreePicker = (PickerView) findViewById(R.id.picker_test_type_three_picker);

        houseTypeOnePicker.getPickerScrollView().updateData(oneArray);
        houseTypeOnePicker.getPickerScrollView().setSelectedIndex(one);
        houseTypeOnePicker.getPickerScrollView().setOnItemChangedListener(new PickerScrollView.OnItemChangedListener() {

            @Override
            public void OnItemChanged(View TextView, int lastIndex, int newIndex) {

                typeOne=newIndex;
            }
        });

        houseTypeTwoPicker.getPickerScrollView().updateData(twoArray);
        houseTypeTwoPicker.getPickerScrollView().setSelectedIndex(two);
        houseTypeTwoPicker.getPickerScrollView().setOnItemChangedListener(new PickerScrollView.OnItemChangedListener() {

            @Override
            public void OnItemChanged(View TextView, int lastIndex, int newIndex) {

                typeTwo=newIndex;
            }
        });

        houseTypeThreePicker.getPickerScrollView().updateData(threeArray);
        houseTypeThreePicker.getPickerScrollView().setSelectedIndex(three);
        houseTypeThreePicker.getPickerScrollView().setOnItemChangedListener(new PickerScrollView.OnItemChangedListener() {

            @Override
            public void OnItemChanged(View TextView, int lastIndex, int newIndex) {

                typeThree=newIndex;
            }
        });
    }

    private void showPicker() {
        typeLayout.setVisibility(1==1?View.VISIBLE:View.GONE);

    }


}
