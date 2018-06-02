package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
import com.moemoe.lalala.model.entity.ClothingEntity;
import com.moemoe.lalala.model.entity.RoleInfoEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhangyan on 2018/5/25.
 * 角色
 */

public class ClothingPresenter implements ClothingContrarct.Presenter {

    private ClothingContrarct.View view;
    private ApiService apiService;

    @Inject
    public ClothingPresenter(ClothingContrarct.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void release() {
        view = null;
    }

    @Override
    public void loadHouseClothesAll(String roleId) {
        apiService.loadHouseClothesAll(roleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<ClothingEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<ClothingEntity> roleInfoEntities) {
                        if (view != null) view.loadHouseClothSuccess(roleInfoEntities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void loadRoleColthesSelect(final int position, String roleId, String clothesId) {
        apiService.loadRoleColthesSelect(roleId, clothesId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if (view != null) view.loadRoleColthesSelectSuccess(position);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }
}
