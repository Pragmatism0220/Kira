package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.LoginEntity;
import com.moemoe.lalala.model.entity.LoginResultEntity;

import java.util.Date;

/**
 * Created by yi on 2016/11/27.
 */

public interface LoginContract {
    interface Presenter extends BasePresenter{
        void login(LoginEntity entity);
        void loginThird(String platform,String DevId);
        void getServerTime();
    }

    interface View extends BaseView{
        void onLoginSuccess(LoginResultEntity entity);
        void onLoginThirdSuccess(String id);
        void onGetTimeSuccess(Date time);
    }
}
