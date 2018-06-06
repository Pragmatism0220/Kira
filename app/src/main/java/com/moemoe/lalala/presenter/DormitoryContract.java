package com.moemoe.lalala.presenter;

import android.content.Context;

import com.moemoe.lalala.model.entity.HouseLikeEntity;
import com.moemoe.lalala.model.entity.HouseMarkContainer;
import com.moemoe.lalala.model.entity.MapEntity;
import com.moemoe.lalala.model.entity.MapMarkContainer;
import com.moemoe.lalala.view.widget.map.MapLayout;
import com.moemoe.lalala.view.widget.map.MapWidget;

import java.util.ArrayList;

/**
 * Created by Hygge on 2018/5/24.
 */

public interface DormitoryContract {
    interface Presenter extends BasePresenter {
        void addMapMark(Context context, MapMarkContainer container, MapLayout map, String type);
        void addMapMark(Context context, MapMarkContainer container, MapWidget map, String type);
        void addEventMark(String id,String icon,MapMarkContainer container,Context context, MapWidget map,String storyId);
        void loadHouseObjects();
        void loadRoleLikeGet();
        void loadRoleLikeCollect(String roleId);
    }

    interface View extends BaseView {
        void onLoadHouseObjects(ArrayList<MapEntity> entities);
        void onLoadRoleLikeGet(ArrayList<HouseLikeEntity> entity);
        void onLoadRoleLikeCollect(HouseLikeEntity entity);
    }
}
