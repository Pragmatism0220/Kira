package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.entity.BagVisitorEntity;
import com.moemoe.lalala.model.entity.UserTopEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Sora on 2018/3/6.
 */

public class BagVisitorPresenter implements BagVisitorContract.Presenter {

    private BagVisitorContract.View view;
    private ApiService apiService;

    @Inject
    public BagVisitorPresenter(BagVisitorContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void release() {
        view = null;
    }

    @Override
    public void loadBagVisitor(String userId, final int index) {
        apiService.loadVisitorInfo(userId, index, ApiService.LENGHT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<UserTopEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<UserTopEntity> entities) {
                        if (view != null) view.onLoadBagVisitorSuccess(entities, index == 0);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }
}
