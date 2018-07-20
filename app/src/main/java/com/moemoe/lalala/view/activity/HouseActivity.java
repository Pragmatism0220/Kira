package com.moemoe.lalala.view.activity;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.AppSetting;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityHouseBinding;
import com.moemoe.lalala.di.components.DaggerHouseComponent;
import com.moemoe.lalala.di.modules.HouseModule;
import com.moemoe.lalala.event.HouseLikeEvent;
import com.moemoe.lalala.event.StageLineEvent;
import com.moemoe.lalala.galgame.FileManager;
import com.moemoe.lalala.galgame.SoundManager;
import com.moemoe.lalala.greendao.gen.DeskmateEntilsDao;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.AuthorInfo;
import com.moemoe.lalala.model.entity.DeskmateEntils;
import com.moemoe.lalala.model.entity.HouseDbEntity;
import com.moemoe.lalala.model.entity.HouseLikeEntity;
import com.moemoe.lalala.model.entity.MapEntity;
import com.moemoe.lalala.model.entity.MapMarkContainer;
import com.moemoe.lalala.model.entity.OrderEntity;
import com.moemoe.lalala.model.entity.PayReqEntity;
import com.moemoe.lalala.model.entity.PayResEntity;
import com.moemoe.lalala.model.entity.PowerEntity;
import com.moemoe.lalala.model.entity.PropUseEntity;
import com.moemoe.lalala.model.entity.RubbishEntity;
import com.moemoe.lalala.model.entity.RubblishBody;
import com.moemoe.lalala.model.entity.SaveVisitorEntity;
import com.moemoe.lalala.model.entity.SearchListEntity;
import com.moemoe.lalala.model.entity.SearchNewListEntity;
import com.moemoe.lalala.model.entity.StageLineEntity;
import com.moemoe.lalala.model.entity.StageLineOptionsEntity;
import com.moemoe.lalala.model.entity.VisitorsEntity;
import com.moemoe.lalala.model.entity.upDateEntity;
import com.moemoe.lalala.presenter.DormitoryContract;
import com.moemoe.lalala.presenter.DormitoryPresenter;
import com.moemoe.lalala.utils.AlertDialogUtil;
import com.moemoe.lalala.utils.DialogUtils;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.FileUtil;
import com.moemoe.lalala.utils.GreenDaoManager;
import com.moemoe.lalala.utils.IntentUtils;
import com.moemoe.lalala.utils.IpAdressUtils;
import com.moemoe.lalala.utils.MapUtil;
import com.moemoe.lalala.utils.NetworkUtils;
import com.moemoe.lalala.utils.NewUtils;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.ShareUtils;
import com.moemoe.lalala.utils.SideCharacterUtils;
import com.moemoe.lalala.utils.StorageUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.widget.netamenu.BottomMenuFragment;
import com.moemoe.lalala.view.widget.netamenu.MenuItem;
import com.pingplusplus.ui.PaymentHandler;
import com.pingplusplus.ui.PingppUI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.view.View.DRAWING_CACHE_QUALITY_AUTO;
import static android.view.View.DRAWING_CACHE_QUALITY_HIGH;
import static android.view.View.DRAWING_CACHE_QUALITY_LOW;

public class HouseActivity extends BaseActivity implements DormitoryContract.View {


    private boolean mIsOut = false;
    private ActivityHouseBinding binding;
    static private Activity instance;
    private String mSchema;
    public int nowpower;

    @Inject
    DormitoryPresenter mPresenter;
    private MapMarkContainer mContainer;
    private Disposable initDisposable;
    private Disposable resolvDisposable;
    private int type;
    private RelativeLayout mRlRoleRoot;
    private TextView mTvRoleNum;
    private TextView mTvRoleName;
    private RelativeLayout mRlRoleJuQing;
    private ImageView mIvCover;
    private ImageView mSeven;
    private TextView mTvRewardName;
    private RelativeLayout mRleSelect;
    private TextView mTvLeft;
    private TextView mTvRight;
    private ImageView mIvGongXI;
    private RubbishEntity mRubbishEntity;
    private TextView mTvJuQing;
    private TextView mTvContent;
    private TextView mTvChuWu;
    private TextView mTvCnanle;
    private int count;
    private BottomMenuFragment bottomMenuFragment;
    private String saveBitmap;
    private Bitmap bitmap;
    private TextView mTvUserName;
    private View sidaMenu;
    private RelativeLayout sidaMenuViewById;
    private RelativeLayout sidaMenuFrist;
    private ImageView sidaMenuIvPersonal;
    private ImageView sidaMenuIvBag;
    private ImageView sidaMenuIvShopping;
    private ImageView sidaMenuIvSignRoot;
    private ImageView sidaMenuIvPhoneMenu;
    private ImageView sidaMenuIvMsg;
    private ImageView mVisitor;
    private ImageView mRoleNews;
    private ImageView mDrameNews;
    private ImageView mStorage;
    private ImageView mMessageNews;
    private View sideLine;
    private TextView sideLineContent;
    private LinearLayout sideLineSelect;
    private TextView sideLineTvLeft;
    private TextView sideLineTvCansl;
    private DeskmateEntils deskmateEntity;
    private ImageView sidaCharacter;
    private RelativeLayout sideLineFrist;
    private StageLineEntity stageLineEntity;
    private List<SearchNewListEntity> news;
    private OrderEntity entitPay;
    private BottomMenuFragment bottomFragment;

    @Override
    protected void initComponent() {
        if (!PreferenceUtils.isLogin()) {
            finish();
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_house);
        binding.setPresenter(new Presenter());
        mRlRoleRoot = findViewById(R.id.rl_role_root);
        mTvRoleNum = findViewById(R.id.tv_role_num);
        mTvRoleName = findViewById(R.id.tv_role_name);
        mRlRoleJuQing = findViewById(R.id.rl_role_juqing);
        mIvCover = findViewById(R.id.iv_cover_next);
        mSeven = findViewById(R.id.iv_seven);
        mRleSelect = findViewById(R.id.rl_select);
        mTvLeft = findViewById(R.id.tv_left);
        mTvRight = findViewById(R.id.tv_right);
        mIvGongXI = findViewById(R.id.iv_gongxi);
        mTvJuQing = findViewById(R.id.tv_juqing);
        mTvContent = findViewById(R.id.tv_content_gongxi);
        mTvChuWu = findViewById(R.id.tv_chuwu);
        mTvCnanle = findViewById(R.id.tv_canle);
        mTvUserName = findViewById(R.id.tv_user_name);
        mVisitor = findViewById(R.id.visitor_news);
        mRoleNews = findViewById(R.id.role_news);
        mDrameNews = findViewById(R.id.drama_news);
        mStorage = findViewById(R.id.storage_news);
        mMessageNews = findViewById(R.id.message_news);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        AppSetting.isRunning = true;
        Intent mIntent = getIntent();
        if (mIntent != null) {
            mSchema = mIntent.getStringExtra("schema");
        }
        DaggerHouseComponent.builder()
                .houseModule(new HouseModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);

        mContainer = new MapMarkContainer();
        initMap();
        SoundManager.init(this);
        FileManager.init(this);
        initPopupMenus();
        mPresenter.loadPower();
        initPayMenu();
        SearchListEntity entity = new SearchListEntity();
        entity.setFunNames(new String[]{"visitor", "user_role", "user_branch_story", "user_tool", "user_furniture", "leave_message"});
        mPresenter.searchHouseNew(entity);
        EventBus.getDefault().register(this);
        if (PreferenceUtils.isActivityFirstLaunch(this, "house")) {
            Intent intent = new Intent(this, MengXinActivity.class);
            intent.putExtra("type", "house");
            ArrayList<String> res = new ArrayList<>();
            res.add("home1.jpg");
            res.add("home2.jpg");
            res.add("home3.jpg");
            res.add("home4.jpg");
            res.add("home5.jpg");
            res.add("home6.jpg");
            res.add("home7.jpg");
            res.add("home8.jpg");
            res.add("home9.jpg");
            res.add("home10.jpg");
            res.add("home11.jpg");
            res.add("home12.jpg");
            res.add("home13.jpg");
            res.add("home14.jpg");
            intent.putExtra("gui", res);
            startActivity(intent);
        }
    }

