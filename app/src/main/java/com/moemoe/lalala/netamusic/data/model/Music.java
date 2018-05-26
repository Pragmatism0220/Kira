package com.moemoe.lalala.netamusic.data.model;

import android.text.TextUtils;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 单曲信息
 * Created by yi on 2018/2/6.
 */
@Entity(nameInDb = "SystemMessage")
public class Music implements Serializable {
    private static final long serialVersionUID = 536871008;

    @Id(autoincrement = true)
    @Property(nameInDb = "id")
    private Long id;

    @NotNull
    @Property(nameInDb = "type")
    private int type; // 歌曲类型:本地/网络
    @Property(nameInDb = "songId")
    private long songId; // [本地]歌曲ID
    @Property(nameInDb = "title")
    private String title; // 音乐标题
    @Property(nameInDb = "artist")
    private String artist; // 艺术家
    @Property(nameInDb = "album")
    private String album; // 专辑
    @Property(nameInDb = "albumId")
    private long albumId; // [本地]专辑ID
    @Property(nameInDb = "coverPath")
    private String coverPath; // [在线]专辑封面路径
    @NotNull
    @Property(nameInDb = "duration")
    private long duration; // 持续时间
    @NotNull
    @Property(nameInDb = "path")
    private String path; // 播放地址
    @Property(nameInDb = "fileName")
    private String fileName; // [本地]文件名
    @Property(nameInDb = "fileSize")
    private long fileSize; // [本地]文件大小
    @Property(nameInDb = "attr")
    private String attr; //额外信息
    @Property(nameInDb = "fileId")
    private String fileId; //文件Id
    @Property(nameInDb = "isFav")
    private boolean isFav;
    @Property(nameInDb = "userId")
    private String userId;
    @Property(nameInDb = "updateTime")
    private String updateTime;
    @Property(nameInDb = "userSex")
    private String userSex;
    @Property(nameInDb = "tag1")
    private String tag1;
    @Property(nameInDb = "tag2")
    private String tag2;
    @Property(nameInDb = "listType")
    private String listType;// normal live2d

    @Generated(hash = 1263212761)
    public Music() {
    }

    @Generated(hash = 69550428)
    public Music(Long id, int type, long songId, String title, String artist,
            String album, long albumId, String coverPath, long duration,
            @NotNull String path, String fileName, long fileSize, String attr,
            String fileId, boolean isFav, String userId, String updateTime,
            String userSex, String tag1, String tag2, String listType) {
        this.id = id;
        this.type = type;
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.albumId = albumId;
        this.coverPath = coverPath;
        this.duration = duration;
        this.path = path;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.attr = attr;
        this.fileId = fileId;
        this.isFav = isFav;
        this.userId = userId;
        this.updateTime = updateTime;
        this.userSex = userSex;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.listType = listType;
    }

    public interface Type {
        int LOCAL = 0;
        int ONLINE = 1;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Music)) {
            return false;
        }
        Music music = (Music) o;
        if(music.getFileId().equals(fileId)){
            return true;
        }
        return false;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public boolean getIsFav() {
        return this.isFav;
    }

    public void setIsFav(boolean isFav) {
        this.isFav = isFav;
    }
}
