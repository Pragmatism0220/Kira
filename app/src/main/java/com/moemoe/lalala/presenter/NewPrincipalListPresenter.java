package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.NewStoryInfoEntity;
import com.moemoe.lalala.model.entity.NewStoryJsonInfoEntity;
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
                .subscribe(new NetResultSubscriber<ArrayList<NewStoryInfoEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<NewStoryInfoEntity> newStoryInfos) {
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
                .subscribe(new NetResultSubscriber<NewStoryJsonInfoEntity>() {
                    @Override
                    public void onSuccess(NewStoryJsonInfoEntity newStoryJsonInfoEntity) {
                        if (view != null) view.playStorySuccess(newStoryJsonInfoEntity);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }
}
