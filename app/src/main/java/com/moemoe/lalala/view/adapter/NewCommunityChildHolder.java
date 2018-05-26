package com.moemoe.lalala.view.adapter;

import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.view.widget.adapter.ClickableViewHolder;

/**
 * Created by Sora on 2018/2/28.
 */

public class NewCommunityChildHolder extends ClickableViewHolder {
    public NewCommunityChildHolder(View itemView) {
        super(itemView);
    }

    public void createItem(final String entity, final int position) {
        setText(R.id.tv_text, entity);
    }
}
