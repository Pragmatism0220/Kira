package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.AllPersonnelEntity;
import com.moemoe.lalala.model.entity.FeedFollowType2Entity;

/**
 * feed流关注页接口
 * Created by yi on 2018/1/11
 */

public interface CommunityContract {
    interface Presenter extends BasePresenter {
        void loadData(String id);
        void loadTagJoin(String tagId,boolean join);
        void loadTagAllPersonnel(String tagId);
    }

    interface View extends BaseView {
        void onLoadListSuccess(FeedFollowType2Entity entity);
        void onLoadTagJoinSuccess(boolean isJoin);
        void onLoadTagAllPersonnelSuccess(AllPersonnelEntity entity);
    }
}
