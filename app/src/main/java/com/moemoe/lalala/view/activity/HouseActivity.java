package com.moemoe.lalala.view.activity;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.AppSetting;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityHouseBinding;
import com.moemoe.lalala.di.components.DaggerHouseComponent;
import com.moemoe.lalala.di.modules.HouseModule;
import com.moemoe.lalala.galgame.FileManager;
import com.moemoe.lalala.galgame.SoundManager;
import com.moemoe.lalala.model.entity.HouseLikeEntity;
import com.moemoe.lalala.model.entity.MapDbEntity;
import com.moemoe.lalala.model.entity.MapEntity;
import com.moemoe.lalala.model.entity.MapMarkContainer;
import com.moemoe.lalala.presenter.DormitoryContract;
import com.moemoe.lalala.presenter.DormitoryPresenter;
import com.moemoe.lalala.utils.DialogUtils;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.FileUtil;
import com.moemoe.lalala.utils.GreenDaoManager;
import com.moemoe.lalala.utils.MapUtil;
import com.moemoe.lalala.utils.NetworkUtils;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.StorageUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.view.base.BaseActivity;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

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

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_house);
        binding.setPresenter(new Presenter());
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
    }

    public void initMap() {
        mPresenter.loadHouseObjects();
        binding.map.setOnImageClickLietener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onFailure(int code, String msg) {
        ErrorCodeUtils.showErrorMsgByCode(HouseActivity.this, code, msg);
    }

    @Override
    public void onLoadHouseObjects(ArrayList<MapEntity> entities) {
        for (MapEntity entity : entities) {
            if (entity.getType() == 2) {
                entity.setShows("1,2,3,4,5");
            } else if (entity.getType() == 3) {
                entity.setShows("1,2,3,4,5");
            }
        }
        final ArrayList<MapDbEntity> res = new ArrayList<>();
        final ArrayList<MapDbEntity> errorList = new ArrayList<>();
        MapUtil.checkAndDownloadHouse(this, true, MapDbEntity.toDb(entities, "house"), "house", new Observer<MapDbEntity>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                initDisposable = d;
            }

            @Override
            public void onNext(@NonNull MapDbEntity mapDbEntity) {
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
                GreenDaoManager.getInstance().getSession().getMapDbEntityDao().insertOrReplaceInTx(res);
                mPresenter.addMapMark(HouseActivity.this, mContainer, binding.map, "house");
                if (errorList.size() > 0) {
                    resolvErrorList(errorList, "house");
                }
            }
        });
    }

    /**
     * 读取好感度值
     */
    @Override
    public void onLoadRoleLikeGet(ArrayList<HouseLikeEntity> entity) {

    }

    /**
     * 采集好感度值
     */
    @Override
    public void onLoadRoleLikeCollect(HouseLikeEntity entity) {

    }

    private void resolvErrorList(ArrayList<MapDbEntity> errorList, final String type) {
        final ArrayList<MapDbEntity> errorListTmp = new ArrayList<>();
        final ArrayList<MapDbEntity> res = new ArrayList<>();
        MapUtil.checkAndDownloadHouse(this, false, errorList, type, new Observer<MapDbEntity>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                resolvDisposable = d;
            }

            @Override
            public void onNext(@NonNull MapDbEntity mapDbEntity) {
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
                GreenDaoManager.getInstance().getSession().getMapDbEntityDao().insertOrReplaceInTx(res);
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
        ObjectAnimator mVisitorInfoAnimator = ObjectAnimator.ofFloat(binding.visitorInfo, "translationY", binding.visitorInfo.getHeight() - getResources().getDimension(R.dimen.y60), 0).setDuration(300);
        mVisitorInfoAnimator.setInterpolator(new OvershootInterpolator());
        AnimatorSet set = new AnimatorSet();
        set.play(phoneAnimator).with(mRoleAnimator);
        set.play(mStorageAnimator).with(mDramaAnimator);
        set.play(mVisitorInfoAnimator);
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
        ObjectAnimator mVisitorInfoAnimator = ObjectAnimator.ofFloat(binding.visitorInfo, "translationY", 0, -getResources().getDimension(R.dimen.y10) - -binding.visitorInfo.getHeight() * 2).setDuration(300);
        mVisitorInfoAnimator.setInterpolator(new OvershootInterpolator());
        AnimatorSet set = new AnimatorSet();
        set.play(phoneAnimator).with(mRoleAnimator);
        set.play(mStorageAnimator).with(mDramaAnimator);
        set.play(mVisitorInfoAnimator);
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
            }
        }
    }
}
