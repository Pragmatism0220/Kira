package com.moemoe.lalala.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerSelectTagComponent;
import com.moemoe.lalala.di.modules.SelectTagModule;
import com.moemoe.lalala.model.entity.OfficialTag;
import com.moemoe.lalala.presenter.SelectTagContract;
import com.moemoe.lalala.presenter.SelectTagPresenter;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.view.activity.LoginActivity;
import com.moemoe.lalala.view.adapter.TabFragmentPagerAdapter;
import com.moemoe.lalala.view.widget.view.KiraTabLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static com.moemoe.lalala.utils.StartActivityConstant.REQ_GO_COMMUNITY;
import static com.moemoe.lalala.utils.StartActivityConstant.REQ_LOGIN;

/**
 * Created by Sora on 2018/2/28.
 */

public class NewCommunityFragment extends BaseFragment implements SelectTagContract.View {

    @BindView(R.id.tl_8)
    KiraTabLayout mTabLayout_8;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.rl_list)
    RelativeLayout mlist;
    @Inject
    SelectTagPresenter mPresenter;
    private ArrayList<OfficialTag> mUserTags;
    View mLogin;
    private TabFragmentPagerAdapter mAdapter;

    public static NewCommunityFragment newInstance() {
        return new NewCommunityFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_new_community;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        clickEvent("社团-社团");
        DaggerSelectTagComponent.builder()
                .selectTagModule(new SelectTagModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);


        if (PreferenceUtils.isLogin()) {
            mPresenter.loadTagAllList();
        } else {
            ViewStub stub = rootView.findViewById(R.id.stub_login);
            View view = stub.inflate();
            mLogin = view.findViewById(R.id.iv_to_login);
            mLogin.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    Intent i = new Intent(getContext(), LoginActivity.class);
                    getActivity().startActivityForResult(i, REQ_LOGIN);
                }
            });
        }

    }

    @Override
    public void onFailure(int code, String msg) {
        ErrorCodeUtils.showErrorMsgByCode(getContext(), code, msg);
    }

    @Override
    public void onLoadOfficialTags(ArrayList<OfficialTag> tags) {

    }

    @Override
    public void onLoadOfficialV2Tags(ArrayList<OfficialTag> tags) {
        mlist.setVisibility(View.VISIBLE);
        if (mLogin != null) {
            mLogin.setVisibility(View.GONE);
        }
        if (tags.size() > 0) {
            mUserTags = tags;
            ArrayList<String> mTitles = new ArrayList<>();
            List<BaseFragment> fragmentList = new ArrayList<>();
            for (int i = 0; i < mUserTags.size(); i++) {
                mTitles.add(mUserTags.get(i).getText());
                fragmentList.add(CommunityTagsFragment.newInstance(mUserTags.get(i).getTagSec().get(0).getTagThi()));
            }
            if (mAdapter == null) {
                mAdapter = new TabFragmentPagerAdapter(getChildFragmentManager(), fragmentList, mTitles);
            } else {
                mAdapter.setFragments(getChildFragmentManager(), fragmentList, mTitles);
            }
            mViewPager.setAdapter(mAdapter);
            mTabLayout_8.setViewPager(mViewPager);
            mViewPager.setCurrentItem(0);
        }
    }

    @Override
    public void onSaveUserTagsSuccess() {

    }

    public void release() {
        if (mPresenter != null) mPresenter.release();
        if (mAdapter != null) mAdapter.release();
        super.release();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_GO_COMMUNITY && resultCode == RESULT_OK) {
            mPresenter.loadTagAllList();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        startTime();
    }

    @Override
    protected void pauseTime() {
        super.pauseTime();
        pauseTime();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stayEvent("社团-社团");
    }
}