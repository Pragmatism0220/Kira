package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.NewStoryGroupInfoEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/6/11.
 */

public interface PrincipalContract {
    interface Presenter extends BasePresenter {
        void getPrincipalGroupInfo();
    }

    interface View extends BaseView {
        void getPrincipalGroupInfoSuccess(ArrayList<NewStoryGroupInfoEntity> newStoryGroupInfoEntities);
    }
}
