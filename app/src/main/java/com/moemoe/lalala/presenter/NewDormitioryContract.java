package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.NewStoryInfoEventEntity;
import com.moemoe.lalala.model.entity.SearchListEntity;
import com.moemoe.lalala.model.entity.SearchNewListEntity;
import com.moemoe.lalala.model.entity.upDateEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/6/11.
 */

public interface NewDormitioryContract {
    interface Presenter extends BasePresenter {
        void getStoryInfo();
        void searchDormitioryNews(SearchListEntity name);
        void updateDormitioryNews(upDateEntity entity);
    }

    interface View extends BaseView {
        void getStoryInfoSuccess(NewStoryInfoEventEntity event);
        void getDormitioryNewsSuccess(ArrayList<SearchNewListEntity> searchNewLists);
        void updateDormitioryNewsSuccess();
    }
}
