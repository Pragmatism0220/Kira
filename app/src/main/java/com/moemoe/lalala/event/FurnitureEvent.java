package com.moemoe.lalala.event;

/**
 * Created by Hygge on 2018/6/2.
 */

public class FurnitureEvent {
    
    private int position;
    private String type;
    public FurnitureEvent(int position,String type) {
        this.position=position;
        this.type=type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
