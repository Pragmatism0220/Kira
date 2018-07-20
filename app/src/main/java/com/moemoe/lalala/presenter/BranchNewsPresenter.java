package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
import com.moemoe.lalala.model.entity.SearchListEntity;
import com.moemoe.lalala.model.entity.SearchNewListEntity;
import com.moemoe.lalala.model.entity.upDateEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/7/19.
 */

public class BranchNewsPresenter implements BranchNewsContract.Presenter {


    private BranchNewsContract.View view;
    private ApiService apiService;

    @Inject
    public BranchNewsPresenter(BranchNewsContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void release() {
        view = null;
    }

//    @Override
//    public void searchBranchNews(SearchListEntity name) {
//        apiService.searchNewList(name)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new NetResultSubscriber<ArrayList<SearchNewListEntity>>() {
//                    @Override
//                    public void onSuccess(ArrayList<SearchNewListEntity> searchNewListEntities) {
//                        if (view != null) view.getBranchNewsSuccess(searchNewListEntities);
//                    }
//
//                    @Override
//                    public void onFail(int code, String msg) {
//                        if (view != null) view.onFailure(code, msg);
//                    }
//                });
//    }

    @Override
    public void updateBranchNews(upDateEntity entity) {
        apiService.updateNews(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if (view != null) view.updateBranchNewsSuccess();
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }
}
