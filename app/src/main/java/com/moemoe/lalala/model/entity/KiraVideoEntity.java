package com.moemoe.lalala.model.entity;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 *
 * Created by yi on 2018/2/2.
 */

public class KiraVideoEntity  implements Parcelable {
//    attr (object): 文件属性(json,图片为:{w:10,h:10},音乐为:{length:200}) ,
//    barrageNum (integer): 弹幕数 ,
//    coin (number): 节操 ,
//    cover (string): 封面 ,
//    fav (boolean, optional): 是否收藏 ,
//    fileName (string): 文件名称 ,
//    id (string): 文件ID ,
//    itemList (Array[书包视频/音乐信息-返回], optional): 此文件夹下的文件 ,
//    path (string): 路径 ,
//    playNum (integer): 播放数 ,
//    state (integer): 状态[0:待审核，1：审核通过，-1审核失败] ,
//    summary (string): 简介 ,
//    texts (Array[关联标签Model-返回]): 标签 ,
//    timestamp (string): 时长 ,
//    type (string): 文件类型[视频：movie，音乐:music] ,
//    updateTime (string): 修改时间
    private JsonObject attr;
    private int barrageNum;
    private int coin;
    private String cover;
    private boolean fav;
    private String fileName;
    private String id;
    private ArrayList<StreamFileEntity> itemList;
    private String path;
    private int playNum;
    private int state;
    private String summary;
    private ArrayList<UserFollowTagEntity> texts;
    private String timestamp;
    private String type;
    private String updateTime;
    private UserTopEntity user;
    private int commentNum;
    private boolean buy; //(boolean, optional): 是否购买 ,
    private String buyType; //(string, optional): 购买方式(FOLDER | FILE) ,

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<KiraVideoEntity> CREATOR = new Creator<KiraVideoEntity>() {

        @Override
        public KiraVideoEntity createFromParcel(Parcel in) {
            KiraVideoEntity entity = new KiraVideoEntity();
            Bundle bundle;
            bundle = in.readBundle(getClass().getClassLoader());
            entity.attr = new Gson().fromJson(bundle.getString("attr"),JsonObject.class);
            entity.barrageNum = bundle.getInt("barrageNum");
            entity.coin = bundle.getInt("coin");
            entity.cover = bundle.getString("cover");
            entity.fav = bundle.getBoolean("fav");
            entity.fileName = bundle.getString("fileName");
            entity.id = bundle.getString("id");
            entity.itemList = bundle.getParcelableArrayList("itemList");
            entity.path = bundle.getString("path");
            entity.playNum = bundle.getInt("playNum");
            entity.state = bundle.getInt("state");
            entity.summary = bundle.getString("summary");
            entity.texts = bundle.getParcelableArrayList("texts");
            entity.timestamp = bundle.getString("timestamp");
            entity.type = bundle.getString("type");
            entity.updateTime = bundle.getString("updateTime");
            entity.user = bundle.getParcelable("user");
            entity.commentNum = bundle.getInt("commentNum");
            entity.buy = bundle.getBoolean("buy");
            entity.buyType = bundle.getString("buyType");
            return entity;
        }

        @Override
        public KiraVideoEntity[] newArray(int size) {

            return new KiraVideoEntity[size];
        }
    };

    @Override
    public void writeToParcel(Parcel out, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString("attr", attr.toString());
        bundle.putInt("barrageNum", barrageNum);
        bundle.putInt("coin", coin);
        bundle.putString("cover", cover);
        bundle.putBoolean("fav", fav);
        bundle.putString("fileName",fileName);
        bundle.putString("id",id);
        bundle.putParcelableArrayList("itemList",itemList);
        bundle.putParcelableArrayList("texts",texts);
        bundle.putString("path",path);
        bundle.putInt("playNum",playNum);
        bundle.putInt("state",state);
        bundle.putInt("commentNum",commentNum);
        bundle.putString("summary",summary);
        bundle.putString("type",type);
        bundle.putString("updateTime",updateTime);
        bundle.putParcelable("user",user);
        bundle.putBoolean("buy",buy);
        bundle.putString("buyType",buyType);
        out.writeBundle(bundle);
    }

    public boolean isBuy() {
        return buy;
    }

    public void setBuy(boolean buy) {
        this.buy = buy;
    }

    public String getBuyType() {
        return buyType;
    }

    public void setBuyType(String buyType) {
        this.buyType = buyType;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public UserTopEntity getUser() {
        return user;
    }

    public void setUser(UserTopEntity user) {
        this.user = user;
    }

    public JsonObject getAttr() {
        return attr;
    }

    public void setAttr(JsonObject attr) {
        this.attr = attr;
    }

    public int getBarrageNum() {
        return barrageNum;
    }

    public void setBarrageNum(int barrageNum) {
        this.barrageNum = barrageNum;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<StreamFileEntity> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<StreamFileEntity> itemList) {
        this.itemList = itemList;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPlayNum() {
        return playNum;
    }

    public void setPlayNum(int playNum) {
        this.playNum = playNum;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public ArrayList<UserFollowTagEntity> getTexts() {
        return texts;
    }

    public void setTexts(ArrayList<UserFollowTagEntity> texts) {
        this.texts = texts;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
