package com.moemoe.lalala.view.adapter;

import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.StoryListEntity;
import com.moemoe.lalala.view.widget.adapter.ClickableViewHolder;

/**
 * Created by Sora on 2018/4/8.
 */

public class StoryListHolder extends ClickableViewHolder {
    public StoryListHolder(View itemView) {
        super(itemView);
    }

    public void creatItem(StoryListEntity entity, int position) {
        setText(R.id.tv_name, entity.getStoryName());
        $(R.id.tv_review).setSelected(entity.isFlag());
    }
}
