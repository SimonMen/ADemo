package com.cedarwood.ademo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cedarwood.ademo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity {


    private String[] names = {"TabBarTestActivity","TabBarViewPagerTestActivity", "PullListViewTestActivity", "SwipeRefreshTestActivity", "DragMapTestActivity", "PickerTestActivity",
            "CameraTestActivity", "CheeseTestActivity", "RecyclerAnimatorsTestActivity", "TouchTestActivity", "DoubleDragTestActivity", "DoubleDragTest2Activity","JazzyViewPagerTestActivity", "OnOffTestActivity",
            "虫儿飞##", "路长", "虫儿飞$$", "虫儿飞","人生路", "虫儿飞~~", "虫儿飞", "人生路", "虫儿飞~~", "虫儿飞", "人生路", "虫儿飞~~", "虫儿飞", "人生路", "虫儿飞~~"};

    private Class[] classNames={ TabBarTestActivity.class,TabBarViewPagerTestActivity.class,PullListViewTestActivity.class,SwipeRefreshTestActivity.class,
            DragMapTestActivity.class, PickerTestActivity.class,CameraTestActivity.class,CheeseTestActivity.class,RecyclerAnimatorsTestActivity.class,
            TouchTestActivity.class,DoubleDragTestActivity.class,DoubleDragTest2Activity.class,JazzyViewPagerTestActivity.class,OnOffTestActivity.class};

    private List<String> texts = new ArrayList<String>();

    private HomeAdapter adapter;
    private RecyclerView recycler;
    private Toolbar toolbar;
    private CollapsingToolbarLayout callapsingToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        texts = new ArrayList<String>(Arrays.asList(names));

        initView();

        initData();

    }


    private void initView() {

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        callapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.main_collapse_toolbar);
        callapsingToolbar.setTitle("Main");
        callapsingToolbar.setTitleEnabled(false);

        recycler = (RecyclerView) findViewById(R.id.main_recycler);

//        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new HomeAdapter();
        recycler.setAdapter(adapter);
//        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
//        recycler.addItemDecoration(new DividerGridItemDecoration(this));
        recycler.setItemAnimator(new DefaultItemAnimator());

        adapter.setOnItemClickLitener(new OnItemClickLitener()
        {

            @Override
            public void onItemClick(View view, int position)
            {

                if(position<classNames.length){
                    Intent intent =new Intent(MainActivity.this,classNames[position]);
                    startActivity(intent);
                }

                Toast.makeText(MainActivity.this, position + " click",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position)
            {
                Toast.makeText(MainActivity.this, position + " long click",
                        Toast.LENGTH_SHORT).show();
                adapter.removeData(position);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initData() {

        toolbar.setTitle("Main");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }



    public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

        private final ArrayList<Integer> mHeights;

        HomeAdapter(){
            mHeights = new ArrayList<Integer>();
            for (int i = 0; i < texts.size(); i++)
            {
                mHeights.add( (int) (300 + Math.random() * 200));
            }
        }

        private OnItemClickLitener mOnItemClickLitener;

        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
        {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }



        @Override
        public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_main_recycler_item, parent, false);
            HomeViewHolder holder = new HomeViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final HomeViewHolder holder, int position) {



//            int height = (int)Math.random()*300+200;

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.layout.getLayoutParams();
            params.height = mHeights.get(position);
            holder.layout.setLayoutParams(params);

            holder.tv.setText(names[position]);

            if (mOnItemClickLitener != null)
            {
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onItemClick(holder.itemView, pos);
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
                {
                    @Override
                    public boolean onLongClick(View v)
                    {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                        return false;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return texts.size();
        }

        public void addData(int position)
        {
            texts.add(position, "Insert One");
            mHeights.add( (int) (300 + Math.random() * 200));
            notifyItemInserted(position);
        }

        public void removeData(int position)
        {
            texts.remove(position);
            notifyItemRemoved(position);
        }

        class HomeViewHolder extends RecyclerView.ViewHolder {

            TextView tv;
            LinearLayout layout;

            public HomeViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.main_recycler_item_text);
                layout = (LinearLayout) view.findViewById(R.id.main_recycler_item_layout);
            }
        }




    }

    public interface OnItemClickLitener{

        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.id_action_add:
                adapter.addData(classNames.length);
                break;
            case R.id.id_action_delete:
                adapter.removeData(classNames.length);
                break;
            case R.id.id_action_gridview:
//                recycler.setLayoutManager(new GridLayoutManager(this, 4));
                break;
            case R.id.id_action_listview:
//                recycler.setLayoutManager(new LinearLayoutManager(this));
                break;
            case R.id.id_action_horizontalGridView:
//                recycler.setLayoutManager(new StaggeredGridLayoutManager(4,
//                        StaggeredGridLayoutManager.HORIZONTAL));
                break;

            case R.id.id_action_staggeredgridview:
//                Intent intent = new Intent(this , StaggeredGridLayoutActivity.class);
//                startActivity(intent);
                break;
        }
        return true;
    }


}
