package com.moemoe.lalala.view.adapter;

import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.event.TagSelectEvent;
import com.moemoe.lalala.model.entity.UserFollowTagEntity;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.view.widget.adapter.ClickableViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by yi on 2017/7/21.
 */

public class SearchTagHolder extends ClickableViewHolder {

    public SearchTagHolder(View itemView) {
        super(itemView);
    }

    public void createItem(final UserFollowTagEntity entity) {
        setText(R.id.tv_content, entity.getText());
        $(R.id.tv_content).setSelected(entity.getSelect());
    }
}
