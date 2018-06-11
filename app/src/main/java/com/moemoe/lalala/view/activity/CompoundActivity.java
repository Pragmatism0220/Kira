package com.moemoe.lalala.view.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.databinding.ActivityCompoundBinding;
import com.moemoe.lalala.view.base.BaseActivity;

public class CompoundActivity extends BaseActivity {

    private ActivityCompoundBinding binding;

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_compound);
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

        }
    }
}
