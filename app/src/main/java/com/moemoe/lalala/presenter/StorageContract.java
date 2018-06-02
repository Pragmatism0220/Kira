package com.moemoe.lalala.presenter;

/**
 * Created by Administrator on 2018/6/2.
 */

public interface StorageContract {


    interface Presenter extends BasePresenter {

        void furnitureUse(String furnitureId);

        void suitUse(String suitTypeId);

    }

    interface View extends BaseView {
        void furnitureUseSuccess();
        void suitUseSuccess();
    }
}
