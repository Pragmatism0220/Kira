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
import com.moemoe.lalala.databinding.ActivityStorageBinding;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.Image;
import com.moemoe.lalala.view.adapter.TabPageAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.fragment.FurnitureFragment;
import com.moemoe.lalala.view.fragment.PropFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 储物柜界面
 */

public class StorageActivity extends BaseActivity implements PropFragment.CallBack {

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


    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_storage);
        binding.setPresenter(new Presenter());
        initViewPager();
    }

    private void initViewPager() {
        mPropFragment = new PropFragment();
        mFurnitureFragment = new FurnitureFragment();
        mPropFragment.setCallBack(this);
        mFragmentLists = new ArrayList<>();
        mFragmentLists.add(mPropFragment);
        mFragmentLists.add(mFurnitureFragment);
        mTabAdapter = new TabPageAdapter(getSupportFragmentManager(), mFragmentLists);
        binding.storageViewpager.setCurrentItem(0);
        binding.storageViewpager.setAdapter(mTabAdapter);
        binding.storageViewpager.setOnPageChangeListener(new myOnPageChangeListener());
        binding.radioGroup.setOnCheckedChangeListener(new myCheckChangeListener());
    }

    @Override
    public void getResult(String id, String name, String image, int toolCount) {
        Log.i("StorageActivity", "getResult: " + id + "/" + name + "/" + image + "/" + toolCount);
        binding.storageCommodityName.setText(name);
        Glide.with(this).load(ApiService.URL_QINIU + image).into(binding.storageImage);
        binding.storageCommodityNum.setText("X" + toolCount);
        this.name = name;
        this.id = id;
        this.toolCount = toolCount;


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
                    Toast.makeText(getApplicationContext(), "道具", Toast.LENGTH_SHORT).show();
                    binding.storageViewpager.setCurrentItem(0, false);
                    break;
                case R.id.choose_furniture_btn:
                    binding.topBg.setBackgroundResource(R.drawable.ic_role_top_bg);
                    Toast.makeText(getApplicationContext(), "家具", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "显示拥有", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "购买", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.storage_commodity_use_btn:
                    Toast.makeText(getApplicationContext(), "使用", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }


    }


}
