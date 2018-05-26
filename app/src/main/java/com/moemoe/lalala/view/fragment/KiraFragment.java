package com.moemoe.lalala.view.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.moemoe.lalala.R;

import butterknife.BindView;
import io.rong.imlib.model.Conversation;

/**
 * Created by Sora on 2018/3/26.
 */

public class KiraFragment extends BaseFragment {

    @BindView(R.id.fl_kira)
    FrameLayout mFl;

    public static KiraFragment newInstance(String packageName) {
        KiraFragment kiraFragment = new KiraFragment();
        Bundle bundle = new Bundle();
        bundle.putString("packageName", packageName);
        kiraFragment.setArguments(bundle);
        return kiraFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_kira;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        String packageName = getArguments().getString("packageName");
        Uri uri = Uri.parse("rong://" + packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
                .build();
        FragmentTransaction mFragmentTransaction = getChildFragmentManager().beginTransaction();
        KiraConversationListFragment kiraConversationListFragment = KiraConversationListFragment.newInstance(uri);
        mFragmentTransaction.add(R.id.fl_kira, kiraConversationListFragment);
        mFragmentTransaction.show(kiraConversationListFragment);
        mFragmentTransaction.commit();
    }
}
