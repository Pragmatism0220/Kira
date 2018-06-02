package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.ClothingEntity;
import com.moemoe.lalala.model.entity.RoleInfoEntity;

import java.util.ArrayList;

/**
 * Created by Hygge on 2018/6/1.
 */

public interface ClothingContrarct {
    interface Presenter extends BasePresenter {
        void loadHouseClothesAll(String roleId);
        void loadRoleColthesSelect(int position,String roleId, String clothesId);
    }

    interface View extends BaseView {
        void loadHouseClothSuccess(ArrayList<ClothingEntity> entities);
        void loadRoleColthesSelectSuccess(int position);
    }
}
