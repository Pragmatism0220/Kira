package com.moemoe.lalala.view.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.moemoe.lalala.view.fragment.BaseFragment;

import java.util.List;

/**
 * Created by Administrator on 2018/5/15.
 */

public class BranchFragmentAdapter extends FragmentPagerAdapter {

//    private String[] title = {"全部", "美藤双树", "沙利尔", "美藤双树"};
    private List<Fragment> fragmentList;

    public BranchFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return title[position];
//    }
}
