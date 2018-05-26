package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.UserTopEntity;

import java.util.ArrayList;

/**
 * Created by Sora on 2018/3/7.
 */

public interface AllMemberContract {
    interface Presenter extends BasePresenter {
        void loadTagMembers(String tagId, int index, String condition);
    }

    interface View extends BaseView {
        void onLoadTagMembersSuccess(ArrayList<UserTopEntity> entity, boolean isPull);
    }
}
