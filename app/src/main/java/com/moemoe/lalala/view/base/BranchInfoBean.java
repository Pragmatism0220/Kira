package com.moemoe.lalala.view.base;

import android.widget.ImageView;

/**
 * Created by Administrator on 2018/5/16.
 */

public class BranchInfoBean {

    private ImageView mPhoto;

    public BranchInfoBean(ImageView mPhoto) {
        this.mPhoto = mPhoto;
    }

    public BranchInfoBean() {
    }

    public ImageView getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(ImageView mPhoto) {
        this.mPhoto = mPhoto;
    }
}
