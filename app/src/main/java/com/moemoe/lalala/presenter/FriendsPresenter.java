package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.entity.PhoneMenuEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Sora on 2018/3/7.
 */

public class FriendsPresenter implements FriendsContract.Presenter {
    private FriendsContract.View view;
    private ApiService apiService;

    @Inject
    public FriendsPresenter(FriendsContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void release() {
        view = null;
    }

    @Override
    public void loadUserFriends(final int index) {
        apiService.loadUserFriends(index, ApiService.LENGHT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<PhoneMenuEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<PhoneMenuEntity> entity) {
                        if (view != null) view.onloadUserFriendsSuccess(entity, index == 0);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }
}
