package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.BagLoadReadprogressEntity;
import com.moemoe.lalala.model.entity.BagReadprogressEntity;
import com.moemoe.lalala.model.entity.LibraryContribute;
import com.moemoe.lalala.model.entity.ManHua2Entity;
import com.moemoe.lalala.model.entity.NewFolderEntity;
import com.moemoe.lalala.model.entity.ShowFolderEntity;

import java.util.ArrayList;

/**
 * Created by yi on 2016/11/29.
 */

public interface NewFolderItemXiaoShuoContract {
    interface Presenter extends BasePresenter {
        void loadBagReadprogress(BagReadprogressEntity entity);
        void loadBagReadpressUpdate(BagReadprogressEntity entity);
    }

    interface View extends BaseView {
        void onLoadBagReadprogressSuccess(BagLoadReadprogressEntity entity);
        void onloadBagReadpressUpdateSuccess();
    }
}
