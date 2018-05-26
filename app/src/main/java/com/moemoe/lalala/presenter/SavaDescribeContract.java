package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.AddressEntity;

/**
 *
 * Created by yi on 2016/11/29.
 */

public interface SavaDescribeContract {
    interface Presenter extends BasePresenter{
        void loadSaveDescribe(String  thiId,String describe);
    }

    interface View extends BaseView{
        void onSaveDescribeSuccess();
    }
}
