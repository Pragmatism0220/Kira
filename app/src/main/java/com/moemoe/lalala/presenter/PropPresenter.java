package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
import com.moemoe.lalala.model.entity.PropInfoEntity;
import com.moemoe.lalala.model.entity.SearchListEntity;
import com.moemoe.lalala.model.entity.SearchNewListEntity;
import com.moemoe.lalala.model.entity.upDateEntity;

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
    public void searchHouseNew(SearchListEntity name) {
        apiService.searchNewList(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<SearchNewListEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<SearchNewListEntity> newListEntities) {
                        if (view != null) view.getHouseNewSuccess(newListEntities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void updateNews(upDateEntity entity) {
        apiService.updateNews(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if (view != null) view.updateSuccess();
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
