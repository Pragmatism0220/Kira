package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
import com.moemoe.lalala.model.entity.upDateEntity;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/7/19.
 */

public class FurnitureUpDatePresenter implements FurnitureUpDateContract.Presenter {

    private FurnitureUpDateContract.View view;
    private ApiService apiService;


    @Inject
    public FurnitureUpDatePresenter(FurnitureUpDateContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }


    @Override
    public void release() {
        view = null;
    }

    @Override
    public void upDateFurnitureNews(upDateEntity entity) {
        apiService.updateNews(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if (view != null) view.upDateFurnitureNewsSuccess();
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }
}

