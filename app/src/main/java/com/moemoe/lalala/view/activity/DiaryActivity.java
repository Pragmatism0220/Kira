package com.moemoe.lalala.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.moemoe.lalala.R;
import com.moemoe.lalala.databinding.ActivityDiaryBinding;
import com.moemoe.lalala.event.ScrollMessage;
import com.moemoe.lalala.view.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

public class DiaryActivity extends BaseActivity {

    private ActivityDiaryBinding binding;
    private int topHeight;
    private final float maxScroll = 500f;
    private float oldAlpha;
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
//
//    private void initDialog() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(2000);
//                    Handler handler = new Handler();
//                    Runnable runnable = new Runnable() {
//                        @Override
//                        public void run() {
//                            binding.diaryDialog.setVisibility(View.GONE);
//                        }
//                    };
//                    handler.postDelayed(runnable, 3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//    }
}
