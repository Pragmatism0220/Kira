package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.NewStoryInfoEvent;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/6/11.
 */

public class NewDormitoryPresenter implements NewDormitioryContract.Presenter {

    private NewDormitioryContract.View view;
    private ApiService apiService;

    @Inject
    public NewDormitoryPresenter(NewDormitioryContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void release() {
        view = null;
    }

    @Override
    public void getStoryInfo() {
        apiService.GetNewStoryInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<NewStoryInfoEvent>() {
                    @Override
                    public void onSuccess(NewStoryInfoEvent newStoryInfoEvent) {
                        if (view != null) view.getStoryInfoSuccess(newStoryInfoEvent);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }
}
