package com.cedarwood.ademo.activity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cedarwood.ademo.R;
import com.cedarwood.ademo.utils.CommonUtil;
import com.cedarwood.ademo.view.camera.CameraContainer;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by wentongmen on 2016/3/9.
 */
public class CameraTestActivity extends BaseActivity {


    private CameraContainer cameraView;
    private Button picBtn;
    private TextView sureText;
    private TextView cancelText;
    private TextView picNumText;
    private int from;
    private int urlNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_test);

        from = getIntent().getIntExtra("from", 2);

        String l = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+File.separator+"camera";

        initView();

        if(from==1){
            ArrayList<File> photoFiles = CommonUtil.getPhotoFiles(l);
            if(photoFiles!=null && photoFiles.size()>0){
                for(int i=0;i<photoFiles.size()-1;i++){
                    File file = photoFiles.get(i);
                    if(file.exists()){
                        file.delete();
                    }
                }
            }
        }

        if(from == 3){
            urlNum = getIntent().getIntExtra("urlNum", 0);
        }

        cameraView.init(from);
    }

    @Override
    protected void onResume() {
        super.onResume();

        cameraView.refreshPhotos();
        updatePicNum();
    }


    private void initView() {


        cameraView = (CameraContainer) findViewById(R.id.camera_test_preview);
        picBtn = (Button) findViewById(R.id.camera_test_take_picture);
        cancelText = (TextView)findViewById(R.id.camera_test_go_back);
        sureText = (TextView) findViewById(R.id.camera_test_picture_finish);
        picNumText = (TextView)findViewById(R.id.camera_test_picture_nums);

        if(from==1){
            sureText.setVisibility(View.INVISIBLE);
            picNumText.setVisibility(View.INVISIBLE);
        }else{
            sureText.setVisibility(View.VISIBLE);
            picNumText.setVisibility(View.VISIBLE);
        }


        //设置点击事件监听器
        picBtn.setOnClickListener(clickListener);
        sureText.setOnClickListener(clickListener);
        cancelText.setOnClickListener(clickListener);
        //完成拍照后的回调接口设置
        cameraView.setFinishTakeListener(cameraUtilListener);


        int w = CommonUtil.getScreenWidth(this);
        ViewGroup.LayoutParams params = cameraView.getLayoutParams();
        params.width = w;
        params.height = (int)(w/(double)3*4);
        cameraView.setLayoutParams(params);

    }


    View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.camera_test_take_picture:
                    takePicture();
                    break;
                case R.id.camera_test_picture_finish:
                    jump();
                    break;
                case R.id.camera_test_go_back:
                    back();
                    break;


                default:
                    break;
            }

        }
    };

    private void takePicture(){
        if(from==3){
            int picNum = cameraView.getPictures().length;
            if(picNum+urlNum>=9){
                showToast("照片数目已达上限");
                return;
            }
        }

        cameraView.takePicture();
    }

    CameraContainer.CameraUtilListener cameraUtilListener = new CameraContainer.CameraUtilListener() {

        @Override
        public void onFinishTakPicture() {
            if(from==1){
                jump();
            }else{
                updatePicNum();
            }


        }

        @Override
        public void onFailedSetResolutionRatio() {

            showToast("相机分辨率太低，无法拍摄照片");
            back();
        }

        @Override
        public void onFailedOpenCamera() {
            showToast("打开相机失败，请确定相关权限是否被允许!");
            back();
        }
    };



    private void updatePicNum(){

        if(picNumText !=null&& cameraView!=null){
            int picNum = cameraView.getPictures().length;

            if(from==3){
                picNum = picNum+urlNum;
            }

            if(picNum>0 && from !=1) {//照片数量大于0才显示照片张数视图
                String info = picNum + "";
                picNumText.setText(info);
                if(picNumText.getVisibility()!=View.VISIBLE){
                    picNumText.setVisibility(View.VISIBLE);
                }
            }else if(picNumText.getVisibility()!=View.GONE){
                picNumText.setVisibility(View.GONE);
            }
        }

    }

    protected void jump() {
        int picNum = cameraView.getPictures().length;
        if(from==1){
//            Intent intent = new Intent(this, HouseManageAddActivity.class);
//            intent.putExtra("building", info);
//            startActivity(intent);
            showToast("from==1  picNum : "+picNum);
        }else if(from==2){
            if(picNum<=0){
                showToast("尚未拍摄照片");
                return;
            }

            showToast("from==2  picNum : "+picNum);

//            Intent intent = new Intent(this, HouseManagePhotoAddActivity.class);
//            intent.putExtra("building", info);
//            intent.putExtra("houseInfo", house);
//            intent.putExtra("position", position);
//            startActivity(intent);
        }else if(from==3){
            showToast("from==3  picNum : "+picNum+urlNum);
//            EventManager.getInstance().post(new EventConsts.ReleasePicEvent());

        }

//        finish();
    }

}
