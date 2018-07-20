package com.moemoe.lalala.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.databinding.AcKissBinding;
import com.moemoe.lalala.view.base.BaseActivity;

/**
 * Created by Hygge on 2018/7/18.
 */

public class KissActivity extends BaseActivity {
    private AcKissBinding binding;

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.ac_kiss);
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

    }

    public class Presenter {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_accept:
                    break;
                case R.id.tv_refuse:
                    break;
            }
        }
    }
}
