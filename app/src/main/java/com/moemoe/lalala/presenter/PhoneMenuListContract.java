package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.AddressEntity;
import com.moemoe.lalala.model.entity.PhoneMenuEntity;

import java.util.ArrayList;

/**
 * Created by yi on 2016/11/29.
 */

public interface PhoneMenuListContract {
    interface Presenter extends BasePresenter{
        void loadUserList(String type,int index);
        void followUser(String id,boolean isFollow,int position);
    }

    interface View extends BaseView{
        void onLoadUserListSuccess(ArrayList<PhoneMenuEntity> entities,boolean isPull);
        void onFollowSuccess(boolean isFollow,int position);
    }
}
