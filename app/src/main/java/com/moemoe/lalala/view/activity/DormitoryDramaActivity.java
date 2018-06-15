package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityDormitoryDramaBinding;
import com.moemoe.lalala.di.components.DaggerNewDormitoryComponent;
import com.moemoe.lalala.di.modules.NewDormitorModule;
import com.moemoe.lalala.event.NewStoryInfoEvent;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.HouseLikeEntity;
import com.moemoe.lalala.model.entity.MapEntity;
import com.moemoe.lalala.presenter.DormitoryContract;
import com.moemoe.lalala.presenter.NewDormitioryContract;
import com.moemoe.lalala.presenter.NewDormitoryPresenter;
import com.moemoe.lalala.view.base.BaseActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * do(zhangyan)
 * 剧情主界面
 */

public class DormitoryDramaActivity extends BaseActivity implements NewDormitioryContract.View {

    private ActivityDormitoryDramaBinding binding;

    @Inject
    NewDormitoryPresenter mPresenter;

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dormitory_drama);
        binding.setPresenter(new Presenter());
        DaggerNewDormitoryComponent.builder()
                .newDormitorModule(new NewDormitorModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
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
        mPresenter.getStoryInfo();
    }

    @Override
    public void onFailure(int code, String msg) {

    }

    @Override
    public void getStoryInfoSuccess(NewStoryInfoEvent event) {
        Log.i("NewStoryInfo", "getStoryInfoSuccess: " + event);
        binding.principalLineSchedule.setText("收集度:" + event.getMasterCollectPercent() + "%");
        binding.branchSchedule.setText("N(" + event.getBranchN() + ")" +
                "R(" + event.getBranchR() + ")" +
                "SR(" + event.getBranchSR() + ")");
        Glide.with(this).load(ApiService.URL_QINIU + event.getBgImage())
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(binding.dramaImage);
    }


    public class Presenter {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.drama_back_btn:
                    finish();
                    break;
                case R.id.principal_line_btn:
//                    Intent intent = new Intent(DormitoryDramaActivity.this, PrincipalLineActivity.class);
                    Intent intent = new Intent(DormitoryDramaActivity.this, PrActivity.class);
                    startActivity(intent);
                    break;
                case R.id.branch_btn:
                    Intent i = new Intent(DormitoryDramaActivity.this, BranchActivity.class);
                    startActivity(i);
                    break;
//                case R.id.every_day_btn:
//                    Intent intent1 = new Intent(DormitoryDramaActivity.this, EveryDayActivity.class);
//                    startActivity(intent1);
//                    break;
                default:
                    break;
            }
        }

    }
}
