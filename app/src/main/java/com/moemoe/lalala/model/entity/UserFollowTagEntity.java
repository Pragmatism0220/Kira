package com.moemoe.lalala.model.entity;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户关注标签
 * Created by yi on 2018/1/11.
 */

public class UserFollowTagEntity implements Parcelable {

    private String text;
    private String id;
    private int num;
    private boolean manager;
    private String url;
    private boolean select;
    private String type;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public boolean isManager() {
        return manager;
    }

    public String getUrl() {
        return url;
    }

    public void setManager(boolean manager) {
        this.manager = manager;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UserFollowTagEntity() {
    }

    public UserFollowTagEntity(String text, String id) {
        this.text = text;
        this.id = id;
    }

    public boolean getSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserFollowTagEntity> CREATOR = new Creator<UserFollowTagEntity>() {
        @Override
        public UserFollowTagEntity createFromParcel(Parcel parcel) {
            UserFollowTagEntity info = new UserFollowTagEntity();
            Bundle bundle = parcel.readBundle(getClass().getClassLoader());
            info.text = bundle.getString("text");
            info.id = bundle.getString("id");
            info.num = bundle.getInt("num");
            return info;
        }

        @Override
        public UserFollowTagEntity[] newArray(int i) {
            return new UserFollowTagEntity[0];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        bundle.putString("text", text);
        bundle.putString("id", id);
        bundle.putInt("num", num);
        parcel.writeBundle(bundle);
    }

    @Override
    public boolean equals(Object obj) {
        UserFollowTagEntity o = (UserFollowTagEntity) obj;
        return this.id.equals(o.getId()) && this.text.equals(o.getText());
    }
}
