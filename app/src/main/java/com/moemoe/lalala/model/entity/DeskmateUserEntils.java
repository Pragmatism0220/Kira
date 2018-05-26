package com.moemoe.lalala.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.util.ArrayList;

/**
 * Created by Hygge on 2018/5/25.
 */
@Entity
public class DeskmateUserEntils {
    @Id
    private String id;
    private long h;
    private String path;
    private String remark;
    private long size;
    private long w;
    private String fileName;
    private int downloadState;//1.未下载 2.下载完成 3.下载失败
    private String md5;

    public static ArrayList<DeskmateUserEntils> toDb(ArrayList<DeskmateImageEntity> entities, String type) {
        ArrayList<DeskmateUserEntils> res = new ArrayList<>();
        for (DeskmateImageEntity entity : entities) {
            DeskmateUserEntils entity1 = new DeskmateUserEntils(entity, type);
            res.add(entity1);
        }
        return res;
    }

    public DeskmateUserEntils(DeskmateImageEntity entity, String type) {
        path = entity.getPath();
        size = entity.getSize();
        remark = entity.getRemark();
        w = entity.getW();
        h = entity.getH();
        md5 = entity.getMd5();
        fileName = entity.getMd5() + path.substring(path.lastIndexOf("."));
        downloadState = 1;
        id = type;
    }

    @Generated(hash = 795643714)
    public DeskmateUserEntils(String id, long h, String path, String remark, long size, long w, String fileName,
            int downloadState, String md5) {
        this.id = id;
        this.h = h;
        this.path = path;
        this.remark = remark;
        this.size = size;
        this.w = w;
        this.fileName = fileName;
        this.downloadState = downloadState;
        this.md5 = md5;
    }

    @Generated(hash = 1804757978)
    public DeskmateUserEntils() {
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDownloadState(int downloadState) {
        this.downloadState = downloadState;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getFileName() {
        return fileName;
    }

    public int getDownloadState() {
        return downloadState;
    }

    public String getMd5() {
        return md5;
    }

    public String getId() {
        return id;
    }

    public long getH() {
        return h;
    }

    public String getPath() {
        return path;
    }

    public String getRemark() {
        return remark;
    }

    public long getSize() {
        return size;
    }

    public long getW() {
        return w;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setH(long h) {
        this.h = h;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setW(long w) {
        this.w = w;
    }
}
