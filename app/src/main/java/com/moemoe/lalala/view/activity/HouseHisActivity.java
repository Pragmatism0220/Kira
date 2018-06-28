package com.moemoe.lalala.view.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
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
import com.moemoe.lalala.model.entity.RubbishEntity;
import com.moemoe.lalala.model.entity.RubblishBody;
import com.moemoe.lalala.model.entity.SaveVisitorEntity;
import com.moemoe.lalala.model.entity.VisitorsEntity;
import com.moemoe.lalala.presenter.DormitoryContract;
import com.moemoe.lalala.presenter.DormitoryPresenter;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.FileUtil;
import com.moemoe.lalala.utils.GreenDaoManager;
import com.moemoe.lalala.utils.MapUtil;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.StorageUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.base.BaseActivity;

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

        binding.map.setIsHis(true);
        mPresenter.loadHouseObjects(false, id);
        mContainer = new MapMarkContainer();
        initMap();
        EventBus.getDefault().register(this);
    }

    public void initMap() {
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

    private void imgIn() {

        ObjectAnimator mIvMesssgeAnimator = ObjectAnimator.ofFloat(binding.ivMessage, "translationX", getResources().getDisplayMetrics().widthPixels, 0).setDuration(300);
        mIvMesssgeAnimator.setInterpolator(new OvershootInterpolator());
        AnimatorSet set = new AnimatorSet();

        set.play(mIvMesssgeAnimator);
        set.start();
    }

    private void imgOut() {
        ObjectAnimator mIvMesssgeAnimator = ObjectAnimator.ofFloat(binding.ivMessage, "translationX", 0, getResources().getDisplayMetrics().widthPixels).setDuration(300);
        mIvMesssgeAnimator.setInterpolator(new OvershootInterpolator());
        AnimatorSet set = new AnimatorSet();
        set.play(mIvMesssgeAnimator);
        set.start();
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

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
    }


    @Override
    protected void initListeners() {
        mRlRoleJuQing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int type = mRubbishEntity.getType();
//                if (type == 3 && mIvGongXI.getVisibility() == View.VISIBLE) {
//                    mRlRoleJuQing.setVisibility(View.GONE);
//                    mTvContent.setVisibility(View.GONE);
//                    mTvJuQing.setVisibility(View.GONE);
//                    mIvGongXI.setVisibility(View.GONE);
//                    mRleSelect.setVisibility(View.GONE);
//                } else if (type == 2 && mIvGongXI.getVisibility() == View.VISIBLE) {
//                    mRlRoleJuQing.setVisibility(View.GONE);
//                    mTvContent.setVisibility(View.GONE);
//                    mTvJuQing.setVisibility(View.GONE);
//                    mIvGongXI.setVisibility(View.GONE);
//                    mRleSelect.setVisibility(View.GONE);
//                } else if (type == 4 && mIvGongXI.getVisibility() == View.VISIBLE) {
//                    mRlRoleJuQing.setVisibility(View.GONE);
//                    mTvContent.setVisibility(View.GONE);
//                    mTvJuQing.setVisibility(View.GONE);
//                    mIvGongXI.setVisibility(View.GONE);
//                    mRleSelect.setVisibility(View.GONE);
//                }
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
                    case 3:
                    case 4:
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
        ErrorCodeUtils.showErrorMsgByCode(this, code, msg);
        finish();
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
    }

    /**
     * 获取
     */
    @Override
    public void onLoadHouseSave() {

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
            }
        }
    }
}
