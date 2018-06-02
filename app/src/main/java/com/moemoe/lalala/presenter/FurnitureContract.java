package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.FurnitureInfoEntity;

/**
 * Created by Administrator on 2018/5/30.
 */

public interface FurnitureContract {
    interface Presenter extends BasePresenter {
        void getFurnitureInfo();

//        void FurnitureUse(String furnitureId);
    }

    interface View extends BaseView {
        void getFurnitureInfoSuccess(FurnitureInfoEntity furnitureInfoEntity);

//        void FurnitureUseSuccess();
    }
}
