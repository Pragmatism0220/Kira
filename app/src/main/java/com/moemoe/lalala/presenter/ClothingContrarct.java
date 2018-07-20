package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.ClothingEntity;
import com.moemoe.lalala.model.entity.CoinShopEntity;
import com.moemoe.lalala.model.entity.CreateOrderEntity;
import com.moemoe.lalala.model.entity.OrderEntity;
import com.moemoe.lalala.model.entity.PayReqEntity;
import com.moemoe.lalala.model.entity.PayResEntity;
import com.moemoe.lalala.model.entity.RoleInfoEntity;
import com.moemoe.lalala.model.entity.SearchListEntity;
import com.moemoe.lalala.model.entity.SearchNewListEntity;
import com.moemoe.lalala.model.entity.upDateEntity;

import java.util.ArrayList;

/**
 * Created by Hygge on 2018/6/1.
 */

public interface ClothingContrarct {
    interface Presenter extends BasePresenter {
        void loadHouseClothesAll(String roleId);
        void loadRoleColthesSelect(int position,String roleId, String clothesId);
        void createOrder(String id);
        void payOrder(PayReqEntity entity);
        void getNewsCloth(SearchListEntity name);
        void updateNewsCloth(upDateEntity entity);
    }

    interface View extends BaseView {
        void loadHouseClothSuccess(ArrayList<ClothingEntity> entities);
        void loadRoleColthesSelectSuccess(int position);
        void onCreateOrderSuccess(OrderEntity entity);
        void onPayOrderSuccess(PayResEntity entity);
        void getClothNewSuccess(ArrayList<SearchNewListEntity> searchNewLists);
        void updateSuccess();
    }
}
