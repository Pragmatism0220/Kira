package com.moemoe.lalala.model.entity;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/30.
 */

public class AllFurnitureInfo implements Serializable {

    private String describe;//家具描述
    private String furnitureType;//家具类型
    private String id;
    private String image;//家具图片
    private boolean isPutInHouse;// 家具是否（使用）放入宅屋，true：已经放入宅屋 ,
    private boolean isUserFurnitureHad;//家具是否拥有，true：拥有当前家具
    private String name;//名称


//    public static final Parcelable.Creator<AllFurnitureInfo> CREATOR = new Parcelable.Creator<AllFurnitureInfo>() {
//        @Override
//        public AllFurnitureInfo createFromParcel(Parcel in) {
//            AllFurnitureInfo entity = new AllFurnitureInfo();
//            Bundle bundle;
//            bundle = in.readBundle(getClass().getClassLoader());
//            entity.describe = bundle.getString("describe");
//            entity.furnitureType = bundle.getString("furnitureType");
//            entity.id = bundle.getString("id");
//            entity.image = bundle.getString("image");
//            entity.isPutInHouse = bundle.getBoolean("isPutInHouse");
//            entity.isUserFurnitureHad = bundle.getBoolean("isUserFurnitureHad");
//            entity.name = bundle.getString("name");
//            return entity;
//        }
//
//        @Override
//        public AllFurnitureInfo[] newArray(int size) {
//            return new AllFurnitureInfo[size];
//        }
//    };
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        Bundle bundle = new Bundle();
//        bundle.putString("furnitureType", describe);
//        bundle.putString("describe", furnitureType);
//        bundle.putString("id", id);
//        bundle.putString("image", image);
//        bundle.putBoolean("isPutInHouse", isPutInHouse);
//        bundle.putBoolean("isUserFurnitureHad", isUserFurnitureHad);
//        bundle.putString("name", name);
//        dest.writeBundle(bundle);
//    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getFurnitureType() {
        return furnitureType;
    }

    public void setFurnitureType(String furnitureType) {
        this.furnitureType = furnitureType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isPutInHouse() {
        return isPutInHouse;
    }

    public void setPutInHouse(boolean putInHouse) {
        isPutInHouse = putInHouse;
    }

    public boolean isUserFurnitureHad() {
        return isUserFurnitureHad;
    }

    public void setUserFurnitureHad(boolean userFurnitureHad) {
        isUserFurnitureHad = userFurnitureHad;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AllFurnitureInfo{" +
                "describe='" + describe + '\'' +
                ", furnitureType='" + furnitureType + '\'' +
                ", id='" + id + '\'' +
                ", image='" + image + '\'' +
                ", isPutInHouse=" + isPutInHouse +
                ", isUserFurnitureHad=" + isUserFurnitureHad +
                ", name='" + name + '\'' +
                '}';
    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }


}
