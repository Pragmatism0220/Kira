package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.FeedNoticeEntity;
import com.moemoe.lalala.model.entity.UserFollowTagEntity;

import java.util.ArrayList;

/**
 * feed流关注页接口
 * Created by yi on 2018/1/11
 */

public interface NoticeContract {
    interface Presenter extends BasePresenter {
        void loadNotifyList(String type,long notifyTime);
    }

    interface View extends BaseView {
        void onLoadNotifyListSuccess(ArrayList<FeedNoticeEntity> entities , boolean isPull);
    }
}
