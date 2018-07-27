package com.moemoe.lalala.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.moemoe.lalala.app.AppSetting;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.event.ScrollMessage;
import com.moemoe.lalala.event.StageLineEvent;
import com.moemoe.lalala.greendao.gen.DeskmateEntilsDao;
import com.moemoe.lalala.model.entity.AuthorInfo;
import com.moemoe.lalala.model.entity.DeskmateEntils;
import com.moemoe.lalala.model.entity.ReceiverInfo;
import com.moemoe.lalala.model.entity.StageLineEntity;
import com.moemoe.lalala.model.entity.StageLineOptionsEntity;
import com.moemoe.lalala.model.entity.TagLikeEntity;
import com.moemoe.lalala.model.entity.TagSendEntity;
import com.moemoe.lalala.utils.DialogUtils;
import com.moemoe.lalala.utils.DocEvent;
import com.moemoe.lalala.utils.GreenDaoManager;
import com.moemoe.lalala.utils.IntentUtils;
import com.moemoe.lalala.utils.MapEevent;
import com.moemoe.lalala.utils.NetworkUtils;
import com.moemoe.lalala.utils.NewUtils;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.SideCharacterUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.TabFragmentPagerAdapter;
import com.moemoe.lalala.view.fragment.BaseFragment;
import com.moemoe.lalala.view.fragment.ClubListFragment;
import com.moemoe.lalala.view.fragment.FeedDynamicV3Fragment;
import com.moemoe.lalala.view.fragment.FeedFollowV4Fragment;
import com.moemoe.lalala.view.fragment.NewCommunityFragment;
import com.moemoe.lalala.view.fragment.NewFollowMainV3Fragment;
import com.moemoe.lalala.view.widget.camera.IFaceDetector;
import com.moemoe.lalala.view.widget.map.utils.LogUtils;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.moemoe.lalala.R.dimen.x428;
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
    @BindView(R.id.rl_ac_root)
    RelativeLayout mRlAcRoot;
    @BindView(R.id.ll_root)
    LinearLayout mLlRoot;

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
    private View sidaMenu;
    private ImageView sidaCharacter;
    private RelativeLayout sidaMenuViewById;
    private RelativeLayout sidaMenuFrist;
    private ImageView sidaMenuIvPersonal;
    private ImageView sidaMenuIvBag;
    private ImageView sidaMenuIvShopping;
    private ImageView sidaMenuIvSignRoot;
    private ImageView sidaMenuIvPhoneMenu;
    private ImageView sidaMenuIvMsg;
    private View sideLine;
    private RelativeLayout sideLineFrist;
    private TextView sideLineContent;
    private LinearLayout sideLineSelect;
    private TextView sideLineTvLeft;
    private TextView sideLineTvCansl;
    private DeskmateEntils deskmateEntity;
    private StageLineEntity stageLineEntity;

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

    //    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
