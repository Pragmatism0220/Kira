package com.moemoe.lalala.view.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityStorageBinding;
import com.moemoe.lalala.di.components.DaggerStorageComponents;
import com.moemoe.lalala.di.modules.StorageModule;
import com.moemoe.lalala.event.FurnitureEvent;
import com.moemoe.lalala.event.StorageDefaultDataEvent;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.AllFurnitureInfo;
import com.moemoe.lalala.model.entity.PropInfoEntity;
import com.moemoe.lalala.presenter.StorageContract;
import com.moemoe.lalala.presenter.StoragePresenter;
import com.moemoe.lalala.view.adapter.TabPageAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.fragment.FurnitureFragment;
import com.moemoe.lalala.view.fragment.PropFragment;

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
    public static boolean sIsRegist = false;

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

    private boolean isProp;


    @Inject
    StoragePresenter mPresenter;
    //家具套装
    private AllFurnitureInfo furnitureInfo;
    //道具
    private PropInfoEntity mPropInfoEntity;


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
        binding.radioGroup.check(R.id.choose_prop_btn);

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
        EventBus.getDefault().post(new FurnitureEvent(position, furnitureInfo.getType()));
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
                    EventBus.getDefault().post(new StorageDefaultDataEvent(false));
                    isProp = true;
                    break;
                case 1:
                    binding.radioGroup.check(R.id.choose_furniture_btn);
                    EventBus.getDefault().post(new StorageDefaultDataEvent(true));
                    isProp = false;
                    break;
                default:
                    break;
            }

            //有没有好的办法解决这

            View propView = binding.choosePropBtn;
            View furnitureView = binding.chooseFurnitureBtn;

            propView.layout(propView.getLeft(), isProp ? propView.getTop() + 6 : propView.getTop() - 6, propView.getRight(),
                    isProp ? propView.getBottom() + 6 : propView.getBottom() - 6);

            furnitureView.layout(furnitureView.getLeft(), isProp ? furnitureView.getTop() - 6 : furnitureView.getTop() + 6, furnitureView.getRight(),
                    isProp ? furnitureView.getBottom() - 6 : furnitureView.getBottom() + 6);

        }


        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
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
                    mFurnitureFragment.furnShowNotHave();
                } else {
                    mPropFragment.showHave();
                    mFurnitureFragment.furnShowHave();
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
                    //家具套装的购买
                    if (furnitureInfo != null) {
                        if (furnitureInfo.equals("套装")) {//套装
                            if (!furnitureInfo.isUserSuitFurnitureHad() && !furnitureInfo.isSuitPutInHouse()) {
                                //TODO 套装购买
                            } else {
                            }
                        } else {//单件
                            if (!furnitureInfo.isUserFurnitureHad() && !furnitureInfo.isPutInHouse()) {
                                //TODO 家具单件购买
                            } else {
                            }
                        }
                    }
                    //TODO 道具购买

                    break;
                case R.id.storage_commodity_use_btn:
                    //家具套装的使用
                    if (furnitureInfo != null) {
                        if (furnitureInfo.equals("套装")) {
                            if (furnitureInfo.isUserSuitFurnitureHad()) {//套装是否拥有
                                if (!furnitureInfo.isSuitPutInHouse()) {
                                    mPresenter.suitUse(furnitureInfo.getSuitTypeId(), furnitureInfo.getPosition());
                                } else {
                                }
                            } else {
                            }
                        } else {
                            //家具是否拥有
                            if (furnitureInfo.isUserFurnitureHad()) {
                                if (!furnitureInfo.isPutInHouse()) {
                                    mPresenter.furnitureUse(furnitureInfo.getId(), furnitureInfo.getPosition());
                                } else {
                                }
                            } else {
                            }
                        }
                    }
//                    //道具的使用
//                    if (!isUserHadTool) {
//                        showToast("未拥有");
//                    } else {
//                        showToast("使用");
//                        //TODO 道具使用
//                    }
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
                Glide.with(this).load(ApiService.URL_QINIU + event.getSuitTypeDetailIcon()).into(binding.storageImage);
                binding.storageCommodityInfo.setText(event.getSuitTypeDescribe());
            } else {
                binding.storageCommodityName.setText(event.getName());
                Glide.with(this).load(ApiService.URL_QINIU + event.getDetailIcon()).into(binding.storageImage);
                binding.storageCommodityInfo.setText(event.getDescribe());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void propInfo(PropInfoEntity propEvent) {
        if (propEvent != null) {
            mPropInfoEntity = propEvent;
        }
    }

}
