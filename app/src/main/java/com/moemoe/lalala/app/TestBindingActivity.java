package com.moemoe.lalala.app;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.moemoe.lalala.R;
import com.moemoe.lalala.databinding.ActivityTestBindingBinding;
import com.moemoe.lalala.view.base.BaseActivity;

public class TestBindingActivity extends BaseActivity {

    private ActivityTestBindingBinding binding;

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_test_binding);
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
        public void onAgreement() {
            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
        }

    }
}
