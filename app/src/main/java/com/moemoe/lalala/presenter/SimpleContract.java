package com.moemoe.lalala.presenter;

/**
 * Created by yi on 2016/11/29.
 */

public interface SimpleContract {
    interface Presenter extends BasePresenter{
        void doRequest(Object data,int type);
        void loadGameShare(String id);
    }

    interface View extends BaseView{
        void onSuccess(Object o);
        void onLoadGameShareSuccess();
    }
}
