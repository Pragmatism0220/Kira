package com.moemoe.lalala.presenter;

/**
 * Created by Administrator on 2018/6/2.
 */

public interface StorageContract {

    interface Presenter extends BasePresenter {
        void furnitureUse(String furnitureId,int position);
        void suitUse(String suitTypeId,int position);
    }

    interface View extends BaseView {
        void furnitureUseSuccess(int position);
        void suitUseSuccess(int position);
    }
}
