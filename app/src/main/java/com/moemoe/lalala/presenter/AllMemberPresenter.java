package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.entity.UserTopEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Sora on 2018/3/7.
 */

public class AllMemberPresenter implements AllMemberContract.Presenter {
    private AllMemberContract.View view;
    private ApiService apiService;

    @Inject
    public AllMemberPresenter(AllMemberContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void release() {
        view = null;
    }

    @Override
    public void loadTagMembers(String tagId,final int index, String condition) {

        apiService.loadTagMembers(tagId, ApiService.LENGHT, index, condition)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<UserTopEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<UserTopEntity> entity) {
                        if (view != null) view.onLoadTagMembersSuccess(entity, index == 0);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }
}
