package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.UserInfo;

/**
 * Created by Administrator on 2018/6/26.
 */

public interface NewSettingContract {

    interface Presenter extends BasePresenter {
        void getUserInfo(String userId);
        void logout();
    }

    interface View extends BaseView {
        void onLoadUserInfo(UserInfo info);
        void onLoadUserInfoFail();
        void logoutSuccess();
    }
}
