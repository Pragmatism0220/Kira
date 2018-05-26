package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.BagVisitorEntity;
import com.moemoe.lalala.model.entity.UserTopEntity;

import java.util.ArrayList;

/**
 * Created by Sora on 2018/3/6.
 */

public interface BagVisitorContract {
    interface Presenter extends BasePresenter {
        void loadBagVisitor(String userId, int index);
    }

    interface View extends BaseView {
        void onLoadBagVisitorSuccess(ArrayList<UserTopEntity> entities, boolean isPull);
    }
}
