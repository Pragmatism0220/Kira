package com.moemoe.lalala.view.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.moemoe.lalala.R;
import com.moemoe.lalala.view.activity.FeedV3Activity;
import com.moemoe.lalala.view.adapter.TabFragmentPagerAdapter;
import com.moemoe.lalala.view.widget.view.KiraTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *  Feed流动态页
 * Created by yi on 2018/1/11.
 */

public class FeedDynamicV3Fragment extends BaseFragment{

    @BindView(R.id.tab_layout)
    KiraTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private TabFragmentPagerAdapter mAdapter;
    private NewFollowMainFragment followMainFragment;
    private FeedFriendFragment friendFragment;

    public static FeedDynamicV3Fragment newInstance(){
        return new FeedDynamicV3Fragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_feed_dynamic_v3;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        //埋点统计：动态-动态
        if (getActivity() instanceof FeedV3Activity){
            clickEvent("动态-动态");
        }

        List<String> titles = new ArrayList<>();
        titles.add("全部");
        titles.add("好友");
        List<BaseFragment> fragmentList = new ArrayList<>();
        followMainFragment = NewFollowMainFragment.newInstance("ground");
        fragmentList.add(followMainFragment);
        friendFragment = FeedFriendFragment.newInstance();
        fragmentList.add(friendFragment);
        mAdapter = new TabFragmentPagerAdapter(getChildFragmentManager(),fragmentList,titles);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setViewPager(mViewPager);
    }

    /**
     * 全部 or 好友-点赞事件
     * @param id
     * @param isLike
     * @param position
     */
    public void likeDynamic(String id,boolean isLike,int position){
        if(mViewPager.getCurrentItem() == 0){
            followMainFragment.likeDynamic(id, isLike, position);
        }else if (mViewPager.getCurrentItem()==1){
            friendFragment.likeDynamic(id, isLike, position);
        }
    }
    @Override
    public void release() {
        if(mAdapter != null){
            mAdapter.release();
        }
    }
}
