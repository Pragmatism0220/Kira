package com.moemoe.lalala.view.adapter;

import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.BadgeEntity;
import com.moemoe.lalala.model.entity.PhoneMenuEntity;
import com.moemoe.lalala.utils.LevelSpan;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.activity.PhoneMenuV3Activity;
import com.moemoe.lalala.view.widget.adapter.ClickableViewHolder;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by yi on 2017/7/21.
 */

public class PhoneMenuListHolder extends ClickableViewHolder {


    public PhoneMenuListHolder(View itemView) {
        super(itemView);
    }

    public void createItem(final PhoneMenuEntity entity, final String type, final int position) {

        int size = (int) itemView.getContext().getResources().getDimension(R.dimen.x80);
        Glide.with(itemView.getContext())
                .load(StringUtils.getUrl(itemView.getContext(), entity.getUserIcon(), size, size, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(itemView.getContext()))
                .into((ImageView) $(R.id.iv_avatar));


        setText(R.id.tv_name, entity.getUserName());
        if (!TextUtils.isEmpty(entity.getUserSex())) {
            $(R.id.iv_sex).setVisibility(View.VISIBLE);
            setImageResource(R.id.iv_sex, entity.getUserSex().equalsIgnoreCase("M") ? R.drawable.ic_user_girl : R.drawable.ic_user_boy);
        } else {
            $(R.id.iv_sex).setVisibility(View.GONE);
        }
        LevelSpan levelSpan = new LevelSpan(ContextCompat.getColor(itemView.getContext(), R.color.white), itemView.getContext().getResources().getDimension(R.dimen.x12));
        final String content = "LV" + entity.getLevel();
        String colorStr = "LV";
        SpannableStringBuilder style = new SpannableStringBuilder(content);
        style.setSpan(levelSpan, content.indexOf(colorStr), content.indexOf(colorStr) + colorStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(R.id.tv_level, style);

        $(R.id.fl_huizhang_1).setVisibility(View.VISIBLE);

        float radius2 = itemView.getContext().getResources().getDimension(R.dimen.y4);
        float[] outerR2 = new float[]{radius2, radius2, radius2, radius2, radius2, radius2, radius2, radius2};
        RoundRectShape roundRectShape2 = new RoundRectShape(outerR2, null, null);
        ShapeDrawable shapeDrawable2 = new ShapeDrawable();
        shapeDrawable2.setShape(roundRectShape2);
        shapeDrawable2.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable2.getPaint().setColor(StringUtils.readColorStr(entity.getLevelColor(), ContextCompat.getColor(itemView.getContext(), R.color.main_cyan)));
        $(R.id.tv_level).setBackgroundDrawable(shapeDrawable2);

        View[] huizhang = {$(R.id.fl_huizhang_1)};
        TextView[] huizhangT = {$(R.id.tv_huizhang_1)};
        BadgeEntity badge = entity.getBadge();
        if (badge != null) {
            ArrayList<BadgeEntity> badgeEntities = new ArrayList<>();
            badgeEntities.add(entity.getBadge());
            ViewUtils.badge(itemView.getContext(), huizhang, huizhangT, badgeEntities);
        } else {
            huizhang[0].setVisibility(View.GONE);
            huizhangT[0].setVisibility(View.GONE);
        }
        setText(R.id.tv_contxt, entity.getSignature());

        setVisible(R.id.tv_follow, true);
        TextView textView = (TextView) $(R.id.tv_follow);
        if (type.equals("recommend")) {
            textView.setSelected(entity.isFollow());
            if (entity.isFollow()) {
                textView.setText("已关注");
                textView.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else {
                textView.setText("关注");
                textView.setTextColor(ContextCompat.getColor(context, R.color.main_cyan));
            }
            textView.setBackgroundResource(R.drawable.btn_rect_corner_cyan_y30);
        } else if (type.equals("follow")) {
            if (entity.isFollow()) {
                textView.setSelected(entity.isEachFollow());
                if (entity.isEachFollow()) {
                    textView.setText("互相关注");
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                } else {
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    textView.setText("已关注");
                }
                textView.setBackgroundResource(R.drawable.btn_rect_corner_cyan_eddfollow_y30);
            } else {
                textView.setSelected(entity.isFollow());
                textView.setText("关注");
                textView.setTextColor(ContextCompat.getColor(context, R.color.main_cyan));
                textView.setBackgroundResource(R.drawable.btn_rect_corner_cyan_y30);
            }
        } else if (type.equals("fans")) {
            textView.setSelected(entity.isEachFollow());
            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
            if (entity.isEachFollow()) {
                textView.setText("互相关注");
                textView.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else {
                textView.setTextColor(ContextCompat.getColor(context, R.color.main_cyan));
                textView.setText("关注");
            }
            textView.setBackgroundResource(R.drawable.btn_rect_corner_cyan_follow_y30);
        }else {
            setVisible(R.id.tv_follow, false);
        }


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof PhoneMenuV3Activity) {
                    if (type.equals("recommend")) {
                        ((PhoneMenuV3Activity) context).loadFollow(entity.getUserId(), entity.isFollow(), position);
                    } else if (type.equals("fans")) {
                        ((PhoneMenuV3Activity) context).loadFollow(entity.getUserId(), entity.isEachFollow(), position);
                    } else if (type.equals("follow")) {
                        ((PhoneMenuV3Activity) context).loadFollow(entity.getUserId(), entity.isFollow(), position);
                    }
                }
            }
        });
    }
}
