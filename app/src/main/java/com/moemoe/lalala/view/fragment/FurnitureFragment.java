package com.moemoe.lalala.view.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.moemoe.lalala.R;
import com.moemoe.lalala.view.adapter.FurnitureAdapter;
import com.moemoe.lalala.view.adapter.TabFragmentPagerAdapter;
import com.moemoe.lalala.view.widget.view.KiraTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangyan on 2018/5/21.
 * 储物箱中家具fragment
 */

public class FurnitureFragment extends BaseFragment {

    @BindView(R.id.furniture_viewpager)
    ViewPager mFurnitureViewPager;
    @BindView(R.id.furniture_tab)
    KiraTabLayout mTab;


    private TabFragmentPagerAdapter mAdapter;


    private static FurnitureFragment newInstance() {
        return new FurnitureFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.furniture_fragment;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ArrayList<String> mTitles = new ArrayList<>();
        mTitles.add("全部");
        mTitles.add("套装");
        mTitles.add("桌子");
        mTitles.add("电脑");
        mTitles.add("电视机");

        List<BaseFragment> fragmentList = new ArrayList<>();
        for (int i = 0; i < mTitles.size(); i++) {
            fragmentList.add(FurnitureInfoFragment.newInstance());
        }
        if (mAdapter == null) {
            mAdapter = new TabFragmentPagerAdapter(getChildFragmentManager(), fragmentList, mTitles);
        } else {
            mAdapter.setFragments(getChildFragmentManager(), fragmentList, mTitles);
        }
        mFurnitureViewPager.setAdapter(mAdapter);
        mTab.setViewPager(mFurnitureViewPager);
        mFurnitureViewPager.setCurrentItem(0);

    }


}
