package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.entity.BannerEntity;
import com.moemoe.lalala.model.entity.ShowFolderEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Hygge on 2018/7/18.
 */

public class LibraryPresenter implements LibraryContract.Presenter {
    private LibraryContract.View view;
    private ApiService apiService;
        
    @Inject
    public LibraryPresenter(LibraryContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void release() {
        view = null;
    }

    @Override
    public void requestBannerData(String room) {
        apiService.requestNewBanner(room)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<BannerEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<BannerEntity> bannerEntities) {
                        if (view != null) view.onBannerLoadSuccess(bannerEntities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                    }
                });
    }

    @Override
    public void loadLibraryBagList(String type, final int index) {
        if (type.equals("历史记录")) {

        } else {
            apiService.loadLibraryBagList(type, ApiService.LENGHT1, index)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultSubscriber<ArrayList<ShowFolderEntity>>() {
                        @Override
                        public void onSuccess(ArrayList<ShowFolderEntity> entities) {
                            if (view != null) view.onLoadLibraryListSuccess(entities, index == 0);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                        }
                    });
        }
    }
}
