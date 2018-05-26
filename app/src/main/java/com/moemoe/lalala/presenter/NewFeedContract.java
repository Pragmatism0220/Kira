package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.BannerEntity;
import com.moemoe.lalala.model.entity.Comment24Entity;
import com.moemoe.lalala.model.entity.DepartmentGroupEntity;
import com.moemoe.lalala.model.entity.DiscoverEntity;
import com.moemoe.lalala.model.entity.DocResponse;
import com.moemoe.lalala.model.entity.NewDynamicEntity;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.model.entity.TagLikeEntity;
import com.moemoe.lalala.model.entity.TagSendEntity;
import com.moemoe.lalala.model.entity.XianChongEntity;

import java.util.ArrayList;

/**
 * Created by yi on 2016/11/29.
 */

public interface NewFeedContract {
    interface Presenter extends BasePresenter{
        void loadList(long time, String type, String id);
        void loadList(int index);
        void requestBannerData(String room);
        void loadXianChongList();
        void loadFolder();
        void loadComment();
        void likeDoc(String id, boolean isLike, int position);
        void loadDiscoverList(String type, long minIdx, long maxIdx, boolean isPull);
        void loadOldDocList(String type,long time);
        void likeTag(boolean isLike,int position, TagLikeEntity entity,int parentPosition);

        void loadDepartmentGroup(String id);
        void loadDocList(String id,long timestamp);
        void joinAuthor(String id,String name);
        void createLabel(TagSendEntity entity,int position);
    }

    interface View extends BaseView{
        void onLoadListSuccess(ArrayList<NewDynamicEntity> resList, boolean isPull);
        void onBannerLoadSuccess(ArrayList<BannerEntity> bannerEntities);
        void onLoadXianChongSuccess(ArrayList<XianChongEntity> entities);
        void onLoadFolderSuccess(ArrayList<ShowFolderEntity> entities);
        void onLoadCommentSuccess(Comment24Entity entity);
        void onLikeDocSuccess(boolean isLike, int position);
        void onLoadDiscoverListSuccess(ArrayList<DiscoverEntity> entities, boolean isPull);
        void loadOldDocListSuccess(ArrayList<DocResponse> list,boolean isPull);
        void onPlusLabel(int position,boolean isLike,int parentPosition);
        void onLoadGroupSuccess(ArrayList<DepartmentGroupEntity> entity);
        void onLoadDocListSuccess(ArrayList<DocResponse> responses, boolean isPull);
        void onJoinSuccess(String id,String name);
        void onCreateLabel(String s,String name,int position);
    }
}
