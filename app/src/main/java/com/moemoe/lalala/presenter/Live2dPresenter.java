package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.entity.CoinShopEntity;
import com.moemoe.lalala.model.entity.CreateOrderEntity;
import com.moemoe.lalala.model.entity.HouseSleepEntity;
import com.moemoe.lalala.model.entity.Live2dMusicEntity;
import com.moemoe.lalala.model.entity.OrderEntity;
import com.moemoe.lalala.model.entity.PayReqEntity;
import com.moemoe.lalala.model.entity.PayResEntity;
import com.moemoe.lalala.model.entity.ShareLive2dEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yi on 2016/11/29.
 */

public class Live2dPresenter implements Live2dContract.Presenter {

    private Live2dContract.View view;
    private ApiService apiService;

    @Inject
    public Live2dPresenter(Live2dContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void release() {
        view = null;
    }

    @Override
    public void loadMusicList() {
        apiService.loadLive2dMusicList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<Live2dMusicEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<Live2dMusicEntity> entities) {
                        if (view != null) view.onLoadMusicListSuccess(entities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void loadShareLive2dList() {
        apiService.loadShareLive2dList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<ShareLive2dEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<ShareLive2dEntity> entities) {
                        if (view != null) view.onLoadShareListSuccess(entities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void loadHouseSleep() {
        apiService.loadHouseSleep()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<HouseSleepEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<HouseSleepEntity> entities) {
                        if (view != null) view.onLoadHouseListSuccess(entities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void createOrder(final String id) {
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

}
