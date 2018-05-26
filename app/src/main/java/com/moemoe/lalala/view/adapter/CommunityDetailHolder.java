package com.moemoe.lalala.view.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.SimpleUserEntity;
import com.moemoe.lalala.utils.BitmapUtils;
import com.moemoe.lalala.utils.DensityUtil;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.widget.adapter.ClickableViewHolder;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Sora on 2018/3/2.
 */

public class CommunityDetailHolder extends ClickableViewHolder {
    public CommunityDetailHolder(View itemView) {
        super(itemView);
    }

    public void createItem(final SimpleUserEntity entity, int position) {

        int size = (DensityUtil.getScreenWidth(itemView.getContext()) - (int) getResources().getDimension(R.dimen.y80)) / 6;
        Glide.with(itemView.getContext())
                .load(StringUtils.getUrl(itemView.getContext(), entity.getUserIcon() + "", size, size, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(itemView.getContext()))
                .into((ImageView) $(R.id.iv_cover));
        ((TextView) $(R.id.tv_name)).setText("" + entity.getUserName());
    }
}