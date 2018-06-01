package com.moemoe.lalala.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.moemoe.lalala.R;
import com.moemoe.lalala.databinding.ActivityBranchBinding;
import com.moemoe.lalala.view.adapter.TabFragmentPagerAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.fragment.BaseFragment;
import com.moemoe.lalala.view.fragment.BranchFragment;
import com.moemoe.lalala.view.widget.view.KiraTabLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * 支线剧情页面
 * by zhangyan
 */

public class BranchActivity extends BaseActivity {

    private ActivityBranchBinding binding;
    private TabFragmentPagerAdapter mAdapter;
    private KiraTabLayout mTab;


    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_branch);
        binding.setPresenter(new Presenter());
//        mTab = findViewById(R.id.branch_tab_layout);
    }


    @Override
    protected void initViews(Bundle savedInstanceState) {
        ArrayList<String> mTitles = new ArrayList<>();
        mTitles.add("全部");
        mTitles.add("美藤双树");
        mTitles.add("沙利尔");
        mTitles.add("美藤双树");
        List<BaseFragment> fragmentList = new ArrayList<>();
        for (int i = 0; i < mTitles.size(); i++) {
            fragmentList.add(BranchFragment.newInstance());
        }
        if (mAdapter == null) {
            mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, mTitles);
        } else {
            mAdapter.setFragments(getSupportFragmentManager(), fragmentList, mTitles);
        }
        binding.branchViewpager.setAdapter(mAdapter);
        mTab.setViewPager(binding.branchViewpager);
        binding.branchViewpager.setCurrentItem(0);
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
                case R.id.check_box_btn:
                    if (binding.checkBoxBtn.isChecked()) {
                        Toast.makeText(getApplicationContext(), "未拥有", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "已拥有", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_back:
                    finish();
                    break;
                default:
                    break;
            }
        }

    }
}
