package com.moemoe.lalala.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.moemoe.lalala.R;

/**
 *
 * Created by yi on 2018/2/2.
 */

public class VideoConfig {
    private static SharedPreferences sp;
    private static final String PLAYER_CONFIG_FILE_NAME = "video_config";

    private static final String KEY_I_DANMU_OPACITY = "danmu_opacity";
    private static final String KEY_I_DANMU_SIZE = "danmu_size";

    private static final int DEFAULT_DANMU_OPACITY = 80;

    public static void init(Context context) {
        sp = context.getSharedPreferences(PLAYER_CONFIG_FILE_NAME, 0);
    }

    public static void saveDanmuOpacity(int danmuOpacity) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(KEY_I_DANMU_OPACITY, danmuOpacity / 100f);
        editor.commit();
    }

    public static float loadDanmuOpacity() {
        return sp.getFloat(KEY_I_DANMU_OPACITY, DEFAULT_DANMU_OPACITY / 100f);
    }

    public static void saveDanmuSize(float danmuSize) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(KEY_I_DANMU_SIZE, danmuSize);
        editor.commit();
    }

    public static float loadDanmuSize(Context context) {
        return sp.getFloat(KEY_I_DANMU_SIZE, context.getResources().getDimension(R.dimen.x30));
    }
}
