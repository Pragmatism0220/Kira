package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.AddressEntity;
import com.moemoe.lalala.model.entity.FolderRepEntity;
import com.moemoe.lalala.model.entity.LibraryContribute;

import java.util.ArrayList;

/**
 *
 * Created by yi on 2016/11/29.
 */

public interface FileUploadContract {
    interface Presenter extends BasePresenter{
        void checkSize(long size);
        void uploadFiles(String folderType,String folderId,String parentFolderId,String name,String orderByType,ArrayList<Object> items,String cover,int coverSize,int coin,String desc,ArrayList<String> tags);
        void createMh(String folderId, String parentFolderId, String name, ArrayList<Object> items);
        void loadLibrarySubmitContribute(LibraryContribute entity);
        void addFolder(FolderRepEntity entity);
    }

    interface View extends BaseView{
        void onCheckSize(boolean isOk);
        void onSuccess(String folderId);
        void onUploadFilesSuccess(String fileId);
        void onLoadLibrarySubmitContribute();
    }
}
