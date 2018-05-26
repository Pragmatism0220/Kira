package com.moemoe.lalala.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.OfficialTag;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.model.entity.UserFollowTagEntity;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.view.widget.adapter.ClickableViewHolder;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Sora on 2018/3/7.
 */

public class DocSelectHolder extends ClickableViewHolder {
    public DocSelectHolder(View itemView) {
        super(itemView);
    }

    public void createItem(final UserFollowTagEntity entity, final int position, boolean isSelect) {

        ((TextView) $(R.id.tv_content)).setText(entity.getText());

        int size = getResources().getDimensionPixelSize(R.dimen.y128);
        Glide.with(itemView.getContext())
                .load(StringUtils.getUrl(itemView.getContext(), entity.getUrl(), size, size, false, true))
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .bitmapTransform(new RoundedCornersTransformation(itemView.getContext(), getResources().getDimensionPixelSize(R.dimen.y16), 0))
                .into((ImageView) $(R.id.iv_cover));
        boolean select = entity.getSelect();
        if (select) {
            ((ImageView) $(R.id.iv_select)).setVisibility(View.VISIBLE);
        } else {
            ((ImageView) $(R.id.iv_select)).setVisibility(View.GONE);
        }
    }
}
