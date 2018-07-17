package com.moemoe.lalala.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.databinding.AcLibraryBinding;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.TabFragmentPagerAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.fragment.BaseFragment;
import com.moemoe.lalala.view.fragment.ClubChildFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hygge on 2018/7/17.
 */

public class LibraryActivity extends BaseActivity {

    private AcLibraryBinding binding;
    private TabFragmentPagerAdapter mAdapter;

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.ac_library);
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        binding.setPresenter(new Presenter());
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {
        List<BaseFragment> fragmentList = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        titles.add("漫画");
        titles.add("图集");
        titles.add("小说");
        titles.add("历史记录");

//        fragmentList.add(ClubChildFragment.newInstance(tags, "我的"));

        if (mAdapter == null) {
            mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, titles);
        } else {
            mAdapter.setFragments(getSupportFragmentManager(), fragmentList, titles);
        }
        binding.viewPager.setAdapter(mAdapter);
        binding.indicatorPersonData.setViewPager(binding.viewPager);
        binding.viewPager.setCurrentItem(0);
    }

    public class Presenter {
        public void onClick(View view) {

        }
    }
}
