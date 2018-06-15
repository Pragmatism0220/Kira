package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.entity.BranchStoryAllEntity;
import com.moemoe.lalala.model.entity.BranchStoryJoinEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Hygge on 2018/6/13.
 */

public class BranchInfoPresenter implements BranchInfoContract.Presenter {

    private BranchInfoContract.View view;
    private ApiService apiService;

    @Inject
    public BranchInfoPresenter(BranchInfoContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }


    @Override
    public void release() {
        view = null;
    }


    @Override
    public void loadBranchStoryInfo(String branchStoryId) {
        apiService.loadBranchStoryInfo(branchStoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<BranchStoryAllEntity>() {
                    @Override
                    public void onSuccess(BranchStoryAllEntity entities) {
                        if (view != null) view.onLoadBranchStoryInfoSuccess(entities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }
}
