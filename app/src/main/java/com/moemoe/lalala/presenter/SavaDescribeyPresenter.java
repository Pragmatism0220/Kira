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

public class SavaDescribeyPresenter implements SavaDescribeContract.Presenter {

    private SavaDescribeContract.View view;
    private ApiService apiService;

    @Inject
    public SavaDescribeyPresenter(SavaDescribeContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void release() {
        view = null;
    }
    @Override
    public void loadSaveDescribe(String thiId, String describe) {
        apiService.loadSavaDescribe(thiId, describe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if (view != null) view.onSaveDescribeSuccess();
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }
}
