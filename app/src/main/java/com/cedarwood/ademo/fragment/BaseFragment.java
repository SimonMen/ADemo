package com.cedarwood.ademo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cedarwood.ademo.utils.CommonUtil;
import com.cedarwood.ademo.utils.DialogUtil;
import com.cedarwood.ademo.utils.ToastUtil;

/**
 * Created by wentongmen on 2016/2/23.
 */
public class BaseFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void showLoading() {
        if(this!=null){
            DialogUtil.getInstance().getLoadingDialogShow(getActivity());
        }

    }

    public void showLoading(String title) {
        if(getActivity()!=null){
            DialogUtil.getInstance().getLoadingDialogShow(getActivity(),title);
        }
    }

    public void hideLoading() {
        DialogUtil.getInstance().setLoadingDialogDismiss();
    }



    public void showToast(String msg){
        ToastUtil.getSimpleToast(getActivity(), msg, 0).show();
    }

    public void showToast(int strId){
        ToastUtil.getSimpleToast(getActivity(), strId, 0).show();
    }

    public void showToast(String msg,int duration){
        ToastUtil.getSimpleToast(getActivity(), msg, duration).show();
    }

    public void showToast(int strId,int duration){
        ToastUtil.getSimpleToast(getActivity(), strId, duration).show();
    }


    public void showDialog(String description, Runnable sureCallback) {
        showDialog(null,description,null,null,sureCallback,null,0);
    }

    //param mp  messagePosition  1 居左   0 居中
    public void showDialog(String title,CharSequence description,String sure,String cancel, final Runnable sureCallback,final Runnable cancelCallback,int mp) {
        CommonUtil.showDialog(getActivity(),title,description,sure,cancel,sureCallback,cancelCallback,mp);
    }


}
