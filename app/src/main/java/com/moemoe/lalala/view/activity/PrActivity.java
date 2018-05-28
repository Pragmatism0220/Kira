package com.moemoe.lalala.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.databinding.ActivityPrBinding;
import com.moemoe.lalala.view.adapter.PrAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.base.MainStoryBean;
import com.moemoe.lalala.view.widget.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class PrActivity extends BaseActivity {

    private ActivityPrBinding binding;
    private PrAdapter mAdapter;
    private List<MainStoryBean> beans;


    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pr);
        binding.setPresenter(new Presenter());
        getData();
        initAdapter();
    }

    private void getData() {
        beans = new ArrayList<>();
        beans.add(new MainStoryBean("没有万圣节", -1, "第一章", 2244));//全收集
        beans.add(new MainStoryBean("没有万圣节", -2, "第一章", 2244));//未解锁
        beans.add(new MainStoryBean("没有万圣节", 63, "第一章", 2244));//未收集完毕
        beans.add(new MainStoryBean("没有万圣节", -1, "第一章", 2244));
        beans.add(new MainStoryBean("没有万圣节", -1, "第一章", 2244));
        beans.add(new MainStoryBean("没有万圣节", -1, "第一章", 2244));
        beans.add(new MainStoryBean("没有万圣节", -1, "第一章", 2244));

    }

    private void initAdapter() {
        mAdapter = new PrAdapter(this, beans);
        binding.prLineRecycle.setLayoutManager(new GridLayoutManager(this, 2));
        int rightSpace = 22;
        int buttonSpace = 24;
        int top = 32;
        binding.prLineRecycle.addItemDecoration(new SpacesItemDecoration(buttonSpace, rightSpace, top));
        binding.prLineRecycle.setAdapter(mAdapter);
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

    }


    public class Presenter {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pr_line_back_btn:
                    finish();
                    break;
                default:
                    break;
            }

        }
    }
}
