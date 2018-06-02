package com.moemoe.lalala.event;

/**
 * Created by Hygge on 2018/6/2.
 */

public class StageLineEvent {
    private String stageLine;
    private String type;

    public StageLineEvent(String stageLine) {
        this.stageLine = stageLine;
    }

    public String getStageLine() {
        return stageLine;
    }

    public void setStageLine(String stageLine) {
        this.stageLine = stageLine;
    }
}
