package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
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
import java.util.Random;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yi on 2016/11/29.
 */

public class NewFeedPresenter implements NewFeedContract.Presenter {

    private NewFeedContract.View view;
    private ApiService apiService;

    @Inject
    public NewFeedPresenter(NewFeedContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void release() {
        view = null;
    }


    @Override
    public void loadList(final long time, String type, String id) {
        if ("follow".equals(type)) {
            apiService.loadFeedFollowList(time)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultSubscriber<ArrayList<NewDynamicEntity>>() {
                        @Override
                        public void onSuccess(ArrayList<NewDynamicEntity> newDynamicEntities) {
                            if (view != null) view.onLoadListSuccess(newDynamicEntities, time == 0);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if (view != null) view.onFailure(code, msg);
                        }
                    });
        } else if ("random".equals(type)) {
            apiService.loadFeedRandomList((int) time, ApiService.LENGHT)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultSubscriber<ArrayList<NewDynamicEntity>>() {
                        @Override
                        public void onSuccess(ArrayList<NewDynamicEntity> newDynamicEntities) {
                            if (view != null) view.onLoadListSuccess(newDynamicEntities, time == 0);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if (view != null) view.onFailure(code, msg);
                        }
                    });
        } else if ("ground".equals(type)) {
            apiService.loadFeedGroundList(time)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultSubscriber<ArrayList<NewDynamicEntity>>() {
                        @Override
                        public void onSuccess(ArrayList<NewDynamicEntity> newDynamicEntities) {
                            if (view != null) view.onLoadListSuccess(newDynamicEntities, time == 0);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if (view != null) view.onFailure(code, msg);
                        }
                    });
        } else if ("my".equals(type)) {
            apiService.loadFeedMyList(id, time)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultSubscriber<ArrayList<NewDynamicEntity>>() {
                        @Override
                        public void onSuccess(ArrayList<NewDynamicEntity> newDynamicEntities) {
                            if (view != null) view.onLoadListSuccess(newDynamicEntities, time == 0);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if (view != null) view.onFailure(code, msg);
                        }
                    });
        }
    }

    @Override
    public void loadList(final int index) {
        apiService.loadFeedFavoriteList(index, ApiService.LENGHT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<NewDynamicEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<NewDynamicEntity> newDynamicEntities) {
                        if (view != null) view.onLoadListSuccess(newDynamicEntities, index == 0);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void requestBannerData(String room) {
        apiService.requestNewBanner(room)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<BannerEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<BannerEntity> bannerEntities) {
                        if (view != null) view.onBannerLoadSuccess(bannerEntities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                    }
                });
    }

    @Override
    public void loadXianChongList() {
        apiService.loadXianChongList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<XianChongEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<XianChongEntity> entities) {
                        if (view != null) view.onLoadXianChongSuccess(entities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void loadFolder() {
        apiService.load24Folder()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<ShowFolderEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<ShowFolderEntity> entities) {
                        if (view != null) view.onLoadFolderSuccess(entities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void loadComment() {
        apiService.load24Comments(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<Comment24Entity>>() {
                    @Override
                    public void onSuccess(ArrayList<Comment24Entity> entities) {
                        if (view != null) {
                            if (entities.size() > 0) {
                                Random random = new Random();
                                int i = random.nextInt(entities.size());
                                view.onLoadCommentSuccess(entities.get(i));
                            } else {
                                view.onLoadCommentSuccess(null);
                            }
                        }
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void likeDoc(String id, final boolean isLike, final int position) {
        apiService.loadDocLike(!isLike,id )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if (view != null) view.onLikeDocSuccess(!isLike, position);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void loadDiscoverList(String type, long minIdx, long maxIdx, final boolean isPull) {
        if ("random".equals(type)) {
            apiService.loadDiscoverList(minIdx, maxIdx)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultSubscriber<ArrayList<DiscoverEntity>>() {
                        @Override
                        public void onSuccess(ArrayList<DiscoverEntity> entities) {
                            if (view != null) view.onLoadDiscoverListSuccess(entities, isPull);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if (view != null) view.onFailure(code, msg);
                        }
                    });
        } else if ("follow".equals(type)) {
            apiService.loadFeedNoticeListV4(minIdx)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultSubscriber<ArrayList<DiscoverEntity>>() {
                        @Override
                        public void onSuccess(ArrayList<DiscoverEntity> entities) {
                            if (view != null) view.onLoadDiscoverListSuccess(entities, isPull);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if (view != null) view.onFailure(code, msg);
                        }
                    });
        } else if ("ground".equals(type)) {
            apiService.loadHotDynamicList(minIdx, maxIdx)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultSubscriber<ArrayList<DiscoverEntity>>() {
                        @Override
                        public void onSuccess(ArrayList<DiscoverEntity> entities) {
                            if (view != null) view.onLoadDiscoverListSuccess(entities, isPull);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if (view != null) view.onFailure(code, msg);
                        }
                    });
        }
    }

    @Override
    public void loadOldDocList(String type, final long time) {
        apiService.loadOldDocList(type,time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<DocResponse>>() {
                    @Override
                    public void onSuccess(ArrayList<DocResponse> list) {
                        if(view != null) view.loadOldDocListSuccess(list,time == 0);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if(view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void loadDepartmentGroup(String id) {
        apiService.loadDepartmentGroup(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<DepartmentGroupEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<DepartmentGroupEntity> entity) {
                        if (view != null) view.onLoadGroupSuccess(entity);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void loadDocList(String id, final long timestamp) {
        apiService.loadDepartmentDocList(id, timestamp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<DocResponse>>() {
                    @Override
                    public void onSuccess(ArrayList<DocResponse> responses) {
                        if (view != null) view.onLoadDocListSuccess(responses, timestamp == 0);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void joinAuthor(final String id, final String name) {
        apiService.JoinAuthorGroup(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if (view != null) view.onJoinSuccess(id, name);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void createLabel(final TagSendEntity entity, final int position) {
        apiService.createNewTag(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if(view != null) view.onCreateLabel(s,entity.getTag(),position);
                    }

                    @Override
                    public void onFail(int code,String msg) {
                        if(view != null) view.onFailure(code,msg);
                    }
                });
    }
    @Override
    public void likeTag(final boolean isLike, final int position, TagLikeEntity entity, final int parentPosition) {
        apiService.plusTag(!isLike, entity.getDocId(), entity.getTagId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if (view != null) view.onPlusLabel(position, !isLike,parentPosition);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }
}
