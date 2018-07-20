package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
import com.moemoe.lalala.model.entity.ClothingEntity;
import com.moemoe.lalala.model.entity.CoinShopEntity;
import com.moemoe.lalala.model.entity.CreateOrderEntity;
import com.moemoe.lalala.model.entity.OrderEntity;
import com.moemoe.lalala.model.entity.PayReqEntity;
import com.moemoe.lalala.model.entity.PayResEntity;
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

    @Override
    public void createOrder(String id) {
        apiService.createOrder(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<CreateOrderEntity>() {
                    @Override
                    public void onSuccess(CreateOrderEntity entity) {
                        OrderEntity orderEntity = new OrderEntity();
                        orderEntity.setAddress(entity.getAddress());
                        orderEntity.setEndTime(entity.getEndTime());
                        orderEntity.setOrderNo(entity.getOrderNo());
                        orderEntity.setLastRemark(entity.getLastRemark());
                        orderEntity.setOrderId(entity.getOrderId());
                        orderEntity.setBuyNum(1);
                        if (view != null) view.onCreateOrderSuccess(orderEntity);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void payOrder(PayReqEntity entity) {
        apiService.payOrder(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<PayResEntity>() {
                    @Override
                    public void onSuccess(PayResEntity entity) {
                        if (view != null) view.onPayOrderSuccess(entity);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void getNewsCloth(SearchListEntity name) {
        apiService.searchNewList(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<SearchNewListEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<SearchNewListEntity> newListEntities) {
                        if (view != null) view.getClothNewSuccess(newListEntities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });

    }

    @Override
    public void updateNewsCloth(upDateEntity entity) {
        apiService.updateNews(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if (view != null) view.updateSuccess();
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }


}
