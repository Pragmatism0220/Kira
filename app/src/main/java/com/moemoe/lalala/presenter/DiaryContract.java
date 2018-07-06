package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.DiaryEntity;

/**
 * Created by Administrator on 2018/7/5.
 */

public interface DiaryContract {
    interface Presenter extends BasePresenter {
        void getDiaryInfo(String roleId);
    }

    interface View extends BaseView {
        void getDiarySuccess(DiaryEntity entity);
    }
}
