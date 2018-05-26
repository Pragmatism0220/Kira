package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
import com.moemoe.lalala.model.entity.AddressEntity;
import com.moemoe.lalala.model.entity.PhoneMenuEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 *
 * Created by yi on 2016/11/29.
 */

public class PhoneMenuListPresenter implements PhoneMenuListContract.Presenter {

    private PhoneMenuListContract.View view;
    private ApiService apiService;

    @Inject
    public PhoneMenuListPresenter(PhoneMenuListContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void release() {
        view = null;
    }

    @Override
    public void loadUserList(String type, final int index) {
        if(type.equals("both")){
            apiService.loadFollowListBoth(index,ApiService.LENGHT)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultSubscriber<ArrayList<PhoneMenuEntity>>() {
                        @Override
                        public void onSuccess(ArrayList<PhoneMenuEntity> entities) {
                            if(view != null) view.onLoadUserListSuccess(entities,index == 0);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if(view != null) view.onFailure(code, msg);
                        }
                    });
        }else if(type.equals("follow")){
            apiService.loadFollowListFollow(index,ApiService.LENGHT)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultSubscriber<ArrayList<PhoneMenuEntity>>() {
                        @Override
                        public void onSuccess(ArrayList<PhoneMenuEntity> entities) {
                            if(view != null) view.onLoadUserListSuccess(entities,index == 0);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if(view != null) view.onFailure(code, msg);
                        }
                    });
        }else if(type.equals("fans")){
            apiService.loadFollowListFans(index,ApiService.LENGHT)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultSubscriber<ArrayList<PhoneMenuEntity>>() {
                        @Override
                        public void onSuccess(ArrayList<PhoneMenuEntity> entities) {
                            if(view != null) view.onLoadUserListSuccess(entities,index == 0);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if(view != null) view.onFailure(code, msg);
                        }
                    });
        }else if (type.equals("recommend")){
            apiService.loadFeedRecommentUserListV2()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultSubscriber<ArrayList<PhoneMenuEntity>>() {
                        @Override
                        public void onSuccess(ArrayList<PhoneMenuEntity> entities) {
                            if(view != null) view.onLoadUserListSuccess(entities,index == 0);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if(view != null) view.onFailure(code, msg);
                        }
                    });
        }
    }
    @Override
    public void followUser(String id, boolean isFollow, final int position) {
        if (!isFollow){
            apiService.followUser(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetSimpleResultSubscriber() {
                        @Override
                        public void onSuccess() {
                            if(view != null) view.onFollowSuccess(true,position);
                        }

                        @Override
                        public void onFail(int code,String msg) {
                            if(view != null) view.onFailure(code,msg);
                        }
                    });
        }else {
            apiService.cancelfollowUser(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetSimpleResultSubscriber() {
                        @Override
                        public void onSuccess() {
                            if(view != null) view.onFollowSuccess(false,position);
                        }

                        @Override
                        public void onFail(int code,String msg) {
                            if(view != null) view.onFailure(code,msg);
                        }
                    });
        }
    }
}
