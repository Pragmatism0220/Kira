package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.entity.FurnitureInfoEntity;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhangyan on 2018/5/30.
 */

public class FurniturePresenter implements FurnitureContract.Presenter {

    private FurnitureContract.View view;
    private ApiService apiService;

    @Inject
    public FurniturePresenter(FurnitureContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }


    @Override
    public void getFurnitureInfo() {
        apiService.loadFurnitureInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<FurnitureInfoEntity>() {
                    @Override
                    public void onSuccess(FurnitureInfoEntity furnitureInfoEntity) {
                        if (view != null) view.getFurnitureInfoSuccess(furnitureInfoEntity);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });

    }

//    @Override
//    public void FurnitureUse(String furnitureId) {
//        apiService.furnitureUse(furnitureId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new NetSimpleResultSubscriber() {
//                    @Override
//                    public void onSuccess() {
//                        if (view != null) view.FurnitureUseSuccess();
//                    }
//
//                    @Override
//                    public void onFail(int code, String msg) {
//                        if (view != null) view.onFailure(code, msg);
//                    }
//                });
//    }


    @Override
    public void release() {
        view = null;
    }
}
