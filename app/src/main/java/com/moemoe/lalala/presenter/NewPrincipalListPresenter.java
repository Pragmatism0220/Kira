package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.NewStoryInfo;
import com.moemoe.lalala.model.entity.NewStoryJsonInfo;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/6/11.
 */

public class NewPrincipalListPresenter implements PrincipalListContract.Presenter {

    private PrincipalListContract.View view;
    private ApiService apiService;


    @Inject
    public NewPrincipalListPresenter(PrincipalListContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }


    @Override
    public void release() {
        view = null;
    }

    @Override
    public void getPrincipalListInfo(String groupId) {
        apiService.getNewStoryListInfo(groupId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<NewStoryInfo>>() {
                    @Override
                    public void onSuccess(ArrayList<NewStoryInfo> newStoryInfos) {
                        if (view != null) view.getPrincipalListInfoSuccess(newStoryInfos);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });

    }

    @Override
    public void playStory(String scriptId) {
        apiService.playStory(scriptId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<NewStoryJsonInfo>() {
                    @Override
                    public void onSuccess(NewStoryJsonInfo newStoryJsonInfo) {
                        if (view != null) view.playStorySuccess(newStoryJsonInfo);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }
}
