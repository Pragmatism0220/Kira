package com.moemoe.lalala.model.entity;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by yi on 2016/12/2.
 */

public class DocDetailEntity {
    @SerializedName("manager")
    private boolean manager;
    @SerializedName("excellent")
    private boolean excellent;
    @SerializedName("top")
    private boolean top;
    @SerializedName("id")
    private String id;
    @SerializedName("coin")
    private int coin;
    @SerializedName("coinDetails")
    private ArrayList<Detail> coinDetails;
    @SerializedName("coinPays")
    private int coinPays;
    @SerializedName("comments")
    private int comments;
    @SerializedName("createTime")
    private String createTime;
    @SerializedName("details")
    private ArrayList<Detail> details;
    @SerializedName("favoriteFlag")
    private boolean favoriteFlag;
    @SerializedName("likes")
    private int likes;
    @SerializedName("share")
    private ShareInfo share;
    @SerializedName("tags")
    private ArrayList<DocTagEntity> tags;
    @SerializedName("title")
    private String title;
    @SerializedName("updateTime")
    private String updateTime;
    @SerializedName("userIcon")
    private String userIcon;
    @SerializedName("userIconH")
    private int userIconH;
    @SerializedName("userIconW")
    private int userIconW;
    @SerializedName("userId")
    private String userId;
    @SerializedName("userLevel")
    private int userLevel;
    @SerializedName("userLevelColor")
    private String userLevelColor;
    @SerializedName("userLevelName")
    private String userLevelName;
    @SerializedName("userScore")
    private int userScore;
    @SerializedName("userSex")
    private String userSex;
    @SerializedName("userName")
    private String userName;
    @SerializedName("folderInfo")
    private BagDirEntity folderInfo;
    @SerializedName("badgeList")
    private ArrayList<BadgeEntity> badgeList;
    @SerializedName("coinComment")
    private boolean coinComment;
    @SerializedName("followUser")
    private boolean followUser;
    @SerializedName("cover")
    private String cover;
    @SerializedName("vip")
    private boolean vip;
    @SerializedName("maxNum")
    private Integer maxNum;
    @SerializedName("nowNum")
    private Integer nowNum;
    @SerializedName("rtNum")
    private int rtNum;
    @SerializedName("texts")
    private ArrayList<UserFollowTagEntity> texts;
    @SerializedName("canDelete")
    private boolean canDelete;
    @SerializedName("tagId")
    private String tagId;
    @SerializedName("tagName")
    private String tagName;
    @SerializedName("joinTag")
    private boolean joinTag;
    @SerializedName("thumb")
    private boolean thumb;
    @SerializedName("thumbs")
    private int thumbs;
    @SerializedName("thumbUsers")
    private ArrayList<SimpleUserV2Entity> thumbUsers;

    public class Detail<T> {
        @SerializedName("type")
        private String type;
        @SerializedName("data")
        private JsonObject data;

        private T trueData;

        public T getTrueData() {
            return trueData;
        }

        public void setTrueData(T trueData) {
            this.trueData = trueData;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public JsonObject getData() {
            return data;
        }

        public void setData(JsonObject data) {
            this.data = data;
        }
    }

