package com.moemoe.lalala.view.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.SearchNormalEntity;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.TabFragmentPagerAdapter;
import com.moemoe.lalala.view.widget.view.KiraTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Sora on 2018/3/24.
 */

public class NoticeAllFragment extends BaseFragment {

    @BindView(R.id.tab_layout)
    KiraTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private TabFragmentPagerAdapter mAdapter;

    public static NoticeAllFragment newInstance() {
        return new NoticeAllFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_feed_follow_v5;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ViewUtils.setStatusBarLight(getActivity().getWindow(), $(R.id.top_view));
        List<SearchNormalEntity> titles = new ArrayList<>();
        SearchNormalEntity entity = new SearchNormalEntity();
        entity.setType("ALL");
        entity.setData("所有");
        titles.add(entity);
        entity = new SearchNormalEntity();
        entity.setData("帖子");
        entity.setType("ARTICLE");
        titles.add(entity);
        entity = new SearchNormalEntity();
        entity.setData("动态");
        entity.setType("DYNAMIC");
        titles.add(entity);
        entity = new SearchNormalEntity();
        entity.setData("书包");
        entity.setType("BAG");
        titles.add(entity);
        entity = new SearchNormalEntity();
        entity.setData("剧情");
        entity.setType("STORY");
        titles.add(entity);
        entity = new SearchNormalEntity();
        entity.setData("系统");
        entity.setType("SYSTEM");
        titles.add(entity);

        List<BaseFragment> fragmentList = new ArrayList<>();
        List<String> title = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            title.add(titles.get(i).getData());
            fragmentList.add(NoticeChildFragment.newInstance(titles.get(i).getType()));
        }

        if (mAdapter == null) {
            mAdapter = new TabFragmentPagerAdapter(getChildFragmentManager(), fragmentList, title);
        } else {
            mAdapter.setFragments(getChildFragmentManager(), fragmentList, title);
        }
        mAdapter = new TabFragmentPagerAdapter(getChildFragmentManager(), fragmentList, title);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setViewPager(mViewPager);

    }

    @Override
    public void release() {
        if (mAdapter != null) {
            mAdapter.release();
        }
    }
}
