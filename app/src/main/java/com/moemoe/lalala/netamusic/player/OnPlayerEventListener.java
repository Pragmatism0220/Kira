package com.moemoe.lalala.netamusic.player;

import com.moemoe.lalala.netamusic.data.model.Music;

/**
 * 播放进度监听器
 * Created by yi on 2018/2/6.
 */

public interface OnPlayerEventListener {

    /**
     * 切换歌曲
     */
    void onChange(Music music);

    /**
     * 继续播放
     */
    void onPlayerStart();

    /**
     * 暂停播放
     */
    void onPlayerPause();

    /**
     * 更新进度
     */
    void onPublish(int progress);

    /**
     * 缓冲百分比
     */
    void onBufferingUpdate(int percent);
}
