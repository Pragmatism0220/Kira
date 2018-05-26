package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.StoryListEntity;

import java.util.ArrayList;

/**
 * Created by Sora on 2018/4/8.
 */

public interface StoryListContract {
    interface Presenter extends BasePresenter {
        void loadStoryFindList(String groupId);
    }

    interface View extends BaseView {
        void onLoadStoryFindListSuccess(ArrayList<StoryListEntity> entity);
    }
}
