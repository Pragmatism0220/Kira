package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
import com.moemoe.lalala.model.entity.CreateOrderEntity;
import com.moemoe.lalala.model.entity.OrderEntity;
import com.moemoe.lalala.model.entity.PayReqEntity;
import com.moemoe.lalala.model.entity.PayResEntity;
import com.moemoe.lalala.model.entity.PropUseEntity;
import com.moemoe.lalala.model.entity.SearchListEntity;
import com.moemoe.lalala.model.entity.SearchNewListEntity;
import com.moemoe.lalala.model.entity.upDateEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/6/2.
 */

public class StoragePresenter implements StorageContract.Presenter {

    private StorageContract.View view;
    private ApiService apiService;

    @Inject
    public StoragePresenter(StorageContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }


    @Override
    public void furnitureUse(String furnitureId, final int position) {
        apiService.furnitureUse(furnitureId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if (view != null) view.furnitureUseSuccess(position);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void suitUse(String suitTypeId, final int position) {
        apiService.suitUse(suitTypeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if (view != null) view.suitUseSuccess(position);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }


    @Override
    public void PropUse(String propID) {
        apiService.propUse(propID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<PropUseEntity>() {
                    @Override
                    public void onSuccess(PropUseEntity entity) {
                        if (view != null) view.propUseSuccess(entity);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void searchStorageNew(SearchListEntity name) {
        apiService.searchNewList(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<SearchNewListEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<SearchNewListEntity> newListEntities) {
                        if (view != null) view.searchStorageNewSuccess(newListEntities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void updateStorageNews(upDateEntity entity) {
        apiService.updateNews(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if (view != null) view.updateStorageNewsSuccess();
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
    public void release() {
        view = null;
    }
}
