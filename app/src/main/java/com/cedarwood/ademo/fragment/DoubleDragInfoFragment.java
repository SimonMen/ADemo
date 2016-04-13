package com.cedarwood.ademo.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.cedarwood.ademo.R;

/**
 * Created by wentongmen on 2016/4/11.
 */
public class DoubleDragInfoFragment extends BaseFragment {

    public View onCreateView(android.view.LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_double_drag_header_info, null);
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




//        View layout = getView().findViewById(R.id.double_drag_header_info_layout);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
//        layout.setLayoutParams(params);

    }




}
