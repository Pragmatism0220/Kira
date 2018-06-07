package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.entity.VisitorsEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/6/6.
 */

public class NewVisitorPresenter implements NewVisitorsContract.Presenter {

    private NewVisitorsContract.View view;
    private ApiService apiService;

    @Inject
    public NewVisitorPresenter(NewVisitorsContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }


    @Override
    public void getNewVisitorsInfo(int size, final int start) {
        apiService.loadVisitor(size, start)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<VisitorsEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<VisitorsEntity> entities) {
                        if (view != null) view.getNewVisitorsInfoSuccess(entities, start == 0);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }


    @Override
    public void release() {
        view = null;
    }
}
