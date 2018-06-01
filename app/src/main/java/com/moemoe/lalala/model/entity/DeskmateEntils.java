package com.moemoe.lalala.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.ArrayList;

/**
 * Created by Hygge on 2018/5/25.
 */
@Entity
public class DeskmateEntils {


    @Id
    private String id;
    private int h;
    private String path;
    private String remark;
    private int size;
    private int w;
    private String fileName;
    private int downloadState;//1.未下载 2.下载完成 3.下载失败
    private String md5;
    private String type;
    
    public static ArrayList<DeskmateEntils> toDb(ArrayList<DeskmateImageEntity> entities, String type) {
        ArrayList<DeskmateEntils> res = new ArrayList<>();
        for (DeskmateImageEntity entity : entities) {
            DeskmateEntils entity1 = new DeskmateEntils(entity, type);
            res.add(entity1);
        }
        return res;
    }

    public DeskmateEntils(DeskmateImageEntity entity, String type) {
        path = entity.getPath();
        size = entity.getSize();
        remark = entity.getRemark();
        w = entity.getW();
        h = entity.getH();
        md5 = entity.getMd5();
        fileName = entity.getMd5() + path.substring(path.lastIndexOf("."));
        downloadState = 1;
        String[] split = entity.getPath().split("/");
        id = split[2] + "";
        this.type = type;
    }

    @Generated(hash = 1784232591)
    public DeskmateEntils(String id, int h, String path, String remark, int size, int w,
            String fileName, int downloadState, String md5, String type) {
        this.id = id;
        this.h = h;
        this.path = path;
        this.remark = remark;
        this.size = size;
        this.w = w;
        this.fileName = fileName;
        this.downloadState = downloadState;
        this.md5 = md5;
        this.type = type;
    }

    @Generated(hash = 1644829858)
    public DeskmateEntils() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getH() {
        return h;
    }

    public String getPath() {
        return path;
    }

    public String getRemark() {
        return remark;
    }

    public int getSize() {
        return size;
    }

    public int getW() {
        return w;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setW(int w) {
        this.w = w;
    }
}
