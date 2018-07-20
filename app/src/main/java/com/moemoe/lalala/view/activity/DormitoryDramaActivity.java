package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityDormitoryDramaBinding;
import com.moemoe.lalala.di.components.DaggerNewDormitoryComponent;
import com.moemoe.lalala.di.modules.NewDormitorModule;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.NewStoryInfoEventEntity;
import com.moemoe.lalala.model.entity.SearchListEntity;
import com.moemoe.lalala.model.entity.SearchNewListEntity;
import com.moemoe.lalala.presenter.NewDormitioryContract;
import com.moemoe.lalala.presenter.NewDormitoryPresenter;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * do(zhangyan)
 * 剧情主界面
 */

public class DormitoryDramaActivity extends BaseActivity implements NewDormitioryContract.View {

    private ActivityDormitoryDramaBinding binding;

    @Inject
    NewDormitoryPresenter mPresenter;
    private NewStoryInfoEventEntity mList;

    @Override
    protected void initComponent() {
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dormitory_drama);
        binding.setPresenter(new Presenter());
        DaggerNewDormitoryComponent.builder()
                .newDormitorModule(new NewDormitorModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        initGuide();
    }

    private void initGuide() {
        if (PreferenceUtils.isActivityFirstLaunch(this, "drama")) {
            Intent intent = new Intent(this, MengXinActivity.class);
            intent.putExtra("type", "drama");
            ArrayList<String> res = new ArrayList<>();
            res.add("plot1.jpg");
            res.add("plot2.jpg");
            res.add("plot3.jpg");
            res.add("plot4.jpg");
            res.add("plot5.jpg");
            res.add("plot6.jpg");
            res.add("plot7.jpg");
            intent.putExtra("gui", res);
            startActivity(intent);
        }
    }


    @Override
    protected void initViews(Bundle savedInstanceState) {
        mPresenter.getStoryInfo();
        SearchListEntity entity = new SearchListEntity();
        entity.setFunNames(new String[]{"user_branch_story"});
        mPresenter.searchDormitioryNews(entity);

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
    public void onFailure(int code, String msg) {
        ErrorCodeUtils.showErrorMsgByCode(this, code, msg);
        finish();
    }

    @Override
    public void getStoryInfoSuccess(NewStoryInfoEventEntity event) {
        mList = event;
        binding.principalLineSchedule.setText("收集度:" + event.getMasterCollectPercent() + "%");
        binding.branchSchedule.setText("N(" + event.getBranchN() + ")" +
                "R(" + event.getBranchR() + ")" +
                "SR(" + event.getBranchSR() + ")");
        if (event.getBgImage() != null) {
            Glide.with(this).load(ApiService.URL_QINIU + event.getBgImage())
                    .into(binding.bgImage);
        } else {
            binding.bgImage.setBackgroundResource(R.drawable.drama_dormitory_bg);
        }
    }

    @Override
    public void getDormitioryNewsSuccess(ArrayList<SearchNewListEntity> searchNewLists) {
        if (searchNewLists != null && searchNewLists.size() > 0) {
            Map<String, Integer> searchDormitioryMap = new HashMap<>();
            for (int i = 0; i < searchNewLists.size(); i++) {
                if (searchNewLists.get(i).getFunName().equals("user_branch_story")) {
                    if (!searchDormitioryMap.containsKey("user_branch_story")) {
                        searchDormitioryMap.put("user_branch_story", 1);
                    }
                }
                if (searchDormitioryMap.get("user_branch_story") != null) {
                    binding.dramaNews.setVisibility(View.VISIBLE);
                } else {
                    binding.dramaNews.setVisibility(View.GONE);
                }
            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        SearchListEntity entity = new SearchListEntity();
        entity.setFunNames(new String[]{"user_branch_story"});
        mPresenter.searchDormitioryNews(entity);
    }

    @Override
    public void updateDormitioryNewsSuccess() {

    }


    public class Presenter {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.drama_back_btn:
                    finish();
                    break;
                case R.id.principal_line_btn:
                    if (mList != null) {
                        Intent intent = new Intent(DormitoryDramaActivity.this, PrActivity.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.branch_btn:
                    if (mList != null) {
                        Intent i = new Intent(DormitoryDramaActivity.this, BranchActivity.class);
                        i.putExtra("BranchN", mList.getBranchN() + "");
                        i.putExtra("BranchR", mList.getBranchR() + "");
                        i.putExtra("BranchSR", mList.getBranchSR() + "");
                        startActivity(i);
                    }
                    break;
                default:
                    break;
            }
        }

    }
}
