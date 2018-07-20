package com.moemoe.lalala.view.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityHouseHisBinding;
import com.moemoe.lalala.di.components.DaggerHouseComponent;
import com.moemoe.lalala.di.modules.HouseModule;
import com.moemoe.lalala.event.HouseHisLikeEvent;
import com.moemoe.lalala.model.api.ApiService;
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
import com.moemoe.lalala.model.entity.SearchNewListEntity;
import com.moemoe.lalala.model.entity.VisitorsEntity;
import com.moemoe.lalala.presenter.DormitoryContract;
import com.moemoe.lalala.presenter.DormitoryPresenter;
import com.moemoe.lalala.utils.AlertDialogUtil;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.FileUtil;
import com.moemoe.lalala.utils.GreenDaoManager;
import com.moemoe.lalala.utils.IpAdressUtils;
import com.moemoe.lalala.utils.MapUtil;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.StorageUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.ViewUtils;
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

import javax.inject.Inject;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;

public class HouseHisActivity extends BaseActivity implements DormitoryContract.View {

    private ActivityHouseHisBinding binding;

    @Inject
    DormitoryPresenter mPresenter;
    private MapMarkContainer mContainer;
    private Disposable initDisposable;
    private Disposable resolvDisposable;
    private String id;
    private boolean mIsOut = false;
    private int type;
    private RelativeLayout mRlRoleJuQing;
    private ImageView mIvCover;
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
    private TextView mTvUserName;
    private int count;
    private BottomMenuFragment bottomFragment;
    private OrderEntity entitPay;

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_house_his);
        binding.setPresenter(new Presenter());
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        id = getIntent().getStringExtra("id");
        DaggerHouseComponent.builder()
                .houseModule(new HouseModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        mPresenter.loadPower();

        mRlRoleJuQing = findViewById(R.id.rl_role_juqing);
        mIvCover = findViewById(R.id.iv_cover_next);
        mRleSelect = findViewById(R.id.rl_select);
        mTvLeft = findViewById(R.id.tv_left);
        mTvRight = findViewById(R.id.tv_right);
        mIvGongXI = findViewById(R.id.iv_gongxi);
        mTvJuQing = findViewById(R.id.tv_juqing);
        mTvContent = findViewById(R.id.tv_content_gongxi);
        mTvChuWu = findViewById(R.id.tv_chuwu);
        mTvCnanle = findViewById(R.id.tv_canle);
        mTvUserName = findViewById(R.id.tv_user_name);

        binding.map.setIsHis(true);
        mPresenter.loadHouseObjects(false, id);
        mContainer = new MapMarkContainer();
        initMap();
        initPayMenu();

        EventBus.getDefault().register(this);
    }

    public void initMap() {

    }

    private void imgIn() {

        ObjectAnimator mIvMesssgeAnimator = ObjectAnimator.ofFloat(binding.ivMessage, "translationX", getResources().getDisplayMetrics().widthPixels, 0).setDuration(300);
        mIvMesssgeAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mIvPowerAnimator = ObjectAnimator.ofFloat(binding.power, "translationX", -binding.power.getWidth(), 0).setDuration(300);
        mIvMesssgeAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mVisitorAnimator = ObjectAnimator.ofFloat(binding.visitorInfo, "translationX", -binding.visitorInfo.getWidth(), 0).setDuration(300);
        mIvMesssgeAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mGoHomeAnimator = ObjectAnimator.ofFloat(binding.ivGoMyHouse, "translationX", -binding.ivGoMyHouse.getWidth(), 0).setDuration(300);
        mGoHomeAnimator.setInterpolator(new OvershootInterpolator());
        AnimatorSet set = new AnimatorSet();
        set.play(mIvMesssgeAnimator).with(mIvPowerAnimator);
        set.play(mVisitorAnimator).with(mGoHomeAnimator);
        set.start();
    }

    private void imgOut() {
        ObjectAnimator mIvMesssgeAnimator = ObjectAnimator.ofFloat(binding.ivMessage, "translationX", 0, getResources().getDisplayMetrics().widthPixels).setDuration(300);
        mIvMesssgeAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mIvPowerAnimator = ObjectAnimator.ofFloat(binding.power, "translationX", 0, -getResources().getDimension(R.dimen.y60) - binding.power.getWidth()).setDuration(300);
        mIvMesssgeAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mVisitorAnimator = ObjectAnimator.ofFloat(binding.visitorInfo, "translationX", 0, -getResources().getDimension(R.dimen.y60) - binding.visitorInfo.getWidth()).setDuration(300);
        mIvMesssgeAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mGoHomeAnimator = ObjectAnimator.ofFloat(binding.ivGoMyHouse, "translationX", 0, -getResources().getDimension(R.dimen.y60) - binding.ivGoMyHouse.getWidth()).setDuration(300);
        mGoHomeAnimator.setInterpolator(new OvershootInterpolator());
        AnimatorSet set = new AnimatorSet();
        set.play(mIvMesssgeAnimator).with(mIvPowerAnimator);
        set.play(mVisitorAnimator).with(mGoHomeAnimator);
        set.start();
    }


    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        binding.map.setOnImageClickLietener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    protected void onDestroy() {
        super.onDestroy();
        if (initDisposable != null && !initDisposable.isDisposed()) initDisposable.dispose();
        if (resolvDisposable != null && !resolvDisposable.isDisposed()) resolvDisposable.dispose();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadPower();
    }

    @Override
    protected void initListeners() {
        mRlRoleJuQing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRubbishEntity.getName().equals("你又扔了3个垃圾进去")) {
                    binding.map.clearAllView();
                    binding.map.addTouchView(HouseHisActivity.this);
                    binding.map.setIsHis(true);
                    mPresenter.getHiVisitorsInfo(id);
                    mPresenter.loadHouseObjects(false, id);
                    binding.map.setOnImageClickLietener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
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

                mRlRoleJuQing.setVisibility(View.GONE);
                mTvContent.setVisibility(View.GONE);
                mTvJuQing.setVisibility(View.GONE);
                mIvGongXI.setVisibility(View.GONE);
                mRleSelect.setVisibility(View.GONE);
            }
        });
        mTvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        Intent i6 = new Intent(HouseHisActivity.this, StorageActivity.class);
                        startActivity(i6);
                        break;
                    case 3:

                        break;
                }
            }
        });
        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int type = mRubbishEntity.getType();
                switch (type) {
                    case 1:
                        mRlRoleJuQing.setVisibility(View.GONE);
                        mIvGongXI.setVisibility(View.GONE);
                        mTvJuQing.setVisibility(View.GONE);
                        mTvContent.setVisibility(View.GONE);
                        mRleSelect.setVisibility(View.GONE);
                        Intent i = new Intent(HouseHisActivity.this, MapEventNewActivity.class);
                        i.putExtra("id", mRubbishEntity.getScriptId());
                        i.putExtra("type", mRubbishEntity.getId());
                        startActivity(i);
                        break;
                    case 2:
                        mPresenter.houseToolUse(mRubbishEntity.getId());
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {
        mPresenter.getHiVisitorsInfo(id);

    }

    @Override
    public void onFailure(int code, String msg) {
        if (!msg.equals("体力值不足")) {
            ErrorCodeUtils.showErrorMsgByCode(this, code, msg);
            finish();
        } else {
            if (!PreferenceUtils.getAuthorInfo().isVip()) {
                goPayVip();
            }
        }

    }

    private void goPayVip() {
        final AlertDialogUtil alertDialogUtilVip = AlertDialogUtil.getInstance();
        alertDialogUtilVip.createNormalDialog(HouseHisActivity.this, "需要VIP酱帮助你增加体力上线呢?");
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

    /**
     * 获取所有宅屋数据
     *
     * @param entities
     */
    @Override
    public void onLoadHouseObjects(ArrayList<MapEntity> entities) {
        for (MapEntity entity : entities) {
            if (entity.getType() == 2) {
                entity.setShows("1,2,3,4,5");
            } else if (entity.getType() == 3) {
                entity.setShows("1,2,3,4,5");
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
                mPresenter.addMapMark(HouseHisActivity.this, mContainer, binding.map, "house");
                if (errorList.size() > 0) {
                    resolvErrorList(errorList, "house");
                }
            }
        });
    }

    /**
     * 收集好感度
     *
     * @param entity
     */
    @Override
    public void onLoadRoleLikeCollect(HouseLikeEntity entity) {

    }

    /**
     * 访客总数
     *
     * @param entities
     */
    @Override
    public void getVisitorsInfoSuccess(ArrayList<VisitorsEntity> entities) {

    }

    @Override
    public void saveVisitorSuccess() {
        if (type == 3) {

        } else {
            showToast("你破坏了TA的好感度哦~");
            mPresenter.loadPower();
        }
    }

    /**
     * 捡垃圾获取碎片
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
                Glide.with(HouseHisActivity.this)
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
                Glide.with(HouseHisActivity.this)
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
                Glide.with(HouseHisActivity.this)
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
     * 获取
     */
    @Override
    public void onLoadHouseSave() {

    }

    @Override
    public void onLoadPowerSuccess(PowerEntity entity) {
        if (entity != null) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) binding.roleHeartNumSmall.getLayoutParams();
            binding.roleHeartNum.setText(entity.getHealthPoint() + "/" + entity.getMaxHealthPoint());
            if (entity.getHealthPoint() < entity.getMaxHealthPoint()) {
                binding.roleHeartNumSmall.setVisibility(View.VISIBLE);
                int num = 240 - (240 * entity.getHealthPoint() / entity.getMaxHealthPoint());
                if (num > 220) {
                    binding.roleHeartNumSmall.setVisibility(View.GONE);
                    binding.fl.setBackgroundResource(R.drawable.shape_role_bg_two);
                } else {
                    binding.roleHeartNumSmall.setVisibility(View.VISIBLE);
                    binding.fl.setBackgroundResource(R.drawable.shape_power_bg);
                    params.width = num;
                }
            }
        }


    }

    /**
     * 他人宅屋访客记录
     *
     * @param entities
     */
    @Override
    public void getHisVisitorsInfo(ArrayList<VisitorsEntity> entities) {
        binding.llLikeUserRoot.removeAllViews();
        if (entities != null && entities.size() > 0) {
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
            for (int i = showSize - 1; i >= 0; i--) {
                final VisitorsEntity userEntity = entities.get(i);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(trueSize, trueSize);
                if (i != 0) {
                    lp.leftMargin = startMargin;
                }
                View likeUser = LayoutInflater.from(this).inflate(R.layout.item_white_border_img, null);
                likeUser.setLayoutParams(lp);
                ImageView img = likeUser.findViewById(R.id.iv_img);
                Glide.with(this)
                        .load(StringUtils.getUrl(this, userEntity.getVisitorImage(), imgSize, imgSize, false, true))
                        .error(R.drawable.bg_default_circle)
                        .placeholder(R.drawable.bg_default_circle)
                        .bitmapTransform(new CropCircleTransformation(this))
                        .into(img);
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
        } else {
            binding.visitorInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadHouseToolUse(PropUseEntity entity) {
        mRlRoleJuQing.setVisibility(View.GONE);
        mIvGongXI.setVisibility(View.GONE);
        mTvContent.setVisibility(View.GONE);
        mTvJuQing.setVisibility(View.GONE);
        mRleSelect.setVisibility(View.GONE);
        showToast(entity.getToolUseMessage());
    }

    /**
     * 创建订单
     *
     * @param entity
     */
    @Override
    public void onCreateOrderSuccess(OrderEntity entity) {
        entitPay = entity;
        if (bottomFragment != null)
            bottomFragment.show(getSupportFragmentManager(), "payMenu");
    }

    /**
     * 支付回调
     *
     * @param entity
     */
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
                PingppUI.createPay(HouseHisActivity.this, entity.getCharge().toString(), new PaymentHandler() {
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

    }

    @Override
    public void getHouseNewSuccess(ArrayList<SearchNewListEntity> searchNewLists) {

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
                        mPresenter.addMapMark(HouseHisActivity.this, mContainer, binding.map, "house");
                    }
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void houseHisLikeEvent(HouseHisLikeEvent event) {
        if (event != null) {
            String roleId = event.getRoleId();
            if (event.getType() == 3) {
                type = 3;
                mPresenter.saveVisitor(new SaveVisitorEntity("", id, 3));
                mPresenter.loadHouseRubblish(new RubblishBody(id, roleId, ""));
            } else {
                type = 2;
                mPresenter.saveVisitor(new SaveVisitorEntity(roleId, id, 2));
            }
        }
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
                        IpAdressUtils.getIpAdress(HouseHisActivity.this),
                        entitPay.getOrderId(),
                        entitPay.getAddress().getPhone(),
                        "",
                        entitPay.getAddress().getUserName());
                mPresenter.payOrder(entity);
            }
        });
    }


    public class Presenter {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_back:
                    finish();
                    break;
                case R.id.iv_message:
                    Intent i1 = new Intent(HouseHisActivity.this, CommentsListActivity.class);
                    i1.putExtra("uuid", id);
                    startActivity(i1);
                    break;
                case R.id.visitor_info:
                    Intent i2 = new Intent(HouseHisActivity.this, NewVisitorActivity.class);
                    i2.putExtra("uuid", id);
                    startActivity(i2);
                    break;
                case R.id.iv_go_my_house:
                    Intent i3 = new Intent(HouseHisActivity.this, HouseActivity.class);
                    startActivity(i3);
                    finish();
                    break;
                case R.id.power:
                    if (!PreferenceUtils.getAuthorInfo().isVip()) {
                        final AlertDialogUtil alertDialogUtilVip = AlertDialogUtil.getInstance();
                        alertDialogUtilVip.createNormalDialog(HouseHisActivity.this, "VIP酱偷偷的说:“VIP可以多20%体力上限哦”");
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
                    }
                    break;

            }
        }
    }

}
