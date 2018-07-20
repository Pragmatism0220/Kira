package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.upDateEntity;

/**
 * Created by Administrator on 2018/7/19.
 */

public interface FurnitureUpDateContract {
    interface Presenter extends BasePresenter {
        void upDateFurnitureNews(upDateEntity entity);
    }

    interface View extends BaseView {
        void upDateFurnitureNewsSuccess();
    }
}
