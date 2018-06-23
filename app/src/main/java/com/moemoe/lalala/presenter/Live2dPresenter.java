package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.entity.HouseSleepEntity;
import com.moemoe.lalala.model.entity.Live2dMusicEntity;
import com.moemoe.lalala.model.entity.ShareLive2dEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yi on 2016/11/29.
 */

public class Live2dPresenter implements Live2dContract.Presenter {

    private Live2dContract.View view;
    private ApiService apiService;

    @Inject
    public Live2dPresenter(Live2dContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void release() {
        view = null;
    }

    @Override
    public void loadMusicList() {
        apiService.loadLive2dMusicList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<Live2dMusicEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<Live2dMusicEntity> entities) {
                        if (view != null) view.onLoadMusicListSuccess(entities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void loadShareLive2dList() {
        apiService.loadShareLive2dList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<ShareLive2dEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<ShareLive2dEntity> entities) {
                        if (view != null) view.onLoadShareListSuccess(entities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void loadHouseSleep() {
        apiService.loadHouseSleep()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<HouseSleepEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<HouseSleepEntity> entities) {
                        if (view != null) view.onLoadHouseListSuccess(entities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }
}
