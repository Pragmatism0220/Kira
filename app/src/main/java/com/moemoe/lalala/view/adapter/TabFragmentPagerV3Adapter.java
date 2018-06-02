package com.moemoe.lalala.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.moemoe.lalala.model.entity.ClothingEntity;
import com.moemoe.lalala.model.entity.PhoneFukuEntity;
import com.moemoe.lalala.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yi on 2016/12/1.
 */

public class TabFragmentPagerV3Adapter extends FragmentPagerAdapter {

    List<BaseFragment> fragmentList = new ArrayList<>();
    private ArrayList<ClothingEntity> mTitles;
    private String signId = "";

    public TabFragmentPagerV3Adapter(FragmentManager fm, List<BaseFragment> fragmentList, ArrayList<ClothingEntity> mTitles) {
        super(fm);
        this.mTitles = mTitles;
        setFragments(fm, fragmentList, mTitles);
    }

    //刷新fragment
    public void setFragments(FragmentManager fm, List<BaseFragment> fragments, ArrayList<ClothingEntity> mTitles) {
        this.mTitles = mTitles;
        if (this.fragmentList != null) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : this.fragmentList) {
                ft.remove(f);
            }
            ft.commitAllowingStateLoss();
            ft = null;
            fm.executePendingTransactions();
        }
        this.fragmentList = fragments;
        notifyDataSetChanged();
    }

    public List<ClothingEntity> getList() {
        return mTitles;
    }
    public void setList(ArrayList<ClothingEntity> entities) {
        this.mTitles = entities;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles != null ? mTitles.get(position).getName() : "";
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