    public class ShareInfo {
        @SerializedName("icon")
        private String icon;
        @SerializedName("title")
        private String title;
        @SerializedName("desc")
        private String desc;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public class DocMusic {
        private Image cover;

        private String name;

        private int timestamp;

        private String url;

        public Image getCover() {
            return cover;
        }

        public void setCover(Image cover) {
            this.cover = cover;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public class DocLink {
        private Image icon;

        private String name;

        private String url;

        public Image getIcon() {
            return icon;
        }

        public void setIcon(Image icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public class DocGroupLink {

        private ArrayList<DocGroupLinkDetail> items;

        public ArrayList<DocGroupLinkDetail> getItems() {
            return items;
        }

        public void setItems(ArrayList<DocGroupLinkDetail> items) {
            this.items = items;
        }

        public class DocGroupLinkDetail {
            private String name;
            private String url;
            private String color;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }
        }
    }

    public ArrayList<UserFollowTagEntity> getTexts() {
        return texts;
    }

    public void setTexts(ArrayList<UserFollowTagEntity> texts) {
        this.texts = texts;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public int getRtNum() {
        return rtNum;
    }

    public void setRtNum(int rtNum) {
        this.rtNum = rtNum;
    }

    public Integer getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }

    public Integer getNowNum() {
        return nowNum;
    }

    public void setNowNum(Integer nowNum) {
        this.nowNum = nowNum;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public ArrayList<Detail> getCoinDetails() {
        return coinDetails;
    }

    public void setCoinDetails(ArrayList<Detail> coinDetails) {
        this.coinDetails = coinDetails;
    }

    public int getCoinPays() {
        return coinPays;
    }

    public void setCoinPays(int coinPays) {
        this.coinPays = coinPays;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public ArrayList<Detail> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<Detail> details) {
        this.details = details;
    }

    public boolean isFavoriteFlag() {
        return favoriteFlag;
    }

    public void setFavoriteFlag(boolean favoriteFlag) {
        this.favoriteFlag = favoriteFlag;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public ShareInfo getShare() {
        return share;
    }

    public void setShare(ShareInfo share) {
        this.share = share;
    }

    public ArrayList<DocTagEntity> getTags() {
        return tags;
    }

    public void setTags(ArrayList<DocTagEntity> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public int getUserIconH() {
        return userIconH;
    }

    public void setUserIconH(int userIconH) {
        this.userIconH = userIconH;
    }

    public int getUserIconW() {
        return userIconW;
    }

    public void setUserIconW(int userIconW) {
        this.userIconW = userIconW;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public String getUserLevelColor() {
        return userLevelColor;
    }

    public void setUserLevelColor(String userLevelColor) {
        this.userLevelColor = userLevelColor;
    }

    public String getUserLevelName() {
        return userLevelName;
    }

    public void setUserLevelName(String userLevelName) {
        this.userLevelName = userLevelName;
    }

    public int getUserScore() {
        return userScore;
    }

    public void setUserScore(int userScore) {
        this.userScore = userScore;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BagDirEntity getFolderInfo() {
        return folderInfo;
    }

    public void setFolderInfo(BagDirEntity folderInfo) {
        this.folderInfo = folderInfo;
    }

    public ArrayList<BadgeEntity> getBadgeList() {
        return badgeList;
    }

    public void setBadgeList(ArrayList<BadgeEntity> badgeList) {
        this.badgeList = badgeList;
    }

    public boolean isCoinComment() {
        return coinComment;
    }

    public void setCoinComment(boolean coinComment) {
        this.coinComment = coinComment;
    }

    public boolean isFollowUser() {
        return followUser;
    }

    public void setFollowUser(boolean followUser) {
        this.followUser = followUser;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public boolean isManager() {
        return manager;
    }

    public void setManager(boolean manager) {
        this.manager = manager;
    }

    public boolean isExcellent() {
        return excellent;
    }

    public boolean isTop() {
        return top;
    }

    public void setExcellent(boolean excellent) {
        this.excellent = excellent;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public String getTagId() {
        return tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public boolean isJoinTag() {
        return joinTag;
    }

    public void setJoinTag(boolean joinTag) {
        this.joinTag = joinTag;
    }

    public boolean isThumb() {
        return thumb;
    }

    public void setThumb(boolean thumb) {
        this.thumb = thumb;
    }

    public void setThumbs(int thumbs) {
        this.thumbs = thumbs;
    }

    public int getThumbs() {
        return thumbs;
    }

    public void setThumbUsers(ArrayList<SimpleUserV2Entity> thumbUsers) {
        this.thumbUsers = thumbUsers;
    }

    public ArrayList<SimpleUserV2Entity> getThumbUsers() {
        return thumbUsers;
    }
}
