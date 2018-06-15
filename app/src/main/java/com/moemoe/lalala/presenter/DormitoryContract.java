package com.moemoe.lalala.presenter;

import android.content.Context;

import com.moemoe.lalala.model.entity.HouseLikeEntity;
import com.moemoe.lalala.model.entity.HouseMarkContainer;
import com.moemoe.lalala.model.entity.MapEntity;
import com.moemoe.lalala.model.entity.MapMarkContainer;
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
    }

    interface View extends BaseView {
        void onLoadHouseObjects(ArrayList<MapEntity> entities);
        void onLoadRoleLikeCollect(HouseLikeEntity entity);
        void getVisitorsInfoSuccess(ArrayList<VisitorsEntity> entities);
        void saveVisitorSuccess();
    }
}
