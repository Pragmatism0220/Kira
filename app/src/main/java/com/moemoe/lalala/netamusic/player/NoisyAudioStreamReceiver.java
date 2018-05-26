package com.moemoe.lalala.netamusic.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 来电/耳机拔出时暂停播放
 * Created by yi on 2018/2/6.
 */

public class NoisyAudioStreamReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AudioPlayer.get().playPause();
    }
}
