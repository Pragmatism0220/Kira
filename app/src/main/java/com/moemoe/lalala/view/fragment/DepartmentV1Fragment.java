package com.moemoe.lalala.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.github.florent37.viewtooltip.ViewTooltip;
import com.moemoe.lalala.R;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.view.adapter.ChatRoomAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.common.RLog;
import io.rong.eventbus.EventBus;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.EmojiTab;
import io.rong.imkit.emoticon.EmoticonTabAdapter;
import io.rong.imkit.emoticon.IEmojiItemClickListener;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.manager.UnReadMessageManager;
import io.rong.imkit.mention.RongMentionManager;
import io.rong.imkit.model.Event;
import io.rong.imkit.model.UIMessage;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MentionedInfo;
import io.rong.imlib.model.Message;
import io.rong.message.CSPullLeaveMessage;
import io.rong.message.TextMessage;

/**
 * Created by Sora on 2018/3/8.
 */

public class DepartmentV1Fragment extends BaseFragment {

    @BindView(R.id.iv_user_1)
    ImageView mIvUser1;
    @BindView(R.id.iv_user_2)
    ImageView mIvUser2;
    @BindView(R.id.iv_user_3)
    ImageView mIvUser3;
    @BindView(R.id.iv_user_4)
    ImageView mIvUser4;
    @BindView(R.id.iv_user_5)
    ImageView mIvUser5;
    @BindView(R.id.iv_user_6)
    ImageView mIvUser6;
    @BindView(R.id.iv_user_7)
    ImageView mIvUser7;
    @BindView(R.id.iv_user_8)
    ImageView mIvUser8;
    @BindView(R.id.iv_user_9)
    ImageView mIvUser9;
    @BindView(R.id.list)
    RecyclerView mList;
//    @BindView(R.id.et_send)
//    EditText mEtContent;


    private String mChatRoomId;
    private boolean isJoin;
    private boolean isManager;
    private ChatRoomAdapter mAdapter;
    private int mCurShowIndex = 0;
    private ArrayList<ImageView> roles = new ArrayList<>();
    private HashMap<ImageView, String> rolesMap = new HashMap<>();

    @Override
    protected int getLayoutId() {
        return R.layout.frag_bushi;
    }

