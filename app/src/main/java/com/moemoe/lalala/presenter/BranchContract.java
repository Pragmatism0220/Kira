package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.BranchStoryAllEntity;
import com.moemoe.lalala.model.entity.BranchStoryJoinEntity;
import com.moemoe.lalala.model.entity.SearchListEntity;
import com.moemoe.lalala.model.entity.SearchNewListEntity;

import java.util.ArrayList;

/**
 * Created by Hygge on 2018/6/12.
 */

public interface BranchContract {
    interface Presenter extends BasePresenter {
        void loadBranchStoryAll(boolean isSelect,int level);
        void loadBranchStoryJoin(BranchStoryJoinEntity entity);
        void searchBranchNews(SearchListEntity name);
    }

    interface View extends BaseView {
        void onLoadBranchStoryAllSuccess(ArrayList<BranchStoryAllEntity> entities);
        void onLoadBranchStoryJoin();
        void getBranchNewsSuccess(ArrayList<SearchNewListEntity> searchNewLists);
    }
}
