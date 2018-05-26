package com.moemoe.lalala.view.base;

/**
 * Created by Administrator on 2018/5/15.
 */

public class TestPrincipalInfo {
    private String titleName = "第一章 夏日将至";

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    private int num = 223;

    public TestPrincipalInfo(String titleName, int num) {
        this.titleName = titleName;
        this.num = num;
    }

    public TestPrincipalInfo() {
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    @Override
    public String toString() {
        return "TestPrincipalInfo{" +
                "titleName='" + titleName + '\'' +
                '}';
    }
}
