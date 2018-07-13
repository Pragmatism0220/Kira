package com.moemoe.lalala.view.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivitySevenDayLoginBinding;
import com.moemoe.lalala.di.components.DaggerSevenDayLoginComponent;
import com.moemoe.lalala.di.modules.SevenDayLoginModule;
import com.moemoe.lalala.model.entity.UserLoginSevenEntity;
import com.moemoe.lalala.presenter.SevenDayLoginContract;
import com.moemoe.lalala.presenter.SevenDayLoginPresenter;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.view.base.BaseActivity;

import java.util.ArrayList;

import javax.inject.Inject;

public class SevenDayLoginActivity extends BaseActivity implements SevenDayLoginContract.View {

    private ActivitySevenDayLoginBinding binding;

    @Inject
    SevenDayLoginPresenter mPresenter;

    private UserLoginSevenEntity user;
    private boolean isGetReward = false;//未领取
    private int days = 0;
    private View[] imageView;

    @Override
    protected void initComponent() {
        DaggerSevenDayLoginComponent.builder()
                .sevenDayLoginModule(new SevenDayLoginModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_seven_day_login);
        binding.setPresenter(new Presenter());
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
    }


    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {
        mPresenter.getSevenLoginInfo();
        mPresenter.isComplete();

    }

    @Override
    public void onFailure(int code, String msg) {
        ErrorCodeUtils.showErrorMsgByCode(getApplicationContext(), code, msg);
    }

    @Override
    public void getSevenLoginInfo(UserLoginSevenEntity entities) {
        if (entities != null) {
            user = entities;
            if (entities.isLoginStatus()) {// ture
                binding.itemOne.setImageResource(R.drawable.ic_7days_complete_white);
            } else {
                binding.itemOne.setImageResource(R.drawable.ic_7days_uncomplete_white);
            }
            if (entities.isDocStatus()) {// false
                binding.itemTwo.setImageResource(R.drawable.ic_7days_complete_white);
            } else {
                binding.itemTwo.setImageResource(R.drawable.ic_7days_uncomplete_white);
            }
            if (entities.isSeeDepartmentStatus()) { //true
                binding.itemThree.setImageResource(R.drawable.ic_7days_complete_white);
            } else {
                binding.itemThree.setImageResource(R.drawable.ic_7days_uncomplete_white);
            }
            if (entities.isReplyDocStatus()) { //false
                binding.itemFour.setImageResource(R.drawable.ic_7days_complete_white);
            } else {
                binding.itemFour.setImageResource(R.drawable.ic_7days_uncomplete_white);
            }
            imageView = new View[]{binding.oneDay, binding.twoDay, binding.threeDay, binding.fourDay, binding.fiveDay, binding.sixDay,binding.sixDay};

            // 处理 days - 1 天显示
            if (entities.getDays() > 1) {
                for (int i = 0; i < entities.getDays() - 1; i++) {
                    imageView[i].setVisibility(View.VISIBLE);
                }
            }
            days = entities.getDays();


            // 处理 days 显示
            if (entities.isLoginStatus() && entities.isDocStatus() && entities.isSeeDepartmentStatus() && entities.isReplyDocStatus()) {
                if (true == entities.isRewardStatus()) {
                    binding.getAwardBtn.setImageResource(R.drawable.btn_7days_reward_null);
                    imageView[entities.getDays() - 1].setVisibility(View.VISIBLE);
                }
            } else {
                binding.getAwardBtn.setImageResource(R.drawable.btn_7days_reward_null);
            }


            if (entities.getDays() - 1 == 0) {
                binding.tvSevenAward.setText("今日奖励: 前传剧情");
            } else if (entities.getDays() - 1 == 1) {
                binding.tvSevenAward.setText("今日奖励: 233节操");
            } else if (entities.getDays() - 1 == 2) {
                binding.tvSevenAward.setText("今日奖励: 夏日躺椅");
            } else if (entities.getDays() - 1 == 3) {
                binding.tvSevenAward.setText("今日奖励: 莲的死库水");
            } else if (entities.getDays() - 1 == 4) {
                binding.tvSevenAward.setText("今日奖励: 随机宅屋地图");
            } else if (entities.getDays() - 1 == 5) {
                binding.tvSevenAward.setText("今日奖励: SR剧情卡");
            } else if (entities.getDays() - 1 == 6) {
                binding.tvSevenAward.setText("今日奖励: 角色 御坂美琴");
            }
            if (entities.isRewardStatus() == true) {//是否可以领取今日奖
                binding.getAwardBtn.setImageResource(R.drawable.btn_7days_reward_null);
            }
        }
    }

    @Override
    public void isCompleteSuccess(boolean isComplete) {

    }

    @Override
    public void getRewardSuccess(String message) {
        isGetReward = true; //领取
        showToast(message);
        binding.getAwardBtn.setImageResource(R.drawable.btn_7days_reward_null);
        imageView[days - 1].setVisibility(View.VISIBLE);
    }

    public class Presenter {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.role_back_btn:
                    finish();
                    break;
                case R.id.get_award_btn:
                    if (user != null) {
                        if (user.isRewardStatus() == true || isGetReward == true) {//是否可以领取今日奖
                            showToast("已经领取过");
                        } else {
                            if (user.isLoginStatus() && user.isDocStatus() && user.isSeeDepartmentStatus() && user.isReplyDocStatus()) {
                                mPresenter.getReward();
                            } else {
                                showToast("今日任务尚未完成");
                            }
                        }

                    }
                    break;
                default:
                    break;
            }
        }
    }
}
