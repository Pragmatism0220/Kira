package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moemoe.lalala.BR;
import com.moemoe.lalala.R;
import com.moemoe.lalala.databinding.ActivityAccountAndSecurityBinding;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.view.base.BaseActivity;

public class AccountAndSecurityActivity extends BaseActivity {

    private ActivityAccountAndSecurityBinding binding;
    private TextView mTvTitle;
    private ImageView mIvBack;
    private String mTvKiraNum;

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account_and_security);
        binding.setPresenter(new Presenter());
        mTvTitle = findViewById(R.id.tv_toolbar_title);
        mIvBack = findViewById(R.id.iv_back);
        Intent intent = getIntent();
        mTvKiraNum = intent.getStringExtra("mTvKiraNum");
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        binding.accountKiraNum.setText(mTvKiraNum);
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {
        mTvTitle.setText(R.string.label_setting);
        mTvTitle.setTextColor(getResources().getColor(R.color.black));
        mIvBack.setVisibility(View.VISIBLE);
        mIvBack.setImageResource(R.drawable.btn_back_black_normal);
        mIvBack.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

    public class Presenter {
        public void onClick(View view) {
            switch (view.getId()) {

            }
        }
    }
}
