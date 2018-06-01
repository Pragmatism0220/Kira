package com.moemoe.lalala.view.activity;


import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.AppSetting;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityHouseBinding;
import com.moemoe.lalala.di.components.DaggerHouseComponent;
import com.moemoe.lalala.di.modules.HouseModule;
import com.moemoe.lalala.galgame.FileManager;
import com.moemoe.lalala.galgame.SoundManager;
import com.moemoe.lalala.model.entity.MapDbEntity;
import com.moemoe.lalala.model.entity.MapEntity;
import com.moemoe.lalala.model.entity.MapMarkContainer;
import com.moemoe.lalala.presenter.DormitoryContract;
import com.moemoe.lalala.presenter.DormitoryPresenter;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.FileUtil;
import com.moemoe.lalala.utils.GreenDaoManager;
import com.moemoe.lalala.utils.MapUtil;
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
//        binding.map.setMapResource(R.drawable.big_house);
        SoundManager.init(this);
        FileManager.init(this);
    }

    public void initMap() {
        mPresenter.loadHouseObjects();
        binding.map.setOnImageClickLietener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsOut) {
                    mIsOut = false;
                } else {
                    mIsOut = true;

                }
            }
        });
//        binding.map.addMapMarkView(R.drawable.ic_home_role_len_1,  0.82f, 0.42f, (int) getResources().getDimension(R.dimen.x150), (int) getResources().getDimension(R.dimen.x150),  "neta://com.moemoe.lalala/department_1.0?uuid=393341d4-5f7f-11e6-a5af-d0a637eac7d7&name=体育馆", null, "00:00:00","24:00:00","00:00:00","24:00:00",null);
//        binding.map.addMapMarkView(R.drawable.ic_home_role_len_1, 0.37f, 0.68f, getResources().getDimension(R.dimen.x150), getResources().getDimension(R.dimen.x150), "neta://com.moemoe.lalala/calui_1.0?uuid=be7a3728-7500-11e6-bdd7-e0576405f084&name=玛莎多拉", "谁特么的是等等", "00:00:00", "24:00:00", "00:00:00", "24:00:00", null);
//        binding.map.addMapMarkView(R.drawable.ic_home_role_len_1, 0.26f, 0.30f, getResources().getDimension(R.dimen.x150), getResources().getDimension(R.dimen.x150), "neta://com.moemoe.lalala/department_1.0?uuid=10f8433e-5f80-11e6-b42a-d0a637eac7d7&name=保健室", "121", "00:00:00", "24:00:00", "00:00:00", "24:00:00", null);
//
//        //-------白天
        //-----事件
//        binding.map.addMapMarkView(R.drawable.btn_map_click, 0.48f, 0.19f,"neta://com.moemoe.lalala/event_1.0",null, "00:00","24:00","00:00","24:00", null);
//        binding.map.addMapMarkView(R.drawable.btn_map_gacha, 0.38f, 0.31f, "neta://com.moemoe.lalala/url_inner_1.0?http://prize.moemoe.la:8000/netaopera/chap1/?pass=" + mPreferMng.getPassEvent() + "&user_id=" + mPreferMng.getUUid(),null, "00:00","24:00","00:00","24:00", null);
//        binding.map.addMapMarkView(R.drawable.btn_map_gacha, 0.93f, 0.46f, "neta://com.moemoe.lalala/url_inner_1.0?http://prize.moemoe.la:8000/netaopera/chap2/?pass=" + mPreferMng.getPassEvent() + "&user_id=" + mPreferMng.getUUid(),null, "00:00","24:00","00:00","24:00", null);
//        binding.map.addMapMarkView(R.drawable.btn_map_gacha, 0.48f, 0.33f, "neta://com.moemoe.lalala/url_inner_1.0?http://prize.moemoe.la:8000/netaopera/chap3/?pass=" + mPreferMng.getPassEvent() + "&user_id=" + mPreferMng.getUUid(),null, "00:00","24:00","00:00","24:00", null);
//        binding.map.addMapMarkView(R.drawable.btn_map_gacha, 0.18f, 0.32f, "neta://com.moemoe.lalala/url_inner_1.0?http://prize.moemoe.la:8000/netaopera/chap5/?pass=" + mPreferMng.getPassEvent() + "&user_id=" + mPreferMng.getUUid(),null, "00:00","24:00","00:00","24:00", null);


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
//                mPresenter.addMapMark(HouseActivity.this, mContainer, binding.map, "house");
                if (errorList.size() > 0) {
                    resolvErrorList(errorList, "house");
                }
            }
        });
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
//                        mPresenter.addMapMark(HouseActivity.this, mContainer, binding.map, "house");
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

    public class Presenter {

    }
}
