package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
import com.moemoe.lalala.model.entity.BagDirEntity;
import com.moemoe.lalala.model.entity.ShowFolderEntity;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yi on 2016/11/29.
 */

public class BagFavoritePresenter implements BagFavoriteContract.Presenter {

    private BagFavoriteContract.View view;
    private ApiService apiService;

    @Inject
    public BagFavoritePresenter(BagFavoriteContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void getFavoriteList(final int index) {
        apiService.getBagFavoriteList(ApiService.LENGHT,index)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<ShowFolderEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<ShowFolderEntity> entities) {
                        if(view != null) view.loadListSuccess(entities,index == 0);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if(view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void deleteFavoriteList(HashMap<Integer, ShowFolderEntity> map) {
        ArrayList<String> ids = new ArrayList<>();
        for(ShowFolderEntity id : map.values()){
            ids.add(id.getFolderId());
        }
        apiService.deleteBagFavoriteList(ids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if(view != null) view.deleteSuccess();
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if(view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void release() {
        view = null;
    }
}
