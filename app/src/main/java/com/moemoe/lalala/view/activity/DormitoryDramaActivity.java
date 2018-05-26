package com.moemoe.lalala.view.activity;

import android.content.Intent;
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
        binding.setPresenter(new Presenter());
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        binding.principalLineSchedule.setText("收集度:" + schedule + "%");

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
                case R.id.drama_back_btn:
                    finish();
                    break;
                case R.id.principal_line_btn:
                    Intent intent = new Intent(DormitoryDramaActivity.this, PrincipalLineActivity.class);
                    startActivity(intent);
                    break;
                case R.id.branch_btn:
                    Intent i = new Intent(DormitoryDramaActivity.this, BranchActivity.class);
                    startActivity(i);
                    break;
                case R.id.every_day_btn:
                    Intent intent1 = new Intent(DormitoryDramaActivity.this, EveryDayActivity.class);
                    startActivity(intent1);
                    break;
                default:
                    break;
            }
        }

    }
}
