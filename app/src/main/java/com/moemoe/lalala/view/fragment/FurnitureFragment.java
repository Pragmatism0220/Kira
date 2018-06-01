package com.moemoe.lalala.view.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerFurnitureComponent;
import com.moemoe.lalala.di.modules.FurnitureModule;
import com.moemoe.lalala.model.entity.AllFurnitureInfo;
import com.moemoe.lalala.model.entity.FurnitureInfoEntity;
import com.moemoe.lalala.presenter.FurnitureContract;
import com.moemoe.lalala.presenter.FurniturePresenter;
import com.moemoe.lalala.view.adapter.FurnitureAdapter;
import com.moemoe.lalala.view.adapter.TabFragmentPagerAdapter;
import com.moemoe.lalala.view.widget.view.KiraTabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by zhangyan on 2018/5/21.
 * 储物箱中家具fragment
 */

public class FurnitureFragment extends BaseFragment implements FurnitureContract.View {

    public static final String TAG = "FurnitureFragment";

    @BindView(R.id.furniture_viewpager)
    ViewPager mFurnitureViewPager;
    @BindView(R.id.furniture_tab)
    KiraTabLayout mTab;

    @Inject
    FurniturePresenter mPresenter;

    private TabFragmentPagerAdapter mAdapter;

    public static FurnitureFragment newInstance() {
        return new FurnitureFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.furniture_fragment;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        DaggerFurnitureComponent.builder()
                .furnitureModule(new FurnitureModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);

        mPresenter.getFurnitureInfo();


    }


    @Override
    public void onFailure(int code, String msg) {

    }

    @Override
    public void getFurnitureInfoSuccess(FurnitureInfoEntity furnitureInfoEntity) {
        Map<String, ArrayList<AllFurnitureInfo>> map = new HashMap<>();

        Map<String, ArrayList<AllFurnitureInfo>> allFurnitures = furnitureInfoEntity.getAllFurnitures();
        ArrayList<AllFurnitureInfo> allList = new ArrayList<>();
        for (String key : allFurnitures.keySet()) {
            ArrayList<AllFurnitureInfo> infos = allFurnitures.get(key);
            for (AllFurnitureInfo allFurnitureInfo : infos) {
                allFurnitureInfo.setType("单品");
            }
            allList.addAll(infos);
        }
        map.put("全部", allList);
        ArrayList<AllFurnitureInfo> suitFurnitures = furnitureInfoEntity.getSuitFurnitures();
        for (AllFurnitureInfo allFurnitureInfo : suitFurnitures) {
            allFurnitureInfo.setType("套装");
        }
        map.put("套装", suitFurnitures);

        ArrayList<String> mTitle = new ArrayList<>();
        mTitle.add("全部");
        mTitle.add("套装");
        for (String key : allFurnitures.keySet()) {
            mTitle.add(key);
            map.put(key, allFurnitures.get(key));
        }
        if (map != null) {
            List<BaseFragment> fragmentList = new ArrayList<>();
            for (int i = 0; i < mTitle.size(); i++) {
                fragmentList.add(FurnitureInfoFragment.newInstance(map.get(mTitle.get(i))));
            }
            if (mAdapter == null) {
                mAdapter = new TabFragmentPagerAdapter(getChildFragmentManager(), fragmentList, mTitle);
            } else {
                mAdapter.setFragments(getChildFragmentManager(), fragmentList, mTitle);
            }
            mFurnitureViewPager.setAdapter(mAdapter);
            mTab.setViewPager(mFurnitureViewPager);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
