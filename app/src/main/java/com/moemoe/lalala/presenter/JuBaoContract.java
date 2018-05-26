package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.AddressEntity;
import com.moemoe.lalala.model.entity.ReportEntity;

/**
 * Created by yi on 2016/11/29.
 */

public interface JuBaoContract {
    interface Presenter extends BasePresenter{
        void doRequest(Object data,int type);
        void deleteDocV2(String id, ReportEntity bean, int position);
    }

    interface View extends BaseView{
        void onSuccess(Object o);
        void onDeleteDocV2Success(int position);
    }
}
