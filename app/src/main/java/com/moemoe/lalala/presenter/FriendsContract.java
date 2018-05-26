package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.PhoneMenuEntity;

import java.util.ArrayList;

/**
 * Created by Sora on 2018/3/7.
 */

public interface FriendsContract {
    interface Presenter extends BasePresenter {
        void loadUserFriends(int index);
    }

    interface View extends BaseView {
        void onloadUserFriendsSuccess(ArrayList<PhoneMenuEntity> entity, boolean isPull);
    }
}
