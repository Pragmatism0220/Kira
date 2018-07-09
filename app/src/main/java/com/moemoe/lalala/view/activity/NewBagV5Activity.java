package com.moemoe.lalala.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.TabFragmentPagerAdapter;
import com.moemoe.lalala.view.fragment.BaseFragment;
import com.moemoe.lalala.view.fragment.FeedBagFragment;
import com.moemoe.lalala.view.fragment.NewMyBagV5Fragment;
import com.moemoe.lalala.view.widget.view.KiraTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 书包V5主界面
 * Created by yi on 2018/1/10.
 */

public class NewBagV5Activity extends BaseAppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.indicator_person_data)
    KiraTabLayout mPageIndicator;
    @BindView(R.id.iv_msg)
    ImageView mIvMsg;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;
    @BindView(R.id.tv_msg_dot)
    TextView mTvMsgDot;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private TabFragmentPagerAdapter mAdapter;
    private FeedBagFragment feedBagFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_new_bag_v_5;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        clickEvent("书包");
        if (savedInstanceState != null) {
            String FRAGMENTS_TAG = "android:support:fragments";
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        String extra = getIntent().getStringExtra("uuid");
        if (TextUtils.isEmpty(extra)) {
            finish();
            return;
        }
        List<BaseFragment> fragmentList = new ArrayList<>();
        feedBagFragment = FeedBagFragment.newInstance();
        fragmentList.add(feedBagFragment);
        fragmentList.add(NewMyBagV5Fragment.newInstance(extra, true));
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.label_find));
        titles.add(getString(R.string.label_mine));


        mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, titles);
        mViewPager.setAdapter(mAdapter);
//        if (type != null) {
//            mViewPager.setCurrentItem(t);
//        }
        mPageIndicator.setViewPager(mViewPager);
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mTvMsgDot.setVisibility(View.GONE);
        mIvBack.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        mIvSearch.setVisibility(View.GONE);
        mIvMsg.setImageDrawable(getResources().getDrawable(R.drawable.btn_trends_search));
        mIvMsg.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //埋点统计：搜索
                clickEvent("搜索-书包");
                Intent i3 = new Intent(NewBagV5Activity.this, AllSearchActivity.class);
                i3.putExtra("type", "folder");
                startActivity(i3);
            }
        });
    }

    @Override
    protected void initListeners() {
        mPageIndicator.setmTabClick(new KiraTabLayout.OnTabViewClickListener() {
            @Override
            public void onTabClick(View tabView, int position) {
                if (mViewPager.getCurrentItem() == 0) {
                    feedBagFragment.setSmoothScrollToPosition();
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseTime();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime();
    }

    @Override
    protected void onDestroy() {
        stayEvent("书包");
        if (mAdapter != null) {
            mAdapter.release();
        }
        super.onDestroy();
    }
}
