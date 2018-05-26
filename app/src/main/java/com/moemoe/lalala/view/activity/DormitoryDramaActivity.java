package com.moemoe.lalala.view.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.databinding.ActivityDormitoryDramaBinding;
import com.moemoe.lalala.view.base.BaseActivity;

/**
 * do(zhangyan)
 * 剧情主界面
 */

public class DormitoryDramaActivity extends BaseActivity {

    private ActivityDormitoryDramaBinding binding;

    private int schedule = 74;

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dormitory_drama);
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

        }

    }
}
