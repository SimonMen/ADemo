package com.cedarwood.ademo.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cedarwood.ademo.R;
import com.cedarwood.ademo.manager.ImageManager;
import com.cedarwood.ademo.model.data.DragMarkInfo;
import com.cedarwood.ademo.model.data.DragMarkInfoLocation;
import com.cedarwood.ademo.utils.CommonUtil;
import com.cedarwood.ademo.utils.Log;
import com.cedarwood.ademo.view.drag.DragContainer;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by wentongmen on 2016/3/8.
 */
public class DragMapTestActivity extends BaseActivity {


    private static final String TAG = DragMapTestActivity.class.getSimpleName();

    private ArrayList<DragMarkInfoLocation> locations;
    private int photoWidth;
    private int photoHeight;
    private DragMarkInfo mark;
    private int screenWidth;
    private int screenHeight;
    private int temp;

    private DragContainer dragLayout;
    private MarkAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_map_test);


        DragMarkInfo mark = (DragMarkInfo) getIntent().getSerializableExtra("buildingPartMark");
        if (mark != null) {
            this.mark = mark;
            locations = mark.getMap_list();
            photoWidth = mark.getPhoto_width();
            photoHeight = mark.getPhoto_height();

            if (photoWidth >= 800) {
                photoHeight = (int) ((photoHeight / (double) photoWidth) * 800);
                photoWidth = 800;

            }
        } else {

//			"photo_height":960,
//					"photo_id":370421907,
//					"photo_url":"http://imgs.focus.cn/upload/bj/37043/a_370421907.jpg",
//					"photo_width":1280,
//					"uploadFlag":false

            //http://imgs.focus.cn/upload/qhd/8225/b_82243980.jpg 800 500
            //http://i2.f.itc.cn/upload/qhd/6232/b_62314340.jpg 800 523
            //http://imgs.focus.cn/upload/bj/37043/a_370421907.jpg 960 1280

            mark = new DragMarkInfo();
            mark.setLouzuo_title("推荐楼座图");

//			mark.setPhoto_url("http://imgs.focus.cn/upload/qhd/8225/b_82243980.jpg");
//			mark.setPhoto_height(500);
//			mark.setPhoto_width(800);

            mark.setPhoto_url("http://i2.f.itc.cn/upload/qhd/6232/b_62314340.jpg");
            mark.setPhoto_height(523);
            mark.setPhoto_width(800);
//
//			mark.setPhoto_url("http://imgs.focus.cn/upload/bj/37043/a_370421907.jpg");
//			mark.setPhoto_height(960);
//			mark.setPhoto_width(1280);

            mark.setUploadFlag(false);

            locations = new ArrayList<DragMarkInfoLocation>();

            DragMarkInfoLocation location = new DragMarkInfoLocation();
            location.setBuilding_id(1444898883);
            location.setCity_id(1);
            location.setTitle("gbbn");
            location.setLeft(0);
            location.setTop(0);
            locations.add(location);

            location = new DragMarkInfoLocation();
            location.setBuilding_id(1444898882);
            location.setCity_id(1);
            location.setTitle("vvb");
            location.setLeft(131);
            location.setTop(259);
            locations.add(location);

            location = new DragMarkInfoLocation();
            location.setBuilding_id(1444898786);
            location.setCity_id(1);
            location.setTitle("您");
            location.setLeft(337);
            location.setTop(76);
            locations.add(location);

            location = new DragMarkInfoLocation();
            location.setBuilding_id(1444898826);
            location.setCity_id(1);
            location.setTitle("笔墨婆婆");
            location.setLeft(0);
            location.setTop(0);
            locations.add(location);


            mark.setMap_list(locations);

            this.mark = mark;

            photoWidth = mark.getPhoto_width();
            photoHeight = mark.getPhoto_height();

            if (photoWidth >= 800) {
                photoHeight = (int) ((photoHeight / (double) photoWidth) * 800);
                photoWidth = 800;

            }
        }


        initView();
        initData();


    }

    private void initView() {



        Toolbar toolbar = (Toolbar) findViewById(R.id.drag_map_test_toolbar);
        toolbar.setTitle("Drag");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);


        screenWidth = CommonUtil.getScreenWidth(this);
        screenHeight = CommonUtil.getScreenHeight(this);


