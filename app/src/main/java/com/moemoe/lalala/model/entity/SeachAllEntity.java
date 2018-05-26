package com.moemoe.lalala.model.entity;

import java.util.ArrayList;

/**
 * Created by Sora on 2018/3/8.
 */

public class SeachAllEntity {
//    doc (Array[帖子列表信息V2-返回], optional): 帖子 ,
//    dynamic (Array[动态信息-返回], optional): 动态 ,
//    user (Array[用户简介信息-返回], optional): 同学

    private ArrayList<DocResponse> doc;
    private ArrayList<NewDynamicEntity> dynamic;
    private ArrayList<UserTopEntity> user;
    private ArrayList<ShowFolderEntity> folder ;

    public ArrayList<DocResponse> getDoc() {
        return doc;
    }

    public void setDoc(ArrayList<DocResponse> doc) {
        this.doc = doc;
    }

    public ArrayList<ShowFolderEntity> getFolder() {
        return folder;
    }

    public void setFolder(ArrayList<ShowFolderEntity> folder) {
        this.folder = folder;
    }

    public ArrayList<NewDynamicEntity> getDynamic() {
        return dynamic;
    }

    public ArrayList<UserTopEntity> getUser() {
        return user;
    }

    public void setDynamic(ArrayList<NewDynamicEntity> dynamic) {
        this.dynamic = dynamic;
    }

    public void setUser(ArrayList<UserTopEntity> user) {
        this.user = user;
    }
}
