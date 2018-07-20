package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.BannerEntity;
import com.moemoe.lalala.model.entity.ShowFolderEntity;

import java.util.ArrayList;

/**
 * Created by Hygge on 2018/7/18.
 */

public interface LibraryContract {
    interface Presenter extends BasePresenter {
        void requestBannerData(String room);
        void loadLibraryBagList(String type, int index);
    }

    interface View extends BaseView {
        void onBannerLoadSuccess(ArrayList<BannerEntity> bannerEntities);
        void onLoadLibraryListSuccess(ArrayList<ShowFolderEntity> entities, boolean isPull);
    }
}
