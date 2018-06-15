package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityPrBinding;
import com.moemoe.lalala.di.components.DaggerPrincipalComponent;
import com.moemoe.lalala.di.modules.PrincipalModule;
import com.moemoe.lalala.event.NewStoryGroupInfo;
import com.moemoe.lalala.event.OnItemListener;
import com.moemoe.lalala.presenter.PrincipalContract;
import com.moemoe.lalala.presenter.PrincipalPresenter;
import com.moemoe.lalala.view.adapter.PrAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.base.MainStoryBean;
import com.moemoe.lalala.view.widget.view.SpacesItemDecoration;

import org.greenrobot.greendao.annotation.Id;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PrActivity extends BaseActivity implements PrincipalContract.View {

    private ActivityPrBinding binding;
    private PrAdapter mAdapter;

    @Inject
    PrincipalPresenter mPresenter;


    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pr);
        binding.setPresenter(new Presenter());
        DaggerPrincipalComponent.builder()
                .principalModule(new PrincipalModule(this))
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
        binding.prLineBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        mPresenter.getPrincipalGroupInfo();
    }

    @Override
    public void onFailure(int code, String msg) {

    }

    @Override
    public void getPrincipalGroupInfoSuccess(ArrayList<NewStoryGroupInfo> newStoryGroupInfos) {
        initAdapter(newStoryGroupInfos);
    }

    private void initAdapter(final List<NewStoryGroupInfo> list) {
        mAdapter = new PrAdapter(this, list);
        binding.prLineRecycle.setLayoutManager(new GridLayoutManager(this, 2));
        int rightSpace = 22;
        int buttonSpace = 24;
        int top = 32;
        binding.prLineRecycle.addItemDecoration(new SpacesItemDecoration(buttonSpace, rightSpace, top));
        binding.prLineRecycle.setAdapter(mAdapter);
        mAdapter.addOnItemClickListener(new OnItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                String id = list.get(position).getId();
                String groupName = list.get(position).getGroupName();
                if (id != null && groupName != null) {
                    Intent intent = new Intent(PrActivity.this, PrincipalListActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("groupName", groupName);
                    startActivity(intent);
                }

            }
        });
    }


    public class Presenter {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pr_line_back_btn:
//                  finish();
                    break;
                default:
                    break;
            }
        }
    }
}
