package com.moemoe.lalala.model.entity;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/30.
 */

public class FurnitureInfoEntity implements Serializable {


    private Map<String, ArrayList<AllFurnitureInfo>> allFurnitures = new LinkedHashMap<>();//全部家具集合，key：家具类型名称（如桌子、电脑、电视机），value：家具明细集合信息 ,
    private ArrayList<AllFurnitureInfo> suitFurnitures;//套装家具集合


    public Map<String, ArrayList<AllFurnitureInfo>> getAllFurnitures() {
        return allFurnitures;
    }

    public void setAllFurnitures(Map<String, ArrayList<AllFurnitureInfo>> allFurnitures) {
        this.allFurnitures = allFurnitures;
    }

    public ArrayList<AllFurnitureInfo> getSuitFurnitures() {
        return suitFurnitures;
    }

    public void setSuitFurnitures(ArrayList<AllFurnitureInfo> suitFurnitures) {
        this.suitFurnitures = suitFurnitures;
    }
}
