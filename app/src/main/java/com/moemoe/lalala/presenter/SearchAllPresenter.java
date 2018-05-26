package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
import com.moemoe.lalala.model.entity.DocResponse;
import com.moemoe.lalala.model.entity.FeedFollowType1Entity;
import com.moemoe.lalala.model.entity.NewDynamicEntity;
import com.moemoe.lalala.model.entity.RecommendTagEntity;
import com.moemoe.lalala.model.entity.SeachAllEntity;
import com.moemoe.lalala.model.entity.SearchEntity;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.model.entity.UserTopEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yi on 2018/1/11.
 */

public class SearchAllPresenter implements SearchAllContract.Presenter {

    private SearchAllContract.View view;
    private ApiService apiService;

    @Inject
    public SearchAllPresenter(SearchAllContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void release() {
        view = null;
    }

    @Override
    public void loadSearchAllList(SearchEntity parameter, final String type) {
        if ("all".equals(type)) {
            apiService.loadSearchAllList(parameter)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultSubscriber<SeachAllEntity>() {
                        @Override
                        public void onSuccess(SeachAllEntity entities) {
                            if (view != null) view.onLoadSearchAllListSuccess(entities, type);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if (view != null) view.onFailure(code, msg);
                        }
                    });
        } else if ("doc".equals(type)) {
            apiService.loadSearchDocList(parameter)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultSubscriber<ArrayList<DocResponse>>() {
                        @Override
                        public void onSuccess(ArrayList<DocResponse> entities) {
                            SeachAllEntity entity = new SeachAllEntity();
                            entity.setDoc(entities);
                            if (view != null) view.onLoadSearchAllListSuccess(entity, type);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if (view != null) view.onFailure(code, msg);
                        }
                    });
        } else if ("folder".equals(type)) {
            apiService.LoadSearchBag(parameter)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultSubscriber<ArrayList<ShowFolderEntity>>() {
                        @Override
                        public void onSuccess(ArrayList<ShowFolderEntity> entities) {
                            SeachAllEntity entity = new SeachAllEntity();
                            entity.setFolder(entities);
                            if (view != null) view.onLoadSearchAllListSuccess(entity, type);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if (view != null) view.onFailure(code, msg);
                        }
                    });
        } else if ("user".equals(type)) {
            apiService.loadSearchMateList(parameter)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultSubscriber<ArrayList<UserTopEntity>>() {
                        @Override
                        public void onSuccess(ArrayList<UserTopEntity> entities) {
                            SeachAllEntity entity = new SeachAllEntity();
                            entity.setUser(entities);
                            if (view != null) view.onLoadSearchAllListSuccess(entity, type);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if (view != null) view.onFailure(code, msg);
                        }
                    });
        } else if ("dynamic".equals(type)) {
            apiService.loadSearchDynamicList(parameter)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultSubscriber<ArrayList<NewDynamicEntity>>() {
                        @Override
                        public void onSuccess(ArrayList<NewDynamicEntity> entities) {
                            SeachAllEntity entity = new SeachAllEntity();
                            entity.setDynamic(entities);
                            if (view != null) view.onLoadSearchAllListSuccess(entity, type);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if (view != null) view.onFailure(code, msg);
                        }
                    });
        }
    }

    @Override
    public void loadRecommendTagV2(String folderType) {
        apiService.loadRecommendTagV2(folderType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<RecommendTagEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<RecommendTagEntity> entities) {
                        if (view != null) view.onLoadRecommendTagV2Success(entities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }
    @Override
    public void loadRecommendTag(String folderType) {
        apiService.loadRecommendTag(folderType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<RecommendTagEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<RecommendTagEntity> entities) {
                        if(view != null) view.onLoadRecommendTagSuccess(entities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if(view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void loadKeyWordTag(String keyWord) {
        apiService.loadKeywordTag(keyWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<RecommendTagEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<RecommendTagEntity> entities) {
                        if(view != null) view.onLoadKeyWordTagSuccess(entities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if(view != null) view.onFailure(code, msg);
                    }
                });
    }
    @Override
    public void likeDynamic(String id, final boolean isLike, final int position) {
        apiService.likeDynamic(id,!isLike)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if(view!=null)view.onLikeDynamicSuccess(!isLike,position);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if(view!=null)view.onFailure(code, msg);
                    }
                });
    }
}
