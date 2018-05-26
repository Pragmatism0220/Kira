package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
import com.moemoe.lalala.model.entity.AllPersonnelEntity;
import com.moemoe.lalala.model.entity.FeedFollowType2Entity;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yi on 2018/1/11.
 */

public class CommunityPresenter implements CommunityContract.Presenter {

    private CommunityContract.View view;
    private ApiService apiService;

    @Inject
    public CommunityPresenter(CommunityContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void release() {
        view = null;
    }

    @Override
    public void loadData(String id) {
        apiService.loadTagManager(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<FeedFollowType2Entity>() {
                    @Override
                    public void onSuccess(FeedFollowType2Entity entity) {
                        if (view != null) view.onLoadListSuccess(entity);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void loadTagJoin(String tagId, final boolean join) {
        apiService.loadTagJoin(join, tagId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if (view != null) view.onLoadTagJoinSuccess(!join);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void loadTagAllPersonnel(String tagId) {
        apiService.loadTagAllPersonnel(tagId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<AllPersonnelEntity>() {
                    @Override
                    public void onSuccess(AllPersonnelEntity entity) {
                        if (view != null) view.onLoadTagAllPersonnelSuccess(entity);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });

    }
}
