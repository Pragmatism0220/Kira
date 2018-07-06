package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.VisitorsEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/6/6.
 */

public interface NewVisitorsContract {
    interface Presenter extends BasePresenter {
        void getNewVisitorsInfo(int size, int start);
        void getHisVisitorsInfo(int size,int start,String hostUserId);
    }

    interface View extends BaseView {
        void getNewVisitorsInfoSuccess(ArrayList<VisitorsEntity> entities, boolean isPull);
        void getHiiVisitorsInfoSccess(ArrayList<VisitorsEntity> entities,boolean isPull);
    }
}
