package com.moemoe.lalala.event;

/**
 *
 * Created by yi on 2018/2/3.
 */

public class FavoriteCommentEvent {
    private boolean isFav;
    private String commentId;
    private int position;

    public FavoriteCommentEvent(boolean isFav, String commentId, int position) {
        this.isFav = isFav;
        this.commentId = commentId;
        this.position = position;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
