package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
import com.moemoe.lalala.model.entity.HomeEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Hygge on 2018/5/9.
 */

public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View view;
    private ApiService apiService;

    @Inject
    public HomePresenter(HomeContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void release() {
        view = null;
    }

    @Override
    public void loadHomeAllUser() {
        apiService.loadHomeAllUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<HomeEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<HomeEntity> entities) {
                        if (view != null) view.onLoadHomeAllUserSuccess(entities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }
}
