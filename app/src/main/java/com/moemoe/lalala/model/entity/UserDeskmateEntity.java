package com.moemoe.lalala.model.entity;

import java.util.ArrayList;

/**
 * Created by Hygge on 2018/5/23.
 */
public class UserDeskmateEntity {
    
    private String id;
    private String name;
  
    private ArrayList<DeskmateImageEntity> pics;
    private DeskmateStageLineEntity stageLine;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<DeskmateImageEntity> getPics() {
        return pics;
    }

    public DeskmateStageLineEntity getStageLine() {
        return stageLine;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPics(ArrayList<DeskmateImageEntity> pics) {
        this.pics = pics;
    }

    public void setStageLine(DeskmateStageLineEntity stageLine) {
        this.stageLine = stageLine;
    }
}
