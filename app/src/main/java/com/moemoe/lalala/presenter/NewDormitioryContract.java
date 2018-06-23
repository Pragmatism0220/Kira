package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.NewStoryInfoEventEntity;

/**
 * Created by Administrator on 2018/6/11.
 */

public interface NewDormitioryContract {
    interface Presenter extends BasePresenter {
        void getStoryInfo();
    }

    interface View extends BaseView {
        void getStoryInfoSuccess(NewStoryInfoEventEntity event);
    }
}
