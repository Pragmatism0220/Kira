package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.entity.FeedNoticeEntity;
import com.moemoe.lalala.model.entity.UserFollowTagEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yi on 2018/1/11.
 */

public class NoticePresenter implements NoticeContract.Presenter {

    private NoticeContract.View view;
    private ApiService apiService;

    @Inject
    public NoticePresenter(NoticeContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void release() {
        view = null;
    }


    @Override
    public void loadNotifyList(String type, final long notifyTime) {
        apiService.loadNotifyList(type, notifyTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<FeedNoticeEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<FeedNoticeEntity> entities) {
                        if (view != null) view.onLoadNotifyListSuccess(entities, notifyTime == 0);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }
}