////        if (MoeMoeApplication.getInstance().GoneDiaLog()) {
////            return true;
////        }
////        if (MoeMoeApplication.getInstance().isMenu()) {
////            MoeMoeApplication.getInstance().GoneMenu();
////            return true;
////        }
//        if (sidaMenu != null && sidaMenu.getVisibility() == View.VISIBLE) {
//            sidaMenu.setVisibility(View.GONE);
//            return true;
//        }
//        if (sideLine != null && sideLine.getVisibility() == View.VISIBLE) {
//            sideLine.setVisibility(View.GONE);
//            return true;
//        }
//        return super.dispatchTouchEvent(ev);
//    }
    public View getSideMenu() {
        return sidaMenu;
    }

    public void getGoneSideMenu() {
        sidaMenu.setVisibility(View.GONE);
    }

    public View getSideLine() {
        return sideLine;
    }

    public void getGoneSideLine() {
        sideLine.setVisibility(View.GONE);
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
                if (sidaMenu != null && sidaMenu.getVisibility() == View.VISIBLE) {
                    sidaMenu.setVisibility(View.GONE);
                    return;
                }
                if (sideLine != null && sideLine.getVisibility() == View.VISIBLE) {
                    sideLine.setVisibility(View.GONE);
                    return;
                }
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
    public void GoneSidaMenuOrLine() {
        sidaMenu.setVisibility(View.GONE);
        sideLine.setVisibility(View.GONE);
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
        sidaCharacter = SideCharacterUtils.addFloatDragView(this, mRlAcRoot, new SideCHaracterOnClick());


        sidaMenu = LayoutInflater.from(this).inflate(R.layout.float_renwu_layout, null);
        sidaMenu.setLayoutParams(new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.x428)
                , (int) getResources().getDimension(R.dimen.y320)));
        sidaMenuViewById = (RelativeLayout) sidaMenu.findViewById(R.id.rl_renwu);
        sidaMenuFrist = (RelativeLayout) sidaMenu.findViewById(R.id.ll_frist);
        sidaMenuIvPersonal = (ImageView) sidaMenu.findViewById(R.id.iv_personal);
        sidaMenuIvBag = (ImageView) sidaMenu.findViewById(R.id.iv_bag);
        sidaMenuIvShopping = (ImageView) sidaMenu.findViewById(R.id.iv_shopping);
        sidaMenuIvSignRoot = (ImageView) sidaMenu.findViewById(R.id.iv_sign_root);
        sidaMenuIvPhoneMenu = (ImageView) sidaMenu.findViewById(R.id.iv_phone_menu);
        sidaMenuIvMsg = (ImageView) sidaMenu.findViewById(R.id.iv_msg);
        sidaMenu.setVisibility(View.GONE);
        mRlAcRoot.addView(sidaMenu);

        sidaMenuIvPersonal.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (NetworkUtils.checkNetworkAndShowError(FeedV3Activity.this) && DialogUtils.checkLoginAndShowDlg(FeedV3Activity.this)) {
                    //埋点统计：手机个人中心
                    Intent i1 = new Intent(FeedV3Activity.this, PersonalV2Activity.class);
                    i1.putExtra(UUID, PreferenceUtils.getUUid());
                    startActivity(i1);
                }
                sidaMenu.setVisibility(View.GONE);
            }
        });
        sidaMenuIvBag.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (NetworkUtils.checkNetworkAndShowError(FeedV3Activity.this) && DialogUtils.checkLoginAndShowDlg(FeedV3Activity.this)) {
                    if (PreferenceUtils.getAuthorInfo().isOpenBag()) {
                        Intent i4 = new Intent(FeedV3Activity.this, NewBagV5Activity.class);
                        i4.putExtra("uuid", PreferenceUtils.getUUid());
                        startActivity(i4);
                    } else {
                        Intent i4 = new Intent(FeedV3Activity.this, BagOpenActivity.class);
                        startActivity(i4);
                    }
                }
                sidaMenu.setVisibility(View.GONE);
            }
        });
        sidaMenuIvShopping.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Intent i7 = new Intent(FeedV3Activity.this, CoinShopActivity.class);
                startActivity(i7);
                sidaMenu.setVisibility(View.GONE);
            }
        });
        sidaMenuIvSignRoot.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (DialogUtils.checkLoginAndShowDlg(FeedV3Activity.this)) {
                    DailyTaskActivity.startActivity(FeedV3Activity.this);
                }
                sidaMenu.setVisibility(View.GONE);
            }
        });
        sidaMenuIvPhoneMenu.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (NetworkUtils.checkNetworkAndShowError(FeedV3Activity.this) && DialogUtils.checkLoginAndShowDlg(FeedV3Activity.this)) {
                    //埋点统计：通讯录
                    startActivity(new Intent(FeedV3Activity.this, PhoneMenuV3Activity.class));
                }
                sidaMenu.setVisibility(View.GONE);
            }
        });
        sidaMenuIvMsg.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (NetworkUtils.checkNetworkAndShowError(FeedV3Activity.this) && DialogUtils.checkLoginAndShowDlg(FeedV3Activity.this)) {
                    //埋点统计：手机聊天
                    NoticeActivity.startActivity(FeedV3Activity.this, 1);
                }
                sidaMenu.setVisibility(View.GONE);
            }
        });


        sideLine = LayoutInflater.from(this).inflate(R.layout.float_dialog_layout, null);
        sideLine.setLayoutParams(new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.x428)
                , (int) getResources().getDimension(R.dimen.y320)));
        sideLineFrist = (RelativeLayout) sideLine.findViewById(R.id.rl_dialog);
        sideLineContent = (TextView) sideLine.findViewById(R.id.tv_content);
        sideLineSelect = (LinearLayout) sideLine.findViewById(R.id.rl_select);
        sideLineTvLeft = (TextView) sideLine.findViewById(R.id.iv_left);
        sideLineTvCansl = (TextView) sideLine.findViewById(R.id.iv_right);
        sideLine.setVisibility(View.GONE);
        mRlAcRoot.addView(sideLine);

        sideLineTvLeft.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (sideLine != null && stageLineEntity != null) {
                    stageLineEntity = getStageLineEntity(stageLineEntity, sideLineTvLeft.getText().toString());
                    if (stageLineEntity == null) {
                        sideLine.setVisibility(View.GONE);
                    } else {
                        setSidaLineData(stageLineEntity);
                    }
                }
            }
        });
        sideLineTvCansl.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (sideLine != null && stageLineEntity != null) {
                    stageLineEntity = getStageLineEntity(stageLineEntity, sideLineTvCansl.getText().toString());
                    if (stageLineEntity == null) {
                        sideLine.setVisibility(View.GONE);
                    } else {
                        setSidaLineData(stageLineEntity);
                    }
                }
            }
        });

    }

    /**
     * 扒边小人的点击事件
     */
    public class SideCHaracterOnClick extends NoDoubleClickListener {

        @Override
        public void onNoDoubleClick(View view) {

            if (sideLine != null && sideLine.getVisibility() == View.VISIBLE) {
                sideLine.setVisibility(View.GONE);
                return;
            }

            if (sidaMenu != null && sidaMenu.getVisibility() == View.GONE) {
                sidaMenu.setVisibility(View.VISIBLE);
                Log.e("view.getLeft()--", view.getLeft() + "");
                Log.e("view.getTop()--", view.getTop() + "");
                Log.e("view.getRight()--", view.getRight() + "");
                Log.e("view.getBottom()--", view.getBottom() + "");
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) sidaMenu.getLayoutParams();
                if (view.getLeft() <= 10) {//左方
                    if (view.getTop() > getResources().getDisplayMetrics().heightPixels / 2) {
                        sidaMenuViewById.setBackgroundResource(R.drawable.bg_classmate_menu_bottom_left);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) sidaMenuFrist.getLayoutParams();
                        layoutParams.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.y48));
                        sidaMenuFrist.setLayoutParams(layoutParams);
                        params.setMargins((int) (view.getLeft() + getResources().getDimension(R.dimen.x24)),
                                view.getTop() - (view.getHeight() / 2), 0, 0);
                    } else {
                        sidaMenuViewById.setBackgroundResource(R.drawable.bg_classmate_menu_top_left);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) sidaMenuFrist.getLayoutParams();
                        layoutParams.setMargins(0, (int) getResources().getDimension(R.dimen.y48), 0, 0);
                        sidaMenuFrist.setLayoutParams(layoutParams);
                        params.setMargins((int) (view.getLeft() + getResources().getDimension(R.dimen.x24)),
                                (int) (view.getTop() + (view.getHeight() / 2) - getResources().getDimension(R.dimen.status_bar_height)), 0, 0);
                    }
                } else if (view.getLeft() + view.getWidth() == getResources().getDisplayMetrics().widthPixels) {//右方
                    if (view.getTop() > getResources().getDisplayMetrics().heightPixels / 2) {
                        sidaMenuViewById.setBackgroundResource(R.drawable.bg_classmate_menu_bottom_right);
                        sidaMenuViewById.setLayoutParams(new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.x428)
                                , (int) getResources().getDimension(R.dimen.y320)));
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) sidaMenuFrist.getLayoutParams();
                        layoutParams.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.y48));
                        sidaMenuFrist.setLayoutParams(layoutParams);
                        params.setMargins((int) (view.getLeft() - sidaMenu.getWidth() + view.getWidth() - getResources().getDimension(R.dimen.x24)),
                                view.getTop() - (view.getHeight() / 2), 0, 0);
                    } else {
                        sidaMenuViewById.setBackgroundResource(R.drawable.bg_classmate_menu_top_right);
                        sidaMenuViewById.setLayoutParams(new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.x428)
                                , (int) getResources().getDimension(R.dimen.y320)));
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) sidaMenuFrist.getLayoutParams();
                        layoutParams.setMargins(0, (int) getResources().getDimension(R.dimen.y48), 0, 0);
                        sidaMenuFrist.setLayoutParams(layoutParams);
                        params.setMargins((int) (view.getLeft() - (int) getResources().getDimension(R.dimen.x428) + view.getWidth() - getResources().getDimension(R.dimen.x24)),
                                (int) (view.getTop() + (view.getHeight() / 2) - getResources().getDimension(R.dimen.status_bar_height)), 0, 0);
                    }
                } else if (view.getTop() <= getResources().getDimension(R.dimen.status_bar_height)) {//上方
                    if (view.getLeft() > getResources().getDisplayMetrics().widthPixels / 2) {
                        sidaMenuViewById.setBackgroundResource(R.drawable.bg_classmate_menu_top_right);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) sidaMenuFrist.getLayoutParams();
                        layoutParams.setMargins(0, (int) getResources().getDimension(R.dimen.y48), 0, 0);
                        sidaMenuFrist.setLayoutParams(layoutParams);
                        params.setMargins((int) (view.getLeft() - sidaMenu.getWidth() + view.getWidth() - getResources().getDimension(R.dimen.x24)),
                                view.getTop() + (view.getHeight() / 2), 0, 0);
                    } else {
                        sidaMenuViewById.setBackgroundResource(R.drawable.bg_classmate_menu_top_left);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) sidaMenuFrist.getLayoutParams();
                        layoutParams.setMargins(0, (int) getResources().getDimension(R.dimen.y48), 0, 0);
                        sidaMenuFrist.setLayoutParams(layoutParams);
                        params.setMargins((int) (view.getLeft() - getResources().getDimension(R.dimen.x24)),
                                view.getTop() + (view.getHeight() / 2), 0, 0);
                    }

                } else if (view.getTop() + view.getHeight()  >=(getResources().getDisplayMetrics().heightPixels-getResources().getDimension(R.dimen.y20))) {//下方
                    if (view.getLeft() > getResources().getDisplayMetrics().widthPixels / 2) {
                        sidaMenuViewById.setBackgroundResource(R.drawable.bg_classmate_menu_bottom_right);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) sidaMenuFrist.getLayoutParams();
                        layoutParams.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.y48));
                        sidaMenuFrist.setLayoutParams(layoutParams);
                        params.setMargins((int) (view.getLeft() - sidaMenu.getWidth() + view.getWidth() - getResources().getDimension(R.dimen.x24)),
                                (view.getTop() + (int) getResources().getDimension(R.dimen.status_bar_height)) - (view.getHeight() / 2), 0, 0);
                    } else {
                        sidaMenuViewById.setBackgroundResource(R.drawable.bg_classmate_menu_bottom_left);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) sidaMenuFrist.getLayoutParams();
                        layoutParams.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.y48));
                        sidaMenuFrist.setLayoutParams(layoutParams);
                        params.setMargins((int) (view.getLeft() - getResources().getDimension(R.dimen.x24)),
                                (view.getTop() + (int) getResources().getDimension(R.dimen.status_bar_height)) - (view.getHeight() / 2), 0, 0);
                    }
                }
                sidaMenu.setLayoutParams(params);
            } else {
                sidaMenu.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sidaMenu != null && sidaMenu.getVisibility() == View.VISIBLE) {
            sidaMenu.setVisibility(View.GONE);
            return;
        }
        if (sideLine != null && sideLine.getVisibility() == View.VISIBLE) {
            sideLine.setVisibility(View.GONE);
            return;
        }
        pauseTime();
    }


    @Override
    protected void onResume() {
        super.onResume();
        startTime();
        if (!PreferenceUtils.isLogin()) {
            finish();
        }
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void stageLineEvent(StageLineEvent event) {
        if (event != null && NewUtils.isActivityTop(FeedV3Activity.class, this)) {
            goGreenDao();
            String stageLine = PreferenceUtils.getStageLine(FeedV3Activity.this);
            Gson gson = new Gson();
            StageLineEntity entity = gson.fromJson(stageLine, StageLineEntity.class);
            if (deskmateEntity.getId().equals(entity.getRoleId()) && stageLine != null) {
                VisibleSidaLine();
            }
        }
    }

    public void VisibleSidaLine() {
        if (sidaMenu != null && sidaMenu.getVisibility() == View.GONE) {
            return;
        }
        if (sideLine.getVisibility() == View.GONE) {
            sideLine.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) sideLine.getLayoutParams();
            if (sidaCharacter.getLeft() <=10) {//左边
                if (sidaCharacter.getTop() > getResources().getDisplayMetrics().heightPixels / 2) {
                    sideLineFrist.setBackgroundResource(R.drawable.bg_classmate_talkbg_bottom_left);
                    params.setMargins((int) (sidaCharacter.getLeft() + getResources().getDimension(R.dimen.x24)),
                            sidaCharacter.getTop() - (sidaCharacter.getHeight() / 2),
                            0, 0);
                } else {
                    sideLineFrist.setBackgroundResource(R.drawable.bg_classmate_talkbg_top_left);
                    params.setMargins((int) (sidaCharacter.getLeft() + getResources().getDimension(R.dimen.x24)),
                            (int) (sidaCharacter.getTop() + (sidaCharacter.getHeight() / 2) - getResources().getDimension(R.dimen.status_bar_height)),
                            0, 0);
                }
            } else if (sidaCharacter.getLeft() + sidaCharacter.getWidth() == getResources().getDisplayMetrics().widthPixels) {//右边
                if (sidaCharacter.getTop() > getResources().getDisplayMetrics().heightPixels / 2) {
                    sideLineFrist.setBackgroundResource(R.drawable.bg_classmate_talkbg_bottom_right);
                    params.setMargins((int) (sidaCharacter.getLeft() - sidaMenu.getWidth() + sidaCharacter.getWidth() - getResources().getDimension(R.dimen.x24)),
                            sidaCharacter.getTop() - (sidaCharacter.getHeight() / 2),
                            0, 0);
                } else {
                    sideLineFrist.setBackgroundResource(R.drawable.bg_classmate_talkbg_top_right);
                    params.setMargins((int) (sidaCharacter.getLeft() - (int) getResources().getDimension(R.dimen.x428) + sidaCharacter.getWidth() - getResources().getDimension(R.dimen.x24)),
                            (int) (sidaCharacter.getTop() + (sidaCharacter.getHeight() / 2) - getResources().getDimension(R.dimen.status_bar_height)),
                            0, 0);

                }
            } else if (sidaCharacter.getTop() <= getResources().getDimension(R.dimen.status_bar_height)) {//上方
                if (sidaCharacter.getLeft() > getResources().getDisplayMetrics().widthPixels / 2) {
                    sideLineFrist.setBackgroundResource(R.drawable.bg_classmate_talkbg_top_right);
                    params.setMargins((int) (sidaCharacter.getLeft() - sidaMenu.getWidth() + sidaCharacter.getWidth() - getResources().getDimension(R.dimen.x24)),
                            sidaCharacter.getTop() + (sidaCharacter.getHeight() / 2),
                            0, 0);
                } else {
                    sideLineFrist.setBackgroundResource(R.drawable.bg_classmate_talkbg_top_left);
                    params.setMargins((int) (sidaCharacter.getLeft() - getResources().getDimension(R.dimen.x24)),
                            (int) (sidaCharacter.getTop() + (sidaCharacter.getHeight() / 2)),
                            0, 0);

                }
            } else if (sidaCharacter.getTop() + sidaCharacter.getHeight()  >=( getResources().getDisplayMetrics().heightPixels-getResources().getDimension(R.dimen.y20))) {//下方
                if (sidaCharacter.getLeft() > getResources().getDisplayMetrics().widthPixels / 2) {
                    sideLineFrist.setBackgroundResource(R.drawable.bg_classmate_talkbg_bottom_right);
                    params.setMargins((int) (sidaCharacter.getLeft() - sidaMenu.getWidth() + sidaCharacter.getWidth() - getResources().getDimension(R.dimen.x24)),
                            (sidaCharacter.getTop() + (int) getResources().getDimension(R.dimen.status_bar_height)) - (sidaCharacter.getHeight() / 2), 0, 0);
                } else {
                    sideLineFrist.setBackgroundResource(R.drawable.bg_classmate_talkbg_bottom_left);
                    params.setMargins((int) (sidaCharacter.getLeft() - getResources().getDimension(R.dimen.x24)),
                            (sidaCharacter.getTop() + (int) getResources().getDimension(R.dimen.status_bar_height)) - (sidaCharacter.getHeight() / 2), 0, 0);
                }
            }

            String stageLine = PreferenceUtils.getStageLine(this);
            Gson gson = new Gson();
            stageLineEntity = gson.fromJson(stageLine, StageLineEntity.class);
            setSidaLineData(stageLineEntity);
        } else {
            // TODO: 2018/7/12  考虑正在执行台词又来一条台词的情况   后面那条进行存储  在次播放
        }
    }

    public void setSidaLineData(StageLineEntity entity) {
        if (sideLine != null) {
            if (entity.getId() != null) {
                if (!TextUtils.isEmpty(entity.getSchema())) {
                    String temp = entity.getSchema();
                    if (temp.contains("map_event_1.0") || temp.contains("game_1.0")) {
                        if (!DialogUtils.checkLoginAndShowDlg(this)) {
                            return;
                        }
                    }
                    if (temp.contains("http://s.moemoe.la/game/devil/index.html")) {
                        AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
                        temp += "?user_id=" + authorInfo.getUserId() + "&full_screen";
                    }

                    if (temp.contains("http://kiratetris.leanapp.cn/tab001/index.html")) {
                        AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
                        temp += "?id=" + authorInfo.getUserId() + "&name=" + authorInfo.getUserName();
                    }
                    if (temp.contains("http://prize.moemoe.la:8000/mt")) {
                        AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
                        temp += "?user_id=" + authorInfo.getUserId() + "&nickname=" + authorInfo.getUserName();
                    }
                    if (temp.contains("http://prize.moemoe.la:8000/netaopera/chap")) {
                        AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
                        temp += "?pass=" + PreferenceUtils.getPassEvent(this) + "&user_id=" + authorInfo.getUserId();
                    }
                    if (temp.contains("http://neta.facehub.me/")) {
                        AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
                        temp += "?open_id=" + authorInfo.getUserId() + "&nickname=" + authorInfo.getUserName() + "&pay_way=alipay,wx,qq" + "&full_screen";
                    }
                    if (temp.contains("fanxiao/final.html")) {
                        temp += "?token=" + PreferenceUtils.getToken()
                                + "&full_screen";
                    }
                    if (temp.contains("fanxiao/paihang.html")) {
                        temp += "?token=" + PreferenceUtils.getToken();
                    }
                    if (temp.contains("game_1.0")) {
                        temp += "&token=" + PreferenceUtils.getToken() + "&version=" + AppSetting.VERSION_CODE + "&userId=" + PreferenceUtils.getUUid() + "&channel=" + AppSetting.CHANNEL;
                    }
                    Uri uri = Uri.parse(temp);
                    IntentUtils.toActivityFromUri(this, uri, sideLine);

                } else {
                    sideLineContent.setText(entity.getContent() + "");
                }
                if (entity.getDialogType() != null && entity.getDialogType().equals("dialog_option")) {
                    sideLineSelect.setVisibility(View.VISIBLE);
                    for (int i = 0; i < entity.getOptions().size(); i++) {
                        if (i == 0) {
                            sideLineTvLeft.setText(entity.getOptions().get(i).getOption() + "");
                        } else {
                            if (entity.getOptions().get(i) == null) {
                                sideLineTvCansl.setVisibility(View.GONE);
                            } else {
                                sideLineTvCansl.setText(entity.getOptions().get(i).getOption() + "");
                            }
                        }
                    }
                } else {
                    sideLineSelect.setVisibility(View.GONE);
                }
            } else {
                sideLine.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 解析台词层级
     *
     * @param mData
     * @return
     */
    private StageLineEntity getStageLineEntity(StageLineEntity mData, String isSelect) {
        List<StageLineOptionsEntity> options = mData.getOptions();
        String LeftId = null;
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).getOption().equals(isSelect)) {
                LeftId = options.get(i).getId();
            }
        }
        List<StageLineEntity> children = mData.getChildren();
        if (children == null) {
            return null;
        } else {
            StageLineEntity stageLineEntity = new StageLineEntity();
            for (int i = 0; i < children.size(); i++) {
                if (children.get(i).getParentOptionId().equals(LeftId)) {
                    stageLineEntity = children.get(i);
                }
            }
            return stageLineEntity;
        }
    }

    /**
     * 数据压制
     */
    public int goGreenDao() {
        ArrayList<DeskmateEntils> entilsDao = (ArrayList<DeskmateEntils>) GreenDaoManager.getInstance().getSession().getDeskmateEntilsDao()
                .queryBuilder()
                .where(DeskmateEntilsDao.Properties.Type.eq("HousUser"))
                .list();
        if (entilsDao != null && entilsDao.size() > 0) {
            for (DeskmateEntils entils : entilsDao) {
                deskmateEntity = entils;
            }
        }
        return entilsDao.size() > 0 ? entilsDao.size() : 0;
    }
}
