package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.VisitorsEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/5/31.
 */

public interface VisitorsContract {

    interface Presenter extends BasePresenter {
        void getVisitorsInfo(int size, int start);
    }

    interface View extends BaseView {
        void getVisitorsInfoSuccess(ArrayList<VisitorsEntity> entities, boolean isPull);
    }
}
