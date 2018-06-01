package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.PropInfoEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/5/29.
 */

public interface PropContract {
    interface Presenter extends BasePresenter {
        void getPropInfo();
    }

    interface View extends BaseView {
        void getPropInfoSuccess(ArrayList<PropInfoEntity> propInfoEntities);
    }
}
