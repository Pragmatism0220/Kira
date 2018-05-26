package com.moemoe.lalala.model.entity;

/**
 *
 * Created by yi on 2016/12/29.
 */

public class CoinDetailEntity {
    private int coin;
    private String createTime;
    private String type;
    private String schema;

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}
