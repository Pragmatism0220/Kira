package com.moemoe.lalala.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.AcClothingBinding;
import com.moemoe.lalala.di.components.DaggerClothComponent;
import com.moemoe.lalala.di.modules.ClothModule;
import com.moemoe.lalala.event.KiraTabLayoutEvent;
import com.moemoe.lalala.model.entity.ClothingEntity;
import com.moemoe.lalala.presenter.ClothingContrarct;
import com.moemoe.lalala.presenter.ClothingPresenter;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.view.adapter.TabFragmentPagerV3Adapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.fragment.BaseFragment;
import com.moemoe.lalala.view.fragment.ClothingFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * Created by Hygge on 2018/6/1.
 */

public class ClothingActivity extends BaseActivity implements ClothingContrarct.View {

    AcClothingBinding binding;


    @Inject
    ClothingPresenter mPresenter;
    private TabFragmentPagerV3Adapter mAdapter;
    private String roleId;
    private ArrayList<ClothingEntity> titles;

    public static void startActivity(Context context, String roleId) {
        Intent i = new Intent(context, ClothingActivity.class);
        i.putExtra("roleId", roleId);
        context.startActivity(i);
    }


    @Override
    protected void initComponent() {
        DaggerClothComponent.builder()
                .clothModule(new ClothModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.ac_clothing);
        binding.setPresenter(new Presenter());
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        roleId = getIntent().getStringExtra("roleId");
        mPresenter.loadHouseClothesAll(roleId);

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
    }

    @Override
    public void loadHouseClothSuccess(ArrayList<ClothingEntity> entities) {
        if (entities != null) {
            List<BaseFragment> fragmentList = new ArrayList<>();
            titles = entities;
            for (ClothingEntity entity : titles) {
                fragmentList.add(ClothingFragment.newInstance(entity));
            }
            if (mAdapter == null) {
                mAdapter = new TabFragmentPagerV3Adapter(getSupportFragmentManager(), fragmentList, titles);
            } else {
                mAdapter.setFragments(getSupportFragmentManager(), fragmentList, titles);
            }

            binding.viewPager.setAdapter(mAdapter);
            binding.kiraBar.setViewPager(binding.viewPager);

            ClothingEntity entity = titles.get(0);
            binding.tvName.setText(entity.getName());
            binding.tvAcquisitonName.setText(entity.getCondition());
            if (entity.isUse()) {
                binding.ivClothNameSelect.setVisibility(View.VISIBLE);
            } else {
                binding.ivClothNameSelect.setVisibility(View.GONE);
            }
            binding.tvClothContent.setText(entity.getDescribe());
        }
    }

    /**
     * 使用成功
     */
    @Override
    public void loadRoleColthesSelectSuccess(int position) {
        showToast("操作成功");
        if (titles == null) {
            return;
        }
        List<ClothingEntity> list = mAdapter.getList();
        for (int i = 0; i < list.size(); i++) {
            titles.get(i).setUse(false);
            mAdapter.getList().get(i).setUse(false);
            if (i == position) {
                titles.get(i).setUse(true);
                mAdapter.getList().get(position).setUse(true);
            }
        }
        binding.kiraBar.notifyDataSetChanged();
        binding.ivClothNameSelect.setVisibility(View.VISIBLE);
    }


    public class Presenter {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_cloth_back_btn:
                    finish();
                    break;
                case R.id.iv_cloth_select:
                    int viewPosition = binding.kiraBar.getViewPosition();
                    if (titles.get(viewPosition).isUserClothesHad()) {
                        mPresenter.loadRoleColthesSelect(viewPosition, roleId, titles.get(viewPosition).getId());
                    } else {
                        showToast("你还没拥有该皮肤哟~~~");
                    }
                    break;
                case R.id.iv_left:
                    int position = binding.kiraBar.getViewPosition();
                    if (position >= 1) {
                        binding.kiraBar.setOnTabClick(binding.kiraBar.getTabView(position - 1), position - 1);
                    }
                    break;
                case R.id.iv_right:
                    int position2 = binding.kiraBar.getViewPosition();
                    if (position2 <= titles.size() - 2) {
                        binding.kiraBar.setOnTabClick(binding.kiraBar.getTabView(position2 + 1), position2 + 1);
                    }
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.release();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void kiraTabLayoutEvent(KiraTabLayoutEvent event) {
        if (event != null) {
            int position = event.getPosition();
            ClothingEntity entity = titles.get(position);
            binding.tvName.setText(entity.getName());
            binding.tvAcquisitonName.setText(entity.getCondition());
            if (entity.isUse()) {
                binding.ivClothNameSelect.setVisibility(View.VISIBLE);
            } else {
                binding.ivClothNameSelect.setVisibility(View.GONE);
            }
            binding.tvClothContent.setText(entity.getDescribe());
        }
    }

}
