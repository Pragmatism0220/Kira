package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.HomeEntity;

import java.util.ArrayList;

/**
 * Created by Hygge on 2018/5/9.
 */

public interface HomeContract {
    interface Presenter extends BasePresenter {
        void loadHomeAllUser();
    }

    interface View extends BaseView {
        void onLoadHomeAllUserSuccess(ArrayList<HomeEntity> entities);
    }
}
