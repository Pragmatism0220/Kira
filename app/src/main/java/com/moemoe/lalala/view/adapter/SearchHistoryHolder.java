package com.moemoe.lalala.view.adapter;

import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.view.widget.adapter.ClickableViewHolder;

/**
 * Created by Sora on 2018/3/12.
 */

public class SearchHistoryHolder extends ClickableViewHolder {
    public SearchHistoryHolder(View itemView) {
        super(itemView);
    }

    public void createItem(final String entity, final int position) {
        setText(R.id.tv_text, entity);
    }
}
