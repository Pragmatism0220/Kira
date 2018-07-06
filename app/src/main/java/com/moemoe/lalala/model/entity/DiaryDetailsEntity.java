package com.moemoe.lalala.model.entity;

import java.util.Date;

/**
 * Created by Administrator on 2018/7/5.
 */

public class DiaryDetailsEntity {
    private String createTime;//创建时间
    private String eventContent;//事件内容
    private String id;//羁绊日记明细ID
    private String weather;//天气

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEventContent() {
        return eventContent;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
