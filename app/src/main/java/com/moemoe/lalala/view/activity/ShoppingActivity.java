package com.moemoe.lalala.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.databinding.ActivityShoppingBinding;
import com.moemoe.lalala.view.adapter.TabFragmentPagerAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.fragment.BaseFragment;
import com.moemoe.lalala.view.fragment.ShoppingFragment;

import java.util.ArrayList;
import java.util.List;

public class ShoppingActivity extends BaseActivity {


    private ActivityShoppingBinding binding;
    private TabFragmentPagerAdapter mAdapter;


    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shopping);
        binding.setPresenter(new Presenter());
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ArrayList<String> mTitles = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            mTitles.add("热销");
        }
        List<BaseFragment> fragmentList = new ArrayList<>();
        for (int i = 0; i < mTitles.size(); i++) {
            fragmentList.add(ShoppingFragment.newInstance());
        }
        if (mAdapter == null) {
            mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, mTitles);
        } else {
            mAdapter.setFragments(getSupportFragmentManager(), fragmentList, mTitles);
        }
        binding.viewPager.setAdapter(mAdapter);
        binding.tl8.setViewPager(binding.viewPager);
        binding.viewPager.setCurrentItem(0);
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }

    public class Presenter {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.shopping_back:
                    finish();
                    break;
            }
        }
    }
}
