package com.moemoe.lalala.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.PhoneFukuEntity;
import com.moemoe.lalala.view.adapter.TabFragmentPagerV3Adapter;
import com.moemoe.lalala.view.fragment.BaseFragment;
import com.moemoe.lalala.view.fragment.RoleFragment;
import com.moemoe.lalala.view.widget.view.KiraRoleTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Hygge on 2018/5/9.
 */

public class DemoAcitivity extends BaseAppCompatActivity {

    @BindView(R.id.iv_left)
    ImageView mIvLeft;
    @BindView(R.id.iv_right)
    ImageView mIvRight;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.kira_bar)
    KiraRoleTabLayout mTabLayout;
    private TabFragmentPagerV3Adapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_demo;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        List<PhoneFukuEntity> titles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            PhoneFukuEntity entity = new PhoneFukuEntity();
            entity.setClothesName("迎春");
            titles.add(entity);
        }
        List<BaseFragment> fragmentList = new ArrayList<>();
        for (PhoneFukuEntity entity : titles) {
            fragmentList.add(RoleFragment.newInstance(entity.getClothesName()));
        }

        if (mAdapter == null) {
            mAdapter = new TabFragmentPagerV3Adapter(getSupportFragmentManager(), fragmentList, titles);
        } else {
            mAdapter.setFragments(getSupportFragmentManager(), fragmentList, titles);
        }

        mViewPager.setAdapter(mAdapter);
        mTabLayout.setViewPager(mViewPager);
//        if (mUserTags.size() > 0) {
//            mUserTags.get(0).setSelect(true);
//            mPreItem = 0;
//        }
//        tagAdapter = null;
//        tagAdapter = new TagAdapter();
//        tagAdapter.setShowClose(false);
//        tagAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                if (position != mPreItem) {
//                    mViewPager.setCurrentItem(position);
//                }
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position) {
//
//            }
//        });
//        mRvTags.setLayoutManager(new FlowLayoutManager());
//        mRvTags.setAdapter(tagAdapter);
//        tagAdapter.setList(mUserTags);

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
}
