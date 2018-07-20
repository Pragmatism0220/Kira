package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.OrderEntity;
import com.moemoe.lalala.model.entity.PayReqEntity;
import com.moemoe.lalala.model.entity.PayResEntity;

/**
 * Created by Hygge on 2018/5/24.
 */

public interface SelectUpterContract {
    interface Presenter extends BasePresenter {
        void createOrder(String id);
        void payOrder(PayReqEntity entity);
    }

    interface View extends BaseView {
        void onCreateOrderSuccess(OrderEntity entity);
        void onPayOrderSuccess(PayResEntity entity);
    }
}
