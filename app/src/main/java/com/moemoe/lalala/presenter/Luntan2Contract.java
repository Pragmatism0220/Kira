package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.DepartmentGroupEntity;
import com.moemoe.lalala.model.entity.DocResponse;
import com.moemoe.lalala.model.entity.TagLikeEntity;
import com.moemoe.lalala.model.entity.TagSendEntity;

import java.util.ArrayList;

/**
 *
 * Created by yi on 2016/11/29.
 */

public interface Luntan2Contract {
    interface Presenter extends BasePresenter{
        void loadDepartmentGroup(String id);
        void loadDocList(String id,long timestamp);
        void joinAuthor(String id,String name);
        void loadFollowList(int index);
        void likeDoc(String id, boolean isLike, int position);
        void createLabel(TagSendEntity entity,int position);
        void likeTag(boolean isLike,int position, TagLikeEntity entity,int parentPosition);
    }

    interface View extends BaseView{
        void onLoadGroupSuccess(ArrayList<DepartmentGroupEntity> entity);
        void onLoadDocListSuccess(ArrayList<DocResponse> responses,boolean isPull);
        void onLoadFollowListSuccess(ArrayList<DocResponse> responses,boolean isPull);
        void onJoinSuccess(String id,String name);
        void onLikeDocSuccess(boolean isLike, int position);
        void onCreateLabel(String s,String name,int position);
        void onPlusLabel(int position,boolean isLike,int parentPosition);
    }
}
