package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.BuildConfig;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerCommunityV1Component;
import com.moemoe.lalala.di.modules.CommunityModule;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
import com.moemoe.lalala.model.entity.AllPersonnelEntity;
import com.moemoe.lalala.model.entity.FeedFollowType2Entity;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.TagLikeEntity;
import com.moemoe.lalala.model.entity.TagSendEntity;
import com.moemoe.lalala.model.entity.UserTopEntity;
import com.moemoe.lalala.presenter.CommunityContract;
import com.moemoe.lalala.presenter.CommunityPresenter;
import com.moemoe.lalala.utils.AlertDialogUtil;
import com.moemoe.lalala.utils.AndroidBug5497Workaround;
import com.moemoe.lalala.utils.BoldSpan;
import com.moemoe.lalala.utils.DensityUtil;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.SoftKeyboardUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.fragment.DepartmentV1Fragment;
import com.moemoe.lalala.view.fragment.FeedFollowOther2Fragment;
import com.moemoe.lalala.view.fragment.NewFeedFollowOther1Fragment;
import com.moemoe.lalala.view.widget.netamenu.BottomMenuFragment;
import com.moemoe.lalala.view.widget.netamenu.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import io.reactivex.schedulers.Schedulers;
import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.EmojiTab;
import io.rong.imkit.emoticon.EmoticonTabAdapter;
import io.rong.imkit.emoticon.IEmojiItemClickListener;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imkit.mention.RongMentionManager;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MentionedInfo;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.moemoe.lalala.utils.StartActivityConstant.REQUEST_CODE_CREATE_DOC;

/**
 * Created by Sora on 2018/2/28.
 */

public class CommunityV1Activity extends BaseAppCompatActivity implements CommunityContract.View, AppBarLayout.OnOffsetChangedListener {
    private int RESULT_DETSIL = 100;
    @BindView(R.id.tv_content_num)
    TextView mTvFileNum;
    @BindView(R.id.tv_square_num)
    TextView mTvDocNum;
    @BindView(R.id.tv_bag_num)
    TextView mTvBagNum;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_menu_list)
    ImageView mMenuList;
    @BindView(R.id.tv_toolbar_title)
    TextView mTvTitle;
    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mTitleView;
    @BindView(R.id.fl_container)
    FrameLayout mBottomRoot;
    @BindView(R.id.iv_menu_search)
    ImageView mIvSearch;
    @BindView(R.id.iv_to_wen)
    ImageView mIvToWen;
    @BindView(R.id.ll_bottom_root)
    View mBottomSendRoot;
    @BindView(R.id.et_send)
    EditText mEtContent;
    @BindView(R.id.ll_emoj_root)
    LinearLayout mRoot;
    @BindView(R.id.view_close_key)
    View mCloseKey;
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    @Inject
    CommunityPresenter mPresenter;
    private BottomMenuFragment fragment;
    private DepartmentV1Fragment mItem1Fragment;
    private FeedFollowOther2Fragment mItem2Fragment;
    private String id = "";
    private NewFeedFollowOther1Fragment mItem3Fragment;
    private boolean manager;
    private TextView view;
    private boolean join;
    private FeedFollowType2Entity mFeedFollow;
    private EmoticonTabAdapter mEmotionTabAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_community_v1;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        DaggerCommunityV1Component.builder()
                .communityModule(new CommunityModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        ViewUtils.setStatusBarLight(getWindow(), null);
        AndroidBug5497Workaround.assistActivity(this);
        mEmotionTabAdapter = new EmoticonTabAdapter();
        initEmoticonTabs();
        id = getIntent().getStringExtra("uuid");
        mPresenter.loadData(id + "");
        mToolbar.setVisibility(View.VISIBLE);


        fragment = new BottomMenuFragment();

        initMenu();
        int h = (int) getResources().getDimension(R.dimen.y100) + (int) getResources().getDimension(R.dimen.status_bar_height);
        ViewGroup.LayoutParams params = mToolbar.getLayoutParams();
        params.height = h;
        mToolbar.setLayoutParams(params);
        mMenuList.setVisibility(View.VISIBLE);
        mMenuList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare();
            }
        });
        mIvCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickEvent("社团详情");
                Intent intent = new Intent(CommunityV1Activity.this, CommunityDetailsActivity.class);
                intent.putExtra("uuid", id);
                startActivityForResult(intent, RESULT_DETSIL);
            }
        });

    }

    private void showShare() {
        final OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        String url;
        if (BuildConfig.DEBUG) {
            url = ApiService.SHARE_BASE_DEBUG + "";
        } else {
            url = ApiService.SHARE_BASE + "";
        }
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("" + " " + url);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        oks.setImageUrl(ApiService.URL_QINIU + "");
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);
        // 启动分享GUI
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                MoeMoeApplication.getInstance().getNetComponent().getApiService().shareKpi("{\"doc\":\"" + "" + "\"}")//{"doc":"uuid"}
                        .subscribeOn(Schedulers.io())
                        .subscribe(new NetSimpleResultSubscriber() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onFail(int code, String msg) {

                            }
                        });
