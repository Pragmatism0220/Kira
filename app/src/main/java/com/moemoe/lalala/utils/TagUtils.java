package com.moemoe.lalala.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.moemoe.lalala.R;

/**
 *
 * Created by yi on 2018/1/16.
 */

public class TagUtils {

    private static int[] mBackGround = { R.drawable.shape_rect_label_cyan,
            R.drawable.shape_rect_label_yellow,
            R.drawable.shape_rect_label_orange,
            R.drawable.shape_rect_label_pink,
            R.drawable.shape_rect_border_green_y4,
            R.drawable.shape_rect_label_purple,
            R.drawable.shape_rect_label_tab_blue};

    private static int[] colors = { R.color.blue_39d8d8,
            R.color.yellow_f2cc2c,
            R.color.orange_ed853e,
            R.color.pink_fb7ba2,
            R.color.green_93d856,
            R.color.purple_cd8add,
            R.color.blue_4fc3f7
    };

    private static int[] mBorder = {
            R.drawable.shape_rect_border_main_no_background_y4,
            R.drawable.shape_rect_border_yellow_no_background_y4,
            R.drawable.shape_rect_border_orange_no_background_y4,
            R.drawable.shape_rect_border_pink_no_background_y4,
            R.drawable.shape_rect_border_green_no_background_y4,
            R.drawable.shape_rect_border_purple_no_background_y4,
            R.drawable.shape_rect_border_tab_blue_no_background_y4,
    };

    private TagUtils(){

    }

    public static void setBackGround(String content, View view){
        int index = StringUtils.getHashOfString(content, mBackGround.length);
        view.setBackgroundResource(mBackGround[index]);
        if(view instanceof TextView){
            ((TextView) view).setText(content);
        }
    }

    public static void setTextColor(Context context,String content, View view){
        if(view instanceof TextView){
            int index = StringUtils.getHashOfString(content, colors.length);
            ((TextView) view).setTextColor(ContextCompat.getColor(context,colors[index]));
        }
    }

    public static void setTextAndBorderColor(Context context,TextView view,String content){
        int index = StringUtils.getHashOfString(content, colors.length);
        view.setText(content);
        view.setTextColor(ContextCompat.getColor(context,colors[index]));
        view.setBackgroundResource(mBorder[index]);
    }

    public static int getColor(String content){
        int index = StringUtils.getHashOfString(content, colors.length);
        return colors[index];
    }
}
