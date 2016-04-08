package com.cedarwood.ademo.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cedarwood.ademo.R;

/**
 * Created by wentongmen on 2016/2/23.
 */
public class SimpleToast {

    /**
     * toast
     */
    public static Toast getSimpleToast(Context context,String text,int duration) {
        return getSimpleToast(context,-1,text,duration);
    }

    /**
     * 获取自定义toast  该toast比较简单如果需要定制性比较高的可在下面再写
     * @param context 上下文
     * @param resId  资源id（如是sd卡的资源，可在下面再下一个方法）如-1则不显示图片
     * @param text  显示的文字
     * @param duration 0 短的Toast.LENGTH_SHORT，1 长的
     * @return
     */
    public static Toast getSimpleToast(Context context, int resId,String text,int duration) {
        Toast toast = new Toast(context);
        View view = View.inflate(context, R.layout.view_simple_toast, null);
        TextView tv = (TextView) view.findViewById(R.id.simple_toast_text);
        ImageView iv = (ImageView) view.findViewById(R.id.simple_toast_img);


//			String tempBbStr = "drawable"+File.separator+imageName;
//			int resId = context.getResources().getIdentifier(tempBbStr, null, context.getPackageName());
        if(resId>0){
            iv.setVisibility(View.VISIBLE);
            iv.setImageResource(resId);
        }else if(resId==-1){
            iv.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(text)){
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        }else{
            tv.setVisibility(View.GONE);
        }

        //如需自定义时长可参考http://www.apkbus.com/forum.php?mod=viewthread&tid=48443
        if(duration==0)
            toast.setDuration(Toast.LENGTH_SHORT);
        else
            toast.setDuration(Toast.LENGTH_LONG);

        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(view);
        return toast;
    }

}
