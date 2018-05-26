package com.moemoe.lalala.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerBagVisitorComponent;
import com.moemoe.lalala.di.modules.BagVisitorModule;
import com.moemoe.lalala.model.entity.UserTopEntity;
import com.moemoe.lalala.presenter.BagVisitorContract;
import com.moemoe.lalala.presenter.BagVisitorPresenter;
import com.moemoe.lalala.utils.BagVisitorDecoration;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.activity.BagAllVisitorActivity;
import com.moemoe.lalala.view.activity.FeedV3Activity;
import com.moemoe.lalala.view.adapter.BagVisitorAdapter;
import com.moemoe.lalala.view.adapter.TabFragmentPagerAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.view.KiraTabLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Sora on 2018/2/28.
 */

public class NewMyBagV5Fragment extends BaseFragment implements BagVisitorContract.View {

    @BindView(R.id.tab_layout)
    KiraTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.rl_lately_visit)
    RelativeLayout mRlLatelyVisit;
    @BindView(R.id.rl_list)
    RecyclerView mListDosc;
    @BindView(R.id.iv_go_more)
    ImageView mIvGoMore;

    @Inject
    BagVisitorPresenter mPresenter;

    private TabFragmentPagerAdapter mAdapter;
    private BagVisitorAdapter adapter;
    private ArrayList<UserTopEntity> list = new ArrayList<>();
    private String id;

    public static NewMyBagV5Fragment newInstance(String id) {
        NewMyBagV5Fragment fragment = new NewMyBagV5Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static NewMyBagV5Fragment newInstance(String id, boolean isCommunity) {
        NewMyBagV5Fragment fragment = new NewMyBagV5Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", id);
        bundle.putBoolean("isCommunity", isCommunity);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_new_bag_v5;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        //埋点统计：动态-书包
        if (getActivity() instanceof FeedV3Activity) {
            clickEvent("书包-我的");
        }

        DaggerBagVisitorComponent.builder()
                .bagVisitorModule(new BagVisitorModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        id = getArguments().getString("uuid");
        List<String> titles = new ArrayList<>();
        titles.add("我的创建");
        titles.add("我的购买");
        titles.add("我的收藏");
        titles.add("我的下载");
        List<BaseFragment> fragmentList = new ArrayList<>();
        fragmentList.add(BagMyFragment.newInstance("my", PreferenceUtils.getUUid(), "my"));
        fragmentList.add(BagBuyFragment.newInstance());
        fragmentList.add(BagMyFragment.newInstance("collection", PreferenceUtils.getUUid(), "myCollection"));
        fragmentList.add(DownLoadListFragment.newInstance());
        mAdapter = new TabFragmentPagerAdapter(getChildFragmentManager(), fragmentList, titles);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setViewPager(mViewPager);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayout.HORIZONTAL);
        mListDosc.setLayoutManager(layoutManager);
        adapter = new BagVisitorAdapter();
        mListDosc.addItemDecoration(new BagVisitorDecoration());
        mListDosc.setAdapter(adapter);
        mPresenter.loadBagVisitor(id, 0);

        mIvGoMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BagAllVisitorActivity.class);
                startActivity(intent);
            }
        });
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (list.size() > 0) {
                    ViewUtils.toPersonal(getContext(), list.get(position).getUserId());
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    @Override
    public void release() {

        if (mAdapter != null) {
            mAdapter.release();
        }
        if (mPresenter != null) {
            mPresenter.release();
        }
    }

    @Override
    public void onFailure(int code, String msg) {
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
        stayEvent("书包-我的");
    }
    @Override
    public void onLoadBagVisitorSuccess(ArrayList<UserTopEntity> entities, boolean isPull) {
        if (entities.size() > 0) {
            mRlLatelyVisit.setVisibility(View.VISIBLE);
            mIvGoMore.setVisibility(View.VISIBLE);
            if (entities.size() >= 6) {
                mRlLatelyVisit.setVisibility(View.VISIBLE);
                int size = entities.size();
                int i = size > 6 ? 5 : size;
                for (int e = 0; e < i; e++) {
                    list.add(entities.get(e));
                }
                adapter.setList(list);
            } else {
                list.clear();
                list = entities;
                adapter.setList(list);
            }
        } else {
            mRlLatelyVisit.setVisibility(View.GONE);
        }
    }
}
