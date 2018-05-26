package com.moemoe.lalala.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.event.GroupMsgChangeEvent;
import com.moemoe.lalala.event.SystemMessageEvent;
import com.moemoe.lalala.utils.AndroidBug5497Workaround;
import com.moemoe.lalala.utils.DialogUtils;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.TabFragmentPagerV2Adapter;
import com.moemoe.lalala.view.fragment.KiraConversationListFragment;
import com.moemoe.lalala.view.fragment.NoticeAllFragment;
import com.moemoe.lalala.view.widget.netamenu.BottomMenuFragment;
import com.moemoe.lalala.view.widget.netamenu.MenuItem;
import com.moemoe.lalala.view.widget.view.KiraTabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.model.Conversation;

/**
 * Created by Sora on 2018/3/24.
 */

public class NoticeActivity extends BaseAppCompatActivity implements IUnReadMessageObserver {

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

    private TabFragmentPagerV2Adapter mAdapter;
    private BottomMenuFragment bottomFragment;

    public static void startActivity(Context context, int type) {
        Intent i = new Intent(context, NoticeActivity.class);
        i.putExtra("type", type);
        context.startActivity(i);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ac_new_dynamic;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        AndroidBug5497Workaround.assistActivity(this);
        final Conversation.ConversationType[] conversationTypes = {
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP
        };
        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);
        EventBus.getDefault().register(this);
        int type = getIntent().getIntExtra("type", 0);
        List<String> titles = new ArrayList<>();
        titles.add("通知");
        titles.add("聊天");

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(NoticeAllFragment.newInstance());

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
                .build();
        fragmentList.add(KiraConversationListFragment.newInstance(uri));
        mAdapter = new TabFragmentPagerV2Adapter(getSupportFragmentManager(), fragmentList, titles);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setViewPager(mViewPager);
        mViewPager.setCurrentItem(type);
        initPopupMenus();
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mIvMsg.setImageResource(R.drawable.btn_menu_normal);
        mIvSearch.setImageResource(R.drawable.btn_toolbar_add_blue);
        mIvMsg.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Intent intent = new Intent(NoticeActivity.this, MsgSetUpActivity.class);
                startActivity(intent);
            }
        });
        int num = PreferenceUtils.hasMsg(this);
        if (num > 0) {
            if (num > 99) num = 99;
            mTabLayout.onShowMsg(0, num);
        } else {
            mTabLayout.hideMsg(0);
        }

        mIvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomFragment != null)
                    bottomFragment.show(getSupportFragmentManager(), "kira_msg_list");
            }
        });
    }

    private void initPopupMenus() {
        bottomFragment = new BottomMenuFragment();
        ArrayList<MenuItem> items = new ArrayList<>();
        MenuItem item = new MenuItem(4, "选择好友");
        items.add(item);
        item = new MenuItem(3, "我的群聊");
        items.add(item);
        item = new MenuItem(1, "加入群聊");
        items.add(item);
        item = new MenuItem(2, "vip创建群聊(需审核)");
        items.add(item);

        bottomFragment.setMenuItems(items);
        bottomFragment.setShowTop(false);
        bottomFragment.setMenuType(BottomMenuFragment.TYPE_VERTICAL);
        bottomFragment.setmClickListener(new BottomMenuFragment.MenuItemClickListener() {
            @Override
            public void OnMenuItemClick(int itemId) {
                if (DialogUtils.checkLoginAndShowDlg(NoticeActivity.this)) {
                    if (itemId == 1) {
                        ChatActivity.startActivity(NoticeActivity.this, "", "", "Group");
                    }
                    if (itemId == 2) {
                        ChatActivity.startActivity(NoticeActivity.this, "", "", "EditGroup");
                    }
                    if (itemId == 3) {
                        ChatActivity.startActivity(NoticeActivity.this, "", "", "GroupListV2");
                    }
                    if (itemId == 4) {
                        FriendsActivity.startActivity(NoticeActivity.this);
                    }
                }
            }
        });
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void systemMsgEvent(SystemMessageEvent event) {
        int num = PreferenceUtils.hasMsg(this);
        if (num > 0) {
            if (num > 99) num = 99;
            mTabLayout.onShowMsg(0, num);
        } else {
            mTabLayout.hideMsg(0);
        }
    }

    @Override
    public void onCountChanged(int i) {
        PreferenceUtils.setRCDotNum(this, i);
        int dotNum = PreferenceUtils.getGroupDotNum(this) + i + PreferenceUtils.getJuQIngDotNum(this);
        if (dotNum > 0) {
            if (dotNum > 999) dotNum = 999;
            mTabLayout.onShowMsg(1, dotNum);
        } else {
            mTabLayout.hideMsg(1);
        }
    }

    @Override
    protected void onDestroy() {
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(this);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void groupMsgEvent(GroupMsgChangeEvent event) {
        mAdapter.notifyDataSetChanged();
    }
}
