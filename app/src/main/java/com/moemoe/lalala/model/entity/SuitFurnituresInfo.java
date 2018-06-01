package com.moemoe.lalala.model.entity;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/30.
 */

public class SuitFurnituresInfo implements Serializable {

    private boolean isSuitPutInHouse;//套装是否（使用）放入宅屋，true：已经放入宅屋 ,
    private boolean isUserSuitFurnitureHad;// 套装是否拥有，true：拥有当前家具 ,
    private String suitTypeDescribe;//套装风格描述
    private String suitTypeImage;//套装风格封面图片
    private String suitTypeName;//套装风格名称


//    public static final Creator<SuitFurnituresInfo> CREATOR = new Creator<SuitFurnituresInfo>() {
//        @Override
//        public SuitFurnituresInfo createFromParcel(Parcel in) {
//            SuitFurnituresInfo entity = new SuitFurnituresInfo();
//            Bundle bundle;
//            bundle = in.readBundle(getClass().getClassLoader());
//            entity.isSuitPutInHouse = bundle.getBoolean("isSuitPutInHouse");
//            entity.isUserSuitFurnitureHad = bundle.getBoolean("isUserSuitFurnitureHad");
//            entity.suitTypeDescribe = bundle.getString("suitTypeDescribe");
//            entity.suitTypeImage = bundle.getString("suitTypeImage");
//            entity.suitTypeName = bundle.getString("suitTypeName");
//            return entity;
//        }
//
//        @Override
//        public SuitFurnituresInfo[] newArray(int size) {
//            return new SuitFurnituresInfo[size];
//        }
//    };
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        Bundle bundle = new Bundle();
//        bundle.putBoolean("isSuitPutInHouse", isSuitPutInHouse);
//        bundle.putBoolean("isUserSuitFurnitureHad", isUserSuitFurnitureHad);
//        bundle.putString("suitTypeDescribe", suitTypeDescribe);
//        bundle.putString("suitTypeImage", suitTypeImage);
//        bundle.putString("suitTypeName", suitTypeName);
//
//    }

    public boolean isSuitPutInHouse() {
        return isSuitPutInHouse;
    }

    public void setSuitPutInHouse(boolean suitPutInHouse) {
        isSuitPutInHouse = suitPutInHouse;
    }

    public boolean isUserSuitFurnitureHad() {
        return isUserSuitFurnitureHad;
    }

    public void setUserSuitFurnitureHad(boolean userSuitFurnitureHad) {
        isUserSuitFurnitureHad = userSuitFurnitureHad;
    }

    public String getSuitTypeDescribe() {
        return suitTypeDescribe;
    }

    public void setSuitTypeDescribe(String suitTypeDescribe) {
        this.suitTypeDescribe = suitTypeDescribe;
    }

    public String getSuitTypeImage() {
        return suitTypeImage;
    }

    public void setSuitTypeImage(String suitTypeImage) {
        this.suitTypeImage = suitTypeImage;
    }

    public String getSuitTypeName() {
        return suitTypeName;
    }

    public void setSuitTypeName(String suitTypeName) {
        this.suitTypeName = suitTypeName;
    }

    @Override
    public String toString() {
        return "SuitFurnituresInfo{" +
                "isSuitPutInHouse=" + isSuitPutInHouse +
                ", isUserSuitFurnitureHad=" + isUserSuitFurnitureHad +
                ", suitTypeDescribe='" + suitTypeDescribe + '\'' +
                ", suitTypeImage='" + suitTypeImage + '\'' +
                ", suitTypeName='" + suitTypeName + '\'' +
                '}';
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }


}
