package com.cedarwood.ademo.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cedarwood.ademo.R;


public class ChoiceDialog extends Dialog{
    
    private View.OnClickListener mPositiveOnClickListener;
    private View.OnClickListener mNegativeOnClickListener;


    public ChoiceDialog(Context context) {
        super(context);
        
        Window window = getWindow();  
//        WindowManager.LayoutParams windowparams = window.getAttributes();  
        window.setGravity(Gravity.BOTTOM);  
//        Rect rect = new Rect();  
//        View view1 = window.getDecorView();  
//        view1.getWindowVisibleDisplayFrame(rect);  
//        windowparams.width = CommonUtil.getServiceScreenWidth(getContext());
        window.setWindowAnimations(R.style.AnimBottom);  
        window.setBackgroundDrawableResource(android.R.color.transparent);  
//        window.setAttributes((android.view.WindowManager.LayoutParams) windowparams);  
        
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_choice);
        
    }


    public static class Builder {
        private DialogParams dialogParams;

        public Builder(Context context) {
            dialogParams = new DialogParams(context);
        }

        public Builder setIcon(int iconId) {
            dialogParams.mIconId = iconId;
            return this;
        }

        public Builder setTitle(String title) {
            dialogParams.mTitle = title;
            return this;
        }

        public Builder setMessage(CharSequence message) {
            dialogParams.mMessage = message;
            return this;
        }
        
        public Builder setMessagePosition(int p) {
            dialogParams.mMessagePosition = p;
            return this;
        }
        
        public Builder setNegativeColor(int color) {
            dialogParams.mNegativeColor = color;
            return this;
        }
        
        public Builder setPositiveColor(int color) {
            dialogParams.mPositiveColor = color;
            return this;
        }

        public Builder setPositiveButton(int textId, final View.OnClickListener listener) {
            dialogParams.mPositiveButtonListener = listener;
            dialogParams.mPositiveButtonText = dialogParams.mContext.getString(textId);
            return this;
        }

        public Builder setPositiveButton(String text, final View.OnClickListener listener) {
            dialogParams.mPositiveButtonText = text;
            dialogParams.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(int textId, final View.OnClickListener listener) {
            dialogParams.mNegativeButtonText = dialogParams.mContext.getString(textId);
            dialogParams.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(String text, final View.OnClickListener listener) {
            dialogParams.mNegativeButtonText = text;
            dialogParams.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            dialogParams.mCancelable = cancelable;
            return this;
        }
        

        public ChoiceDialog create() {
            final ChoiceDialog dialog = new ChoiceDialog(dialogParams.mContext);
            dialog.apply(dialogParams);
            dialog.setCancelable(dialogParams.mCancelable);
            if (dialogParams.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            return dialog;
        }
    }

    private void apply(DialogParams dialogParams) {
//        if (TextUtils.isEmpty(dialogParams.mNegativeButtonText)
//                && TextUtils.isEmpty(dialogParams.mPositiveButtonText)) {
//            findViewById(R.id.dialog_bottom).setVisibility(View.GONE);
//        }

        if (!TextUtils.isEmpty(dialogParams.mTitle)) {
            findViewById(R.id.choice_title).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.choice_title)).setText(dialogParams.mTitle);
        }

        if (!TextUtils.isEmpty(dialogParams.mMessage)) {
            findViewById(R.id.choice_content).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.choice_content)).setText(dialogParams.mMessage);
            
            TextView message = (TextView) findViewById(R.id.choice_content);
        	LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) message.getLayoutParams();
            
            if(dialogParams.mMessagePosition==1){
            	params.gravity = Gravity.LEFT;
            }else{
            	params.gravity = Gravity.CENTER_HORIZONTAL;
            }
            message.setLayoutParams(params);
        }

        if (!TextUtils.isEmpty(dialogParams.mNegativeButtonText)) {
            findViewById(R.id.choice_cancel_text).setVisibility(View.VISIBLE);
            findViewById(R.id.choice_btn_line).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.choice_cancel_text)).setText(dialogParams.mNegativeButtonText);
            findViewById(R.id.choice_cancel_text).setOnClickListener(mButtonClickListener);
            if (dialogParams.mNegativeColor != 0) {
                ((TextView) findViewById(R.id.choice_cancel_text)).setTextColor(dialogParams.mNegativeColor);
            }
        }
        if (!TextUtils.isEmpty(dialogParams.mPositiveButtonText)) {
            findViewById(R.id.choice_sure_text).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.choice_sure_text)).setText(dialogParams.mPositiveButtonText);
            findViewById(R.id.choice_sure_text).setOnClickListener(mButtonClickListener);
            if (dialogParams.mPositiveColor != 0) {
                ((TextView) findViewById(R.id.choice_sure_text)).setTextColor(dialogParams.mPositiveColor);
            }
        }

        mPositiveOnClickListener = dialogParams.mPositiveButtonListener;
        mNegativeOnClickListener = dialogParams.mNegativeButtonListener;

    }

    View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.choice_cancel_text:
                    if (mNegativeOnClickListener != null)
                        mNegativeOnClickListener.onClick(findViewById(R.id.choice_cancel_text));
                    break;
                case R.id.choice_sure_text:
                    if (mPositiveOnClickListener != null)
                        mPositiveOnClickListener.onClick(findViewById(R.id.choice_sure_text));
                    break;
            }
            dismiss();
        }
    };

    static class DialogParams {
        public final Context mContext;

        public int mIconId;
        public String mTitle;
        public CharSequence mMessage;
        public String mPositiveButtonText;
        public View.OnClickListener mPositiveButtonListener;
        public String mNegativeButtonText;
        public View.OnClickListener mNegativeButtonListener;
        public int mNegativeColor = 0;
        public int mPositiveColor = 0;
        public boolean mCancelable;
        public int mMessagePosition;

        DialogParams(Context mContext) {
            this.mContext = mContext;
        }
    }

//    @Override
//    public void show() {
//        super.show();
//        ColorDrawable dw = new ColorDrawable(0x0000ff00);
//        getWindow().setBackgroundDrawable(dw);
//        int width = AppApplication.getInstance().mScreenWidth * 6 / 7;
//        getWindow().setLayout(width, -2);
//    }
    
    
    
}
