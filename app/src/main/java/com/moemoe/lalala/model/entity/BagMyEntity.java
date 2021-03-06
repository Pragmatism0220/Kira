package com.moemoe.lalala.model.entity;

import java.util.ArrayList;

/**
 *
 * Created by yi on 2017/8/16.
 */

public class BagMyEntity {

    private int synthesizeNum; // 综合项数

    private ArrayList<ShowFolderEntity> synthesizeList;// 综合Top3

    private int imageNum; // 图集项数

    private ArrayList<ShowFolderEntity> imageList; // 图集Top3

    private int cartoonNum; // 漫画项

    private ArrayList<ShowFolderEntity> cartoonList; // 漫画Top3

    private int fictionNum; // 小说项

    private ArrayList<ShowFolderEntity> fictionList;// 小说Top3

    private int articleNum; // 文章项

    private ArrayList<ShowFolderEntity> articleList;

    private ArrayList<ShowFolderEntity> followList;

    private int musicNum;

    private ArrayList<ShowFolderEntity> musicList;

    private int videoNum;

    private ArrayList<ShowFolderEntity> videoList;


    public BagMyEntity(){
        synthesizeList = new ArrayList<>();
        imageList = new ArrayList<>();
        cartoonList = new ArrayList<>();
        fictionList = new ArrayList<>();
        followList = new ArrayList<>();
        articleList = new ArrayList<>();
        musicList = new ArrayList<>();
        videoList = new ArrayList<>();
    }

    public int getSynthesizeNum() {
        return synthesizeNum;
    }

    public void setSynthesizeNum(int synthesizeNum) {
        this.synthesizeNum = synthesizeNum;
    }

    public ArrayList<ShowFolderEntity> getSynthesizeList() {
        return synthesizeList;
    }

    public void setSynthesizeList(ArrayList<ShowFolderEntity> synthesizeList) {
        this.synthesizeList = synthesizeList;
    }

    public int getImageNum() {
        return imageNum;
    }

    public void setImageNum(int imageNum) {
        this.imageNum = imageNum;
    }

    public ArrayList<ShowFolderEntity> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<ShowFolderEntity> imageList) {
        this.imageList = imageList;
    }

    public int getCartoonNum() {
        return cartoonNum;
    }

    public void setCartoonNum(int cartoonNum) {
        this.cartoonNum = cartoonNum;
    }

    public ArrayList<ShowFolderEntity> getCartoonList() {
        return cartoonList;
    }

    public void setCartoonList(ArrayList<ShowFolderEntity> cartoonList) {
        this.cartoonList = cartoonList;
    }

    public int getFictionNum() {
        return fictionNum;
    }

    public void setFictionNum(int fictionNum) {
        this.fictionNum = fictionNum;
    }

    public ArrayList<ShowFolderEntity> getFictionList() {
        return fictionList;
    }

    public void setFictionList(ArrayList<ShowFolderEntity> fictionList) {
        this.fictionList = fictionList;
    }

    public int getArticleNum() {
        return articleNum;
    }

    public void setArticleNum(int articleNum) {
        this.articleNum = articleNum;
    }

    public ArrayList<ShowFolderEntity> getFollowList() {
        return followList;
    }

    public void setFollowList(ArrayList<ShowFolderEntity> followList) {
        this.followList = followList;
    }

    public ArrayList<ShowFolderEntity> getArticleList() {
        return articleList;
    }

    public void setArticleList(ArrayList<ShowFolderEntity> articleList) {
        this.articleList = articleList;
    }

    public int getMusicNum() {
        return musicNum;
    }

    public void setMusicNum(int musicNum) {
        this.musicNum = musicNum;
    }

    public ArrayList<ShowFolderEntity> getMusicList() {
        return musicList;
    }

    public void setMusicList(ArrayList<ShowFolderEntity> musicList) {
        this.musicList = musicList;
    }

    public int getVideoNum() {
        return videoNum;
    }

    public void setVideoNum(int videoNum) {
        this.videoNum = videoNum;
    }

    public ArrayList<ShowFolderEntity> getVideoList() {
        return videoList;
    }

    public void setVideoList(ArrayList<ShowFolderEntity> videoList) {
        this.videoList = videoList;
    }
}
