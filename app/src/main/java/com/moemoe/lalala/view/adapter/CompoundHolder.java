package com.moemoe.lalala.view.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.CompoundEntity;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.view.activity.CompoundActivity;

/**
 * Created by Hygge on 2018/6/12.
 */

public class CompoundHolder extends ClubChildHolder {
    public CompoundHolder(View itemView) {
        super(itemView);
    }

    public void createItem(CompoundEntity entiy, final int position) {
        int w = (int) context.getResources().getDimension(R.dimen.x180);
        int h = (int) context.getResources().getDimension(R.dimen.y180);
        Glide.with(context)
                .load(StringUtils.getUrl(context, entiy.getCoverImage(), w, h, false, true))
                .error(R.drawable.btn_home_plot_memory_add)
                .placeholder(R.drawable.btn_home_plot_memory_add)
                .into((ImageView) $(R.id.iv_four_cover));
        if (TextUtils.isEmpty(entiy.getRoleId())) {
            $(R.id.iv_four_deletl).setVisibility(View.INVISIBLE);
        } else {
            $(R.id.iv_four_deletl).setVisibility(View.VISIBLE);
        }

        ((ImageView) $(R.id.iv_four_deletl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemView.getContext() instanceof CompoundActivity) {
                    ((CompoundActivity) itemView.getContext()).removeItem(position);
                }
            }
        });
    }
}
