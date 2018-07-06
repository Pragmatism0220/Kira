package com.moemoe.lalala.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;


import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityDiaryBinding;
import com.moemoe.lalala.di.components.DaggerDiaryComponent;
import com.moemoe.lalala.di.modules.DiaryModule;
import com.moemoe.lalala.model.entity.DiaryEntity;
import com.moemoe.lalala.presenter.DiaryContract;
import com.moemoe.lalala.presenter.DiaryPresenter;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.view.adapter.DiaryAdapter;
import com.moemoe.lalala.view.base.BaseActivity;


import javax.inject.Inject;

public class DiaryActivity extends BaseActivity implements DiaryContract.View {

    private ActivityDiaryBinding binding;
    private DiaryAdapter mAdapter;

    private Handler handler = new Handler();
    @Inject
    DiaryPresenter mPresenter;
    private String id;

    @Override
    protected void initComponent() {
        DaggerDiaryComponent.builder()
                .diaryModule(new DiaryModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_diary);
        binding.setPresenter(new Presenter());
        mAdapter = new DiaryAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setSmoothScrollbarEnabled(true);
//        linearLayoutManager.setAutoMeasureEnabled(true);
        binding.diaryRv.setLayoutManager(linearLayoutManager);
//        binding.diaryRv.setHasFixedSize(true);
//        binding.diaryRv.setNestedScrollingEnabled(false);
        binding.diaryRv.setAdapter(mAdapter);
        binding.diaryFavorability.setAlpha(0.8f);
        binding.diaryNumberOfDay.setAlpha(0.8f);
        binding.diaryRecall.setAlpha(0.8f);
        binding.haveCloth.setAlpha(0.8f);


//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                Runnable runnable = new Runnable() {
//                    @Override
//                    public void run() {
//                        binding.diaryDialog.setVisibility(View.GONE);
//                    }
//                };
//                handler.postDelayed(runnable, 3000);
//            }
//        };
//        Timer timer = new Timer();
//        timer.schedule(task, 3000);

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        id = getIntent().getStringExtra("roleId");
        if (TextUtils.isEmpty(id)) {
            finish();
        }
        mPresenter.getDiaryInfo(id);

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

    @Override
    public void onFailure(int code, String msg) {
        ErrorCodeUtils.showErrorMsgByCode(this, code, msg);
    }

    @Override
    public void getDiarySuccess(DiaryEntity entity) {
        if (entity != null) {
            binding.numberOfDayNum.setText(entity.getHadRoleDays() + "");
            binding.haveClothNum.setText(entity.getHadRoleClothesCount() + "/" + entity.getRoleClothesSum());
            binding.recallNum.setText(entity.getHadRoleStoryCount() + "/" + entity.getRoleStorySum());
//            binding.diaryName.setText(entity.getRoleName());
            binding.title1.setText(entity.getRoleName());
//            binding.diaryTopBg.setBackgroundResource(R.drawable.diary_bg);
            binding.view.setBackgroundResource(R.drawable.bg_diray_background);
            if (entity.getRoleLike() <= 20) {
                binding.diaryIcon.setBackgroundResource(R.drawable.ic_home_roles_emotion_1);
            } else if (entity.getRoleLike() <= 40) {
                binding.diaryIcon.setBackgroundResource(R.drawable.ic_home_roles_emotion_2);
            } else if (entity.getRoleLike() <= 80) {
                binding.diaryIcon.setBackgroundResource(R.drawable.ic_home_roles_emotion_3);
            } else if (entity.getRoleLike() <= 160) {
                binding.diaryIcon.setBackgroundResource(R.drawable.ic_home_roles_emotion_4);
            } else if (entity.getRoleLike() < 320) {
                binding.diaryIcon.setBackgroundResource(R.drawable.ic_home_roles_emotion_5);
            } else if (entity.getRoleLike() >= 320) {
                binding.diaryIcon.setBackgroundResource(R.drawable.ic_home_roles_emotion_6);
            }

            if (entity.getRoleId().equals("af3e01f8-0d88-477b-ad53-15481c8a56c6")) {
                binding.diaryImage.setBackgroundResource(R.drawable.diary_role_view_len);
                binding.diaryName.setBackgroundResource(R.drawable.diary_name_len);
//                binding.bottomBgBg.setBackgroundResource(R.drawable.bg_home_roles_diary_txtback_len);
            } else if (entity.getRoleId().equals("811337cf-6a84-4c4f-a078-8a5c88e13c39")) {
                binding.diaryImage.setBackgroundResource(R.drawable.diary_role_view_fuzi);
                binding.diaryName.setBackgroundResource(R.drawable.diary_name_fuzi);
//                binding.bottomBgBg.setBackgroundResource(R.drawable.bg_home_roles_diary_txtback_fuzi);
            } else if (entity.getRoleId().equals("fe141680-62e6-49ee-94d1-71e993d007d5")) {
                binding.diaryImage.setBackgroundResource(R.drawable.diary_role_view_rem);
                binding.diaryName.setBackgroundResource(R.drawable.diary_name_rem);
//                binding.bottomBgBg.setBackgroundResource(R.drawable.bg_home_roles_diary_txtback_rem);
            } else if (entity.getRoleId().equals("120ca372-0a7e-4811-8b5a-719c4eef8dc7")) {
                binding.diaryImage.setBackgroundResource(R.drawable.diary_role_view_ichigo);
                binding.diaryName.setBackgroundResource(R.drawable.diary_name_ichigo);
//                binding.bottomBgBg.setBackgroundResource(R.drawable.bg_home_roles_diary_txtback_ichigo);
            } else if (entity.getRoleId().equals("7f4ca01d-db86-443d-b637-a814029e874a")) {
                binding.diaryImage.setBackgroundResource(R.drawable.diary_role_view_sari);
                binding.diaryName.setBackgroundResource(R.drawable.diary_name_sari);
//                binding.bottomBgBg.setBackgroundResource(R.drawable.bg_home_roles_diary_txtback_sari);
            } else if (entity.getRoleId().equals("21a17f3c-55de-424a-8090-c21311d9a327")) {
                binding.diaryImage.setBackgroundResource(R.drawable.diary_role_view_mei);
                binding.diaryName.setBackgroundResource(R.drawable.diary_name_mei);
//                binding.bottomBgBg.setBackgroundResource(R.drawable.bg_home_roles_diary_txtback_mei);
            } else if (entity.getRoleId().equals("cebd3879-fbbb-49df-ad8c-b55a2b244850")) {
                binding.diaryImage.setBackgroundResource(R.drawable.diary_role_view_misaka);
                binding.diaryName.setBackgroundResource(R.drawable.diary_name_misaka);
//                binding.bottomBgBg.setBackgroundResource(R.drawable.bg_home_roles_diary_txtback_misaka);
            }

            if (entity.getRoleLike() >= 320) {
                binding.favorabilityNum.setText(entity.getRoleLike() + "");
            } else if (entity.getRoleLike() == 0) {
                binding.favorabilityNum.setText(entity.getRoleLike() + "");
            } else {
                binding.favorabilityNum.setText(entity.getRoleLike() + "/" + entity.getRoleLikeMax());
            }
            mAdapter.setList(entity.getDiaryDetails());
        }

    }


    public class Presenter {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.diary_btn:
                    finish();
                    break;
//                case R.id.diary_share:
//                    break;
                default:
                    break;
            }
        }
    }
}
