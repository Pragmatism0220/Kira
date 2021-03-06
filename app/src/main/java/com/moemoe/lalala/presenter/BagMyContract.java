package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.AddressEntity;
import com.moemoe.lalala.model.entity.BagMyEntity;
import com.moemoe.lalala.model.entity.BagMyShowEntity;
import com.moemoe.lalala.model.entity.NewBagEntity;

import java.util.ArrayList;

/**
 * Created by yi on 2016/11/29.
 */

public interface BagMyContract {
    interface Presenter extends BasePresenter{
        void loadContent(String type,String userId);
        void loadBagData(String userId);
    }

    interface View extends BaseView{
        void onLoadSuccess(ArrayList<BagMyShowEntity> entities);
        void onLoadBagSuccess(NewBagEntity entity);
    }

}
