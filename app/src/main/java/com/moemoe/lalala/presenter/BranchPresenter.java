package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
import com.moemoe.lalala.model.entity.BranchStoryAllEntity;
import com.moemoe.lalala.model.entity.BranchStoryJoinEntity;
import com.moemoe.lalala.model.entity.MapEntity;
import com.moemoe.lalala.model.entity.SearchListEntity;
import com.moemoe.lalala.model.entity.SearchNewListEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Hygge on 2018/6/12.
 */

public class BranchPresenter implements BranchContract.Presenter {
    private BranchContract.View view;
    private ApiService apiService;

    @Inject
    public BranchPresenter(BranchContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }


    @Override
    public void release() {
        view = null;
    }

    @Override
    public void loadBranchStoryAll(boolean isSelect, int level) {
        if (isSelect) {
            apiService.loadBranchStoryAll()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultSubscriber<ArrayList<BranchStoryAllEntity>>() {
                        @Override
                        public void onSuccess(ArrayList<BranchStoryAllEntity> entities) {
                            if (view != null) view.onLoadBranchStoryAllSuccess(entities);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if (view != null) view.onFailure(code, msg);
                        }
                    });
        } else {
            apiService.loadBranchStoryAllLevel(level)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultSubscriber<ArrayList<BranchStoryAllEntity>>() {
                        @Override
                        public void onSuccess(ArrayList<BranchStoryAllEntity> entities) {
                            if (view != null) view.onLoadBranchStoryAllSuccess(entities);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if (view != null) view.onFailure(code, msg);
                        }
                    });
        }
    }

    @Override
    public void loadBranchStoryJoin(BranchStoryJoinEntity entity) {
        apiService.loadBranchStoryJoin(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if (view != null) view.onLoadBranchStoryJoin();
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void searchBranchNews(SearchListEntity name) {
        apiService.searchNewList(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<SearchNewListEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<SearchNewListEntity> newListEntities) {
                        if (view != null) view.getBranchNewsSuccess(newListEntities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }
}
