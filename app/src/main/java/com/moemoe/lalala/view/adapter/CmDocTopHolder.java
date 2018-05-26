package com.moemoe.lalala.view.adapter;

import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.DocResponse;
import com.moemoe.lalala.view.widget.adapter.ClickableViewHolder;

/**
 * Created by Sora on 2018/3/7.
 */

public class CmDocTopHolder extends ClickableViewHolder {

    public CmDocTopHolder(View itemView) {
        super(itemView);
    }

    public void createItem(final DocResponse entity, final int position) {
        setText(R.id.tv_content, entity.getTitle());
    }
}
