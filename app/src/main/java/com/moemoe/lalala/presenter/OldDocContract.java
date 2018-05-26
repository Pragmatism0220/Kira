package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.BannerEntity;
import com.moemoe.lalala.model.entity.DocResponse;
import com.moemoe.lalala.model.entity.TagLikeEntity;
import com.moemoe.lalala.model.entity.TagSendEntity;

import java.util.ArrayList;

/**
 * Created by yi on 2016/11/29.
 */

public interface OldDocContract {
    interface Presenter extends BasePresenter{
        void loadOldDocList(String type,long time);
        void requestBannerData(String room);
        void createLabel(TagSendEntity entity, int position);
        void loadDocLike(String docId,boolean like,int position);
        void likeTag(boolean isLike, int position, TagLikeEntity entity, int parentPosition);
    }

    interface View extends BaseView{
        void loadOldDocListSuccess(ArrayList<DocResponse> list,boolean isPull);
        void onBannerLoadSuccess(ArrayList<BannerEntity> bannerEntities);
        void onCreateLabel(String s,String name,int position);
        void onLoadDocLikeSuccess(boolean isLike,int position);
        void onPlusLabel(int position,boolean isLike,int parentPosition);
    }
}
