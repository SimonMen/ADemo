package com.cedarwood.ademo.fragment;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cedarwood.ademo.R;


public class DoubleDragPicFragment extends BaseFragment {


    Integer [] imageIds = {R.mipmap.cheese_1,R.mipmap.cheese_2,R.mipmap.cheese_3,R.mipmap.cheese_4,R.mipmap.cheese_5};
    private ViewPager mViewPager;

    private boolean showfronttoast = true;
    private boolean showendtoast = true;

    private StateListener listener;
    private LinearLayout mNumberLayout;
    private TextView mLeftText;
    private TextView mRightText;


    public View onCreateView(android.view.LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_double_drag_header_pic, null);
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        setClosed();
    }

    private void initView() {
        mViewPager = (ViewPager) getView().findViewById(R.id.double_drag_header_image_viewpager);
        mNumberLayout = (LinearLayout) getView().findViewById(R.id.double_drag_header_image_number_layout);
        mLeftText = (TextView) getView().findViewById(R.id.double_drag_header_image_number_left_text);
        mRightText = (TextView) getView().findViewById(R.id.double_drag_header_image_number_right_text);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(onPageChangeListener);

    }





    private ViewPager.OnPageChangeListener onPageChangeListener =
            new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i2) {}

                @Override
                public void onPageSelected(int i) {
                    mLeftText.setText((i + 1) + "/"+ mAdapter.getCount());
                    mRightText.setText((i + 1) + "/"+ mAdapter.getCount());
                }

                @Override
                public void onPageScrollStateChanged(int i) {
                    laststatus = currstatus;
                    currstatus = i;
                    if (1 == mAdapter.getCount()) {
                        showToast("只有一张图片");
                    } else {
                        if (laststatus == 1 && currstatus == 0
                                && mViewPager.getCurrentItem() == mAdapter.getCount() - 1) {
                            if (showendtoast) {
                                showToast("没有更多图片了");
                                showendtoast = false;
                            }
                        }
                        if (mViewPager.getCurrentItem() != mAdapter.getCount() - 1) {
                            showendtoast = true;
                        }

                        if (laststatus == 1 && currstatus == 0 && mViewPager.getCurrentItem() == 0) {
                            if (showfronttoast) {
                                showToast("前面没有更多图片了");
                                showfronttoast = false;
                            }
                        }

                        if (mViewPager.getCurrentItem() > 0) {
                            showfronttoast = true;
                        }

                    }
                }
            };
    private int currentIndex = -11;
    private int laststatus, currstatus;



    private PagerAdapter mAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            if (imageIds == null || imageIds.length==0) return 0;
            return imageIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == (View) o;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // PhotoView imageView = new PhotoView(getActivity());
            ImageView imageView = new ImageView(getActivity());
            // imageView.setZoomTimer(PhotoView.SINGLE);
            imageView.setBackgroundColor(getResources().getColor(R.color.co_global_bg));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));

//                RequestLoader.getInstance().displayImage(
//                        mList.get(position).getPicUrl(), imageView, ImageView.ScaleType.FIT_CENTER,
//                        R.drawable.logo_image_browser_default,
//                        R.drawable.logo_image_browser_default, "", null);
            imageView.setImageResource(imageIds[position]);

            imageView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onStateclick();
                    }
                }
            });
            ((ViewPager) container).addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    };



    public interface StateListener {
        void onStateclick();
    }

    public StateListener getListener() {
        return listener;
    }

    public void setListener(StateListener listener) {
        this.listener = listener;
    }


    public void setClosed() {

        mLeftText.setVisibility(View.VISIBLE);
        mRightText.setVisibility(View.INVISIBLE);

    }

    public void setOpen() {
        mRightText.setVisibility(View.VISIBLE);
        mLeftText.setVisibility(View.INVISIBLE);
        setMatch();
    }

    public void setTouch() {
        mRightText.setVisibility(View.INVISIBLE);
        mLeftText.setVisibility(View.INVISIBLE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
        mViewPager.setLayoutParams(params);
    }

    public void setMatch() {
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
        mViewPager.setLayoutParams(params);
    }
}