//		Rect frame = new Rect();
//        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//        int statusBarHeight = frame.top;

        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        TypedValue tv = new TypedValue();
        int titleBarHeight = 0;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            titleBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
//        int titleBarHeight = getResources().getDimensionPixelSize(android.R.dimen.)

        temp = (screenHeight - titleBarHeight - statusBarHeight);

        Log.i(TAG, ">>> temp " + temp);

        dragLayout = (DragContainer) findViewById(R.id.drag_map_test_drag_layout);
        LinearLayout.LayoutParams p1 = (LinearLayout.LayoutParams) dragLayout.getLayoutParams();
        p1.height = temp;
        p1.width = (int) (temp * (photoWidth / (double) photoHeight));
        dragLayout.setLayoutParams(p1);

        dragLayout.init(temp,photoWidth,photoHeight);
        dragLayout.setBg(getResources().getDrawable(R.mipmap.drag_photo_fail));
        dragLayout.setOnMoveUpListener(new DragContainer.OnMoveUpListener() {
            @Override
            public void onMoveUp(int positon, DragMarkInfoLocation location) {
                locations.set(positon, location);
                adapter.updateView(locations);
            }
        });



        ImageManager.getInstance().init(getApplicationContext());

        ImageManager.getInstance().loadImage(mark.getPhoto_url(), R.mipmap.drag_photo_fail, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String arg0, View arg1) {
            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

            }

            @SuppressLint("NewApi")
            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {

                BitmapDrawable drawable = new BitmapDrawable(getResources(), arg2);
                dragLayout.setBg(drawable);
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {


            }
        });



        GridView markGrid = (GridView) findViewById(R.id.drag_map_test_grid);

        adapter = new MarkAdapter(this);
        markGrid.setAdapter(adapter);

        adapter.updateView(locations);


        markGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final DragMarkInfoLocation location = locations.get(position);
                if (location.getLeft() > 0 || location.getTop() > 0) {
                    dragLayout.removeMark(position);
                    location.setLeft(0);
                    location.setTop(0);
                    adapter.updateView(locations);
                } else {
                    String title = location.getTitle();
                    if (!TextUtils.isEmpty(title) && title.length() > 5) {
//						showModifyTitleDialog(position,location);
                    } else {
                        location.setLeft(photoWidth / 2);
                        location.setTop(photoHeight / 2);
                        dragLayout.addMark(position, location);
                        adapter.updateView(locations);
                    }
                }

            }
        });

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < locations.size(); i++) {
                    DragMarkInfoLocation location = locations.get(i);
                    if (location.getLeft() > 0 || location.getTop() > 0) {
                        dragLayout.addMark(i, location);
                    }
                }

            }
        }, 300);

    }

    private void initData() {


    }


    private class MarkAdapter extends BaseAdapter {

        private ArrayList<DragMarkInfoLocation> locations;

        private Context mContext;

        public MarkAdapter(Context context) {
            this.mContext = context;
        }

        public void updateView(ArrayList<DragMarkInfoLocation> locations) {
            this.locations = locations;
            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return locations == null ? 0 : locations.size();
        }

        @Override
        public Object getItem(int position) {
            return locations.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.view_drag_test_mark_item, null);
                holder = new ViewHolder();
                holder.nameText = (TextView) convertView.findViewById(R.id.drag_map_test_mark_item_name_text);
                holder.layout = (LinearLayout) convertView.findViewById(R.id.drag_map_test_mark_item_layout);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            DragMarkInfoLocation location = locations.get(position);


            holder.nameText.setText(location.getTitle());

            if (location.getLeft() > 0 || location.getTop() > 0) {
                holder.nameText.setTextColor(getResources().getColor(R.color.co_text_white));
                holder.layout.setSelected(true);
            } else {
                holder.nameText.setTextColor(getResources().getColor(R.color.co_text_red));
                holder.layout.setSelected(false);
            }

            return convertView;
        }

        class ViewHolder {
            TextView nameText;
            LinearLayout layout;
        }

    }


}
