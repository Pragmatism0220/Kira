package com.moemoe.lalala.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.utils.AndroidBug5497Workaround;
import com.moemoe.lalala.utils.DialogUtils;
import com.moemoe.lalala.utils.NetworkUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.fragment.BaseFragment;
import com.moemoe.lalala.view.fragment.JuQingChatV2Fragment;
import com.moemoe.lalala.view.fragment.KiraConversationFragment;
import com.moemoe.lalala.view.fragment.KiraConversationListFragment;
import com.moemoe.lalala.view.fragment.PhoneEditGroupV2Fragment;
import com.moemoe.lalala.view.fragment.PhoneGroupDetailV2Fragment;
import com.moemoe.lalala.view.fragment.PhoneGroupListV2Fragment;
import com.moemoe.lalala.view.fragment.PhoneMsgListV2Fragment;
import com.moemoe.lalala.view.fragment.PhoneTicketV2Fragment;

import org.greenrobot.eventbus.EventBus;

import java.util.Stack;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

import static com.moemoe.lalala.utils.StartActivityConstant.REQ_GROUP_DETAIL;

/**
 * Created by Sora on 2018/3/26.
 */

public class ChatActivity extends BaseAppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_toolbar_title)
    TextView mTvTitle;
    @BindView(R.id.iv_menu_list)
    ImageView mIvMenu;
    @BindView(R.id.phone_container)
    View mFragmentRoot;

    private Stack<Fragment> fragmentStack;
    private String type;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_chat;
    }

    public static void startActivity(Context context, String role, String id, String type) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("role", role);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String targetId, String title, String name, String packageName, String type) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("targetId", targetId);
        intent.putExtra("targetTitle", title);
        intent.putExtra("targetName", name);
        intent.putExtra("packageName", packageName);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        AndroidBug5497Workaround.assistActivity(this);
        fragmentStack = new Stack<>();

        type = getIntent().getStringExtra("type");
        String role = getIntent().getStringExtra("role");
        String id = getIntent().getStringExtra("id");
        if (type != null) {
            if (type.equals("JuQing")) {
                BaseFragment juQingChatFragment = JuQingChatV2Fragment.newInstance(role, id);

                toFragment(juQingChatFragment);
            } else if (type.equals("MsgV2")) {
                toFragment(PhoneMsgListV2Fragment.newInstance());
            } else if (type.equals("Group")) {
                toFragment(PhoneGroupListV2Fragment.newInstance(false, "加入群聊"));
            } else if (type.equals("EditGroup")) {
                toFragment(PhoneEditGroupV2Fragment.newInstance("create"));
            } else if (type.equals("GroupListV2")) {
                toFragment(PhoneGroupListV2Fragment.newInstance(true, "我的群聊"));
            } else if (type.equals("KiraConversation")) {
                String targetId = getIntent().getStringExtra("targetId");
                String targetTitle = getIntent().getStringExtra("targetTitle");
                String targetName = getIntent().getStringExtra("targetName");
                String packageName = getIntent().getStringExtra("packageName");
                Uri uri = Uri.parse("rong://" + packageName).buildUpon()
                        .appendPath("conversation").appendPath(targetName)
                        .appendQueryParameter("targetId", targetId).build();
                toFragment(KiraConversationFragment.newInstance(targetId, targetTitle, targetName, uri));
            }
        }
        toUri();
    }


    private void toUri() {
        Uri uri = getIntent().getData();
        if (uri != null && uri.getScheme().equals("rong")) {
            if (NetworkUtils.checkNetworkAndShowError(this) && DialogUtils.checkLoginAndShowDlg(ChatActivity.this)) {
                Conversation.ConversationType type = Conversation.ConversationType.valueOf(uri
                        .getLastPathSegment().toUpperCase());
                String path = uri.getPath();
                String targetId = uri.getQueryParameter("targetId");
                if (targetId.contains("juqing")) {
                    String[] juQing = targetId.split(":");
                    toFragment(JuQingChatV2Fragment.newInstance(juQing[2], juQing[1]));
                } else {
                    if (path.contains("conversation")) {
                        if ("conversation/detail".equals(path)) {
                            toFragment(PhoneGroupDetailV2Fragment.newInstance(targetId));
                        } else {
                            Uri uri1 = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                                    .appendPath("conversation").appendPath(type.getName())
                                    .appendQueryParameter("targetId", targetId).build();
                            toFragment(KiraConversationFragment.newInstance(targetId, uri.getQueryParameter("title"), type.getName(), uri1));
                        }
                    } else if (path.contains("conversationlist")) {
                        Uri uri1 = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                                .appendPath("conversationlist")
                                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
                                .build();
                        toFragment(KiraConversationListFragment.newInstance(uri1));
                    }
                }
            }
        }
    }


    public void toFragment(Fragment fragment) {
        mFragmentRoot.setVisibility(View.VISIBLE);

        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (!fragmentStack.empty()) {
            Fragment fragmentP = fragmentStack.pop();
            mFragmentTransaction.hide(fragmentP).add(R.id.phone_container, fragment, fragment.getClass().getSimpleName() + "_fragment");
            fragmentStack.push(fragmentP);
        } else {
            mFragmentTransaction.add(R.id.phone_container, fragment, fragment.getClass().getSimpleName() + "_fragment");
        }
        mFragmentTransaction.commit();
        fragmentStack.push(fragment);
        setTitle(((IPhoneFragment) fragment).getTitle());
        setMenu(((IPhoneFragment) fragment).getMenu());
        setBack(((IPhoneFragment) fragment).getBack());
        setTitleColor(((IPhoneFragment) fragment).getTitleColor());
    }

    public void setMenu(@DrawableRes int res) {
        if (res == 0) {
            mIvMenu.setVisibility(View.GONE);
        } else {
            mIvMenu.setVisibility(View.VISIBLE);
            mIvMenu.setImageResource(res);
        }
    }

    public void setTitle(String title) {
        if (title != null) mTvTitle.setText(title);
    }

    public void setTitleColor(int res) {
        if (res != 0) {
            mTvTitle.setTextColor(ContextCompat.getColor(this, res));
        }
    }

    public void setBack(@DrawableRes int res) {
        if (res == 0) {
            mIvBack.setVisibility(View.GONE);
        } else {
            mIvBack.setVisibility(View.VISIBLE);
            mIvBack.setImageResource(res);
        }
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mIvBack.setVisibility(View.VISIBLE);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mIvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragmentF = fragmentStack.pop();
                fragmentStack.push(fragmentF);
                ((IPhoneFragment) fragmentF).onMenuClick();
            }
        });
    }

    @Override
    protected void initListeners() {

    }


    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (fragmentStack.size() == 1) {
            super.onBackPressed();
        }
        if (fragmentStack.empty()) {
            super.onBackPressed();
        } else {
            Fragment fragment = fragmentStack.pop();
            if (fragment instanceof PhoneTicketV2Fragment) {
//                mTvMenu.setVisibility(View.GONE);
            }
            if (!((IPhoneFragment) fragment).onBackPressed()) {
                if (fragmentStack.empty()) {
//                    mIvRole.setVisibility(View.VISIBLE);
//                    mMainRoot.setVisibility(View.VISIBLE);
                    mIvBack.setVisibility(View.VISIBLE);
//                    mAppBar.setVisibility(View.GONE);
                    mFragmentRoot.setVisibility(View.GONE);
                    FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                    mFragmentTransaction.remove(fragment);
                    mFragmentTransaction.commit();
                    fragment = null;
                } else {
                    Fragment fragmentS = fragmentStack.pop();
                    FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                    mFragmentTransaction.remove(fragment).show(fragmentS);
                    mFragmentTransaction.commit();
                    fragmentStack.push(fragmentS);
                    fragment = null;
                    setTitle(((IPhoneFragment) fragmentS).getTitle());
                    setMenu(((IPhoneFragment) fragmentS).getMenu());
                    setBack(((IPhoneFragment) fragmentS).getBack());
                    setTitleColor(((IPhoneFragment) fragmentS).getTitleColor());
                }
            } else {
                fragmentStack.push(fragment);
            }
        }
    }

    public void onFragmentResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_GROUP_DETAIL && resultCode == RESULT_OK) {
            if (!fragmentStack.empty()) {
                Fragment fragmentP = fragmentStack.pop();
                fragmentStack.push(fragmentP);
                if (fragmentP instanceof KiraConversationFragment) {
                    onBackPressed();
                } else {
                    fragmentP.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }
}
