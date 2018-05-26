package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.RecommendTagEntity;
import com.moemoe.lalala.model.entity.SeachAllEntity;
import com.moemoe.lalala.model.entity.SearchEntity;

import java.util.ArrayList;

/**
 * feed流关注页接口
 * Created by yi on 2018/1/11
 */

public interface SearchAllContract {
    interface Presenter extends BasePresenter {
        void loadSearchAllList(SearchEntity parameter, String type);
        void loadRecommendTagV2(String folderType);
        void loadRecommendTag(String folderType);
        void loadKeyWordTag(String keyWord);
        void likeDynamic(String id,boolean isLike,int position);
    }

    interface View extends BaseView {
        void onLoadSearchAllListSuccess(SeachAllEntity entity, String type);
        void onLoadRecommendTagV2Success(ArrayList<RecommendTagEntity> entities);
        void onLoadRecommendTagSuccess(ArrayList<RecommendTagEntity> entities);
        void onLoadKeyWordTagSuccess(ArrayList<RecommendTagEntity> entities);
        void onLikeDynamicSuccess(boolean isLike,int position);
    }
}
