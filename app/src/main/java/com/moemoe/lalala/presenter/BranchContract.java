package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.BranchStoryAllEntity;
import com.moemoe.lalala.model.entity.BranchStoryJoinEntity;

import java.util.ArrayList;

/**
 * Created by Hygge on 2018/6/12.
 */

public interface BranchContract {
    interface Presenter extends BasePresenter {
        void loadBranchStoryAll(boolean isSelect,int level);
        void loadBranchStoryJoin(BranchStoryJoinEntity entity);
    }

    interface View extends BaseView {
        void onLoadBranchStoryAllSuccess(ArrayList<BranchStoryAllEntity> entities);
        void onLoadBranchStoryJoin();
    }
}
