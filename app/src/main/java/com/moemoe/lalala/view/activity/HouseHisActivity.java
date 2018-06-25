package com.moemoe.lalala.view.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.databinding.DataBindingUtil;
import android.os.WorkSource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityHouseHisBinding;
import com.moemoe.lalala.di.components.DaggerHouseComponent;
import com.moemoe.lalala.di.modules.HouseModule;
import com.moemoe.lalala.event.HouseLikeEvent;
import com.moemoe.lalala.model.entity.HouseDbEntity;
import com.moemoe.lalala.model.entity.HouseLikeEntity;
import com.moemoe.lalala.model.entity.MapEntity;
import com.moemoe.lalala.model.entity.MapMarkContainer;
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
import com.moemoe.lalala.view.widget.map.TouchImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

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
        binding.map.setIsHis(true);
        mPresenter.loadHouseObjects(false, id);
        mContainer = new MapMarkContainer();
        initMap();
        EventBus.getDefault().register(this);
    }

    public void initMap() {
//        mPresenter.addMapMark(HouseHisActivity.this, mContainer, binding.map, "house");
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
            showToast("捡起了一个垃圾哟~~真勤快~~");
        } else {
            showToast("你破坏了对方的好感度气泡");
        }
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
    public void houseLikeEvent(HouseLikeEvent event) {
        if (event != null) {
            String roleId = event.getRoleId();
            if (TextUtils.isEmpty(roleId)) {
                type = 3;
                mPresenter.saveVisitor(new SaveVisitorEntity("", id, 3));
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
            }
        }
    }
}
