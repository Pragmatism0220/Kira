package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.NewStoryGroupInfo;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/6/11.
 */

public class PrincipalPresenter implements PrincipalContract.Presenter {

    private PrincipalContract.View view;
    private ApiService apiService;

    @Inject
    public PrincipalPresenter(PrincipalContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }


    @Override
    public void release() {
        view = null;
    }

    @Override
    public void getPrincipalGroupInfo() {
        apiService.getNewStoryGroupInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<NewStoryGroupInfo>>() {
                    @Override
                    public void onSuccess(ArrayList<NewStoryGroupInfo> newStoryGroupInfos) {
                        if (view != null) view.getPrincipalGroupInfoSuccess(newStoryGroupInfos);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }
}
