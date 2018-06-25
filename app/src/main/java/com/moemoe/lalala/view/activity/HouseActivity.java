package com.moemoe.lalala.view.activity;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.AppSetting;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityHouseBinding;
import com.moemoe.lalala.di.components.DaggerHouseComponent;
import com.moemoe.lalala.di.modules.HouseModule;
import com.moemoe.lalala.event.HouseLikeEvent;
import com.moemoe.lalala.galgame.FileManager;
import com.moemoe.lalala.galgame.SoundManager;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.HouseDbEntity;
import com.moemoe.lalala.model.entity.HouseLikeEntity;
import com.moemoe.lalala.model.entity.MapEntity;
import com.moemoe.lalala.model.entity.MapMarkContainer;
import com.moemoe.lalala.model.entity.RubbishEntity;
import com.moemoe.lalala.model.entity.RubblishBody;
import com.moemoe.lalala.model.entity.SaveVisitorEntity;
import com.moemoe.lalala.model.entity.VisitorsEntity;
import com.moemoe.lalala.presenter.DormitoryContract;
import com.moemoe.lalala.presenter.DormitoryPresenter;
import com.moemoe.lalala.utils.AlertDialogUtil;
import com.moemoe.lalala.utils.DialogUtils;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.FileUtil;
import com.moemoe.lalala.utils.GreenDaoManager;
import com.moemoe.lalala.utils.MapUtil;
import com.moemoe.lalala.utils.NetworkUtils;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.StorageUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.widget.map.MapLayout;
import com.moemoe.lalala.view.widget.map.TouchImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.reactivestreams.Subscription;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableSubscriber;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.view.View.DRAWING_CACHE_QUALITY_AUTO;
import static android.view.View.DRAWING_CACHE_QUALITY_HIGH;
import static android.view.View.DRAWING_CACHE_QUALITY_LOW;

public class HouseActivity extends BaseActivity implements DormitoryContract.View {


    private boolean mIsOut = false;
    private ActivityHouseBinding binding;
    static private Activity instance;
    private String mSchema;

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
    private TextView mTvRewardName;
    private RelativeLayout mRleSelect;
    private TextView mTvLeft;
    private TextView mTvRight;
    private ImageView mIvGongXI;
    private RubbishEntity mRubbishEntity;
    private TextView mTvJuQing;
    private TextView mTvContent;

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_house);
        binding.setPresenter(new Presenter());
        mRlRoleRoot = findViewById(R.id.rl_role_root);
        mTvRoleNum = findViewById(R.id.tv_role_num);
        mTvRoleName = findViewById(R.id.tv_role_name);
        mRlRoleJuQing = findViewById(R.id.rl_role_juqing);
        mIvCover = findViewById(R.id.iv_cover_next);
        mRleSelect = findViewById(R.id.rl_select);
        mTvLeft = findViewById(R.id.tv_left);
        mTvRight = findViewById(R.id.tv_right);
        mIvGongXI = findViewById(R.id.iv_gongxi);
        mTvJuQing = findViewById(R.id.tv_juqing);
        mTvContent = findViewById(R.id.tv_content_gongxi);
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
        EventBus.getDefault().register(this);
    }

    public void initMap() {
        mPresenter.addMapMark(HouseActivity.this, mContainer, binding.map, "house");
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.map.clearAllView();
        binding.map.addTouchView(HouseActivity.this);
        mPresenter.getVisitorsInfo();
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
        mRlRoleRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRlRoleRoot.setVisibility(View.GONE);
            }
        });
        mRlRoleJuQing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int type = mRubbishEntity.getType();
                if (type == 3 && mIvGongXI.getVisibility() == View.VISIBLE) {
                    mRlRoleJuQing.setVisibility(View.GONE);
                    mTvContent.setVisibility(View.GONE);
                    mTvJuQing.setVisibility(View.GONE);
                    mIvCover.setImageResource(R.drawable.bg_garbage_background_1);
                    mIvGongXI.setVisibility(View.GONE);
                    mRleSelect.setVisibility(View.GONE);
                }
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
                        mIvCover.setImageResource(R.drawable.bg_garbage_background_1);
                        break;
                    case 2:
                        showToast("放入成功");
                        mRlRoleJuQing.setVisibility(View.GONE);
                        mIvGongXI.setVisibility(View.GONE);
                        mTvContent.setVisibility(View.GONE);
                        mTvJuQing.setVisibility(View.GONE);
                        mRleSelect.setVisibility(View.GONE);
                        mIvCover.setImageResource(R.drawable.bg_garbage_background_1);
                        mPresenter.loadHouseSave(new RubblishBody(PreferenceUtils.getUUid(), "", mRubbishEntity.getId()));
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
                        mIvCover.setImageResource(R.drawable.bg_garbage_background_1);
                        Intent i = new Intent(HouseActivity.this, MapEventNewActivity.class);
                        i.putExtra("id", mRubbishEntity.getScriptId());
                        i.putExtra("type", true);
                        startActivity(i);
                        break;
                    case 2:
                    case 3:
                        showToast("功能未开放~");
