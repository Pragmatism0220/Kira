package com.moemoe.lalala.event;

/**
 * Created by Administrator on 2018/6/11.
 * 主线剧情脚本信息
 */

public class NewStoryJsonInfo {
    private String id;
    private String json;
    private String name;
    private String xml;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    @Override
    public String toString() {
        return "NewStoryJsonInfo{" +
                "id='" + id + '\'' +
                ", json='" + json + '\'' +
                ", name='" + name + '\'' +
                ", xml='" + xml + '\'' +
                '}';
    }
}
