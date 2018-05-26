package com.moemoe.lalala.model.entity;

import java.util.ArrayList;

/**
 * Created by yi on 2018/1/17.
 */

public class FeedFollowOther1Entity {

    private ArrayList<ShowFolderEntity> videos;
    private ArrayList<ShowFolderEntity> mangas;
    private ArrayList<FeedFollowType1Entity> articles;
    private ArrayList<FeedFollowType1Entity> musics;
    private int articleNum;
    private int folderNum;
    private int videoNum;
    private int musicNum;

    public FeedFollowOther1Entity() {
        videos = new ArrayList<>();
        mangas = new ArrayList<>();
        articles = new ArrayList<>();
        musics = new ArrayList<>();
    }

    public ArrayList<ShowFolderEntity> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<ShowFolderEntity> videos) {
        this.videos = videos;
    }

    public ArrayList<ShowFolderEntity> getMangas() {
        return mangas;
    }

    public void setMangas(ArrayList<ShowFolderEntity> mangas) {
        this.mangas = mangas;
    }

    public ArrayList<FeedFollowType1Entity> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<FeedFollowType1Entity> articles) {
        this.articles = articles;
    }

    public ArrayList<FeedFollowType1Entity> getMusics() {
        return musics;
    }

    public void setMusics(ArrayList<FeedFollowType1Entity> musics) {
        this.musics = musics;
    }

    public void setVideoNum(int videoNum) {
        this.videoNum = videoNum;
    }

    public int getVideoNum() {
        return videoNum;
    }

    public void setArticleNum(int articleNum) {
        this.articleNum = articleNum;
    }

    public int getArticleNum() {
        return articleNum;
    }

    public void setFolderNum(int folderNum) {
        this.folderNum = folderNum;
    }

    public int getFolderNum() {
        return folderNum;
    }

    public void setMusicNum(int musicNum) {
        this.musicNum = musicNum;
    }

    public int getMusicNum() {
        return musicNum;
    }

}
