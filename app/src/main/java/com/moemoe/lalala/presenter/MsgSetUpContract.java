package com.moemoe.lalala.presenter;

/**
 * Created by yi on 2016/11/29.
 */

public interface MsgSetUpContract {
    interface Presenter extends BasePresenter{
        void loadGroupShield(boolean falg);
    }

    interface View extends BaseView{
        void onloadGroupShield(boolean falg);
    }
}
