package com.moemoe.lalala.view.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.databinding.ActivityEveryDayInfoBinding;
import com.moemoe.lalala.utils.SpacesItemDecoration;
import com.moemoe.lalala.view.adapter.ActivityEveryDayInfoAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.base.PrincipalListBean;

import java.util.ArrayList;
import java.util.List;

public class EveryDayInfoActivity extends BaseActivity {

    private ActivityEveryDayInfoBinding binding;
    private ActivityEveryDayInfoAdapter mAdapter;
    private List<PrincipalListBean> data;
    private List<Bitmap> extraBmps;


    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_every_day_info);
        binding.setPresenter(new Presenter());
        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new ActivityEveryDayInfoAdapter(this, data);
        binding.everyDayInfoRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.everyDayInfoRecycleView.addItemDecoration(new SpacesItemDecoration(30));
        binding.everyDayInfoRecycleView.setAdapter(mAdapter);
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
                case R.id.every_day_info_back_btn:
                    finish();
                    break;
                default:
                    break;
            }
        }
    }
}
