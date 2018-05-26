package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.ShowFolderEntity;

import java.util.ArrayList;

/**
 * Created by Sora on 2018/3/6.
 */

public interface NewUploadContract {
    interface Presenter extends BasePresenter {
        void loadTagBagNewest(String tagId, int index);
    }

    interface View extends BaseView {
        void onLoadTagBagNewestSuccess(ArrayList<ShowFolderEntity> entities,boolean isPull);
    }
}