//                mPresenter.shareDoc();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        oks.show(this);
    }

    private void initEmoticonTabs() {
        EmojiTab emojiTab = new EmojiTab();
        emojiTab.setOnItemClickListener(new IEmojiItemClickListener() {
            public void onEmojiClick(String emoji) {
                int start = mEtContent.getSelectionStart();
                mEtContent.getText().insert(start, emoji);
            }

            public void onDeleteClick() {
                mEtContent.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            }
        });
        List<IEmoticonTab> list = new ArrayList<>();
        list.add(emojiTab);
        this.mEmotionTabAdapter.initTabs(list, "bushi");
    }

    private void hideEmoticonBoard() {
        this.mEmotionTabAdapter.setVisibility(View.GONE);
    }

    private void setEmoticonBoard() {
        if (this.mEmotionTabAdapter.isInitialized()) {
            if (this.mEmotionTabAdapter.getVisibility() == View.VISIBLE) {
                this.mEmotionTabAdapter.setVisibility(View.GONE);
            } else {
                this.mEmotionTabAdapter.setVisibility(View.VISIBLE);
                hideKey();
            }
        } else {
            this.mEmotionTabAdapter.bindView(mRoot);
            this.mEmotionTabAdapter.setVisibility(View.VISIBLE);
            hideKey();
        }
    }

    private void showKey() {
        mCloseKey.setVisibility(View.VISIBLE);
        SoftKeyboardUtils.showInputKeyBoard(CommunityV1Activity.this, mEtContent);
        hideEmoticonBoard();
    }

    private void hideKey() {
        mCloseKey.setVisibility(View.GONE);
        SoftKeyboardUtils.hideInputKeyBoard(CommunityV1Activity.this, mEtContent);
    }

    @OnClick({R.id.iv_emoj, R.id.tv_send, R.id.view_close_key})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_emoj:
                setEmoticonBoard();
                break;
            case R.id.tv_send:
                sendMessage();
                break;
            case R.id.view_close_key:
                hideKey();
                break;
        }
    }

    private void sendMessage() {
        hideEmoticonBoard();
        String text = mEtContent.getText().toString();
        mEtContent.getText().clear();
        mEtContent.setText("");
        if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(text.trim())) {
            TextMessage textMessage = TextMessage.obtain(text);
            try {
                JSONObject extra = new JSONObject();
                if (manager) {
                    extra.put("member", "部长");
                } else if (join) {
                    extra.put("member", "部员");
                } else {
                    extra.put("member", "路人");
                }
                extra.put("mapRole", PreferenceUtils.getAuthorInfo().getPicPath());
                extra.put("userId", PreferenceUtils.getAuthorInfo().getUserId());
                textMessage.setExtra(extra.toString());
                MentionedInfo mentionedInfo = RongMentionManager.getInstance().onSendButtonClick();
                if (mentionedInfo != null) {
                    textMessage.setMentionedInfo(mentionedInfo);
                }

                Message message = Message.obtain(id, Conversation.ConversationType.CHATROOM, textMessage);
                RongIM.getInstance().sendMessage(message, (String) null, (String) null, (IRongCallback.ISendMessageCallback) null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mIvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //埋点统计：动态
                clickEvent("社团--社团-搜索");
                Intent i3 = new Intent(CommunityV1Activity.this, AllSearchActivity.class);
                i3.putExtra("type", "doc");
                startActivity(i3);
            }
        });
    }

    @Override
    protected void initListeners() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mEtContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (0 == event.getAction()) {
                    showKey();
                }
                return false;
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onFailure(int code, String msg) {
        ErrorCodeUtils.showErrorMsgByCode(this, code, msg);
    }

    @Override
    public void onLoadListSuccess(FeedFollowType2Entity entity) {
        formerFunction(entity);
    }


    private void formerFunction(final FeedFollowType2Entity entity) {
        mFeedFollow = entity;
        int w = DensityUtil.getScreenWidth(this);
        int h = getResources().getDimensionPixelSize(R.dimen.y300);
        Glide.with(this)
                .load(StringUtils.getUrl(this, entity.getBg(), w, h, false, true))
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .bitmapTransform(new CropTransformation(this, w, h)
                        , new BlurTransformation(this, 10, 4)
                        , new ColorFilterTransformation(this, ContextCompat.getColor(this, R.color.alpha_20)))
                .into((ImageView) $(R.id.iv_bg));

        int size = getResources().getDimensionPixelSize(R.dimen.y128);
        Glide.with(this)
                .load(StringUtils.getUrl(this, entity.getBg(), size, size, false, true))
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .bitmapTransform(new RoundedCornersTransformation(this, getResources().getDimensionPixelSize(R.dimen.y16), 0))
                .into((ImageView) $(R.id.iv_cover));
        ((TextView) $(R.id.tv_member)).setText("部员：" + entity.getFocusNums());
        ((TextView) $(R.id.tv_title)).setText(entity.getTitle());
        ((TextView) $(R.id.tv_desc)).setText(entity.getContent());
        view = (TextView) $(R.id.tv_admin);
        join = entity.getJoin();
        if (join) {
            manager = entity.getManager();
            if (manager) {
                ((TextView) $(R.id.tv_admin)).setText("部长");
                view.setBackground(getResources().getDrawable(R.drawable.shape_minister));
            } else {
                ((TextView) $(R.id.tv_admin)).setText("部员");
                view.setBackground(getResources().getDrawable(R.drawable.shape_member));
            }
        } else {
            ((TextView) $(R.id.tv_admin)).setText("加入");
            view.setBackground(getResources().getDrawable(R.drawable.shape_join));
        }

        if (entity.getAdmins().size() > 0) {
            int adminSize = getResources().getDimensionPixelSize(R.dimen.y44);
            Glide.with(this)
                    .load(StringUtils.getUrl(this, entity.getAdmins().get(0).getHeadPath(), adminSize, adminSize, false, true))
                    .error(R.drawable.bg_default_circle)
                    .placeholder(R.drawable.bg_default_circle)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into((ImageView) $(R.id.iv_admin_avatar));
            $(R.id.fl_admin_avatar_root).setVisibility(View.GONE);
        } else {
            $(R.id.fl_admin_avatar_root).setVisibility(View.GONE);
        }
        $(R.id.ll_admin_root).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                AdminListActivity.startActivity(CommunityV1Activity.this, entity.getAdmins(), PhoneMainV2Activity.UUID);
            }
        });


        String fileNum = "部室";
        BoldSpan span = new BoldSpan(ContextCompat.getColor(this, R.color.white));
        SpannableStringBuilder style = new SpannableStringBuilder(fileNum);
        style.setSpan(span, fileNum.indexOf("部室"), fileNum.indexOf("部室") + "部室".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvFileNum.setText(fileNum);
        mTvFileNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEvent("社团-部室");
                mBottomSendRoot.setVisibility(View.VISIBLE);
                mItem1Fragment.setShowTip(true);
                mIvToWen.setVisibility(View.GONE);
                if (mItem1Fragment.isHidden()) {
                    FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                    mFragmentTransaction.show(mItem1Fragment).hide(mItem2Fragment).hide(mItem3Fragment);
                    mFragmentTransaction.commit();
                    mTvFileNum.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(CommunityV1Activity.this, R.drawable.ic_trends_tag_switch));
                    mTvFileNum.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.y4));
                    mTvFileNum.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
                    mTvDocNum.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTvDocNum.setCompoundDrawablePadding(0);
                    mTvDocNum.setGravity(Gravity.CENTER);
                    mTvBagNum.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTvBagNum.setCompoundDrawablePadding(0);
                    mTvBagNum.setGravity(Gravity.CENTER);
                }
            }
        });


        String docNum = "广场 " + entity.getDocNums();
        BoldSpan span1 = new BoldSpan(ContextCompat.getColor(this, R.color.white));
        SpannableStringBuilder style1 = new SpannableStringBuilder(docNum);
        style1.setSpan(span1, docNum.indexOf("广场"), docNum.indexOf("广场") + "广场".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvDocNum.setText(style1);
        mTvDocNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEvent("社团-广场");
                mBottomSendRoot.setVisibility(View.GONE);
                mItem1Fragment.setShowTip(false);
                hideKey();
                if (mItem2Fragment.isHidden()) {
                    FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                    mFragmentTransaction.show(mItem2Fragment).hide(mItem1Fragment).hide(mItem3Fragment);
                    mFragmentTransaction.commit();
                    mTvDocNum.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(CommunityV1Activity.this, R.drawable.ic_trends_tag_switch));
                    mTvDocNum.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.y4));
                    mTvDocNum.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
                    mTvFileNum.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTvFileNum.setCompoundDrawablePadding(0);
                    mTvFileNum.setGravity(Gravity.CENTER);
                    mTvBagNum.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTvBagNum.setCompoundDrawablePadding(0);
                    mTvBagNum.setGravity(Gravity.CENTER);
                    isChanged = true;
                    goIvMenu();
                }
                mItem2Fragment.setSmoothScrollToPosition();
            }
        });

        String bagNum = "书包 " + entity.getFileNums();
        BoldSpan span2 = new BoldSpan(ContextCompat.getColor(this, R.color.white));
        SpannableStringBuilder style2 = new SpannableStringBuilder(bagNum);
        style2.setSpan(span2, bagNum.indexOf("书包"), bagNum.indexOf("书包") + "书包".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvBagNum.setText(style2);
        mTvBagNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEvent("社团-书包");
                mBottomSendRoot.setVisibility(View.GONE);
                mItem1Fragment.setShowTip(false);
                hideKey();
                if (!join) {
                    final AlertDialogUtil alertDialogUtil = AlertDialogUtil.getInstance();
                    alertDialogUtil.createPromptNormalDialog(CommunityV1Activity.this, "是否关注社团");
                    alertDialogUtil.setOnClickListener(new AlertDialogUtil.OnClickListener() {
                        @Override
                        public void CancelOnClick() {
                            alertDialogUtil.dismissDialog();
                        }

                        @Override
                        public void ConfirmOnClick() {
                            if (mItem3Fragment.isHidden()) {
                                FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                                mFragmentTransaction.show(mItem3Fragment).hide(mItem2Fragment).hide(mItem1Fragment);
                                mFragmentTransaction.commit();
                                mTvBagNum.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(CommunityV1Activity.this, R.drawable.ic_trends_tag_switch));
                                mTvBagNum.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.y4));
                                mTvBagNum.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
                                mTvFileNum.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                                mTvFileNum.setCompoundDrawablePadding(0);
                                mTvFileNum.setGravity(Gravity.CENTER);
                                mTvDocNum.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                                mTvDocNum.setCompoundDrawablePadding(0);
                                mTvDocNum.setGravity(Gravity.CENTER);
                                isChanged = false;
                                goIvMenu();
                            }
                            mItem3Fragment.setSmoothScrollToPosition();
                            mPresenter.loadTagJoin(id, true);
                            alertDialogUtil.dismissDialog();
                        }
                    });
                    alertDialogUtil.showDialog();
                } else {
                    if (mItem3Fragment.isHidden()) {
                        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        mFragmentTransaction.show(mItem3Fragment).hide(mItem2Fragment).hide(mItem1Fragment);
                        mFragmentTransaction.commit();
                        mTvBagNum.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(CommunityV1Activity.this, R.drawable.ic_trends_tag_switch));
                        mTvBagNum.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.y4));
                        mTvBagNum.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
                        mTvFileNum.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        mTvFileNum.setCompoundDrawablePadding(0);
                        mTvFileNum.setGravity(Gravity.CENTER);
                        mTvDocNum.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        mTvDocNum.setCompoundDrawablePadding(0);
                        mTvDocNum.setGravity(Gravity.CENTER);
                        isChanged = false;
                        goIvMenu();
                    }
                    mItem3Fragment.setSmoothScrollToPosition();
                }
            }
        });

        boolean isAdmin = false;
        for (UserTopEntity entity1 : entity.getAdmins()) {
            if (PreferenceUtils.getUUid().equals(entity1.getUserId())) {
                isAdmin = true;
                break;
            }
        }

        mTvTitle.setText(getIntent().getStringExtra("title"));


        mItem1Fragment = DepartmentV1Fragment.newInstance(id, join, manager);
        mItem2Fragment = FeedFollowOther2Fragment.newInstance(id, getIntent().getStringExtra("title"), join);
        mItem3Fragment = NewFeedFollowOther1Fragment.newInstance(id, isAdmin);

        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.add(R.id.fl_container, mItem1Fragment);
        mFragmentTransaction.add(R.id.fl_container, mItem2Fragment);
        mFragmentTransaction.add(R.id.fl_container, mItem3Fragment);
        mFragmentTransaction.show(mItem2Fragment).hide(mItem1Fragment).hide(mItem3Fragment);
        mFragmentTransaction.commit();


        mTvDocNum.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(CommunityV1Activity.this, R.drawable.ic_trends_tag_switch));
        mTvDocNum.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.y4));
        mTvDocNum.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        mTvFileNum.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        mTvFileNum.setCompoundDrawablePadding(0);
        mTvFileNum.setGravity(Gravity.CENTER);
        mTvBagNum.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        mTvBagNum.setCompoundDrawablePadding(0);
        mTvBagNum.setGravity(Gravity.CENTER);
        if (!mItem2Fragment.isHidden()) {
            isChanged = true;
            goIvMenu();
        }

        ((TextView) $(R.id.tv_admin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String admin = ((TextView) $(R.id.tv_admin)).getText().toString();
                if (admin.equals("加入")) {
                    mPresenter.loadTagJoin(id, true);
                }
            }
        });
        mIvToWen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChanged) {
                    clickEvent("社团-社团-广场发帖");
                    Intent intent = new Intent(CommunityV1Activity.this, CreateRichDocActivity.class);
                    intent.putExtra(CreateRichDocActivity.TYPE_QIU_MING_SHAN, 4);
                    intent.putExtra(CreateRichDocActivity.TYPE_TAG_NAME_DEFAULT, getIntent().getStringExtra("title"));
                    intent.putExtra("tagId", id);
                    intent.putExtra("from_name", getIntent().getStringExtra("title"));
                    intent.putExtra("from_schema", "");
                    startActivityForResult(intent, REQUEST_CODE_CREATE_DOC);
                } else {
                    fragment.show(getSupportFragmentManager(), "FeedBag");
                }
            }
        });
    }

    public void goIvMenu() {
        if (join) {
            mIvToWen.setVisibility(View.VISIBLE);
        } else {
            mIvToWen.setVisibility(View.GONE);
        }
        if (isChanged) {
            mIvToWen.setImageResource(R.drawable.btn_send_wen);
        } else {
            mIvToWen.setImageResource(R.drawable.btn_feed_create_bag);

        }
    }

    @Override
    public void onLoadTagJoinSuccess(boolean isJoin) {
        showToast("加入成功");

        if (manager) {
            ((TextView) $(R.id.tv_admin)).setText("部长");
            view.setBackground(getResources().getDrawable(R.drawable.shape_minister));
        } else {
            ((TextView) $(R.id.tv_admin)).setText("部员");
            view.setBackground(getResources().getDrawable(R.drawable.shape_member));
        }
        join = true;
        manager = false;
        mItem1Fragment.setJoin(true);

        mIvToWen.setVisibility(View.VISIBLE);
        if (isChanged) {
            mIvToWen.setImageResource(R.drawable.btn_send_wen);
        } else {
            mIvToWen.setImageResource(R.drawable.btn_feed_create_bag);
        }

    }

    @Override
    public void onLoadTagAllPersonnelSuccess(AllPersonnelEntity entity) {

    }

    /**
     * 添加标签
     *
     * @param entity
     */
    public void createLabel(TagSendEntity entity, int position) {
        if (!mItem2Fragment.isHidden()) {
            mItem2Fragment.createLabel(entity, position);
        }
    }

    /**
     * 社区详情-广场-item点赞
     *
     * @param id
     * @param isLike
     * @param position
     */
    public void likeDoc(String id, boolean isLike, int position) {
        if (!mItem2Fragment.isHidden()) {
            mItem2Fragment.likeDoc(id, isLike, position);
        }
    }

    @Override
    protected void onDestroy() {
        stayEvent("社团-社团-进入社团");
        setResult(RESULT_OK);
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.release();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        startTime();
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseTime();
        mAppBarLayout.removeOnOffsetChangedListener(this);
    }

    private boolean isChanged;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int temp = getResources().getDimensionPixelSize(R.dimen.y180) - getResources().getDimensionPixelSize(R.dimen.status_bar_height);
        float percent = (float) Math.abs(verticalOffset) / temp;
        if (percent > 0.4) {
//            if (!isChanged) {
            mIvSearch.setImageResource(R.drawable.btn_trends_search);
            mToolbar.setNavigationIcon(R.drawable.btn_back_blue_normal_unity);
            mMenuList.setImageResource(R.drawable.btn_toolbar_share_blue);
            mToolbar.setBackgroundColor(getResources().getColor(R.color.white));
//                isChanged = true;
//            }
            mIvSearch.setAlpha(percent);
            mToolbar.setAlpha(percent);
            mMenuList.setAlpha(percent);
            mTvTitle.setAlpha(percent);
            mTvTitle.setTextColor(getResources().getColor(R.color.main_cyan));
            mTvTitle.setVisibility(View.VISIBLE);

        } else {
//            if (isChanged) {
            mIvSearch.setImageResource(R.drawable.btn_trends_search_white);
            mToolbar.setNavigationIcon(R.drawable.btn_back_white_normal);
            mMenuList.setImageResource(R.drawable.btn_toolbar_share);
            mToolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
//                isChanged = false;
//            }
            mIvSearch.setAlpha(1 - percent);
            mToolbar.setAlpha(1 - percent);
            mMenuList.setAlpha(1 - percent);
            mTvTitle.setAlpha(1 - percent);
            mTvTitle.setVisibility(View.GONE);
        }
        mTvTitle.setAlpha(percent);
    }

    private void initMenu() {
        ArrayList<MenuItem> items = new ArrayList<>();

        MenuItem item = new MenuItem(1, "综合");
        items.add(item);
        item = new MenuItem(2, "漫画");
        items.add(item);
        item = new MenuItem(3, "图集");
        items.add(item);
        item = new MenuItem(4, "小说");
        items.add(item);
        item = new MenuItem(5, "视频");
        items.add(item);
        item = new MenuItem(6, "音乐");
        items.add(item);

        fragment.setShowTop(false);
        fragment.setMenuItems(items);
        fragment.setMenuType(BottomMenuFragment.TYPE_VERTICAL);
        fragment.setmClickListener(new BottomMenuFragment.MenuItemClickListener() {
            @Override
            public void OnMenuItemClick(int itemId) {
                clickEvent("社团-社团-建书包");
                String mFolderType = FolderType.ZH.toString();
                if (itemId == 1) {
                    mFolderType = FolderType.ZH.toString();
                } else if (itemId == 2) {
                    mFolderType = FolderType.MH.toString();
                } else if (itemId == 3) {
                    mFolderType = FolderType.TJ.toString();
                } else if (itemId == 4) {
                    mFolderType = FolderType.XS.toString();
                } else if (itemId == 5) {
                    mFolderType = FolderType.SP.toString();
                } else if (itemId == 6) {
                    mFolderType = FolderType.YY.toString();
                }
                NewFolderEditActivity.startActivity(CommunityV1Activity.this, "create", mFolderType, null);
            }
        });

    }

    public String getCommunityName() {
        if (mItem2Fragment != null) {
            return mItem2Fragment.getCommuntiyName();
        }
        return "";
    }

    /**
     * 社区标签点赞
     * @param isLike
     * @param position
     * @param entity
     * @param parentPosition
     */
    public void likeTag(boolean isLike, int position, TagLikeEntity entity, int parentPosition) {
        if (!mItem2Fragment.isHidden()) {
            mItem2Fragment.likeTag(isLike, position, entity, parentPosition);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_DETSIL && resultCode == RESULT_OK) {
            if (data != null) {
                join = data.getBooleanExtra("join", false);
                manager = data.getBooleanExtra("manager", false);
                goIvMenu();
                if (join) {
                    if (manager) {
                        ((TextView) $(R.id.tv_admin)).setText("部长");
                        view.setBackground(getResources().getDrawable(R.drawable.shape_minister));
                    } else {
                        ((TextView) $(R.id.tv_admin)).setText("部员");
                        view.setBackground(getResources().getDrawable(R.drawable.shape_member));
                    }
                } else {
                    ((TextView) $(R.id.tv_admin)).setText("加入");
                    view.setBackground(getResources().getDrawable(R.drawable.shape_join));
                }
            }
        }
    }
}
