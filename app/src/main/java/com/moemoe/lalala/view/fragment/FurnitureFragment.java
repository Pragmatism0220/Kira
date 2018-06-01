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
import java.util.Iterator;
import java.util.List;

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

    private List<FurnitureInfoEntity> lists;
    private List<AllFurnitureInfo> allFurnitureInfoList = new ArrayList<>();


    private static FurnitureFragment newInstance() {
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
        Log.i(TAG, "getFurnitureInfoSuccess: " + furnitureInfoEntity);
        lists = new ArrayList<>();
        lists.add(furnitureInfoEntity);

        /**
         * Map的Key(title)
         */
        ArrayList<String> mTitle = new ArrayList<>();
        mTitle.add("全部");
        mTitle.add("套装");
        Iterator<String> iterator = furnitureInfoEntity.getAllFurnitures().keySet().iterator();
        while (iterator.hasNext()) {
            mTitle.add(iterator.next());
        }

        /**
         * Map的value
         */
        Iterator<ArrayList<AllFurnitureInfo>> arrayListIterator = furnitureInfoEntity.getAllFurnitures().values().iterator();
        while (arrayListIterator.hasNext()) {
            allFurnitureInfoList.addAll(arrayListIterator.next());
        }

        Log.i(TAG, "Map的Key(title): " + mTitle);
        Log.i(TAG, "Map的value: "+allFurnitureInfoList);

        if (allFurnitureInfoList != null) {
            List<BaseFragment> fragmentList = new ArrayList<>();
            for (int i = 0; i < mTitle.size(); i++) {
                fragmentList.add(FurnitureInfoFragment.newInstance());
            }
            if (mAdapter == null) {
                mAdapter = new TabFragmentPagerAdapter(getChildFragmentManager(), fragmentList, mTitle);
            } else {
                mAdapter.setFragments(getChildFragmentManager(), fragmentList, mTitle);
            }
            mFurnitureViewPager.setAdapter(mAdapter);
            mTab.setViewPager(mFurnitureViewPager);
            mFurnitureViewPager.setCurrentItem(0);
        }

        EventBus.getDefault().post(furnitureInfoEntity);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
