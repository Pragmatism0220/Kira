package com.moemoe.lalala.event;

import android.view.View;

/**
 * Created by Administrator on 2018/7/17.
 */

public class CommentEvent {

    private View view;
    private int paposition;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CommentEvent(View view, int paposition, String id) {
        this.view = view;
        this.paposition = paposition;
        this.id = id;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getPaposition() {
        return paposition;
    }

    public void setPaposition(int paposition) {
        this.paposition = paposition;
    }
}
