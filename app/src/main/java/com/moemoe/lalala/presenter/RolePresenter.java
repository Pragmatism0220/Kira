package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
import com.moemoe.lalala.model.entity.RoleInfoEntity;
import com.moemoe.lalala.model.entity.SearchListEntity;
import com.moemoe.lalala.model.entity.SearchNewListEntity;
import com.moemoe.lalala.model.entity.upDateEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhangyan on 2018/5/25.
 * 角色
 */

public class RolePresenter implements RoleContract.Presenter {

    private RoleContract.View view;
    private ApiService apiService;

    @Inject
    public RolePresenter(RoleContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void getRoleInfo() {
        apiService.loadRoleInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<RoleInfoEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<RoleInfoEntity> roleInfoEntities) {
                        if (view != null) view.getRoleInfo(roleInfoEntities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void setDeskMate(String roleId) {
        apiService.setDeskMate(roleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if (view != null) view.setDeskMateSuccess();
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void putInHouse(String roleId) {
        apiService.putInHouse(roleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if (view != null) view.putInHouseSuccess();
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void removeOutHouse(String roleId) {
        apiService.removeInHouse(roleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if (view != null) view.removeOutHouseSuccess();
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void getStoryInfo() {
//        apiService.getstroyIfo()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new NetResultSubscriber<NewStoryInfoEventEntity>() {
//                    @Override
//                    public void onSuccess(NewStoryInfoEventEntity newStoryInfoEvent) {
//                        Log.e("---StorySuccess---", newStoryInfoEvent.getBgImage() + "---");
//                    }
//
//                    @Override
//                    public void onFail(int code, String msg) {
//
//                    }
//                });
    }

    @Override
    public void searchRoleNew(SearchListEntity name) {
        apiService.searchNewList(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<SearchNewListEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<SearchNewListEntity> entities) {
                        if (view!=null) view.getRoleNewListSuccess(entities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void updateNews(upDateEntity entity) {
        apiService.updateNews(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if (view!=null) view.upDateNewsSuccess();
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view!=null) view.onFailure(code, msg);
                    }
                });
    }


    @Override
    public void release() {
        view = null;
    }
}
