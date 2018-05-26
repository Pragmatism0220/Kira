package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.DocResponse;
import com.moemoe.lalala.model.entity.TagLikeEntity;
import com.moemoe.lalala.model.entity.TagSendEntity;

import java.util.ArrayList;

/**
 * Created by yi on 2016/11/29.
 */

public interface FeedFollowOther2Contract {
    interface Presenter extends BasePresenter {
        void loadDocTagList(String tagId, long timestamp);
        void loadDocTagTopList(String tagId);
        void loadDocLike(String docId,boolean like,int position);
        void createLabel(TagSendEntity entity, int position);
        void likeTag(boolean isLike, int position, TagLikeEntity entity, int parentPosition);
    }

    interface View extends BaseView {
        void onLoadDocTagListSuccess(ArrayList<DocResponse> response, boolean isPull);
        void onLoadDocTagTopListSuccess(ArrayList<DocResponse> response);
        void onLoadDocLikeSuccess(boolean isLike,int position);
        void onCreateLabel(String s,String name,int position);
        void onPlusLabel(int position,boolean isLike,int parentPosition);
    }
}
