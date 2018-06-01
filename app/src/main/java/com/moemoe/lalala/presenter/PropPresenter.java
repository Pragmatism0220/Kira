package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.entity.PropInfoEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/5/29.
 */

public class PropPresenter implements PropContract.Presenter {

    private PropContract.View view;
    private ApiService apiService;

    @Inject
    public PropPresenter(PropContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void getPropInfo() {
        apiService.loadPropInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<PropInfoEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<PropInfoEntity> propInfoEntities) {
                        if (view != null) view.getPropInfoSuccess(propInfoEntities);
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