    public static DepartmentV1Fragment newInstance(String mChatRoomId, boolean isJoin, boolean isManager) {
        DepartmentV1Fragment fragment = new DepartmentV1Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("chatRoomId", mChatRoomId);
        bundle.putBoolean("isJoin", isJoin);
        bundle.putBoolean("isManager", isManager);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setJoin(boolean join) {
        isJoin = join;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        roles.add(mIvUser1);
        roles.add(mIvUser2);
        roles.add(mIvUser3);
        roles.add(mIvUser4);
        roles.add(mIvUser5);
        roles.add(mIvUser6);
        roles.add(mIvUser7);
        roles.add(mIvUser8);
        roles.add(mIvUser9);


        mChatRoomId = getArguments().getString("chatRoomId", "");
        isJoin = getArguments().getBoolean("isJoin");
        isManager = getArguments().getBoolean("isManager");
        RongIMClient.getInstance().joinChatRoom(this.mChatRoomId, 10, new RongIMClient.OperationCallback() {
            public void onSuccess() {
            }

            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
        mAdapter = new ChatRoomAdapter();
        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mList.setAdapter(mAdapter);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startTime();
        setShowTip(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        setShowTip(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stayEvent("社团-社团-部室");
        RongIMClient.getInstance().quitChatRoom(mChatRoomId, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
        EventBus.getDefault().unregister(this);
    }

    private boolean isShowTip = false;

    public void setShowTip(boolean showTip){
        this.isShowTip = showTip;
    }

    public final void onEventMainThread(Event.OnReceiveMessageEvent event) {
        Message message = event.getMessage();
        Conversation.ConversationType conversationType = message.getConversationType();
        String targetId = message.getTargetId();
        if (Conversation.ConversationType.CHATROOM.equals(conversationType) && mChatRoomId.equals(targetId)) {
            if (message.getMessageId() > 0) {
                message.getReceivedStatus().setRead();
                RongIMClient.getInstance().setMessageReceivedStatus(message.getMessageId(), message.getReceivedStatus(), null);
                if (message.getMessageDirection().equals(Message.MessageDirection.RECEIVE)) {
                    UnReadMessageManager.getInstance().onMessageReceivedStatusChanged();
                }
            }

            onEventMainThread(message);
        }
    }

    public final void onEventMainThread(Message msg) {
        if (mChatRoomId.equals(msg.getTargetId()) && Conversation.ConversationType.CHATROOM.equals(msg.getConversationType()) && msg.getMessageId() > 0) {
            int position = mAdapter.findPosition((long) msg.getMessageId());
            if (position >= 0) {
                mAdapter.getItem(position).setMessage(msg);
                mAdapter.notifyItemChanged(position);
            } else {
                UIMessage uiMessage = UIMessage.obtain(msg);
                this.mAdapter.addItem(uiMessage);
                // this.mAdapter.notifyDataSetChanged();
            }

            try {
                JSONObject o = new JSONObject(((TextMessage) msg.getContent()).getExtra());
                String role = o.getString("mapRole");
                String userId = o.getString("userId");
                String content = ((TextMessage) msg.getContent()).getContent();

                ImageView iv = null;
                for (Map.Entry<ImageView, String> entry : rolesMap.entrySet()) {
                    if (userId.equals(entry.getValue())) {
                        iv = entry.getKey();
                        break;
                    }
                }
                int w = getResources().getDimensionPixelSize(R.dimen.x144);
                int h = getResources().getDimensionPixelSize(R.dimen.y176);

                if (iv == null) {
                    iv = roles.get(mCurShowIndex);
                    rolesMap.put(iv, userId);
                    mCurShowIndex++;
                    if (mCurShowIndex > 9) {
                        mCurShowIndex = 0;
                    }
                }
                Glide.with(getContext())
                        .load(StringUtils.getUrl(getContext(), role, w, h, false, true))
                        .into(iv);

                showTip(iv, content);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private View mPreView;

    private void showTip(View view, String content) {
        if(!isShowTip) return;
        //根据显示做调整位置
        if (mPreView != null) {
            ViewTooltip.on(mPreView)
                    .close();
            mPreView = null;
        }

        int y = getResources().getDimensionPixelSize(R.dimen.y70);
        int corner = getResources().getDimensionPixelSize(R.dimen.y8);
        mPreView = view;
        ViewTooltip.on(view)
                .autoHide(true, 1000)
                .text(content)
                .align(ViewTooltip.ALIGN.CENTER)
                .position(ViewTooltip.Position.TOP)
                .textColor(ContextCompat.getColor(getContext(), R.color.black_1e1e1e))
                .corner(corner)
                .color(Color.WHITE)
                .show();

    }

//    @OnClick({R.id.iv_emoj,R.id.tv_send})
//    public void onClick(View view){
//        switch (view.getId()){
//            case R.id.iv_emoj:
//                setEmoticonBoard();
//                break;
//            case R.id.tv_send:
//                sendMessage();
//                break;
//        }
//    }

//    private void sendMessage(){
//        hideEmoticonBoard();
//        String text = mEtContent.getText().toString();
//        mEtContent.getText().clear();
//        mEtContent.setText("");
//        if(!TextUtils.isEmpty(text) && !TextUtils.isEmpty(text.trim())) {
//            TextMessage textMessage = TextMessage.obtain(text);
//            try {
//                JSONObject extra = new JSONObject();
//                if(isManager){
//                    extra.put("member","部长");
//                }else if(isJoin){
//                    extra.put("member","部员");
//                }else {
//                    extra.put("member","路人");
//                }
//                extra.put("mapRole", PreferenceUtils.getAuthorInfo().getPicPath());
//                extra.put("userId", PreferenceUtils.getAuthorInfo().getUserId());
//                textMessage.setExtra(extra.toString());
//                MentionedInfo mentionedInfo = RongMentionManager.getInstance().onSendButtonClick();
//                if(mentionedInfo != null) {
//                    textMessage.setMentionedInfo(mentionedInfo);
//                }
//
//                Message message = Message.obtain(mChatRoomId, Conversation.ConversationType.CHATROOM, textMessage);
//                RongIM.getInstance().sendMessage(message, (String)null, (String)null, (IRongCallback.ISendMessageCallback)null);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
