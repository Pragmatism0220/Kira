package com.moemoe.lalala.model.entity;

import java.util.ArrayList;

/**
 * Created by Sora on 2018/3/7.
 */

public class NewDocResponseEntity {
//    title (string, optional): 标题

    private int comments;

    //    comments (integer, optional): 评论数 ,
//    content (string, optional): 内容 ,
//    cover (string, optional): 封面 ,
//    createTime (string, optional): 创建时间 ,
//    createUser (用户简介信息-返回, optional): 用户信息 ,
//    departmentId (string, optional): 学部ID ,
//    departmentName (string, optional): 学部名称 ,
//    excellent (boolean, optional): 是否为精品 ,
//    id (string, optional): 帖子ID ,
//    images (Array[Image], optional): 图片 ,
//    likes (integer, optional): 喜欢数 ,
//    manager (boolean, optional): 是否为部长 ,
//    retweets (integer, optional): 转发数 ,
//    tags (Array[TagResponse], optional): 拥有的标签 ,
//    texts (Array[关联标签Model-返回], optional): 标签信息 ,
//    thumb (boolean, optional): 是否点赞 ,
//    thumbUsers (Array[单个用户信息-返回], optional): 点赞的用户 ,
//    thumbs (integer, optional): 点赞数 ,
//    timestamp (integer, optional): 时间戳 ,

    private String content;
    private String cover;
    private String createTime;
    private UserTopEntity createUser;
    private String departmentId;
    private String departmentName;
    private boolean excellent;
    private String id;
    private ArrayList<Image> images;
    private ArrayList<DocTagEntity> tags;
    private ArrayList<UserFollowTagEntity> texts;
    private boolean thumb;
    private ArrayList<SimpleUserEntity> thumbUsers;
    private int thumbs;
    private int timestamp;
    private String title;
    private int likes;
    private boolean manager;
    private int retweets;

    public int getLikes() {
        return likes;
    }

    public boolean isManager() {
        return manager;
    }

    public int getRetweets() {
        return retweets;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setManager(boolean manager) {
        this.manager = manager;
    }

    public void setRetweets(int retweets) {
        this.retweets = retweets;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setCreateUser(UserTopEntity createUser) {
        this.createUser = createUser;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setExcellent(boolean excellent) {
        this.excellent = excellent;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public void setTags(ArrayList<DocTagEntity> tags) {
        this.tags = tags;
    }

    public void setTexts(ArrayList<UserFollowTagEntity> texts) {
        this.texts = texts;
    }

    public void setThumb(boolean thumb) {
        this.thumb = thumb;
    }

    public void setThumbUsers(ArrayList<SimpleUserEntity> thumbUsers) {
        this.thumbUsers = thumbUsers;
    }

    public void setThumbs(int thumbs) {
        this.thumbs = thumbs;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getComments() {
        return comments;
    }

    public String getContent() {
        return content;
    }

    public String getCover() {
        return cover;
    }

    public String getCreateTime() {
        return createTime;
    }

    public UserTopEntity getCreateUser() {
        return createUser;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public boolean isExcellent() {
        return excellent;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public ArrayList<DocTagEntity> getTags() {
        return tags;
    }

    public ArrayList<UserFollowTagEntity> getTexts() {
        return texts;
    }

    public boolean isThumb() {
        return thumb;
    }

    public ArrayList<SimpleUserEntity> getThumbUsers() {
        return thumbUsers;
    }

    public int getThumbs() {
        return thumbs;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getTitle() {
        return title;
    }
}