    public void initMap() {
        mPresenter.addMapMark(HouseActivity.this, mContainer, binding.map, "house");
    }

    //    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (MoeMoeApplication.getInstance().GoneDiaLog()) {
//            return true;
//        }
//        if (MoeMoeApplication.getInstance().isMenu()) {
//            MoeMoeApplication.getInstance().GoneMenu();
//            return true;
//        }
//        return super.dispatchTouchEvent(ev);
//    }
    public void GoneSidaMenuOrLine() {
        sidaMenu.setVisibility(View.GONE);
        sideLine.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onResumeNew();
        SearchListEntity entity = new SearchListEntity();
        entity.setFunNames(new String[]{"visitor", "user_role", "user_branch_story", "user_tool", "user_furniture", "leave_message"});
        mPresenter.searchHouseNew(entity);
    }

    private void onResumeNew() {
        binding.ivHouseQrCode.setVisibility(View.INVISIBLE);
        binding.tvHouseName.setVisibility(View.INVISIBLE);
        binding.tvHouseVivit.setVisibility(View.INVISIBLE);
        binding.map.clearAllView();
        binding.map.addTouchView(HouseActivity.this);
        mPresenter.getVisitorsInfo();
        mPresenter.loadPower();
        mPresenter.loadHouseObjects(true, "");
        if (TextUtils.isEmpty(PreferenceUtils.getUUid())) {
            binding.ivPersonal.setImageResource(R.drawable.bg_default_circle);
        } else {
            int size = (int) getResources().getDimension(R.dimen.x64);
            Glide.with(this)
                    .load(StringUtils.getUrl(this, PreferenceUtils.getAuthorInfo().getHeadPath(), size, size, false, true))
                    .error(R.drawable.bg_default_circle)
                    .placeholder(R.drawable.bg_default_circle)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(binding.ivPersonal);
        }
        binding.map.setOnImageClickLietener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sidaMenu != null && sidaMenu.getVisibility() == View.VISIBLE) {
                    sidaMenu.setVisibility(View.GONE);
                    return;
                }
                if (mIsOut) {
                    imgIn();
                    mIsOut = false;
                } else {
                    imgOut();
                    mIsOut = true;
                }
            }
        });
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }


    @Override
    protected void initListeners() {
        mRlRoleRoot.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                mRlRoleRoot.setVisibility(View.GONE);
            }
        });
        mRlRoleJuQing.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mRubbishEntity.getName().equals("你又扔了3个垃圾进去")) {
                    onResume();
                }
                mRlRoleJuQing.setVisibility(View.GONE);
                mTvContent.setVisibility(View.GONE);
                mTvJuQing.setVisibility(View.GONE);
                mIvGongXI.setVisibility(View.GONE);
                mRleSelect.setVisibility(View.GONE);
            }
        });
        mTvLeft.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                int type = mRubbishEntity.getType();
                switch (type) {
                    case 1:
                        mRlRoleJuQing.setVisibility(View.GONE);
                        mTvJuQing.setVisibility(View.GONE);
                        mTvContent.setVisibility(View.GONE);
                        mIvGongXI.setVisibility(View.GONE);
                        mRleSelect.setVisibility(View.GONE);
                        break;
                    case 2:
                        mRlRoleJuQing.setVisibility(View.GONE);
                        mIvGongXI.setVisibility(View.GONE);
                        mTvContent.setVisibility(View.GONE);
                        mTvJuQing.setVisibility(View.GONE);
                        mRleSelect.setVisibility(View.GONE);
//                        mPresenter.loadHouseSave(new RubblishBody(PreferenceUtils.getUUid(), "", mRubbishEntity.getId()));
                        Intent i6 = new Intent(HouseActivity.this, StorageActivity.class);
                        startActivity(i6);
                        break;
                    case 3:

                        break;
                }
            }
        });
        mTvRight.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                int type = mRubbishEntity.getType();
                switch (type) {
                    case 1:
                        mRlRoleJuQing.setVisibility(View.GONE);
                        mIvGongXI.setVisibility(View.GONE);
                        mTvJuQing.setVisibility(View.GONE);
                        mTvContent.setVisibility(View.GONE);
                        mRleSelect.setVisibility(View.GONE);
                        Intent i = new Intent(HouseActivity.this, MapEventNewActivity.class);
                        i.putExtra("id", mRubbishEntity.getScriptId());
                        i.putExtra("type", true);
                        startActivity(i);
                        break;
                    case 3:
                        break;
                    case 2:
                        mPresenter.houseToolUse(mRubbishEntity.getId());
                        break;
                    case 4:
                        break;
                }
            }
        });
        sidaCharacter = SideCharacterUtils.addFloatDragView(this, binding.rlHouse, new SideCHaracterOnClick());
        sidaMenu = LayoutInflater.from(this).inflate(R.layout.float_renwu_layout, null);
        sidaMenu.setLayoutParams(new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.x428), (int) getResources().getDimension(R.dimen.y320)));
        sidaMenuViewById = (RelativeLayout) sidaMenu.findViewById(R.id.rl_renwu);
        sidaMenuFrist = (RelativeLayout) sidaMenu.findViewById(R.id.ll_frist);
        sidaMenuIvPersonal = (ImageView) sidaMenu.findViewById(R.id.iv_personal);
        sidaMenuIvBag = (ImageView) sidaMenu.findViewById(R.id.iv_bag);
        sidaMenuIvShopping = (ImageView) sidaMenu.findViewById(R.id.iv_shopping);
        sidaMenuIvSignRoot = (ImageView) sidaMenu.findViewById(R.id.iv_sign_root);
        sidaMenuIvPhoneMenu = (ImageView) sidaMenu.findViewById(R.id.iv_phone_menu);
        sidaMenuIvMsg = (ImageView) sidaMenu.findViewById(R.id.iv_msg);
        sidaMenu.setVisibility(View.GONE);
        binding.rlHouse.addView(sidaMenu);

        sidaMenuIvPersonal.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (NetworkUtils.checkNetworkAndShowError(HouseActivity.this) && DialogUtils.checkLoginAndShowDlg(HouseActivity.this)) {
                    //埋点统计：手机个人中心
                    Intent i1 = new Intent(HouseActivity.this, PersonalV2Activity.class);
                    i1.putExtra(UUID, PreferenceUtils.getUUid());
                    startActivity(i1);
                }
                sidaMenu.setVisibility(View.GONE);
            }
        });
        sidaMenuIvBag.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (NetworkUtils.checkNetworkAndShowError(HouseActivity.this) && DialogUtils.checkLoginAndShowDlg(HouseActivity.this)) {
                    if (PreferenceUtils.getAuthorInfo().isOpenBag()) {
                        Intent i4 = new Intent(HouseActivity.this, NewBagV5Activity.class);
                        i4.putExtra("uuid", PreferenceUtils.getUUid());
                        startActivity(i4);
                    } else {
                        Intent i4 = new Intent(HouseActivity.this, BagOpenActivity.class);
                        startActivity(i4);
                    }
                }
                sidaMenu.setVisibility(View.GONE);
            }
        });
        sidaMenuIvShopping.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Intent i7 = new Intent(HouseActivity.this, CoinShopActivity.class);
                startActivity(i7);
                sidaMenu.setVisibility(View.GONE);
            }
        });
        sidaMenuIvSignRoot.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (DialogUtils.checkLoginAndShowDlg(HouseActivity.this)) {
                    DailyTaskActivity.startActivity(HouseActivity.this);
                }
                sidaMenu.setVisibility(View.GONE);
            }
        });
        sidaMenuIvPhoneMenu.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (NetworkUtils.checkNetworkAndShowError(HouseActivity.this) && DialogUtils.checkLoginAndShowDlg(HouseActivity.this)) {
                    //埋点统计：通讯录
                    startActivity(new Intent(HouseActivity.this, PhoneMenuV3Activity.class));
                }
                sidaMenu.setVisibility(View.GONE);
            }
        });
        sidaMenuIvMsg.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (NetworkUtils.checkNetworkAndShowError(HouseActivity.this) && DialogUtils.checkLoginAndShowDlg(HouseActivity.this)) {
                    //埋点统计：手机聊天
                    NoticeActivity.startActivity(HouseActivity.this, 1);
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
        binding.rlHouse.addView(sideLine);

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
                if (view.getLeft() == 0) {//左方
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
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) sidaMenuFrist.getLayoutParams();
                        layoutParams.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.y48));
                        sidaMenuFrist.setLayoutParams(layoutParams);
                        params.setMargins((int) (view.getLeft() - sidaMenu.getWidth() + view.getWidth() - getResources().getDimension(R.dimen.x24)),
                                view.getTop() - (view.getHeight() / 2), 0, 0);
                    } else {
                        sidaMenuViewById.setBackgroundResource(R.drawable.bg_classmate_menu_top_right);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) sidaMenuFrist.getLayoutParams();
                        layoutParams.setMargins(0, (int) getResources().getDimension(R.dimen.y48), 0, 0);
                        sidaMenuFrist.setLayoutParams(layoutParams);
                        params.setMargins((int) (view.getLeft() - (int) getResources().getDimension(R.dimen.x428) + view.getWidth() - getResources().getDimension(R.dimen.x24)),
                                (int) (view.getTop() + (view.getHeight() / 2) - getResources().getDimension(R.dimen.status_bar_height)), 0, 0);
                    }
                } else if (view.getTop() == getResources().getDimension(R.dimen.status_bar_height)) {//上方
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
                        layoutParams.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.y48));
                        sidaMenuFrist.setLayoutParams(layoutParams);
                        params.setMargins((int) (view.getLeft() - getResources().getDimension(R.dimen.x24)),
                                view.getTop() + (view.getHeight() / 2), 0, 0);
                    }

                } else if (view.getTop() + view.getHeight() == getResources().getDisplayMetrics().heightPixels) {//下方
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
    }

    public void GoneSidaMenu() {
        sidaMenu.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        mPresenter.isComplete();

    }

    @Override
    public void onFailure(int code, String msg) {
        ErrorCodeUtils.showErrorMsgByCode(HouseActivity.this, code, msg);
        if (!msg.equals("体力值不足")) {
//            if (!this.isDestroyed() && !this.isFinishing()) {
//                if (AlertDialogUtil.getInstance() != null && AlertDialogUtil.getInstance().isShow()) {
//                    AlertDialogUtil.getInstance().dismissDialog();
//                }
//            }
            finish();
        } else {
            if (!PreferenceUtils.getAuthorInfo().isVip()) {
                goPayVip();
            }
        }

    }

    private void goPayVip() {
        final AlertDialogUtil alertDialogUtilVip = AlertDialogUtil.getInstance();
        alertDialogUtilVip.createNormalDialog(HouseActivity.this, "需要VIP酱帮助你增加体力上线呢?");
        alertDialogUtilVip.setOnClickListener(new AlertDialogUtil.OnClickListener() {
            @Override
            public void CancelOnClick() {
                alertDialogUtilVip.dismissDialog();
            }

            @Override
            public void ConfirmOnClick() {
                alertDialogUtilVip.dismissDialog();
                mPresenter.createOrder("d61547ce-62c7-4665-993e-81a78cd32976");
            }
        });
        alertDialogUtilVip.showDialog();
    }

    @Override
    public void onLoadHouseObjects(ArrayList<MapEntity> entities) {
        for (MapEntity entity : entities) {
            if (entity.getType() == 2) {
                entity.setShows("1,2,3,4,5,6");
            } else if (entity.getType() == 3) {
                entity.setShows("1,2,3,4,5,6");
            }
        }
        final ArrayList<HouseDbEntity> res = new ArrayList<>();
        final ArrayList<HouseDbEntity> errorList = new ArrayList<>();
        MapUtil.checkAndDownloadHouse(this, true, HouseDbEntity.toDb(entities, "house"), "house", new Observer<HouseDbEntity>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                initDisposable = d;
            }

            @Override
            public void onNext(@NonNull HouseDbEntity mapDbEntity) {
                if (!mapDbEntity.getType().equals("3")) {
                    File file = new File(StorageUtils.getHouseRootPath() + mapDbEntity.getFileName());
                    String md5 = mapDbEntity.getMd5();
                    if (md5.length() < 32) {
                        int n = 32 - md5.length();
                        for (int i = 0; i < n; i++) {
                            md5 = "0" + md5;
                        }
                    }
                    if (mapDbEntity.getDownloadState() == 3 || !md5.equals(StringUtils.getFileMD5(file))) {
                        FileUtil.deleteFile(StorageUtils.getHouseRootPath() + mapDbEntity.getFileName());
                        errorList.add(mapDbEntity);
                    } else {
                        res.add(mapDbEntity);
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                GreenDaoManager.getInstance().getSession().getHouseDbEntityDao().insertOrReplaceInTx(res);
                mPresenter.addMapMark(HouseActivity.this, mContainer, binding.map, "house");
                if (errorList.size() > 0) {
                    resolvErrorList(errorList, "house");
                }
            }
        });
    }

    /**
     * 采集好感度值
     */
    @Override
    public void onLoadRoleLikeCollect(HouseLikeEntity entity) {
        binding.map.setTimerLikeView(entity);
        mRlRoleRoot.setVisibility(View.VISIBLE);
        mTvRoleNum.setText("好感度+" + entity.getRoleLike());
        mTvRoleName.setText(entity.getRoleName());
    }

    /**
     * 访客总数
     *
     * @param entities
     */
    @Override
    public void getVisitorsInfoSuccess(ArrayList<VisitorsEntity> entities) {
        binding.llLikeUserRoot.removeAllViews();
        if (entities != null && entities.size() > 0) {

            for (int i = 0; i < entities.size() - 1; i++) {
                for (int j = entities.size() - 1; j > i; j--) {
                    if (entities.get(j).getVisitorId().equals(entities.get(i).getVisitorId())) {
                        entities.remove(j);
                    }
                }
            }
            binding.visitorInfo.setVisibility(View.VISIBLE);
            int trueSize = (int) getResources().getDimension(R.dimen.y48);
            int imgSize = (int) getResources().getDimension(R.dimen.y44);
            int startMargin = (int) -getResources().getDimension(R.dimen.y10);
            int showSize = 4;
            if (entities.size() < showSize) {
                showSize = entities.size();
            }
            if (showSize == 4) {
                ImageView iv = new ImageView(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(trueSize, trueSize);
                lp.leftMargin = startMargin;
                iv.setLayoutParams(lp);
                iv.setImageResource(R.drawable.btn_feed_like_more);
                binding.llLikeUserRoot.addView(iv);
            }
//            binding.visitorCount.setText(entities.get(0).getCount());
            for (int i = showSize - 1; i >= 0; i--) {
                final VisitorsEntity userEntity = entities.get(i);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(trueSize, trueSize);
                if (i != 0) {
                    lp.leftMargin = startMargin;
                }
                View likeUser = LayoutInflater.from(this).inflate(R.layout.item_white_border_img, null);
                likeUser.setLayoutParams(lp);
                ImageView img = likeUser.findViewById(R.id.iv_img);
                if (!(HouseActivity.this.isFinishing())) {
                    Glide.with(this)
                            .load(StringUtils.getUrl(this, userEntity.getVisitorImage(), imgSize, imgSize, false, true))
                            .error(R.drawable.bg_default_circle)
                            .placeholder(R.drawable.bg_default_circle)
                            .bitmapTransform(new CropCircleTransformation(this))
                            .into(img);
                }
//                img.setOnClickListener(new NoDoubleClickListener() {
//                    @Override
//                    public void onNoDoubleClick(View v) {
//                        ViewUtils.toPersonal(HouseActivity.this, userEntity.getVisitorId());
//                    }
//                });
                binding.llLikeUserRoot.addView(likeUser);
            }
            count = entities.get(0).getCount();
            binding.visitorCount.setText(entities.get(0).getCount() + "");
            binding.tvHouseName.setText(PreferenceUtils.getAuthorInfo().getUserName() + "的宅屋");
            binding.tvHouseVivit.setText("访客数量:" + count);
        } else

        {
            binding.visitorInfo.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.setMargins(0, 0, 0, 40);
            binding.power.setLayoutParams(layoutParams);
        }
    }

    /**
     * 捡垃圾
     */
    @Override
    public void saveVisitorSuccess() {
        if (type == 3) {

        } else {

        }
    }

    /**
     * 捡垃圾获取奖池
     *
     * @param entity
     */
    @Override
    public void onLoadHouseRubblish(final RubbishEntity entity) {
        mRlRoleJuQing.setVisibility(View.GONE);
        mTvContent.setVisibility(View.GONE);
        mTvJuQing.setVisibility(View.GONE);
        mIvGongXI.setVisibility(View.GONE);
        mRleSelect.setVisibility(View.GONE);
        mTvContent.setText("");
        mTvCnanle.setVisibility(View.GONE);
        mTvCnanle.setText("");
        mTvChuWu.setText("");
        mTvChuWu.setVisibility(View.GONE);
        mTvUserName.setVisibility(View.GONE);
        mTvUserName.setText("");
        mRubbishEntity = entity;
        if (entity != null && entity.getType() != 0) {
            mRlRoleJuQing.setVisibility(View.VISIBLE);
            mTvLeft.setVisibility(View.VISIBLE);
            mIvGongXI.setVisibility(View.VISIBLE);
            int type = entity.getType();
            if (type == 1) {//剧情
                int w = getResources().getDimensionPixelSize(R.dimen.x456);
                int h = getResources().getDimensionPixelSize(R.dimen.y608);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mIvCover.getLayoutParams();
                layoutParams.width = w;
                layoutParams.height = h;
                mIvCover.setLayoutParams(layoutParams);
                Glide.with(HouseActivity.this)
                        .load(ApiService.URL_QINIU + entity.getImage())
                        .error(R.drawable.shape_transparent_background)
                        .placeholder(R.drawable.shape_transparent_background)
                        .into(mIvCover);
                mRleSelect.setVisibility(View.VISIBLE);
                mTvJuQing.setText(entity.getName());
                mTvJuQing.setVisibility(View.VISIBLE);
                mTvLeft.setVisibility(View.GONE);
                mTvRight.setText("观看剧情");
                mTvChuWu.setVisibility(View.VISIBLE);
                mTvCnanle.setVisibility(View.VISIBLE);
                mTvChuWu.setText("(已放入储物箱)");
                mTvCnanle.setText("点击任意区域关闭");
            } else if (type == 2) {
                int w = getResources().getDimensionPixelSize(R.dimen.x360);
                int h = getResources().getDimensionPixelSize(R.dimen.y360);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mIvCover.getLayoutParams();
                layoutParams.width = w;
                layoutParams.height = h;
                mIvCover.setLayoutParams(layoutParams);
                Glide.with(HouseActivity.this)
                        .load(ApiService.URL_QINIU + entity.getImage())
                        .error(R.drawable.shape_transparent_background)
                        .placeholder(R.drawable.shape_transparent_background)
                        .into(mIvCover);
                mRleSelect.setVisibility(View.VISIBLE);
                mTvJuQing.setText(entity.getName());
                mTvJuQing.setVisibility(View.VISIBLE);
                mTvChuWu.setVisibility(View.VISIBLE);
                mTvCnanle.setVisibility(View.VISIBLE);
                mTvChuWu.setText("(已放入储物箱)");
                mTvCnanle.setText("点击任意区域关闭");
                mTvRight.setText("立即使用");
                mTvLeft.setVisibility(View.VISIBLE);
                mTvLeft.setText("查看储物箱");
                mPresenter.loadHouseSave(new RubblishBody(PreferenceUtils.getUUid(), "", mRubbishEntity.getId()));
            } else if (type == 3) {
                mIvCover.setImageResource(R.drawable.bg_garbage_background_3);
                mRleSelect.setVisibility(View.GONE);
                mTvJuQing.setText("(点击任意区域关闭)");
                mTvContent.setText(entity.getName());
                mTvContent.setVisibility(View.VISIBLE);
                mTvJuQing.setVisibility(View.VISIBLE);
                mTvUserName.setVisibility(View.VISIBLE);
                mTvUserName.setText(entity.getName());
            } else if (type == 4) {
                int w = getResources().getDimensionPixelSize(R.dimen.x360);
                int h = getResources().getDimensionPixelSize(R.dimen.y360);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mIvCover.getLayoutParams();
                layoutParams.width = w;
                layoutParams.height = h;
                mIvCover.setLayoutParams(layoutParams);
                Glide.with(HouseActivity.this)
                        .load(ApiService.URL_QINIU + entity.getImage())
                        .error(R.drawable.shape_transparent_background)
                        .placeholder(R.drawable.shape_transparent_background)
                        .into(mIvCover);
                mRleSelect.setVisibility(View.GONE);
                mTvJuQing.setText(entity.getName());
                mTvJuQing.setVisibility(View.VISIBLE);
                mPresenter.loadHouseSave(new RubblishBody(PreferenceUtils.getUUid(), "", mRubbishEntity.getId()));
                mTvChuWu.setVisibility(View.VISIBLE);
                mTvCnanle.setVisibility(View.GONE);
                mTvChuWu.setText("(已放入储物箱)");
            }
        }
        mPresenter.loadPower();
    }

    /**
     * 保存
     */
    @Override
    public void onLoadHouseSave() {

    }


    /**
     * 获取体力值成功
     *
     * @param entity
     */
    @Override
    public void onLoadPowerSuccess(PowerEntity entity) {
        if (entity != null) {
            nowpower = entity.getHealthPoint();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) binding.roleHeartNumSmall.getLayoutParams();
            binding.roleHeartNum.setText(entity.getHealthPoint() + "/" + entity.getMaxHealthPoint());
            if (entity.getHealthPoint() < entity.getMaxHealthPoint()) {
                binding.roleHeartNumSmall.setVisibility(View.VISIBLE);
                int num = 240 - (240 * entity.getHealthPoint() / entity.getMaxHealthPoint());
                if (num > 220) {
                    binding.roleHeartNumSmall.setVisibility(View.GONE);
                    binding.fl.setBackgroundResource(R.drawable.shape_role_bg_two);
                } else {
//                    if (num < 10) {
//                        binding.roleHeartNumSmall.setVisibility(View.GONE);
//                        binding.fl.setBackgroundResource(R.drawable.shape_role_bg_two);
//                    }
                    binding.roleHeartNumSmall.setVisibility(View.VISIBLE);
                    binding.fl.setBackgroundResource(R.drawable.shape_power_bg);
                    params.width = num;
                }
            }
        }
    }

    @Override
    public void getHisVisitorsInfo(ArrayList<VisitorsEntity> entities) {

    }

    /**
     * 道具使用
     *
     * @param entity
     */
    @Override
    public void onLoadHouseToolUse(PropUseEntity entity) {
        mRlRoleJuQing.setVisibility(View.GONE);
        mIvGongXI.setVisibility(View.GONE);
        mTvContent.setVisibility(View.GONE);
        mTvJuQing.setVisibility(View.GONE);
        mRleSelect.setVisibility(View.GONE);
        showToast(entity.getToolUseMessage());
    }

    @Override
    public void onCreateOrderSuccess(OrderEntity entity) {
        entitPay = entity;
        if (bottomFragment != null)
            bottomFragment.show(getSupportFragmentManager(), "payMenu");
    }

    private void initPayMenu() {
        ArrayList<MenuItem> items = new ArrayList<>();
        MenuItem item = new MenuItem(0, getString(R.string.label_alipay));
        items.add(item);
        item = new MenuItem(1, getString(R.string.label_wx));
        items.add(item);
        item = new MenuItem(2, getString(R.string.label_qpay));
        items.add(item);
        bottomFragment = new BottomMenuFragment();
        bottomFragment.setShowTop(true);
        bottomFragment.setTopContent("选择支付方式");
        bottomFragment.setMenuItems(items);
        bottomFragment.setMenuType(BottomMenuFragment.TYPE_VERTICAL);
        bottomFragment.setmClickListener(new BottomMenuFragment.MenuItemClickListener() {
            @Override
            public void OnMenuItemClick(int itemId) {
                createDialog();
                String payType = "";
                if (itemId == 0) {
                    payType = "alipay";
                } else if (itemId == 1) {
                    payType = "wx";
                } else if (itemId == 2) {
                    payType = "qpay";
                }
                PayReqEntity entity = new PayReqEntity(entitPay.getAddress().getAddress(),
                        payType,
                        IpAdressUtils.getIpAdress(HouseActivity.this),
                        entitPay.getOrderId(),
                        entitPay.getAddress().getPhone(),
                        "",
                        entitPay.getAddress().getUserName());
                mPresenter.payOrder(entity);
            }
        });
    }

    @Override
    public void onPayOrderSuccess(PayResEntity entity) {
        if (entity.isSuccess()) {
            finalizeDialog();
            showToast("支付成功");
            Intent i = new Intent();
            i.putExtra("position", -1);
            i.putExtra("type", "pay");
            setResult(RESULT_OK, i);
            finish();
        } else {
            if (entity.getCharge() != null) {
//                if("qpay".equals(entity.getCharge().get("channel"))){
//                    Pingpp.createPayment(OrderActivity.this, entity.getCharge().toString(),"qwallet1104765197");
//                }else {
//                    Pingpp.createPayment(OrderActivity.this, entity.getCharge().toString());
//                }
                PingppUI.createPay(HouseActivity.this, entity.getCharge().toString(), new PaymentHandler() {
                    @Override
                    public void handlePaymentResult(Intent intent) {
                        String result = intent.getExtras().getString("result");
                        if (result.contains("success")) {
                            result = "success";
                        } else if (result.contains("fail")) {
                            result = "fail";
                        } else if (result.contains("cancel")) {
                            result = "cancel";
                        } else if (result.contains("invalid")) {
                            result = "invalid";
                        }
                        finalizeDialog();
                        if ("success".equals(result)) {
                            PreferenceUtils.getAuthorInfo().setVip(true);
                            showToast("支付成功");
                            Intent i = new Intent();
                            i.putExtra("position", -1);
                            i.putExtra("type", "pay");
                            setResult(RESULT_OK, i);
                            finish();
                        } else {
                            showToast(result);
//                            finish();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void isCompleteSuccess(boolean isComplete) {
        if (isComplete == true) {//完成所有任务
            mSeven.setVisibility(View.GONE);
        } else {
            mSeven.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取宅屋新消息提醒
     *
     * @param searchNewLists
     */
    @Override
    public void getHouseNewSuccess(ArrayList<SearchNewListEntity> searchNewLists) {
        if (searchNewLists != null && searchNewLists.size() > 0) {
            news = new ArrayList<>();
            news = searchNewLists;
            Map<String, Integer> searchMap = new HashMap<>();
            for (int i = 0; i < searchNewLists.size(); i++) {
                if (searchNewLists.get(i).getFunName().equals("visitor")) {
                    if (!searchMap.containsKey("visitor")) {
                        searchMap.put("visitor", 1);
                    }
                }
                if (searchNewLists.get(i).getFunName().equals("user_role")) {
                    if (!searchMap.containsKey("user_role")) {
                        searchMap.put("user_role", 2);
                    }
                }
                if (searchNewLists.get(i).getFunName().equals("user_tool") || searchNewLists.get(i).getFunName().equals("user_furniture")) {
                    if (!searchMap.containsKey("user_tool")) {
                        searchMap.put("user_tool", 3);
                    }
                    if (!searchMap.containsKey("user_furniture")) {
                        searchMap.put("user_furniture", 4);
                    }
                }
                if (searchNewLists.get(i).getFunName().equals("user_branch_story")) {
                    if (!searchMap.containsKey("user_branch_story")) {
                        searchMap.put("user_branch_story", 5);
                    }
                }
                if (searchNewLists.get(i).getFunName().equals("leave_message")) {
                    if (!searchMap.containsKey("leave_message")) {
                        searchMap.put("leave_message", 6);
                    }
                }
            }
            if (searchMap.get("visitor") != null) {
                mVisitor.setVisibility(View.VISIBLE);
            } else {
                mVisitor.setVisibility(View.GONE);
            }
            if (searchMap.get("user_role") != null) {
                mRoleNews.setVisibility(View.VISIBLE);
            } else {
                mRoleNews.setVisibility(View.GONE);
            }
            if (searchMap.get("user_tool") != null || searchMap.get("user_furniture") != null) {
                mStorage.setVisibility(View.VISIBLE);
            } else {
                mStorage.setVisibility(View.GONE);
            }
//            if (searchMap.get("user_branch_story") != null) {
//                mDrameNews.setVisibility(View.VISIBLE);
//            } else {
//                mDrameNews.setVisibility(View.GONE);
//            }
            if (searchMap.get("leave_message") != null) { 
                mMessageNews.setVisibility(View.VISIBLE);
            } else {
                mMessageNews.setVisibility(View.GONE);
            }


        }
    }

    @Override
    public void updateSuccess() {
    }

    private void resolvErrorList(ArrayList<HouseDbEntity> errorList, final String type) {
        final ArrayList<HouseDbEntity> errorListTmp = new ArrayList<>();
        final ArrayList<HouseDbEntity> res = new ArrayList<>();
        MapUtil.checkAndDownloadHouse(this, false, errorList, type, new Observer<HouseDbEntity>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                resolvDisposable = d;
            }

            @Override
            public void onNext(@NonNull HouseDbEntity mapDbEntity) {
                File file = new File(StorageUtils.getHouseRootPath() + mapDbEntity.getFileName());
                String md5 = mapDbEntity.getMd5();
                if (md5.length() < 32) {
                    int n = 32 - md5.length();
                    for (int i = 0; i < n; i++) {
                        md5 = "0" + md5;
                    }
                }
                if (!md5.equals(StringUtils.getFileMD5(file)) || mapDbEntity.getDownloadState() == 3) {
                    FileUtil.deleteFile(StorageUtils.getHouseRootPath() + mapDbEntity.getFileName());
                    errorListTmp.add(mapDbEntity);
                } else {
                    res.add(mapDbEntity);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                GreenDaoManager.getInstance().getSession().getHouseDbEntityDao().insertOrReplaceInTx(res);
                if (errorListTmp.size() > 0) {
                    resolvErrorList(errorListTmp, type);
                } else {
                    if ("house".equals(type)) {
                        mPresenter.addMapMark(HouseActivity.this, mContainer, binding.map, "house");
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (initDisposable != null && !initDisposable.isDisposed()) initDisposable.dispose();
        if (resolvDisposable != null && !resolvDisposable.isDisposed()) resolvDisposable.dispose();
        binding.map.clearAllView();
        EventBus.getDefault().unregister(this);
    }

    private void imgIn() {
        ObjectAnimator phoneAnimator = ObjectAnimator.ofFloat(binding.llToolBar, "translationY", -binding.llToolBar.getHeight() - getResources().getDimension(R.dimen.y60), 0).setDuration(300);
        phoneAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mRoleAnimator = ObjectAnimator.ofFloat(binding.dormitoryRole, "translationY", binding.dormitoryRole.getHeight() - getResources().getDimension(R.dimen.y60), 0).setDuration(300);
        mRoleAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mStorageAnimator = ObjectAnimator.ofFloat(binding.dormitoryStorage, "translationY", binding.dormitoryStorage.getHeight() - getResources().getDimension(R.dimen.y60), 0).setDuration(300);
        mStorageAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mDramaAnimator = ObjectAnimator.ofFloat(binding.dormitoryDrama, "translationY", binding.dormitoryDrama.getHeight() - getResources().getDimension(R.dimen.y60), 0).setDuration(300);
        mDramaAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mVisitorInfoAnimator = ObjectAnimator.ofFloat(binding.visitorInfo, "translationX", -binding.visitorInfo.getWidth(), 0).setDuration(300);
        mVisitorInfoAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mIvSleepAnimator = ObjectAnimator.ofFloat(binding.ivSleep, "translationX", binding.ivSleep.getWidth() + getResources().getDimension(R.dimen.y60), 0).setDuration(300);
        mIvSleepAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mIvBagAnimator = ObjectAnimator.ofFloat(binding.ivBag, "translationX", binding.ivBag.getWidth() + getResources().getDimension(R.dimen.y60), 0).setDuration(300);
        mIvBagAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mIvTrandsAnimator = ObjectAnimator.ofFloat(binding.ivTrends, "translationX", binding.ivTrends.getWidth() + getResources().getDimension(R.dimen.y60), 0).setDuration(300);
        mIvTrandsAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mIvAlarmsAnimator = ObjectAnimator.ofFloat(binding.ivAlarm, "translationX", binding.ivAlarm.getWidth() + getResources().getDimension(R.dimen.y60), 0).setDuration(300);
        mIvAlarmsAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mIvMesssgeAnimator = ObjectAnimator.ofFloat(binding.ivMessage, "translationX", binding.ivMessage.getWidth() + getResources().getDimension(R.dimen.y60), 0).setDuration(300);
        mIvMesssgeAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mIvShareAnimator = ObjectAnimator.ofFloat(binding.ivShare, "translationX", -binding.ivShare.getWidth(), 0).setDuration(300);
        mIvMesssgeAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mIvPowerAnimator = ObjectAnimator.ofFloat(binding.power, "translationX", -binding.power.getWidth(), 0).setDuration(300);
        mIvMesssgeAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mIvSevenAnimator = ObjectAnimator.ofFloat(binding.ivSeven, "translationX", -binding.ivSeven.getWidth(), 0).setDuration(300);
        mIvSevenAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mRoleNewsAnimator = ObjectAnimator.ofFloat(binding.roleNews, "translationY", binding.roleNews.getHeight() + binding.roleNews.getHeight() - getResources().getDimension(R.dimen.y60), 0).setDuration(300);
        mRoleNewsAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mDramaNewsAnimator = ObjectAnimator.ofFloat(binding.dramaNews, "translationY", binding.dramaNews.getHeight() + binding.dramaNews.getHeight() - getResources().getDimension(R.dimen.y60), 0).setDuration(300);
        mRoleNewsAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mSrorageNewsAnimator = ObjectAnimator.ofFloat(binding.storageNews, "translationY", binding.storageNews.getHeight() + binding.storageNews.getHeight() - getResources().getDimension(R.dimen.y60), 0).setDuration(300);
        mSrorageNewsAnimator.setInterpolator(new OvershootInterpolator());
        AnimatorSet set = new AnimatorSet();
        set.play(phoneAnimator).with(mRoleAnimator);
        set.play(mStorageAnimator).with(mDramaAnimator);
        set.play(mVisitorInfoAnimator).with(mIvSleepAnimator);
        set.play(mIvTrandsAnimator).with(mIvAlarmsAnimator);
        set.play(mIvMesssgeAnimator).with(mIvShareAnimator);
        set.play(mIvPowerAnimator).with(mIvBagAnimator);
        set.play(mIvSevenAnimator).with(mRoleNewsAnimator);
        set.play(mDramaNewsAnimator).with(mSrorageNewsAnimator);
        set.start();
    }

    private void imgOut() {
        ObjectAnimator phoneAnimator = ObjectAnimator.ofFloat(binding.llToolBar, "translationY", 0, -getResources().getDimension(R.dimen.y60) - binding.llToolBar.getHeight()).setDuration(300);
        phoneAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mStorageAnimator = ObjectAnimator.ofFloat(binding.dormitoryRole, "translationY", 0, -getResources().getDimension(R.dimen.y60) - -binding.dormitoryRole.getHeight() * 2).setDuration(300);
        mStorageAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mRoleAnimator = ObjectAnimator.ofFloat(binding.dormitoryStorage, "translationY", 0, -getResources().getDimension(R.dimen.y60) - -binding.dormitoryStorage.getHeight() * 2).setDuration(300);
        mRoleAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mDramaAnimator = ObjectAnimator.ofFloat(binding.dormitoryDrama, "translationY", 0, -getResources().getDimension(R.dimen.y60) - -binding.dormitoryDrama.getHeight() * 2).setDuration(300);
        mDramaAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mVisitorInfoAnimator = ObjectAnimator.ofFloat(binding.visitorInfo, "translationX", 0, -binding.visitorInfo.getWidth()).setDuration(300);
        mVisitorInfoAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mIvSleepAnimator = ObjectAnimator.ofFloat(binding.ivSleep, "translationX", 0, binding.ivSleep.getWidth() + getResources().getDimension(R.dimen.y60)).setDuration(300);
        mIvSleepAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mIvBagAnimator = ObjectAnimator.ofFloat(binding.ivBag, "translationX", 0, binding.ivBag.getWidth() + getResources().getDimension(R.dimen.y60)).setDuration(300);
        mIvBagAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mIvTrandsAnimator = ObjectAnimator.ofFloat(binding.ivTrends, "translationX", 0, binding.ivTrends.getWidth() + getResources().getDimension(R.dimen.y60)).setDuration(300);
        mIvTrandsAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mIvAlarmsAnimator = ObjectAnimator.ofFloat(binding.ivAlarm, "translationX", 0, binding.ivAlarm.getWidth() + getResources().getDimension(R.dimen.y60)).setDuration(300);
        mIvAlarmsAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mIvMesssgeAnimator = ObjectAnimator.ofFloat(binding.ivMessage, "translationX", 0, binding.ivMessage.getWidth() + getResources().getDimension(R.dimen.y60)).setDuration(300);
        mIvMesssgeAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mIvShareAnimator = ObjectAnimator.ofFloat(binding.ivShare, "translationX", 0, -getResources().getDimension(R.dimen.y60) - binding.dormitoryDrama.getWidth()).setDuration(300);
        mIvMesssgeAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mIvPowerAnimator = ObjectAnimator.ofFloat(binding.power, "translationX", 0, -getResources().getDimension(R.dimen.y60) - binding.power.getWidth()).setDuration(300);
        mIvMesssgeAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mIvSevenAnimator = ObjectAnimator.ofFloat(binding.ivSeven, "translationX", 0, -getResources().getDimension(R.dimen.y60) - binding.ivSeven.getWidth()).setDuration(300);
        mIvMesssgeAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mRoleNewsAnimator = ObjectAnimator.ofFloat(binding.roleNews, "translationY", 0, -getResources().getDimension(R.dimen.y60) - -binding.roleNews.getHeight() - (-binding.dormitoryRole.getHeight()) * 2).setDuration(300);
        mRoleNewsAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mDramaNewsAnimator = ObjectAnimator.ofFloat(binding.dramaNews, "translationY", 0, -getResources().getDimension(R.dimen.y60) - -binding.dramaNews.getHeight() - (-binding.dormitoryDrama.getHeight()) * 2).setDuration(300);
        mDramaAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mStorageNewsAnimator = ObjectAnimator.ofFloat(binding.storageNews, "translationY", 0, -getResources().getDimension(R.dimen.y60) - -binding.storageNews.getHeight() - (-binding.dormitoryStorage.getHeight()) * 2).setDuration(300);
        mStorageNewsAnimator.setInterpolator(new OvershootInterpolator());
        AnimatorSet set = new AnimatorSet();
        set.play(phoneAnimator).with(mRoleAnimator);
        set.play(mStorageAnimator).with(mDramaAnimator);
        set.play(mVisitorInfoAnimator).with(mIvSleepAnimator);
        set.play(mIvTrandsAnimator).with(mIvAlarmsAnimator);
        set.play(mIvMesssgeAnimator).with(mIvShareAnimator);
        set.play(mIvPowerAnimator).with(mIvBagAnimator);
        set.play(mIvSevenAnimator).with(mRoleNewsAnimator);
        set.play(mDramaNewsAnimator).with(mStorageNewsAnimator);
        set.start();
    }

    public class Presenter {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_left:
//                    Intent i3 = new Intent(HouseActivity.this, FeedV3Activity.class);
//                    startActivity(i3);
                    if (sidaMenu != null && sidaMenu.getVisibility() == View.VISIBLE) {
                        sidaMenu.setVisibility(View.GONE);
                        return;
                    }
                    if (sideLine != null && sideLine.getVisibility() == View.VISIBLE) {
                        sideLine.setVisibility(View.GONE);
                        return;
                    }

                    finish();
                    break;
                case R.id.iv_right:
                    finish();
                    break;
                case R.id.iv_personal:
                    if (NetworkUtils.checkNetworkAndShowError(HouseActivity.this) && DialogUtils.checkLoginAndShowDlg(HouseActivity.this)) {
                        //埋点统计：手机个人中心
                        clickEvent("个人中心");
                        Intent i1 = new Intent(HouseActivity.this, PersonalV2Activity.class);
                        i1.putExtra(UUID, PreferenceUtils.getUUid());
                        startActivity(i1);
                    }
                    break;
                case R.id.tv_search:
                    //埋点统计：手机个人中心
                    clickEvent("宅屋-搜索");
                    Intent intent = new Intent(HouseActivity.this, AllSearchActivity.class);
                    intent.putExtra("type", "all");
                    startActivity(intent);
                    break;
                case R.id.visitor_info:
                    Intent i7 = new Intent(HouseActivity.this, NewVisitorActivity.class);
//                    i7.putExtra(UUID, PreferenceUtils.getUUid());
                    startActivity(i7);
                    upDateEntity event = new upDateEntity("visitor", null);
                    mPresenter.updateNews(event);
                    break;
                case R.id.dormitory_storage:
                    Intent i6 = new Intent(HouseActivity.this, StorageActivity.class);
                    startActivity(i6);
                    break;
                case R.id.dormitory_role:
                    Intent i4 = new Intent(HouseActivity.this, RoleActivity.class);
                    startActivity(i4);
                    break;
                case R.id.dormitory_drama:
                    Intent i8 = new Intent(HouseActivity.this, DormitoryDramaActivity.class);
                    startActivity(i8);
                    break;
                case R.id.iv_sleep:
                    Intent i9 = new Intent(HouseActivity.this, Live3dActivity.class);
                    startActivity(i9);
//                    binding.tvHouseName.setText(PreferenceUtils.getAuthorInfo().getUserName() + "的宅屋");
//                    binding.tvHouseVivit.setText("访客数量:" + count);
//                    binding.ivHouseQrCode.setVisibility(View.VISIBLE);
//                    binding.tvHouseName.setVisibility(View.VISIBLE);
//                    binding.tvHouseVivit.setVisibility(View.VISIBLE);
//                    Bitmap bitmap = getBitmap(binding.flShare);
////                    Bitmap viewBitmap = getViewBitmap(R.layout.activity_house);
//                    binding.rlCoverInfo.setVisibility(View.VISIBLE);
//                    binding.ivCoverNextLie.setImageBitmap(bitmap);
                    break;
                case R.id.iv_alarm://同桌电话
                    Intent intent1 = new Intent(HouseActivity.this, AlarmActivity.class);
                    startActivity(intent1);
//                    Intent intent1 = new Intent(HouseActivity.this, SevenDayLoginActivity.class);
//                    startActivity(intent1);
                    break;
                case R.id.iv_trends://动态
                    Intent i5 = new Intent(HouseActivity.this, NewDynamicActivity.class);
                    startActivity(i5);
                    break;
                case R.id.iv_message://留言板
                    Intent i1 = new Intent(HouseActivity.this, CommentsListActivity.class);
                    i1.putExtra("uuid", PreferenceUtils.getUUid());
                    for (int i = 0; i < news.size(); i++) {
                        if (news.get(i).getFunName().equals("leave_message")) {
                            upDateEntity eventMessage = new upDateEntity("leave_message", null);
                            mPresenter.updateNews(eventMessage);
                        }
                    }
                    startActivity(i1);
                    break;
                case R.id.iv_bag:
                    if (PreferenceUtils.getAuthorInfo().isOpenBag()) {
                        Intent intent2 = new Intent(HouseActivity.this, NewBagV5Activity.class);
                        intent2.putExtra("uuid", PreferenceUtils.getUUid());
                        startActivity(intent2);
                    } else {
                        Intent intent2 = new Intent(HouseActivity.this, BagOpenActivity.class);
                        startActivity(intent2);
                    }
                    break;
                case R.id.iv_seven:
                    Intent intent2 = new Intent(HouseActivity.this, SevenDayLoginActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.iv_share:
                    if (bottomMenuFragment != null) {
                        createDialog("生成图片中...");
                        binding.ivHouseQrCode.setVisibility(View.VISIBLE);
                        binding.tvHouseName.setVisibility(View.VISIBLE);
                        binding.tvHouseVivit.setVisibility(View.VISIBLE);
                        bitmap = getBitmap(binding.flShare);
                        saveBitmap = StorageUtils.saveBitmap(HouseActivity.this, bitmap);
                        if (!TextUtils.isEmpty(saveBitmap) && bottomMenuFragment != null) {
                            finalizeDialog();
                            binding.ivHouseQrCode.setVisibility(View.INVISIBLE);
                            binding.tvHouseName.setVisibility(View.INVISIBLE);
                            binding.tvHouseVivit.setVisibility(View.INVISIBLE);
                            bottomMenuFragment.show(getSupportFragmentManager(), "house");
                        } else {
                            finalizeDialog();
                            binding.ivHouseQrCode.setVisibility(View.INVISIBLE);
                            binding.tvHouseName.setVisibility(View.INVISIBLE);
                            binding.tvHouseVivit.setVisibility(View.INVISIBLE);
                            showToast("图片生成失败，请重新生成~");
                        }
                    }
                    break;
                case R.id.power:
                    if (!PreferenceUtils.getAuthorInfo().isVip()) {
                        final AlertDialogUtil alertDialogUtilVip = AlertDialogUtil.getInstance();
                        alertDialogUtilVip.createNormalDialog(HouseActivity.this, "VIP酱偷偷的说:“VIP可以多20%体力上限哦”");
                        alertDialogUtilVip.setOnClickListener(new AlertDialogUtil.OnClickListener() {
                            @Override
                            public void CancelOnClick() {
                                alertDialogUtilVip.dismissDialog();
                            }

                            @Override
                            public void ConfirmOnClick() {
                                alertDialogUtilVip.dismissDialog();
                                mPresenter.createOrder("d61547ce-62c7-4665-993e-81a78cd32976");
                            }
                        });
                        alertDialogUtilVip.showDialog();
                    }
                    break;

            }
        }

    }

    /**
     * 把一个view转化成bitmap对象
     */

    public Bitmap getViewBitmap(int layoutId) {

        View view = getLayoutInflater().inflate(layoutId, null);

        int me = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        view.measure(me, me);

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache();

        Bitmap bitmap = view.getDrawingCache();


        return bitmap;

    }

    public Bitmap getBitmap(View view) {


        Bitmap bitmap = null;
        int width = view.getRight() - view.getLeft();
        int height = view.getBottom() - view.getTop();
        final boolean opaque = view.getDrawingCacheBackgroundColor() != 0 || view.isOpaque();
        Bitmap.Config quality;
        if (!opaque) {
            switch (view.getDrawingCacheQuality()) {
                case DRAWING_CACHE_QUALITY_AUTO:
                case DRAWING_CACHE_QUALITY_LOW:
                case DRAWING_CACHE_QUALITY_HIGH:
                default:
                    quality = Bitmap.Config.ARGB_8888;
                    break;
            }
        } else {
            quality = Bitmap.Config.RGB_565;
        }
        bitmap = Bitmap.createBitmap(getResources().getDisplayMetrics(),
                width, height, quality);
        bitmap.setDensity(getResources().getDisplayMetrics().densityDpi);
        if (opaque) bitmap.setHasAlpha(false);
        boolean clear = view.getDrawingCacheBackgroundColor() != 0;
        Canvas canvas = new Canvas(bitmap);
        if (clear) {
            bitmap.eraseColor(view.getDrawingCacheBackgroundColor());
        }
        view.computeScroll();
        final int restoreCount = canvas.save();
        canvas.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(canvas);
        canvas.restoreToCount(restoreCount);
        canvas.setBitmap(null);

        return bitmap;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void houseLikeEvent(HouseLikeEvent event) {
        if (event != null) {
            String roleId = event.getRoleId();
            int type = event.getType();
            if (type == 3) {
                mPresenter.saveVisitor(new SaveVisitorEntity(event.getRoleId(), PreferenceUtils.getUUid(), 3));
                mPresenter.loadHouseRubblish(new RubblishBody(PreferenceUtils.getUUid(), roleId, ""));
            } else {
                this.type = 2;
                mPresenter.loadRoleLikeCollect(roleId);
            }
        }
    }


    /**
     * 分享
     */
    private void initPopupMenus() {
        bottomMenuFragment = new BottomMenuFragment();
        ArrayList<com.moemoe.lalala.view.widget.netamenu.MenuItem> items = new ArrayList<>();

        MenuItem item = new MenuItem(1, "QQ", R.drawable.btn_doc_option_send_qq);
        items.add(item);

        item = new MenuItem(2, "QQ空间", R.drawable.btn_doc_option_send_qzone);
        items.add(item);

        item = new MenuItem(3, "微信", R.drawable.btn_doc_option_send_wechat);
        items.add(item);

        item = new MenuItem(4, "微信朋友圈", R.drawable.btn_doc_option_send_pengyouquan);
        items.add(item);

        item = new MenuItem(5, "微博", R.drawable.btn_doc_option_send_weibo);
        items.add(item);

        bottomMenuFragment.setShowTop(false);
        bottomMenuFragment.setMenuItems(items);
        bottomMenuFragment.setMenuType(BottomMenuFragment.TYPE_HORIZONTAL);
        bottomMenuFragment.setmClickListener(new BottomMenuFragment.MenuItemClickListener() {
            @Override
            public void OnMenuItemClick(final int itemId) {
                // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
                // text是分享文本，所有平台都需要这个字段
                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数

                switch (itemId) {
                    case 1:
                        ShareUtils.shareQQBimtp(HouseActivity.this, "", "", "", saveBitmap, platformActionListener);
                        break;
                    case 2:
                        ShareUtils.shareQQzoneBitmap(HouseActivity.this, "", "", "", saveBitmap, platformActionListener);
                        break;
                    case 3:
                        ShareUtils.shareWechatBitmap(HouseActivity.this, "", "", "", bitmap, platformActionListener);
                        break;
                    case 4:
                        ShareUtils.sharepyqBitmap(HouseActivity.this, "", "", "", bitmap, platformActionListener);
                        break;
                    case 5:
                        ShareUtils.shareWeiboBitMap(HouseActivity.this, "", "", "", bitmap, platformActionListener);
                        break;

                }

            }
        });
    }

    /**
     * 分享回调
     */
    PlatformActionListener platformActionListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            Log.e("kid", "分享成功");
            if (bottomMenuFragment != null) {
                bottomMenuFragment.dismiss();
            }
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            Log.e("kid", "分享失败");
            if (bottomMenuFragment != null) {
                bottomMenuFragment.dismiss();
            }
        }

        @Override
        public void onCancel(Platform platform, int i) {
            Log.e("kid", "分享取消");
            if (bottomMenuFragment != null) {
                bottomMenuFragment.dismiss();
            }
        }
    };


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void stageLineEvent(StageLineEvent event) {
        if (event != null && NewUtils.isActivityTop(HouseActivity.class, this)) {
            goGreenDao();
            String stageLine = PreferenceUtils.getStageLine(HouseActivity.this);
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
            if (sidaCharacter.getLeft() == 0) {//左边
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
            } else if (sidaCharacter.getTop() == getResources().getDimension(R.dimen.status_bar_height)) {//上方
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
            } else if (sidaCharacter.getTop() + sidaCharacter.getHeight() == getResources().getDisplayMetrics().heightPixels) {//下方
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
