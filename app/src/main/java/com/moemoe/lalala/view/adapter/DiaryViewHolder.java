package com.moemoe.lalala.view.adapter;

import android.view.View;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.DiaryDetailsEntity;
import com.moemoe.lalala.model.entity.DiaryEntity;
import com.moemoe.lalala.view.widget.adapter.ClickableViewHolder;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2018/6/13.
 */

public class DiaryViewHolder extends ClickableViewHolder {

    private TextView mTime;
    private TextView mInfo;
    private TextView mWeather;

    public DiaryViewHolder(View itemView) {
        super(itemView);
        mTime = $(R.id.item_diary_time);
        mInfo = $(R.id.item_diary_text);
        mWeather = $(R.id.item_diary_weather);
    }

    public void createItem(final DiaryDetailsEntity entity) {
        if (entity != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            mTime.setText(sdf.format(new Date(Long.valueOf(entity.getCreateTime()))));
            mInfo.setText(entity.getEventContent());
            mWeather.setText(entity.getWeather());
        }


    }
}

