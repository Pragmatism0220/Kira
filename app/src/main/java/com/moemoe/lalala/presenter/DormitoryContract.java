package com.moemoe.lalala.presenter;

import android.content.Context;

import com.moemoe.lalala.model.entity.HouseLikeEntity;
import com.moemoe.lalala.model.entity.HouseMarkContainer;
import com.moemoe.lalala.model.entity.MapEntity;
import com.moemoe.lalala.model.entity.MapMarkContainer;
import com.moemoe.lalala.model.entity.OrderEntity;
import com.moemoe.lalala.model.entity.PayReqEntity;
import com.moemoe.lalala.model.entity.PayResEntity;
import com.moemoe.lalala.model.entity.PowerEntity;
import com.moemoe.lalala.model.entity.PropUseEntity;
import com.moemoe.lalala.model.entity.RubbishEntity;
import com.moemoe.lalala.model.entity.RubblishBody;
import com.moemoe.lalala.model.entity.SaveVisitorEntity;
import com.moemoe.lalala.model.entity.VisitorsEntity;
import com.moemoe.lalala.view.widget.map.MapLayout;
import com.moemoe.lalala.view.widget.map.MapWidget;

import java.util.ArrayList;

/**
 * Created by Hygge on 2018/5/24.
 */

public interface DormitoryContract {
    interface Presenter extends BasePresenter {
        void addMapMark(Context context, MapMarkContainer container, MapLayout map, String type);
        void loadHouseObjects(boolean isHouse,String visitorId);
        void loadRoleLikeCollect(String roleId);
        void getVisitorsInfo();
        void saveVisitor(SaveVisitorEntity request);
        void loadHouseRubblish(RubblishBody req);
        void loadHouseSave(RubblishBody re);
        void getHiVisitorsInfo(String userId);
        void loadPower();
        void houseToolUse(String toolId);
        void createOrder(String id);
        void payOrder(PayReqEntity entity);
        void isComplete();
    }

    interface View extends BaseView {
        void onLoadHouseObjects(ArrayList<MapEntity> entities);
        void onLoadRoleLikeCollect(HouseLikeEntity entity);
        void getVisitorsInfoSuccess(ArrayList<VisitorsEntity> entities);
        void saveVisitorSuccess();
        void onLoadHouseRubblish(RubbishEntity entity);
        void onLoadHouseSave();
        void onLoadPowerSuccess(PowerEntity entity);
        void getHisVisitorsInfo(ArrayList<VisitorsEntity> entities);
        void onLoadHouseToolUse(PropUseEntity entity);
        void onCreateOrderSuccess(OrderEntity entity);
        void onPayOrderSuccess(PayResEntity entity);
        void isCompleteSuccess(boolean isComplete);
    }
}
