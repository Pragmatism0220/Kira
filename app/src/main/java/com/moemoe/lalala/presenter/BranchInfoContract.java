package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.BranchStoryAllEntity;

import java.util.ArrayList;

/**
 * Created by Hygge on 2018/6/13.
 */

public interface BranchInfoContract {
    interface Presenter extends BasePresenter {
        void loadBranchStoryInfo(String branchStoryId);
    }
    interface View extends BaseView {
        void onLoadBranchStoryInfoSuccess(BranchStoryAllEntity entities);
    }
}
