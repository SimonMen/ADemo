package com.cedarwood.ademo.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.cedarwood.ademo.utils.CommonUtil;
import com.cedarwood.ademo.utils.DialogUtil;
import com.cedarwood.ademo.utils.ToastUtil;

/**
 * Created by wentongmen on 2016/2/23.
 */
public class BaseActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }


    public void showLoading() {
        if(this!=null){
            DialogUtil.getInstance().getLoadingDialogShow(this);
        }

    }

    public void showLoading(String title) {
        if(this!=null){
            DialogUtil.getInstance().getLoadingDialogShow(this,title);
        }
    }

    public void hideLoading() {
        DialogUtil.getInstance().setLoadingDialogDismiss();
    }



    public void showToast(String msg){
        ToastUtil.getSimpleToast(this, msg, 0).show();
    }

    public void showToast(int strId){
        ToastUtil.getSimpleToast(this, strId, 0).show();
    }

    public void showToast(String msg,int duration){
        ToastUtil.getSimpleToast(this, msg, duration).show();
    }

    public void showToast(int strId,int duration){
        ToastUtil.getSimpleToast(this, strId, duration).show();
    }


    public void showDialog(String description, Runnable sureCallback) {
        showDialog(null,description,null,null,sureCallback,null,0);
    }

    //param mp  messagePosition  1 居左   0 居中
    public void showDialog(String title,CharSequence description,String sure,String cancel, final Runnable sureCallback,final Runnable cancelCallback,int mp) {
        CommonUtil.showDialog(this,title,description,sure,cancel,sureCallback,cancelCallback,mp);
    }


    public void back(){
        finish();
//        overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
    }


    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
            back();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    };

}
