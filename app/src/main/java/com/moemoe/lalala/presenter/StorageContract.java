package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.OrderEntity;
import com.moemoe.lalala.model.entity.PayReqEntity;
import com.moemoe.lalala.model.entity.PayResEntity;

/**
 * Created by Administrator on 2018/6/2.
 */

public interface StorageContract {

    interface Presenter extends BasePresenter {
        void furnitureUse(String furnitureId,int position);
        void suitUse(String suitTypeId,int position);
        void createOrder(String id);
        void payOrder(PayReqEntity entity);
    }

    interface View extends BaseView {
        void furnitureUseSuccess(int position);
        void suitUseSuccess(int position);
        void onCreateOrderSuccess(OrderEntity entity);
        void onPayOrderSuccess(PayResEntity entity);
    }
}
