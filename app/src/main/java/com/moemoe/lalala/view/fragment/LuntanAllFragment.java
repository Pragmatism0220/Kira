package com.moemoe.lalala.view.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerLuntanComponent;
import com.moemoe.lalala.di.modules.LuntanModule;
import com.moemoe.lalala.model.entity.LuntanTabEntity;
import com.moemoe.lalala.presenter.LuntanContract;
import com.moemoe.lalala.presenter.LuntanPresenter;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.view.adapter.TabFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 *
 * Created by yi on 2017/9/4.
 */

public class LuntanAllFragment extends BaseFragment  implements LuntanContract.View {

    @BindView(R.id.tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.pager_person_data)
    ViewPager mPager;

    @Inject
    LuntanPresenter mPresenter;

    private TabFragmentPagerAdapter mAdapter;

    public static LuntanAllFragment newInstance(){
        return new LuntanAllFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_luntanall;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        DaggerLuntanComponent.builder()
                .luntanModule(new LuntanModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        mPresenter.loadTabList();
    }

    @Override
    public void onFailure(int code, String msg) {
        ErrorCodeUtils.showErrorMsgByCode(getContext(),code,msg);
    }

    public void release(){
        if(mPresenter != null) mPresenter.release();
        if(mAdapter != null) mAdapter.release();
        super.release();
    }

    @Override
    public void onLoadTabListSuccess(ArrayList<LuntanTabEntity> entities) {
        List<BaseFragment> fragmentList = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for(LuntanTabEntity entity : entities){
            fragmentList.add(LuntanFragment.newInstance(entity.getId(),entity.getName(),entity.isCanDoc()));
            titles.add(entity.getName());
        }
        mAdapter = new TabFragmentPagerAdapter(getChildFragmentManager(),fragmentList,titles);
        mPager.setAdapter(mAdapter);
        mTabLayout.setViewPager(mPager);
    }
}
