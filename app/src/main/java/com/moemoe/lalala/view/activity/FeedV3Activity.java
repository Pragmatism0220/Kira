package com.moemoe.lalala.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.event.ScrollMessage;
import com.moemoe.lalala.model.entity.ReceiverInfo;
import com.moemoe.lalala.model.entity.StageLineEntity;
import com.moemoe.lalala.model.entity.TagLikeEntity;
import com.moemoe.lalala.model.entity.TagSendEntity;
import com.moemoe.lalala.utils.DialogUtils;
import com.moemoe.lalala.utils.DocEvent;
import com.moemoe.lalala.utils.MapEevent;
import com.moemoe.lalala.utils.NetworkUtils;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.TabFragmentPagerAdapter;
import com.moemoe.lalala.view.fragment.BaseFragment;
import com.moemoe.lalala.view.fragment.ClubListFragment;
import com.moemoe.lalala.view.fragment.FeedDynamicV3Fragment;
import com.moemoe.lalala.view.fragment.FeedFollowV4Fragment;
import com.moemoe.lalala.view.fragment.NewCommunityFragment;
import com.moemoe.lalala.view.fragment.NewFollowMainV3Fragment;
import com.moemoe.lalala.view.widget.map.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.moemoe.lalala.utils.StartActivityConstant.REQUEST_CODE_CREATE_DOC;
import static com.moemoe.lalala.utils.StartActivityConstant.REQ_LOGIN;
import static com.moemoe.lalala.utils.StartActivityConstant.REQ_SELECT_TAG;
import static com.moemoe.lalala.view.activity.LoginActivity.RESPONSE_LOGIN_SUCCESS;

/**
 * feed流主界面
 * Created by yi on 2018/1/10.
 */

public class FeedV3Activity extends BaseAppCompatActivity implements IUnReadMessageObserver {

    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.iv_left)
    ImageView mIvLeft;
    @BindView(R.id.iv_right)
    ImageView mIvRight;

    @BindView(R.id.rl_club_square)
    RelativeLayout mRlClubSquare;
    @BindView(R.id.iv_club_square)
    ImageView mIvClubSquare;
    @BindView(R.id.tv_club_square)
    TextView mTvClubSquare;
    @BindView(R.id.rl_club_club)
    RelativeLayout mRlClubClub;
    @BindView(R.id.iv_club_club)
    ImageView mIvClubClub;
    @BindView(R.id.tv_club_club)
    TextView mTvClubClub;
    @BindView(R.id.rl_club_follow)
    RelativeLayout mRlClubFollow;
    @BindView(R.id.iv_club_follow)
    ImageView mIvClubFollow;
    @BindView(R.id.tv_club_follow)
    TextView mTvClubFollow;
    @BindView(R.id.rl_club_inform)
    RelativeLayout mRlClubInform;
    @BindView(R.id.iv_club_inform)
    ImageView mIvClubInform;
    @BindView(R.id.tv_club_inform)
    TextView mTvClubInform;
    @BindView(R.id.tv_club_inform_dot)
    TextView mTvCLubDot;
    @BindView(R.id.iv_club_post)
    ImageView mIvClubPost;
    @BindView(R.id.include_toolbar)
    LinearLayout mIncludeToolbar;
    @BindView(R.id.iv_personal)
    ImageView mIvPresonal;
    @BindView(R.id.fl_container)
    FrameLayout mFlContainer;
    @BindView(R.id.tool)
    LinearLayout mTitle;


    private TabFragmentPagerAdapter mAdapter;
    private FeedDynamicV3Fragment dynamicV3Fragment;
    private FeedFollowV4Fragment feedFollowV4Fragment;
    private NewFollowMainV3Fragment mainV3Fragment;
    private NewCommunityFragment newCommunityFragment;
    private ClubListFragment clubListFragment;

    private int titleHeight;
    private final float maxScroll = 500f;//滚动多少像素会完全隐藏title，可以随意设置，不建议直接使用定值像素，可使用固定dp值转像素或者固定屏幕比例转像素
    private float oldAlpha;//获取当前的透明度
    private TextView title;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_feed_v3;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initViews(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String FRAGMENTS_TAG = "android:support:fragments";
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        final Conversation.ConversationType[] conversationTypes = {
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP
        };
        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);
        clickEvent("社团");
