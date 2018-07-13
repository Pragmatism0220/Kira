package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.UserLoginSevenEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/12.
 */

public interface SevenDayLoginContract {
    interface Presenter extends BasePresenter {
        void getSevenLoginInfo();
        void isComplete();
        void getReward();
    }

    interface View extends BaseView {
        void getSevenLoginInfo(UserLoginSevenEntity entities);
        void isCompleteSuccess(boolean isComplete);
        void getRewardSuccess(String message);
    }
}
