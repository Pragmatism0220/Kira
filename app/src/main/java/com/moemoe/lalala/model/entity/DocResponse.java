package com.moemoe.lalala.model.entity;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class DocResponse implements Parcelable {

    private int comments;// 评论数
    private String content;
    private String cover;
    private String createTime;
    private UserTopEntity createUser;
    private String departmentId;
    private String departmentName;
    private boolean excellent;
    private String id;
    private String image;
    private ArrayList<Image> images;
    private ArrayList<DocTagEntity> tags;
    private ArrayList<UserFollowTagEntity> texts;
    private boolean thumb;
    private ArrayList<SimpleUserEntity> thumbUsers;
    private int thumbs;
    private long timestamp;
    private String title;
    private int likes;
    private boolean manager;
    private int retweets;
    private String from;//来自(USER : 关注人 其他:社团名称) ,
    private String schema;
    private String docId;
    private String docType;
    private String docTypeSchema;
    private String createUserId;
    private String createUserName;
    private String desc;
    private String icon;
    private String tagId;
    private int tagLikes;
    private int coinPayed;

    public int getCoinPayed() {
        return coinPayed;
    }

    public void setCoinPayed(int coinPayed) {
        this.coinPayed = coinPayed;
    }

    private int position;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isExcellent() {
        return excellent;
    }

    public String getImage() {
        return image;
    }

    public boolean isThumb() {
        return thumb;
    }

    public boolean isManager() {
        return manager;
    }

    public String getSchema() {
        return schema;
    }

    public String getDocId() {
        return docId;
    }

    public String getDocType() {
        return docType;
    }

    public String getDocTypeSchema() {
        return docTypeSchema;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public String getDesc() {
        return desc;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public void setDocTypeSchema(String docTypeSchema) {
        this.docTypeSchema = docTypeSchema;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public boolean getExcellent() {
        return excellent;
    }

    public boolean getThumb() {
        return thumb;
    }

    public ArrayList<SimpleUserEntity> getThumbUsers() {
        return thumbUsers;
    }

    public int getThumbs() {
        return thumbs;
    }

    public boolean getManager() {
        return manager;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public void setExcellent(boolean excellent) {
        this.excellent = excellent;
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

    public void setManager(boolean manager) {
        this.manager = manager;
    }

    public ArrayList<UserFollowTagEntity> getTexts() {
        return texts;
    }

    public void setTexts(ArrayList<UserFollowTagEntity> texts) {
        this.texts = texts;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserTopEntity getCreateUser() {
        return createUser;
    }

    public void setCreateUser(UserTopEntity createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public ArrayList<DocTagEntity> getTags() {
        return tags;
    }

    public void setTags(ArrayList<DocTagEntity> tags) {
        this.tags = tags;
    }

    public int getRetweets() {
        return retweets;
    }

    public void setRetweets(int retweets) {
        this.retweets = retweets;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getTagLikes() {
        return tagLikes;
    }

    public void setTagLikes(int tagLikes) {
        this.tagLikes = tagLikes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<DocResponse> CREATOR = new Parcelable.Creator<DocResponse>() {
        @Override
        public DocResponse createFromParcel(Parcel in) {
            DocResponse entity = new DocResponse();
            Bundle bundle;
            bundle = in.readBundle(getClass().getClassLoader());
            entity.comments = bundle.getInt("comments");
            entity.createUser = bundle.getParcelable("createUser");
            entity.createTime = bundle.getString("createTime");
            entity.timestamp = bundle.getLong("timestamp");
            entity.images = bundle.getParcelableArrayList("images");
            entity.manager = bundle.getBoolean("manager");
            entity.title = bundle.getString("title");
            entity.from = bundle.getString("from");
            entity.texts = bundle.getParcelableArrayList("texts");
            entity.tags = bundle.getParcelableArrayList("tags");
            entity.thumbUsers = bundle.getParcelableArrayList("thumbUsers");
            entity.id = bundle.getString("id");
            entity.excellent = bundle.getBoolean("excellent");
            entity.departmentName = bundle.getString("departmentName");
            entity.thumb = bundle.getBoolean("thumb");
            entity.retweets = bundle.getInt("retweets");
            entity.comments = bundle.getInt("comments");
            entity.likes = bundle.getInt("likes");
            entity.departmentId = bundle.getString("departmentId");
            entity.thumbs = bundle.getInt("thumbs");
            entity.cover = bundle.getString("cover");
            entity.content = bundle.getString("content");
            return entity;
        }

        @Override
        public DocResponse[] newArray(int size) {
            return new DocResponse[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("comments", comments);
        bundle.putParcelable("createUser", createUser);
        bundle.putString("createTime", createTime);
        bundle.putLong("timestamp", timestamp);
        bundle.putParcelableArrayList("images", images);
        bundle.putBoolean("manager", manager);
        bundle.putString("title", title);
        bundle.putString("from", from);
        bundle.putParcelableArrayList("texts", texts);
        bundle.putParcelableArrayList("tags", tags);
        bundle.putParcelableArrayList("thumbUsers", thumbUsers);
        bundle.putString("id", id);
        bundle.putString("departmentName", departmentName);
        bundle.putString("departmentId", departmentId);
        bundle.putBoolean("thumb", thumb);
        bundle.putInt("retweets", retweets);
        bundle.putInt("comments", comments);
        bundle.putInt("likes", likes);
        bundle.putString("cover", cover);
        bundle.putInt("thumbs", thumbs);
        bundle.putString("content", content);
        parcel.writeBundle(bundle);
    }

}
