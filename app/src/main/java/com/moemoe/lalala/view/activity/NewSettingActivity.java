package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.AppSetting;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityNewSettingBinding;
import com.moemoe.lalala.di.components.DaggerNewSettingComponent;
import com.moemoe.lalala.di.modules.NewSettingModule;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.Image;
import com.moemoe.lalala.model.entity.UserInfo;
import com.moemoe.lalala.netamusic.player.AudioPlayer;
import com.moemoe.lalala.presenter.NewSettingContract;
import com.moemoe.lalala.presenter.NewSettingPresenter;
import com.moemoe.lalala.utils.JuQingUtil;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.view.base.BaseActivity;

import javax.inject.Inject;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import io.rong.imkit.RongIM;

import static com.moemoe.lalala.utils.StringUtils.isThirdParty;

public class NewSettingActivity extends BaseActivity implements NewSettingContract.View {

    private ActivityNewSettingBinding binding;
    private TextView mTvTitle;
    private ImageView mIvBack;
    public static final int REQUEST_SETTING_LOGIN = 3000;
    public static final int REQUEST_SETTING_LOGOUT = 3100;
    private String userId;
    private boolean mIsLogin;
    private int mTvKiraNum;
    private UserInfo mInfo;


    @Inject
    NewSettingPresenter mPresenter;

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_setting);
        DaggerNewSettingComponent.builder()
                .newSettingModule(new NewSettingModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        mTvTitle = findViewById(R.id.tv_toolbar_title);
        mIvBack = findViewById(R.id.iv_back);
        binding.setPresenter(new Presenter());
        Intent intent = getIntent();
        userId = intent.getStringExtra("useId");
        mPresenter.getUserInfo(userId);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mIsLogin = PreferenceUtils.isLogin();
        if (mIsLogin) {
            binding.settingLogOut.setText(R.string.label_log_out);
        } else {
            binding.settingLogOut.setText(R.string.label_login);
        }

    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mTvTitle.setText(R.string.label_setting);
        mTvTitle.setTextColor(getResources().getColor(R.color.black));
        mIvBack.setVisibility(View.VISIBLE);
        mIvBack.setImageResource(R.drawable.btn_back_black_normal);
        mIvBack.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initListeners() {


    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SETTING_LOGIN) {
            finish();
        }
    }

    /**
     * 登出
     */
    private void logout() {
        mPresenter.logout();
    }

    @Override
    public void onFailure(int code, String msg) {

    }

    @Override
    public void onLoadUserInfo(UserInfo info) {
        if (info != null) {
            mInfo = info;
            binding.settingName.setText(info.getUserName());
            mTvKiraNum = info.getUserNo();
            if (info.getVipTime() == "") {//不是会员
                binding.settingVip.setVisibility(View.GONE);
                binding.settingTime.setVisibility(View.GONE);
                Glide.with(this).load(ApiService.URL_QINIU + info.getHeadPath()).into(binding.settingAvatar);
            } else {
                binding.settingVip.setVisibility(View.VISIBLE);
                binding.settingTime.setVisibility(View.VISIBLE);
                binding.settingTime.setText(String.format("会员截止日期%s", info.getVipTime()));
                Glide.with(this).load(ApiService.URL_QINIU + info.getHeadPath()).into(binding.settingAvatar);
            }
        }
    }

    @Override
    public void onLoadUserInfoFail() {
        showToast("获取个人信息失败,请稍后再试!");

    }

    @Override
    public void logoutSuccess() {

        //清除数据库相关私信信息
        //私信列表
        try {
            AppSetting.isLoadDone = false;
            RongIM.getInstance().logout();
            PreferenceUtils.setNormalMsgDotNum(this, 0);
            PreferenceUtils.setAtUserMsgDotNum(this, 0);
            PreferenceUtils.setNetaMsgDotNum(this, 0);
            PreferenceUtils.setSysMsgDotNum(this, 0);
            PreferenceUtils.setRCDotNum(this, 0);
            PreferenceUtils.setGroupDotNum(this, 0);
            PreferenceUtils.setJuQingDotNum(this, 0);
            PreferenceUtils.setMessageDot(this, "system", false);
            PreferenceUtils.setMessageDot(this, "neta", false);
            PreferenceUtils.setMessageDot(this, "at_user", false);
            PreferenceUtils.setMessageDot(this, "normal", false);

            AudioPlayer.get().clearList(true);
            if (AudioPlayer.get().isPlaying()) {
                AudioPlayer.get().stopPlayer();
            }

            JuQingUtil.clearJuQingDone();
            if (isThirdParty(PreferenceUtils.getAuthorInfo().getPlatform())) {
                Platform p = ShareSDK.getPlatform(PreferenceUtils.getAuthorInfo().getPlatform());
                if (p.isAuthValid()) {
                    p.removeAccount(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            PreferenceUtils.clearAuthorInfo();
            setResult(REQUEST_SETTING_LOGOUT);
            finish();
        }

    }

    public boolean isThirdParty(String platform) {
        return platform != null && (platform.equals(cn.sharesdk.tencent.qq.QQ.NAME) || platform.equals(Wechat.NAME) || platform.equals(SinaWeibo.NAME));
    }


    public class Presenter {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.setting_item_one:
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), EditMyInfoActivity.class);
                    intent.putExtra("info", mInfo);
                    startActivity(intent);
                    break;
                case R.id.setting_item_two:
                    Intent intent1 = new Intent(getApplicationContext(), AccountAndSecurityActivity.class);
                    intent1.putExtra("mTvKiraNum", mTvKiraNum + "");
                    startActivity(intent1);
                    break;
                case R.id.setting_item_three:
                    Intent intent2 = new Intent(getApplicationContext(), CommonUseActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.setting_item_four:
                    Intent intent3 = new Intent(getApplicationContext(), AboutActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.setting_log_out:
                    if (mIsLogin) {
                        logout();
                    } else {
                        Intent intent5 = new Intent(getApplicationContext(), LoginActivity.class);
                        intent5.putExtra(LoginActivity.EXTRA_KEY_SETTING, false);
                        startActivityForResult(intent5, REQUEST_SETTING_LOGIN);
                    }
                    break;
                default:
                    break;
            }
        }

    }
}