//        fragmentList.add(FeedFollowV3Fragment.newInstance());
        mainV3Fragment = NewFollowMainV3Fragment.newInstance("ground");
//        dynamicV3Fragment = FeedDynamicV3Fragment.newInstance();
//        fragmentList.add(FeedBagFragment.newInstance());
        feedFollowV4Fragment = FeedFollowV4Fragment.newInstance("all", "全部", false);
//        newCommunityFragment = NewCommunityFragment.newInstance();
        clubListFragment = ClubListFragment.newInstance();
        mRlClubSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainV3Fragment.isHidden()) {
                    FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                    mFragmentTransaction.show(mainV3Fragment).hide(feedFollowV4Fragment).hide(clubListFragment);
                    mFragmentTransaction.commit();
                    mIvClubSquare.setSelected(true);
                    mTvClubSquare.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.main_cyan));
                    mIvClubClub.setSelected(false);
                    mTvClubClub.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.gray_d7d7d7));
                    mIvClubFollow.setSelected(false);
                    mTvClubFollow.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.gray_d7d7d7));
                    mIvClubInform.setSelected(false);
                    mTvClubInform.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.gray_d7d7d7));
                }
                if (mainV3Fragment.isVisible()) {
                    mainV3Fragment.onSmoothScrollBy();
                }
            }
        });

        mRlClubClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clubListFragment.isHidden()) {
                    FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                    mFragmentTransaction.show(clubListFragment).hide(mainV3Fragment).hide(feedFollowV4Fragment);
                    mFragmentTransaction.commit();
                    mIvClubSquare.setSelected(false);
                    mTvClubSquare.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.gray_d7d7d7));
                    mIvClubClub.setSelected(true);
                    mTvClubClub.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.main_cyan));
                    mIvClubFollow.setSelected(false);
                    mTvClubFollow.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.gray_d7d7d7));
                    mIvClubInform.setSelected(false);
                    mTvClubInform.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.gray_d7d7d7));
                }
            }
        });
        mRlClubFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedFollowV4Fragment.isHidden()) {
                    FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                    mFragmentTransaction.show(feedFollowV4Fragment).hide(mainV3Fragment).hide(clubListFragment);
                    mFragmentTransaction.commit();
                    mIvClubSquare.setSelected(false);
                    mTvClubSquare.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.gray_d7d7d7));
                    mIvClubClub.setSelected(false);
                    mTvClubClub.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.gray_d7d7d7));
                    mIvClubFollow.setSelected(true);
                    mTvClubFollow.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.main_cyan));
                    mIvClubInform.setSelected(false);
                    mTvClubInform.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.gray_d7d7d7));
                }
                if (feedFollowV4Fragment.isVisible()) {
                    feedFollowV4Fragment.onSmoothScrollBy();
                }
            }
        });
        mRlClubInform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.checkNetworkAndShowError(FeedV3Activity.this) && DialogUtils.checkLoginAndShowDlg(FeedV3Activity.this)) {
                    NoticeActivity.startActivity(FeedV3Activity.this, 0);
//                Intent intent = new Intent(FeedV3Activity.this, ShoppingActivity.class);
//                Intent intent = new Intent(FeedV3Activity.this, DormitoryActivity.class);
//                startActivity(intent);
                }
            }
        });


        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.add(R.id.fl_container, mainV3Fragment);
        mFragmentTransaction.add(R.id.fl_container, feedFollowV4Fragment);
        mFragmentTransaction.add(R.id.fl_container, clubListFragment);
        mFragmentTransaction.show(mainV3Fragment).

                hide(feedFollowV4Fragment).

                hide(clubListFragment);
        mFragmentTransaction.commit();
        mIvClubSquare.setSelected(true);
        mTvClubSquare.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.main_cyan));
        mIvClubClub.setSelected(false);
        mTvClubClub.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.gray_d7d7d7));
        mIvClubFollow.setSelected(false);
        mTvClubFollow.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.gray_d7d7d7));
        mIvClubInform.setSelected(false);
        mTvClubInform.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.gray_d7d7d7));
        mainV3Fragment.onSmoothScrollBy();
        //获取标题栏的高度
        mTitle.getViewTreeObserver().
                addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        titleHeight = mTitle.getHeight();
                    }
                });
        oldAlpha = mTitle.getAlpha();
        EventBus.getDefault().register(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (MoeMoeApplication.getInstance().GoneDiaLog()) {
            return true;
        }
        if (MoeMoeApplication.getInstance().isMenu()) {
            MoeMoeApplication.getInstance().GoneMenu();
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mIncludeToolbar.setEnabled(false);

        int num = PreferenceUtils.hasMsg(this);
        if (num > 0) {
            mTvCLubDot.setVisibility(View.VISIBLE);
            if (num > 99) num = 99;
            mTvCLubDot.setText(String.valueOf(num));
        } else {
            mTvCLubDot.setVisibility(View.GONE);
        }

        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickEvent("搜索-社团");
                Intent i3 = new Intent(FeedV3Activity.this, AllSearchActivity.class);
                i3.putExtra("type", "doc");
                startActivity(i3);
            }
        });
        mIvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIvRight.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (NetworkUtils.checkNetworkAndShowError(FeedV3Activity.this) && DialogUtils.checkLoginAndShowDlg(FeedV3Activity.this)) {
                    startActivity(new Intent(FeedV3Activity.this, HouseActivity.class));
                    finish();
                }
            }
        });
        mIvClubPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEvent("社区-发帖");
                Intent intent = new Intent(FeedV3Activity.this, CreateRichDocActivity.class);
                intent.putExtra(CreateRichDocActivity.TYPE_QIU_MING_SHAN, 4);
                intent.putExtra("tagId", "81942b2a-7b74-429b-a003-fb3713a5e226");
                intent.putExtra("from_name", "摸鱼部");
                intent.putExtra("from_schema", "");
                startActivityForResult(intent, REQUEST_CODE_CREATE_DOC);
            }
        });
    }

    public void likeTag(boolean isLike, int position, TagLikeEntity entity, int parentPosition) {
        if (!feedFollowV4Fragment.isHidden()) {
            feedFollowV4Fragment.likeTag(isLike, position, entity, parentPosition);
        } else if (!mainV3Fragment.isHidden()) {
            mainV3Fragment.likeTag(isLike, position, entity, parentPosition);
        }
    }

    /**
     * 动态-全部-item点赞
     *
     * @param id
     * @param isLike
     * @param position
     */
    public void likeDoc(String id, boolean isLike, int position) {
        if (!feedFollowV4Fragment.isHidden()) {
            feedFollowV4Fragment.likeDoc(id, isLike, position);
        } else if (!mainV3Fragment.isHidden()) {
            mainV3Fragment.likeDoc(id, isLike, position);
        }
    }

    /**
     * 添加标签
     *
     * @param entity
     */
    public void createLabel(TagSendEntity entity, int ppposition) {
        createDialog();
        if (!feedFollowV4Fragment.isHidden()) {
            feedFollowV4Fragment.createLabel(entity, ppposition);
        } else if (!mainV3Fragment.isHidden()) {
            mainV3Fragment.createLabel(entity, ppposition);
        }

    }

    @Override
    protected void initListeners() {
        mIvPresonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.checkNetworkAndShowError(FeedV3Activity.this) && DialogUtils.checkLoginAndShowDlg(FeedV3Activity.this)) {
                    //埋点统计：手机个人中心
                    clickEvent("个人中心");
                    Intent i1 = new Intent(FeedV3Activity.this, PersonalV2Activity.class);
                    i1.putExtra(UUID, PreferenceUtils.getUUid());
                    startActivity(i1);
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
        MoeMoeApplication.getInstance().GoneWindowMager(this);
        MoeMoeApplication.getInstance().GoneDiaLog();
        MoeMoeApplication.getInstance().GoneMenu();
        pauseTime();
    }


    @Override
    protected void onResume() {
        super.onResume();
        MoeMoeApplication.getInstance().VisibilityWindowMager(this);
        startTime();
        if (!RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
            if (!TextUtils.isEmpty(PreferenceUtils.getAuthorInfo().getRcToken())) {
                RongIM.connect(PreferenceUtils.getAuthorInfo().getRcToken(), new RongIMClient.ConnectCallback() {
                    @Override
                    public void onTokenIncorrect() {

                    }

                    @Override
                    public void onSuccess(String s) {

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
            }
        }

        if (TextUtils.isEmpty(PreferenceUtils.getUUid())) {
            mIvPresonal.setImageResource(R.drawable.bg_default_circle);
        } else {
            int size = (int) getResources().getDimension(R.dimen.x64);
            Glide.with(this)
                    .load(StringUtils.getUrl(this, PreferenceUtils.getAuthorInfo().getHeadPath(), size, size, false, true))
                    .error(R.drawable.bg_default_circle)
                    .placeholder(R.drawable.bg_default_circle)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(mIvPresonal);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_LOGIN && resultCode == RESPONSE_LOGIN_SUCCESS) {
            setmAdapter();
        } else if (requestCode == REQ_SELECT_TAG && resultCode == RESULT_OK) {
            setmAdapter();
        }
    }

    private void setmAdapter() {
        if (TextUtils.isEmpty(PreferenceUtils.getUUid())) {
            mIvPresonal.setImageResource(R.drawable.bg_default_circle);
        } else {
            int size = (int) getResources().getDimension(R.dimen.x64);
            Glide.with(this)
                    .load(StringUtils.getUrl(this, PreferenceUtils.getAuthorInfo().getHeadPath(), size, size, false, true))
                    .error(R.drawable.bg_default_circle)
                    .placeholder(R.drawable.bg_default_circle)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(mIvPresonal);
        }
        if (!mainV3Fragment.isHidden()) {
            mainV3Fragment = NewFollowMainV3Fragment.newInstance("ground");
            feedFollowV4Fragment = FeedFollowV4Fragment.newInstance("all", "全部", false);
//            newCommunityFragment = NewCommunityFragment.newInstance();
            clubListFragment = ClubListFragment.newInstance();
            FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
            mFragmentTransaction.add(R.id.fl_container, mainV3Fragment);
            mFragmentTransaction.add(R.id.fl_container, feedFollowV4Fragment);
            mFragmentTransaction.add(R.id.fl_container, clubListFragment);
            mFragmentTransaction.show(mainV3Fragment).hide(feedFollowV4Fragment).hide(clubListFragment);
            mFragmentTransaction.commit();
            mIvClubSquare.setSelected(true);
            mTvClubSquare.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.main_cyan));
            mIvClubClub.setSelected(false);
            mTvClubClub.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.gray_d7d7d7));
            mIvClubFollow.setSelected(false);
            mTvClubFollow.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.gray_d7d7d7));
            mIvClubInform.setSelected(false);
            mTvClubInform.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.gray_d7d7d7));
            mainV3Fragment.onSmoothScrollBy();
        } else if (!clubListFragment.isHidden()) {
            mainV3Fragment = NewFollowMainV3Fragment.newInstance("ground");
            feedFollowV4Fragment = FeedFollowV4Fragment.newInstance("all", "全部", false);
//            newCommunityFragment = NewCommunityFragment.newInstance();
            clubListFragment = ClubListFragment.newInstance();
            FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
            mFragmentTransaction.add(R.id.fl_container, mainV3Fragment);
            mFragmentTransaction.add(R.id.fl_container, feedFollowV4Fragment);
            mFragmentTransaction.add(R.id.fl_container, clubListFragment);
            mFragmentTransaction.show(clubListFragment).hide(mainV3Fragment).hide(feedFollowV4Fragment);
            mFragmentTransaction.commit();
            mIvClubSquare.setSelected(false);
            mTvClubSquare.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.gray_d7d7d7));
            mIvClubClub.setSelected(true);
            mTvClubClub.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.main_cyan));
            mIvClubFollow.setSelected(false);
            mTvClubFollow.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.gray_d7d7d7));
            mIvClubInform.setSelected(false);
            mTvClubInform.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.gray_d7d7d7));
        } else if (!feedFollowV4Fragment.isHidden()) {
            mainV3Fragment = NewFollowMainV3Fragment.newInstance("ground");
            feedFollowV4Fragment = FeedFollowV4Fragment.newInstance("all", "全部", false);
//            newCommunityFragment = NewCommunityFragment.newInstance();
            clubListFragment = ClubListFragment.newInstance();
            FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
            mFragmentTransaction.show(feedFollowV4Fragment).hide(mainV3Fragment).hide(clubListFragment);
            mFragmentTransaction.commit();
            mIvClubSquare.setSelected(false);
            mTvClubSquare.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.gray_d7d7d7));
            mIvClubClub.setSelected(false);
            mTvClubClub.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.gray_d7d7d7));
            mIvClubFollow.setSelected(true);
            mTvClubFollow.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.main_cyan));
            mIvClubInform.setSelected(false);
            mTvClubInform.setTextColor(ContextCompat.getColor(FeedV3Activity.this, R.color.gray_d7d7d7));
            feedFollowV4Fragment.onSmoothScrollBy();
        }
    }

    @Override
    protected void onDestroy() {
        MoeMoeApplication.getInstance().activities.remove(FeedV3Activity.class.getName());
        EventBus.getDefault().post(new MapEevent());
        stayEvent("社团");
        EventBus.getDefault().unregister(this);
        if (mAdapter != null) {
            mAdapter.release();
        }
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void docEvent(DocEvent event) {
//        if (event != null && event.getPosition() > 0) {
//            mPageIndicator.onShowMsg(0, event.getPosition());
//        } else {
//            mPageIndicator.hideMsg(0);
//        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(ScrollMessage message) {
        if (message != null) {
            int scrollY = message.getScrollY();

            float ratio = scrollY < maxScroll ? (maxScroll - scrollY) / maxScroll : 0;
            float alpha = oldAlpha * ratio;
            mTitle.setAlpha(alpha);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTitle.getLayoutParams();
//            if (((int) (-titleHeight * (1 - ratio)) <= (-titleHeight))) {
//                params.topMargin = -titleHeight;
//            } else {
            params.topMargin = (int) (-titleHeight * (1 - ratio));
//            }
            mTitle.setLayoutParams(params);
            mTitle.invalidate();
        }

    }

    @Override
    public void onCountChanged(int i) {
        PreferenceUtils.setRCDotNum(this, i);
        if (mTvCLubDot != null) {
            int dotNum = PreferenceUtils.getGroupDotNum(this) + i + PreferenceUtils.getJuQIngDotNum(this);
            if (dotNum > 0) {
                mTvCLubDot.setVisibility(View.VISIBLE);
                if (dotNum > 99) dotNum = 99;
                mTvCLubDot.setText(String.valueOf(dotNum));
            } else {
                mTvCLubDot.setVisibility(View.GONE);
            }
        }
    }
}
