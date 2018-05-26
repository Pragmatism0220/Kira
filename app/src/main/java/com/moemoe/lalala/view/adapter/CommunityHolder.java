package com.moemoe.lalala.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.UserFollowTagEntity;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.view.widget.adapter.ClickableViewHolder;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Sora on 2018/3/1.
 */

public class CommunityHolder extends ClickableViewHolder {
    public CommunityHolder(View itemView) {
        super(itemView);
    }

    public void createItem(UserFollowTagEntity entity, int position) {
        setText(R.id.tv_name, entity.getText());
        boolean manager = entity.isManager();
        if (manager) {
            setVisible(R.id.iv_manager, true);
        } else {
            setVisible(R.id.iv_manager, false);
        }

        int size = (int) itemView.getContext().getResources().getDimension(R.dimen.x218);
        ImageView imageView = $(R.id.iv_avatar);
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = size;
        params.height = size;
        imageView.setLayoutParams(params);
        Glide.with(context)
                .load(StringUtils.getUrl(context, "" + entity.getUrl(), size, size, false, true))
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .bitmapTransform(new CropTransformation(context, size, size)
                        , new RoundedCornersTransformation(context, getResources().getDimensionPixelSize(R.dimen.y8), 0))
                .into((ImageView) $(R.id.iv_avatar));
    }
}