//                        mRlRoleJuQing.setVisibility(View.GONE);
//                        mIvGongXI.setVisibility(View.GONE);
//                        mTvContent.setVisibility(View.GONE);
//                        mTvJuQing.setVisibility(View.GONE);
//                        mRleSelect.setVisibility(View.GONE);
//                        mIvCover.setImageResource(R.drawable.bg_garbage_background_1);
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onFailure(int code, String msg) {
        ErrorCodeUtils.showErrorMsgByCode(HouseActivity.this, code, msg);
        finish();
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
            binding.visitorCount.setText(entities.get(0).getCount() + "");
        } else {
            binding.visitorInfo.setVisibility(View.GONE);
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
        mRubbishEntity = entity;
        if (entity != null && entity.getType() != 0) {
            mRlRoleJuQing.setVisibility(View.VISIBLE);
            io.reactivex.Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                    Thread.sleep(200);
                    e.onNext(1);
                    Thread.sleep(200);
                    e.onNext(2);
                    e.onComplete();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Integer integer) {
                            if (integer == 1) {
                                mIvCover.setImageResource(R.drawable.bg_garbage_background_2);
                            } else if (integer == 2) {
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
                                } else if (type == 3) {
                                    mIvCover.setImageResource(R.drawable.bg_garbage_background_3);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            mIvGongXI.setVisibility(View.VISIBLE);
                            int type = entity.getType();
                            if (type == 1) {//剧情
                                mRleSelect.setVisibility(View.VISIBLE);
                                mTvJuQing.setText(entity.getName());
                                mTvJuQing.setVisibility(View.VISIBLE);
                                mTvLeft.setText("之后再看");
                                mTvRight.setText("观看剧情");
                            } else if (type == 2) {
                                mRleSelect.setVisibility(View.VISIBLE);
                                mTvJuQing.setText(entity.getName());
                                mTvJuQing.setVisibility(View.VISIBLE);
                                mTvLeft.setText("放入储物箱");
                                mTvRight.setText("立即使用");
                            } else if (type == 3) {
                                mRleSelect.setVisibility(View.GONE);
                                mTvJuQing.setText("(点击任意区域关闭)");
                                mTvContent.setText(entity.getName());
                                mTvContent.setVisibility(View.VISIBLE);
                                mTvJuQing.setVisibility(View.VISIBLE);
                            }
                        }
                    });

        }
    }

    /**
     * 保存
     */
    @Override
    public void onLoadHouseSave() {
        showToast("放入成功");
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
        ObjectAnimator mIvSleepAnimator = ObjectAnimator.ofFloat(binding.ivSleep, "translationX", getResources().getDisplayMetrics().widthPixels, 0).setDuration(300);
        mIvSleepAnimator.setInterpolator(new OvershootInterpolator());
        AnimatorSet set = new AnimatorSet();
        set.play(phoneAnimator).with(mRoleAnimator);
        set.play(mStorageAnimator).with(mDramaAnimator);
        set.play(mVisitorInfoAnimator).with(mIvSleepAnimator);
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
        ObjectAnimator mIvSleepAnimator = ObjectAnimator.ofFloat(binding.ivSleep, "translationX", 0, getResources().getDisplayMetrics().widthPixels).setDuration(300);
        mIvSleepAnimator.setInterpolator(new OvershootInterpolator());
        AnimatorSet set = new AnimatorSet();
        set.play(phoneAnimator).with(mRoleAnimator);
        set.play(mStorageAnimator).with(mDramaAnimator);
        set.play(mVisitorInfoAnimator).with(mIvSleepAnimator);
        set.start();
    }

    public class Presenter {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_left:
                    Intent i3 = new Intent(HouseActivity.this, FeedV3Activity.class);
                    startActivity(i3);
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
//                    Intent i7 = new Intent(HouseActivity.this, VisitorsActivity.class);
                    startActivity(i7);
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
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void houseLikeEvent(HouseLikeEvent event) {
        if (event != null) {
            String roleId = event.getRoleId();
            int type = event.getType();
            if (type == 3) {
                mPresenter.saveVisitor(new SaveVisitorEntity("", PreferenceUtils.getUUid(), 3));
                mPresenter.loadHouseRubblish(new RubblishBody(PreferenceUtils.getUUid(), roleId, ""));
            } else {
                this.type = 2;
                mPresenter.loadRoleLikeCollect(roleId);
            }
        }
    }
}
