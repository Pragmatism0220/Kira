package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.AddressEntity;
import com.moemoe.lalala.model.entity.saveRecordEntity;

/**
 * Created by yi on 2016/11/29.
 */

public interface JuQIngChatContract {
    interface Presenter extends BasePresenter{
        void doneJuQing(String id);
        void newDownJuQing(saveRecordEntity entity);
    }

    interface View extends BaseView{
        void onDoneSuccess(long time);
        void newDownSuccess(long time);
    }
}
