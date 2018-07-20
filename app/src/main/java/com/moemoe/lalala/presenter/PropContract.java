package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.PropInfoEntity;
import com.moemoe.lalala.model.entity.SearchListEntity;
import com.moemoe.lalala.model.entity.SearchNewListEntity;
import com.moemoe.lalala.model.entity.upDateEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/5/29.
 */

public interface PropContract {
    interface Presenter extends BasePresenter {
        void getPropInfo();
        void searchHouseNew(SearchListEntity name);
        void updateNews(upDateEntity entity);

    }

    interface View extends BaseView {
        void getPropInfoSuccess(ArrayList<PropInfoEntity> propInfoEntities);
        void getHouseNewSuccess(ArrayList<SearchNewListEntity> searchNewLists);
        void updateSuccess();
    }
}
