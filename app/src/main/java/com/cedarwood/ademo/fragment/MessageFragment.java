package com.cedarwood.ademo.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cedarwood.ademo.R;

/**
 * Created by men on 2015/1/30.
 */
public class MessageFragment extends BaseFragment {
    private static final String TAG = MessageFragment.class.getSimpleName();
    private String name;


    public static MessageFragment newInstance(String name){


        MessageFragment fragment=new MessageFragment();

        Bundle bundle = new Bundle();//封装到bundle
        bundle.putString("name",name);
        fragment.setArguments(bundle);
        return fragment;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        name=args.getString("name","");

    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        initView();

    }

    private void initView() {


        TextView text = (TextView) getView().findViewById(R.id.message_text);
        text.setText(name);
    }

}
