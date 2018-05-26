package com.moemoe.lalala.model.entity;

import java.util.ArrayList;

/**
 * Created by Sora on 2018/3/7.
 */

public class AllPersonnelEntity {
    //    managers (Array[单个用户信息-返回], optional),
//    members (Array[单个用户信息-返回], optional)
    private ArrayList<SimpleUserEntity> managers;
    private ArrayList<SimpleUserEntity> members;

    public ArrayList<SimpleUserEntity> getManagers() {
        return managers;
    }

    public ArrayList<SimpleUserEntity> getMembers() {
        return members;
    }

    public void setManagers(ArrayList<SimpleUserEntity> managers) {
        this.managers = managers;
    }

    public void setMembers(ArrayList<SimpleUserEntity> members) {
        this.members = members;
    }
}
