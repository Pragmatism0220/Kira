package com.moemoe.lalala.view.base;

/**
 * Created by Administrator on 2018/5/16.
 */

public class EveryDayBean {
    private String titleName = "莲的日常";
    private int number = 2421;
    private int titleNnm;

    public EveryDayBean(String titleName, int number, int titleNnm) {
        this.titleName = titleName;
        this.number = number;
        this.titleNnm = titleNnm;
    }

    public EveryDayBean() {

    }

    public int getTitleNnm() {
        return titleNnm;
    }

    public void setTitleNnm(int titleNnm) {
        this.titleNnm = titleNnm;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
