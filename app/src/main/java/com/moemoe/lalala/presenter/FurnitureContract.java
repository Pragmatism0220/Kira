package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.FurnitureInfoEntity;
import com.moemoe.lalala.model.entity.SearchListEntity;
import com.moemoe.lalala.model.entity.SearchNewListEntity;
import com.moemoe.lalala.model.entity.upDateEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/5/30.
 */

public interface FurnitureContract {
    interface Presenter extends BasePresenter {
        void getFurnitureInfo();
        void getFurnitureNews(SearchListEntity name);
        void updateFurnitureNews(upDateEntity entity);
    }

    interface View extends BaseView {
        void getFurnitureInfoSuccess(FurnitureInfoEntity furnitureInfoEntity);
        void getFurnitureNewsSuccess(ArrayList<SearchNewListEntity> entities);
        void upDateFurnitureNewsSuccess();
    }
}
