package com.moemoe.lalala.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;


import com.moemoe.lalala.R;
import com.moemoe.lalala.databinding.ActivityDiaryBinding;
import com.moemoe.lalala.view.adapter.DiaryAdapter;
import com.moemoe.lalala.view.base.BaseActivity;


import java.util.Timer;
import java.util.TimerTask;

public class DiaryActivity extends BaseActivity {

    private ActivityDiaryBinding binding;
    private DiaryAdapter mAdapter;

    private Handler handler = new Handler();

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_diary);
        binding.setPresenter(new Presenter());
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        binding.diaryDialog.setVisibility(View.GONE);
                    }
                };
                handler.postDelayed(runnable, 3000);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 3000);
        binding.diaryRv.setNestedScrollingEnabled(false);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public class Presenter {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.diary_btn:
                    finish();
                    break;
                case R.id.diary_share:
                    break;
                default:
                    break;
            }
        }
    }
}
