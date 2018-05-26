package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.DanmakuSend;
import com.moemoe.lalala.model.entity.KiraVideoEntity;
import com.moemoe.lalala.model.entity.ShowFolderEntity;

import java.util.ArrayList;

/**
 *
 * Created by yi on 2016/11/29.
 */

public interface KiraVideoContract {
    interface Presenter extends BasePresenter{
        void loadVideoInfo(String type,String folderId,String fileId);
        void refreshRecommend(String folderName, int page, String excludeFolderId);
        void loadDanmaku(String id);
        void sendDanmaku(DanmakuSend send);
        void favMovie(String type,boolean isFav,String folderId,String fileId);
        void buyFile(String folderId,String fileType,String fileId);
    }

    interface View extends BaseView{
        void onLoadVideoInfoSuccess(KiraVideoEntity entity);
        void onLoadVideoInfoFail();
        void onReFreshSuccess(ArrayList<ShowFolderEntity> entities);
        void onLoadDanmakuSuccess(String uri);
        void onSendDanmakuSuccess();
        void onFavMovieSuccess();
        void onBuyFileSuccess();
    }
}
