package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.TabFragmentPagerAdapter;
import com.moemoe.lalala.view.fragment.BaseFragment;
import com.moemoe.lalala.view.fragment.PhoneMenuListFragment;
import com.moemoe.lalala.view.widget.view.KiraTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Sora on 2018/3/22.
 */

public class PhoneMenuV3Activity extends BaseAppCompatActivity {


    @BindView(R.id.tab_layout)
    KiraTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tv_toolbar_title)
    TextView mTvTitle;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_menu_list)
    ImageView mIvMenuList;

    private TabFragmentPagerAdapter mAdapter;
    private PhoneMenuListFragment recommend;
    private PhoneMenuListFragment fans;
    private PhoneMenuListFragment followFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_phone_menu_v3;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));

        List<String> titles = new ArrayList<>();
        titles.add("推荐");
        titles.add("粉丝");
        titles.add("关注");
        ArrayList<BaseFragment> fragmentList = new ArrayList<>();
        recommend = PhoneMenuListFragment.newInstance("recommend");
        fans = PhoneMenuListFragment.newInstance("fans");
        fragmentList.add(recommend);
        fragmentList.add(fans);
        followFragment = PhoneMenuListFragment.newInstance("follow");
        fragmentList.add(followFragment);
        mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, titles);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setViewPager(mViewPager);
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mTvTitle.setText("通讯录");
        mTvTitle.setTextColor(getResources().getColor(R.color.main_cyan));
        mIvBack.setVisibility(View.VISIBLE);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mIvMenuList.setVisibility(View.VISIBLE);
        mIvMenuList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickEvent("搜索-通讯录");
                Intent i3 = new Intent(PhoneMenuV3Activity.this, AllSearchActivity.class);
                i3.putExtra("type", "user");
                startActivity(i3);
            }
        });
        mIvMenuList.setImageResource(R.drawable.btn_trends_search);
        mTabLayout.setmTabClick(new KiraTabLayout.OnTabViewClickListener() {
            @Override
            public void onTabClick(View tabView, int position) {
                if (mViewPager.getCurrentItem() == 0) {
                    recommend.onLoadng();
                } else if (mViewPager.getCurrentItem() == 1) {
                    fans.onLoadng();
                } else if (mViewPager.getCurrentItem() == 2) {
                    followFragment.onLoadng();
                }
            }
        });
    }

    public void loadFollow(String userId, boolean follow, int position) {
        if (mViewPager.getCurrentItem() == 0) {
            recommend.loadFollow(userId, follow, position);
        } else if (mViewPager.getCurrentItem() == 1) {
            fans.loadFollow(userId, follow, position);
        } else if (mViewPager.getCurrentItem() == 2) {
            followFragment.loadFollow(userId, follow, position);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) mAdapter.release();
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }
}
