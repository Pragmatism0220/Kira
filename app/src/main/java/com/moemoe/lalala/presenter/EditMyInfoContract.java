package com.moemoe.lalala.presenter;

/**
 * Created by Administrator on 2018/6/27.
 */

public interface EditMyInfoContract {
    interface Presenter extends BasePresenter {
        void uploadAvatar(String path, int type);
        void modify(String name, String sex, String birthday, String bg, String headPath, String sign);
    }

    interface View extends BaseView {
        void uploadSuccess(String path, int type);
        void uploadFail(int type);
        void modifySuccess();
    }
}
