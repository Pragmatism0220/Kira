package com.moemoe.lalala.view.activity;


import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.AppSetting;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityHouseBinding;
import com.moemoe.lalala.di.components.DaggerHouseComponent;
import com.moemoe.lalala.di.modules.HouseModule;
import com.moemoe.lalala.galgame.FileManager;
import com.moemoe.lalala.galgame.SoundManager;
import com.moemoe.lalala.model.entity.MapEntity;
import com.moemoe.lalala.presenter.DormitoryContract;
import com.moemoe.lalala.presenter.DormitoryPresenter;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.IntentUtils;
import com.moemoe.lalala.view.base.BaseActivity;

import java.util.ArrayList;

import javax.inject.Inject;

public class HouseActivity extends BaseActivity implements DormitoryContract.View {
    private boolean mIsOut = false;
    private ActivityHouseBinding binding;
    static private Activity instance;
    private String mSchema;

    @Inject
    DormitoryPresenter mPresenter;

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
//        if(mPreferMng == null){
//            mPreferMng = PreferenceManager.getInstance(this);
//        }
        DaggerHouseComponent.builder()
                .houseModule(new HouseModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);

        initMap();
        binding.map.setMapResource(R.drawable.big_house);
        SoundManager.init(this);
        FileManager.init(this);
        instance = this;
        if (!TextUtils.isEmpty(mSchema)) {
            IntentUtils.toActivityFromUri(this, Uri.parse(mSchema), null);
        }
    }

    public void initMap() {
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
        binding.map.addMapMarkView(R.drawable.bg_cardbg_myrole_background, 0.37f, 0.68f, "neta://com.moemoe.lalala/calui_1.0?uuid=be7a3728-7500-11e6-bdd7-e0576405f084&name=玛莎多拉", null, "00:00", "24:00", "00:00", "24:00", null);

        //-------白天
        binding.map.addMapMarkView(R.drawable.ic_home_role_len_1, 0.36f, 0.03f, null, "“他们是樱木军团：水户洋平，等等——”\n" + "“谁特么的是等等！！”", "06:00", "18:00", "06:00", "18:00", null);
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
    public void onLoadHouseInHouseFurnitures(ArrayList<MapEntity> entities) {

    }

    @Override
    public void onLoadHouseInHouseRubblish(ArrayList<MapEntity> entities) {

    }

    @Override
    public void onLoadHouseInHouseRoles(ArrayList<MapEntity> entities) {

    }

    public class Presenter {

    }


}
