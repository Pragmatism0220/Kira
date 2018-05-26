package com.moemoe.lalala.event;

/**
 *
 * Created by yi on 2018/2/7.
 */

public class FavMusicEvent {
    private boolean isFav;
    private String id;

    public FavMusicEvent(boolean isFav,String id) {
        this.isFav = isFav;
        this.id = id;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
