package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.utils.AndroidBug5497Workaround;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.TabFragmentPagerAdapter;
import com.moemoe.lalala.view.fragment.BaseFragment;
import com.moemoe.lalala.view.fragment.FeedFriendFragment;
import com.moemoe.lalala.view.fragment.NewFollowMainFragment;
import com.moemoe.lalala.view.widget.view.KiraTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.model.Conversation;

/**
 * Created by Sora on 2018/3/2.
 */

public class NewDynamicActivity extends BaseAppCompatActivity implements IUnReadMessageObserver {

    @BindView(R.id.tab_layout)
    KiraTabLayout mTabLayout;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.iv_msg)
    ImageView mIvMsg;
    @BindView(R.id.tv_msg_dot)
    TextView mTvMsgDot;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;

    private TabFragmentPagerAdapter mAdapter;
    private NewFollowMainFragment followMainFragment;
    private FeedFriendFragment friendFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_new_dynamic;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        clickEvent("动态");
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        AndroidBug5497Workaround.assistActivity(this);
        final Conversation.ConversationType[] conversationTypes = {
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP
        };
        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);

        List<String> titles = new ArrayList<>();
        titles.add("发现");
        titles.add("好友");
        List<BaseFragment> fragmentList = new ArrayList<>();
        followMainFragment = NewFollowMainFragment.newInstance("ground");
        fragmentList.add(followMainFragment);
        friendFragment = FeedFriendFragment.newInstance();
        fragmentList.add(friendFragment);
        mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, titles);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setViewPager(mViewPager);

        mViewPager.setCurrentItem(1);
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mIvMsg.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                clickEvent("动态-通知");
//                FeedNoticeActivity.startActivity(NewDynamicActivity.this);
                NoticeActivity.startActivity(NewDynamicActivity.this, 0);
            }
        });
        int num = PreferenceUtils.hasMsg(this);
        if (num > 0) {
            mTvMsgDot.setVisibility(View.VISIBLE);
            if (num > 99) num = 99;
            mTvMsgDot.setText(String.valueOf(num));
        } else {
            mTvMsgDot.setVisibility(View.GONE);
        }

        mIvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //埋点统计：搜索
                clickEvent("搜索-动态");
                Intent i3 = new Intent(NewDynamicActivity.this, AllSearchActivity.class);
                i3.putExtra("type", "dynamic");
                startActivity(i3);
            }
        });
    }

    @Override
    protected void initListeners() {
        mTabLayout.setmTabClick(new KiraTabLayout.OnTabViewClickListener() {
            @Override
            public void onTabClick(View tabView, int position) {
                if (mViewPager.getCurrentItem() == 0) {
                    followMainFragment.setSmoothScrollToPosition();
                } else if (mViewPager.getCurrentItem() == 1) {
                    friendFragment.setSmoothScrollToPosition();
                }
            }
        });
    }

    /**
     * 全部 or 好友-点赞事件
     *
     * @param id
     * @param isLike
     * @param position
     */
    public void likeDynamic(String id, boolean isLike, int position) {
        if (mViewPager.getCurrentItem() == 0) {
            followMainFragment.likeDynamic(id, isLike, position);
        } else if (mViewPager.getCurrentItem() == 1) {
            friendFragment.likeDynamic(id, isLike, position);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.release();
        }
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onCountChanged(int i) {
        PreferenceUtils.setRCDotNum(this, i);
        int dotNum = PreferenceUtils.getGroupDotNum(this) + i + PreferenceUtils.getJuQIngDotNum(this);
        if (mTvMsgDot != null) {
            if (dotNum > 0) {
                mTvMsgDot.setVisibility(View.VISIBLE);
                if (dotNum > 99) dotNum = 99;
                mTvMsgDot.setText(String.valueOf(dotNum));
            } else {
                mTvMsgDot.setVisibility(View.GONE);
            }
        }
    }
}

