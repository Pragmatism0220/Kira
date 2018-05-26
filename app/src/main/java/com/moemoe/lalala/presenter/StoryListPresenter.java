package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.entity.StoryListEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Sora on 2018/4/8.
 */

public class StoryListPresenter implements StoryListContract.Presenter {

    private StoryListContract.View view;
    private ApiService apiService;

    @Inject
    public StoryListPresenter(StoryListContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void release() {
         view = null;
    }

    @Override
    public void loadStoryFindList(String groupId) {
        apiService.loadStoryFindList(groupId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<StoryListEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<StoryListEntity> storyListEntity) {
                        if(view != null) view.onLoadStoryFindListSuccess(storyListEntity);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if(view != null) view.onFailure(code,msg);
                    }
                });
    }
}
