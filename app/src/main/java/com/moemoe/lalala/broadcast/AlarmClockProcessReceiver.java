package com.moemoe.lalala.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.moemoe.lalala.event.AlarmEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * 单次闹钟响起，通过此BroadcastReceiver来实现多进程通信，更新闹钟开关
 * Created by yi on 2017/9/12.
 */

public class AlarmClockProcessReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        EventBus.getDefault().post(new AlarmEvent(null,3));
    }
}
