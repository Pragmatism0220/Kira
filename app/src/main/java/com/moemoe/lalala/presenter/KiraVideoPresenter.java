package com.moemoe.lalala.presenter;

import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
import com.moemoe.lalala.model.entity.ApiResult;
import com.moemoe.lalala.model.entity.DanmakuSend;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.KiraVideoEntity;
import com.moemoe.lalala.model.entity.ShowFolderEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 *
 * Created by yi on 2016/11/29.
 */

public class KiraVideoPresenter implements KiraVideoContract.Presenter {

    private KiraVideoContract.View view;
    private ApiService apiService;

    @Inject
    public KiraVideoPresenter(KiraVideoContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void release() {
        view = null;
    }


    @Override
    public void loadVideoInfo(String type,String folderId,String fileId) {
        if(FolderType.SP.toString().equals(type)){
            apiService.loadVideoInfo(folderId, fileId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultSubscriber<KiraVideoEntity>() {
                        @Override
                        public void onSuccess(KiraVideoEntity entity) {
                            if(view != null) view.onLoadVideoInfoSuccess(entity);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if(view != null){
                                view.onFailure(code, msg);
                                view.onLoadVideoInfoFail();
                            }
                        }
                    });
        }else {
            apiService.loadMusicInfo(folderId, fileId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultSubscriber<KiraVideoEntity>() {
                        @Override
                        public void onSuccess(KiraVideoEntity entity) {
                            if(view != null) view.onLoadVideoInfoSuccess(entity);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if(view != null){
                                view.onFailure(code, msg);
                                view.onLoadVideoInfoFail();
                            }
                        }
                    });
        }
    }

    @Override
    public void refreshRecommend(String folderName, int page, String excludeFolderId) {
        apiService.loadRefreshList(excludeFolderId,folderName,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<ShowFolderEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<ShowFolderEntity> entities) {
                        if(view != null) view.onReFreshSuccess(entities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if(view != null) view.onFailure(code,msg);
                    }
                });
    }

    @Override
    public void loadDanmaku(String id) {
        apiService.loadDanmaku(id)
                .subscribeOn(Schedulers.io())
                .map(new Function<ApiResult<String>, String>() {
                    @Override
                    public String apply(ApiResult<String> stringApiResult) throws Exception {
                        if (stringApiResult.getState() == 200) {
                            File danmu = File.createTempFile("kiral_danmu", ".xml", MoeMoeApplication.getInstance().getCacheDir());
                            FileOutputStream fileOutputStream = new FileOutputStream(danmu);
                            fileOutputStream.write(stringApiResult.getData().getBytes());
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            return danmu.getAbsolutePath();
                        }else{
                            if(view != null){
                                view.onFailure(stringApiResult.getState(),stringApiResult.getMessage());
                            }
                            return "";
                        }

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (view != null) view.onLoadDanmakuSuccess(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable){
                        if(view != null){
                            view.onFailure(-1,throwable.getMessage());
                        }
                    }
                });
    }

    @Override
    public void sendDanmaku(DanmakuSend send) {
        apiService.sendDanmaku(send)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if(view != null) view.onSendDanmakuSuccess();
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void favMovie(String type,boolean isFav,String folderId,String fileId) {
        if(!isFav){
            apiService.favMoiveOrMusic(type,folderId,fileId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetSimpleResultSubscriber() {
                        @Override
                        public void onSuccess() {
                            if (view != null) view.onFavMovieSuccess();
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if (view != null) view.onFailure(code, msg);
                        }
                    });
        }else {
            apiService.cancelFavMoiveOrMusic(type,folderId,fileId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetSimpleResultSubscriber() {
                        @Override
                        public void onSuccess() {
                            if (view != null) view.onFavMovieSuccess();
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if (view != null) view.onFailure(code, msg);
                        }
                    });
        }
    }

    @Override
    public void buyFile(String folderId, String fileType, String fileId) {
        apiService.buyFile(folderId, fileType, fileId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if (view != null) view.onBuyFileSuccess();
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }
}
