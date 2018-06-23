package com.moemoe.lalala.presenter;


import com.moemoe.lalala.model.entity.RoleInfoEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/5/25.
 */

public interface RoleContract {
    interface Presenter extends BasePresenter {
        void getRoleInfo();
        void setDeskMate(String roleId);
        void putInHouse(String roleId);
        void removeOutHouse(String roleId);
        void getStoryInfo();
    }

    interface View extends BaseView {
        void getRoleInfo(ArrayList<RoleInfoEntity> entities);
        void setDeskMateSuccess();
        void putInHouseSuccess();
        void removeOutHouseSuccess();
    }
}
