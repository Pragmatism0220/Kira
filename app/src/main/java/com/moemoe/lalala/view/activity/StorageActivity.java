package com.moemoe.lalala.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityStorageBinding;
import com.moemoe.lalala.di.components.DaggerStorageComponents;
import com.moemoe.lalala.di.modules.StorageModule;
import com.moemoe.lalala.event.FurnitureEvent;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.AllFurnitureInfo;
import com.moemoe.lalala.presenter.StorageContract;
import com.moemoe.lalala.presenter.StoragePresenter;
import com.moemoe.lalala.view.adapter.TabPageAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.fragment.FurnitureFragment;
import com.moemoe.lalala.view.fragment.FurnitureInfoFragment;
import com.moemoe.lalala.view.fragment.PropFragment;
import com.moemoe.lalala.view.widget.tooltip.TooltipAnimation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 储物柜界面
 */

public class StorageActivity extends BaseActivity implements PropFragment.CallBack, PropFragment.firstCallBack, StorageContract.View {

    private ActivityStorageBinding binding;

    //道具
    private PropFragment mPropFragment;
    //家具
    private FurnitureFragment mFurnitureFragment;

    private List<Fragment> mFragmentLists;
    private TabPageAdapter mTabAdapter;

    private String name;
    private String describe;
    private int toolCount;
    private String id;
    private boolean isUserHadTool;


    @Inject
    StoragePresenter mPresenter;
    private AllFurnitureInfo furnitureInfo;


    @Override
    protected void initComponent() {
        DaggerStorageComponents.builder()
                .storageModule(new StorageModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_storage);
        binding.setPresenter(new Presenter());
        EventBus.getDefault().register(this);
        initViewPager();
    }

    private void initViewPager() {
        mPropFragment = new PropFragment();
        mFurnitureFragment = new FurnitureFragment();

        mPropFragment.setCallBack(this);
        mPropFragment.setFirstCallBack(this);

        mFragmentLists = new ArrayList<>();
        mFragmentLists.add(mPropFragment);
        mFragmentLists.add(mFurnitureFragment);
        mTabAdapter = new TabPageAdapter(getSupportFragmentManager(), mFragmentLists);
        binding.storageViewpager.setCurrentItem(0);
        binding.storageViewpager.setAdapter(mTabAdapter);
        binding.storageViewpager.setOnPageChangeListener(new myOnPageChangeListener());
        binding.radioGroup.setOnCheckedChangeListener(new myCheckChangeListener());
    }

    /**
     * 选中信息返回
     */
    @Override
    public void getResult(String id, String name, String image, int toolCount, String describe, boolean isUserHadTool) {
        this.isUserHadTool = isUserHadTool;
        this.name = name;
        this.id = id;
        this.toolCount = toolCount;

        binding.storageCommodityName.setText(name);
        Glide.with(this).load(ApiService.URL_QINIU + image).into(binding.storageImage);
        binding.storageCommodityNum.setText("X" + toolCount);
        binding.storageCommodityInfo.setText(describe);
    }

    @Override
    public void firstResult(String id, String name, String image, int toolCount, String describe) {
        Glide.with(this).load(ApiService.URL_QINIU + image).into(binding.storageImage);
        binding.storageCommodityName.setText(name);
        binding.storageCommodityInfo.setText(describe);
        binding.storageCommodityNum.setText(toolCount + "");

    }

    @Override
    public void onFailure(int code, String msg) {

    }

    /**
     * 单品家具使用
     */
    @Override
    public void furnitureUseSuccess(int position) {
        EventBus.getDefault().post(new FurnitureEvent(position, furnitureInfo.getType()));
    }

    /**
     * 套装家具使用
     */
    @Override
    public void suitUseSuccess(int position) {
        EventBus.getDefault().post(new FurnitureEvent(position,furnitureInfo.getType()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * RadioButton切换fragment
     */
    private class myCheckChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.choose_prop_btn:
                    binding.topBg.setBackgroundResource(R.drawable.bg_home_items_prop_background);
                    binding.storageViewpager.setCurrentItem(0, false);
                    break;
                case R.id.choose_furniture_btn:
                    binding.topBg.setBackgroundResource(R.drawable.ic_role_top_bg);
                    binding.storageViewpager.setCurrentItem(1, false);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * ViewPager事件
     */

    private class myOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    binding.radioGroup.check(R.id.choose_prop_btn);
                    break;
                case 1:
                    binding.radioGroup.check(R.id.choose_furniture_btn);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {
        binding.storageCheckBoxBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPropFragment.showNotHave();
                } else {
                    mPropFragment.showHave();
                }
            }
        });

    }

    @Override
    protected void initData() {

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public class Presenter {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.storage_back:
                    finish();
                    break;
                case R.id.storage_commodity_buy_btn:
                    if (!isUserHadTool) {
                        return;
                    }
                    break;
                case R.id.storage_commodity_use_btn:
                    if (furnitureInfo != null) {
                        if (furnitureInfo.equals("套装")) {
                            if (furnitureInfo.isUserSuitFurnitureHad()) {
                                if (!furnitureInfo.isSuitPutInHouse()) {
                                    mPresenter.suitUse(furnitureInfo.getSuitTypeId(), furnitureInfo.getPosition());
                                }
                            } else {
                                showToast("还未拥有该套装家具哦~~~~~");
                            }
                        } else {
                            if (furnitureInfo.isUserFurnitureHad()) {
                                if (!furnitureInfo.isPutInHouse()) {
                                    mPresenter.furnitureUse(furnitureInfo.getId(), furnitureInfo.getPosition());
                                }
                            } else {
                                showToast("还未拥有该家具哦~~~~~");
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void allFurnitureInfo(AllFurnitureInfo event) {
        if (event != null) {
            furnitureInfo = event;
            if (event.getType().equals("套装")) {
                binding.storageCommodityName.setText(event.getSuitTypeName());
                Glide.with(this).load(ApiService.URL_QINIU + event.getSuitTypeImage()).into(binding.storageImage);
                binding.storageCommodityInfo.setText(event.getSuitTypeDescribe());
            } else {
                binding.storageCommodityName.setText(event.getName());
                Glide.with(this).load(ApiService.URL_QINIU + event.getImage()).into(binding.storageImage);
                binding.storageCommodityInfo.setText(event.getDescribe());
            }
        }
    }

}
