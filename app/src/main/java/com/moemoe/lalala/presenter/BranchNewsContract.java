package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.SearchListEntity;
import com.moemoe.lalala.model.entity.SearchNewListEntity;
import com.moemoe.lalala.model.entity.upDateEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/19.
 */

public interface BranchNewsContract {
    interface Presenter extends BasePresenter {
//        void searchBranchNews(SearchListEntity name);
        void updateBranchNews(upDateEntity entity);
    }
    interface View extends BaseView{
//        void getBranchNewsSuccess(ArrayList<SearchNewListEntity> searchNewLists);
        void updateBranchNewsSuccess();
    }

}
