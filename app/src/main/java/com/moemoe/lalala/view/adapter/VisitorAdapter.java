package com.moemoe.lalala.view.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.VisitorsEntity;
import com.moemoe.lalala.utils.LevelSpan;
import com.moemoe.lalala.utils.StringUtils;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Administrator on 2018/5/31.
 */

public class VisitorAdapter extends RecyclerView.Adapter<VisitorAdapter.VisitorViewHolder> {

    private List<VisitorsEntity> list;
    private Context mContext;

    public VisitorAdapter(List<VisitorsEntity> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public VisitorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        VisitorViewHolder holder = new VisitorViewHolder(View.inflate(parent.getContext(), R.layout.visitors_item, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(VisitorViewHolder holder, int position) {
        int size = (int) mContext.getResources().getDimension(R.dimen.x80);
        Glide.with(mContext).load(StringUtils.getUrl(mContext, list.get(position).getVisitorImage(), size, size, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(holder.mImage);
        holder.mName.setText(list.get(position).getRoleName());
        if ("F".equals(list.get(position).getVisitorSex())) {
            //女性
            holder.mSex.setImageResource(R.drawable.ic_user_girl);
        } else if ("".equals(list.get(position).getVisitorSex())) {
            //男性
            holder.mSex.setImageResource(R.drawable.ic_user_boy);
        }

        if (list.get(position).getVisitorType() == 1) {
            holder.mInfo.setText("拜访宅屋");
        } else if (list.get(position).getVisitorType() == 2) {
            holder.mInfo.setText("戳破了" + list.get(position).getRoleName() + "的气球");
        } else if (list.get(position).getVisitorType() == 3) {
            holder.mInfo.setText("收拾了你房间的垃圾");
        }
        holder.mTime.setText(StringUtils.timeFormat(list.get(position).getCreateTime()));

        LevelSpan levelSpan = new LevelSpan(ContextCompat.getColor(mContext, R.color.white), mContext.getResources().getDimension(R.dimen.x12));
        final String content = "LV" + list.get(position).getVisitorLevel();
        String colorStr = "LV";
        SpannableStringBuilder style = new SpannableStringBuilder(content);
        style.setSpan(levelSpan, content.indexOf(colorStr), content.indexOf(colorStr) + colorStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.mLevel.setText(style);

        float radius2 = mContext.getResources().getDimension(R.dimen.y4);
        float[] outerR2 = new float[]{radius2, radius2, radius2, radius2, radius2, radius2, radius2, radius2};
        RoundRectShape roundRectShape2 = new RoundRectShape(outerR2, null, null);
        ShapeDrawable shapeDrawable2 = new ShapeDrawable();
        shapeDrawable2.setShape(roundRectShape2);
        shapeDrawable2.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable2.getPaint().setColor(StringUtils.readColorStr(list.get(position).getVisitorLevelColor(), ContextCompat.getColor(mContext, R.color.main_cyan)));
        holder.mLevel.setBackground(shapeDrawable2);

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class VisitorViewHolder extends RecyclerView.ViewHolder {

        private TextView mTime;
        private ImageView mImage;
        private TextView mName;
        private ImageView mSex;
        private TextView mLevel;
        private TextView mInfo;

        public VisitorViewHolder(View itemView) {
            super(itemView);
            mTime = itemView.findViewById(R.id.visitor_time);
            mImage = itemView.findViewById(R.id.visitor_image);
            mName = itemView.findViewById(R.id.visitor_name);
            mSex = itemView.findViewById(R.id.visitor_sex);
            mLevel = itemView.findViewById(R.id.visitor_level);
            mInfo = itemView.findViewById(R.id.visitor_info);

        }
    }
}
