package com.moemoe.lalala.view.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.BagVisitorEntity;
import com.moemoe.lalala.model.entity.UserTopEntity;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.widget.adapter.ClickableViewHolder;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Sora on 2018/3/6.
 */

public class BagVisitorHolder extends ClickableViewHolder {
    public BagVisitorHolder(View itemView) {
        super(itemView);
    }

    public void createItem(final UserTopEntity entity, int position) {

        int size = (int) itemView.getContext().getResources().getDimension(R.dimen.x72);
        Glide.with(itemView.getContext())
                .load(StringUtils.getUrl(itemView.getContext(), entity.getHeadPath(), size, size, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(itemView.getContext()))
                .into((ImageView) $(R.id.iv_add_cover));
//        $(R.id.iv_add_cover).setOnClickListener(new NoDoubleClickListener() {
//            @Override
//            public void onNoDoubleClick(View v) {
//                ViewUtils.toPersonal(itemView.getContext(), entity.getId());
//            }
//        });
    }
}
