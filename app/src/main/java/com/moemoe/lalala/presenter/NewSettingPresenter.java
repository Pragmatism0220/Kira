package com.moemoe.lalala.presenter;

import android.os.IBinder;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
import com.moemoe.lalala.model.entity.UserInfo;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/6/26.
 */

public class NewSettingPresenter implements NewSettingContract.Presenter {

    private NewSettingContract.View view;
    private ApiService apiService;

    @Inject
    public NewSettingPresenter(NewSettingContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void getUserInfo(String userId) {
        apiService.requestUserInfoV2(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<UserInfo>() {
                    @Override
                    public void onSuccess(UserInfo userInfo) {
                        if (view != null) view.onLoadUserInfo(userInfo);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onLoadUserInfoFail();
                    }
                });
    }

    @Override
    public void logout() {
        apiService.logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if (view != null) view.logoutSuccess();
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void release() {
        view = null;
    }

}
