package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
import com.moemoe.lalala.model.entity.UserLoginSevenEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/7/12.
 */

public class SevenDayLoginPresenter implements SevenDayLoginContract.Presenter {
    private SevenDayLoginContract.View view;
    private ApiService apiService;

    @Inject
    public SevenDayLoginPresenter(SevenDayLoginContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void getSevenLoginInfo() {
        apiService.loadSevenLoginInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<UserLoginSevenEntity>() {
                    @Override
                    public void onSuccess(UserLoginSevenEntity userLoginSevenEntity) {
                        if (view != null) view.getSevenLoginInfo(userLoginSevenEntity);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void isComplete() {
        apiService.iscomplete()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (view != null) view.isCompleteSuccess(aBoolean);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void getReward() {
//        apiService.getReward()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new NetSimpleResultSubscriber() {
//                    @Override
//                    public void onSuccess() {
//                        if (view != null) view.getRewardSuccess();
//                    }
//
//                    @Override
//                    public void onFail(int code, String msg) {
//                        if (view != null) view.onFailure(code, msg);
//                    }
//                });
        apiService.getReward()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if (view != null) view.getRewardSuccess(s);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view!=null)view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void release() {
        view = null;
    }
}
