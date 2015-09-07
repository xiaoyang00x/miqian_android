package com.miqian.mq.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.miqian.mq.R;
import com.miqian.mq.adapter.TabPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/27.
 */
public class FragmentMain extends Fragment {

    private ViewPager mViewPager;
    private ImageButton tabFirst;
    private ImageButton tabSecond;
    private ImageButton tabThird;
    private ImageButton tabMore;

    private List<Fragment> mFragmentList;
    private View view;
    private PagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.frame_main, null);
        }

        tabFirst = (ImageButton) view.findViewById(R.id.image_tab_1);
        tabSecond = (ImageButton) view.findViewById(R.id.image_tab_2);
        tabThird = (ImageButton) view.findViewById(R.id.image_tab_3);
        tabMore = (ImageButton) view.findViewById(R.id.image_tab_4);

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int index) {
                tabFirst.setBackgroundResource(R.mipmap.home_tab_home);
                tabSecond.setBackgroundResource(R.mipmap.home_tab_account);
                tabThird.setBackgroundResource(R.mipmap.home_tab_more);
                tabMore.setBackgroundResource(R.mipmap.home_tab_more);
                switch (index) {
                    case 0:
                        tabFirst.setBackgroundResource(R.mipmap.home_tab_home_select);
                        break;
                    case 1:
                        tabSecond.setBackgroundResource(R.mipmap.home_tab_account_select);
                        break;
                    case 2:
                        tabThird.setBackgroundResource(R.mipmap.home_tab_more_select);
                        break;
                    case 3:
                        tabMore.setBackgroundResource(R.mipmap.home_tab_more_select);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int index) {
            }
        });

        mFragmentList = new ArrayList<Fragment>();

        mFragmentList.add(new FragmentHome());
        mFragmentList.add(new FragmentCurrent());
        mFragmentList.add(new FragmentTab());
        mFragmentList.add(new FragmentTab());

        if (adapter == null) {
            adapter = new TabPagerAdapter(getFragmentManager(), mFragmentList);
        }
        mViewPager.setAdapter(adapter);

        tabFirst.setOnClickListener(new MyOnClickListener(0));
        tabSecond.setOnClickListener(new MyOnClickListener(1));
        tabThird.setOnClickListener(new MyOnClickListener(2));
        tabMore.setOnClickListener(new MyOnClickListener(3));
        return view;
    }


    /**
     * 头标点击监听
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mViewPager.setCurrentItem(index);
        }
    };
}
