package com.moemoe.lalala.view.base;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/5/21.
 */

public class PropInfo {

    private String photo;
    private String name = "次元币";
    private String num = 24 + "";

    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }




    public PropInfo() {

    }

    public PropInfo(String photo, String name, String num) {
        this.photo = photo;
        this.name = name;
        this.num = num;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
