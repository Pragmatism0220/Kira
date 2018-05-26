package com.moemoe.lalala.model.entity;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yi on 2017/11/24.
 */

public class SimpleUserV2Entity implements Parcelable {
    private String headPath;
    private String userId;
    private String userName;

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SimpleUserV2Entity> CREATOR = new Creator<SimpleUserV2Entity>() {
        @Override
        public SimpleUserV2Entity createFromParcel(Parcel in) {
            SimpleUserV2Entity image = new SimpleUserV2Entity();
            Bundle bundle;
            bundle = in.readBundle(getClass().getClassLoader());
            image.headPath= bundle.getString("headPath");
            image.userId = bundle.getString("userId");
            image.userName = bundle.getString("userName");
            return image;
        }

        @Override
        public SimpleUserV2Entity[] newArray(int size) {
            return new SimpleUserV2Entity[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString("headPath",headPath);
        bundle.putString("userId",userId);
        bundle.putString("userName",userName);
        dest.writeBundle(bundle);
    }

    public String getHeadPath() {
        return headPath ;
    }

    public void setHeadPath(String headPath ) {
        this.headPath  = headPath ;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
