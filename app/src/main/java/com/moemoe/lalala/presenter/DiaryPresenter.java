package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.entity.DiaryDetailsEntity;
import com.moemoe.lalala.model.entity.DiaryEntity;
import com.moemoe.lalala.view.activity.DiaryActivity;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/7/5.
 */

public class DiaryPresenter implements DiaryContract.Presenter {
    private DiaryContract.View view;
    private ApiService apiService;

    @Inject
    public DiaryPresenter(DiaryContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void getDiaryInfo(String roleId) {
        apiService.loadDiary(roleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<DiaryEntity>() {
                    @Override
                    public void onSuccess(DiaryEntity diaryEntity) {
                        if (view != null) view.getDiarySuccess(diaryEntity);
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
