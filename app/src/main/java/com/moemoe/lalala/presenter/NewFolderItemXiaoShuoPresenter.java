package com.moemoe.lalala.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
import com.moemoe.lalala.model.entity.BagLoadReadprogressEntity;
import com.moemoe.lalala.model.entity.BagReadprogressEntity;
import com.moemoe.lalala.model.entity.CommonFileEntity;
import com.moemoe.lalala.model.entity.FileXiaoShuoEntity;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.LibraryContribute;
import com.moemoe.lalala.model.entity.ManHua2Entity;
import com.moemoe.lalala.model.entity.NewFolderEntity;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.model.entity.StreamFileEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yi on 2016/11/29.
 */

public class NewFolderItemXiaoShuoPresenter implements NewFolderItemXiaoShuoContract.Presenter {

    private NewFolderItemXiaoShuoContract.View view;
    private ApiService apiService;

    @Inject
    public NewFolderItemXiaoShuoPresenter(NewFolderItemXiaoShuoContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void release() {
        view = null;
    }

    @Override
    public void loadBagReadprogress(BagReadprogressEntity entity) {
        apiService.loadBagReadprogress(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<BagLoadReadprogressEntity>() {
                    @Override
                    public void onSuccess(BagLoadReadprogressEntity entity) {
                        if (view != null) view.onLoadBagReadprogressSuccess(entity);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void loadBagReadpressUpdate(BagReadprogressEntity entity) {
        apiService.loadBagReadpressUpdate(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if (view != null) view.onloadBagReadpressUpdateSuccess();
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

}