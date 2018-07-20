package com.moemoe.lalala.view.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.BranchStoryAllEntity;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.view.activity.CompoundActivity;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Hygge on 2018/6/12.
 */

public class BranchHolder extends ClubChildHolder {
    public BranchHolder(View itemView) {
        super(itemView);
    }

    public void createItem(final BranchStoryAllEntity entity, int position, boolean isSelect) {
        int w = (int) context.getResources().getDimension(R.dimen.x210);
        int h = (int) context.getResources().getDimension(R.dimen.y280);
        Glide.with(context)
                .load(ApiService.URL_QINIU + entity.getCoverImage())
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .into((ImageView) $(R.id.item_branch_bg));
        ((TextView) $(R.id.item_branch_title)).setText(entity.getName());
        ((TextView) $(R.id.item_branch_num)).setText("已拥有:" + entity.getUserBranchLevelCount());
        if (entity.isShowJoinButton()) {
            $(R.id.iv_plot_memory).setVisibility(View.GONE);
            ((ImageView) $(R.id.item_branch_bg)).setAlpha(0.5f);
            ((TextView) $(R.id.item_branch_title)).setAlpha(0.5f);
            ((TextView) $(R.id.item_branch_num)).setAlpha(0.5f);
        } else {
            if (entity.getUserBranchLevelCount() == 0) {
                ((ImageView) $(R.id.item_branch_bg)).setAlpha(0.5f);
                ((TextView) $(R.id.item_branch_title)).setAlpha(0.5f);
                ((TextView) $(R.id.item_branch_num)).setAlpha(0.5f);
            } else {
                ((ImageView) $(R.id.item_branch_bg)).setAlpha(1.0f);
                ((TextView) $(R.id.item_branch_title)).setAlpha(1.0f);
                ((TextView) $(R.id.item_branch_num)).setAlpha(1.0f);
            }
            $(R.id.iv_plot_memory).setVisibility(View.GONE);
        }
        if (isSelect) {
            ((TextView) $(R.id.tv_branch_num_new)).setVisibility(View.GONE);
            ((TextView) $(R.id.tv_branch_num_new)).setText(entity.getUserBranchLevelCount() + "");
        } else {
            ((TextView) $(R.id.tv_branch_num_new)).setVisibility(View.GONE);
        }

        if (entity.isBranchShowNews() == true) {
            ((ImageView) $(R.id.branch_news)).setVisibility(View.VISIBLE);
        } else {
            ((ImageView) $(R.id.branch_news)).setVisibility(View.GONE);
        }

        $(R.id.iv_plot_memory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CompoundActivity.class);
                intent.putExtra("holeCount", entity.getHoleCount());
                intent.putExtra("holeLevel", entity.getHoleLevel());
                intent.putExtra("branchId", entity.getId());
                context.startActivity(intent);
            }
        });
    }
}
