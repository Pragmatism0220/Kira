package com.moemoe.lalala.view.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.moemoe.lalala.R;
import com.moemoe.lalala.view.adapter.FurnitureAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangyan on 2018/5/21.
 * 储物箱中家具fragment
 */

public class FurnitureFragment extends BaseFragment {

//    @BindView(R.id.tab)
//    TabLayout mTab;
    @BindView(R.id.furniture_viewpager)
    ViewPager mFurnitureViewPager;

    private List<Fragment> fragmentList = new ArrayList<>();
    private FurnitureAdapter mAdapter;
    private String[] titles = {"全部", "套装", "桌子", "电脑", "电视机"};


    @Override
    protected int getLayoutId() {
        return R.layout.furniture_fragment;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        FurnitureFragment fragment = new FurnitureFragment();
        fragmentList.add(fragment);
        mAdapter = new FurnitureAdapter(getChildFragmentManager(), fragmentList);
        mFurnitureViewPager.setAdapter(mAdapter);
//        mTab.setupWithViewPager(mFurnitureViewPager);
//        for (int i = 0; i < mAdapter.getCount(); i++) {
//            TabLayout.Tab tab = mTab.getTabAt(i);
//////            tab.setCustomView(R.layout)
////            if (i == 0) {
////                tab.getCustomView().findViewById(R.id.)
////            }
//        }

    }


}
