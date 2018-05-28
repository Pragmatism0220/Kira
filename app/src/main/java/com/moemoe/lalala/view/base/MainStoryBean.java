package com.moemoe.lalala.view.base;

/**
 * Created by Administrator on 2018/5/24.
 */

public class MainStoryBean {

    private String coverImage;
    private String coverDescribe;
    private int percent; // 全收集：-1，未解锁：-2，剧情阅读中：63
    private String name;
    private int count;

    public MainStoryBean() {

    }

    public MainStoryBean(String coverDescribe, int percent, String name, int count) {
        this.coverDescribe = coverDescribe;
        this.percent = percent;
        this.name = name;
        this.count = count;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getCoverDescribe() {
        return coverDescribe;
    }

    public void setCoverDescribe(String coverDescribe) {
        this.coverDescribe = coverDescribe;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "MainStoryBean{" +
                "coverImage='" + coverImage + '\'' +
                ", coverDescribe='" + coverDescribe + '\'' +
                ", percent=" + percent +
                ", name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
