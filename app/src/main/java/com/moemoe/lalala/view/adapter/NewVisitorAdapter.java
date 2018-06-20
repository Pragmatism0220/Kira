package com.moemoe.lalala.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.databinding.RootItemVisitorsBinding;
import com.moemoe.lalala.databinding.VisitorsItemBinding;
import com.moemoe.lalala.model.entity.VisitorsEntity;
import com.moemoe.lalala.utils.LevelSpan;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.activity.HouseActivity;
import com.moemoe.lalala.view.activity.HouseHisActivity;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by zhangyan on 2018/6/4.
 */

public class NewVisitorAdapter extends BaseRecyclerViewAdapter<VisitorsEntity, NewVisitorHolder> {

    static final int TYPE_TITLE = 0x01;
    static final int TYPE_CONTENT = 0x02;
    private Context mContext;

    public NewVisitorAdapter() {
        super(null);
    }


    @Override
    public NewVisitorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        this.mContext = parent.getContext();
        VisitorsItemBinding visitorsItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.visitors_item, parent, false);
        if (viewType == TYPE_TITLE) {
            LinearLayout linearLayout = new LinearLayout(parent.getContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.setLayoutParams(params);

            RootItemVisitorsBinding rootItemVisitorsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.root_item_visitors, parent, false);
            linearLayout.addView(rootItemVisitorsBinding.getRoot());
            linearLayout.addView(visitorsItemBinding.getRoot());
            view = linearLayout;
        } else if (viewType == TYPE_CONTENT) {
            view = visitorsItemBinding.getRoot();
        }
        return new NewVisitorHolder(view, viewType);
    }


    @Override
    protected void convert(NewVisitorHolder helper, final VisitorsEntity item, int position) {
        Log.i("asd", "item: " + item);
        if (helper == null)
            return;
        if (helper.getItemViewType() == TYPE_TITLE) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            helper.mTitleTime.setText(sdf.format(new Date(Long.valueOf(item.getCreateTime()))));
        }
        helper.mName.setText(item.getRoleName());

        int size = (int) mContext.getResources().getDimension(R.dimen.x80);
        Glide.with(mContext).load(StringUtils.getUrl(mContext, item.getVisitorImage(), size, size, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(helper.mImage);

        if ("F".equals(item.getVisitorSex())) {
            //女性
            helper.mSex.setImageResource(R.drawable.ic_user_girl);
        } else if ("M".equals(item.getVisitorSex())) {
            //男性
            helper.mSex.setImageResource(R.drawable.ic_user_boy);
        }
        if (item.getVisitorType() == 1) {
            helper.mInfo.setText("拜访宅屋");
        } else if (item.getVisitorType() == 2) {
            helper.mInfo.setText("戳破了" + item.getRoleName() + "的气球");
        } else if (item.getVisitorType() == 3) {
            helper.mInfo.setText("收拾了你房间的垃圾");
        }

        helper.mTime.setText(StringUtils.timeFormat(Long.valueOf(item.getCreateTime())));

        LevelSpan levelSpan = new LevelSpan(ContextCompat.getColor(mContext, R.color.white), mContext.getResources().getDimension(R.dimen.x12));
        final String content = "LV" + item.getVisitorLevel();
        String colorStr = "LV";
        SpannableStringBuilder style = new SpannableStringBuilder(content);
        style.setSpan(levelSpan, content.indexOf(colorStr), content.indexOf(colorStr) + colorStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        helper.mLevel.setText(style);

        float radius2 = mContext.getResources().getDimension(R.dimen.y4);
        float[] outerR2 = new float[]{radius2, radius2, radius2, radius2, radius2, radius2, radius2, radius2};
        RoundRectShape roundRectShape2 = new RoundRectShape(outerR2, null, null);
        ShapeDrawable shapeDrawable2 = new ShapeDrawable();
        shapeDrawable2.setShape(roundRectShape2);
        shapeDrawable2.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable2.getPaint().setColor(StringUtils.readColorStr(item.getVisitorLevelColor(), ContextCompat.getColor(mContext, R.color.main_cyan)));
        helper.mLevel.setBackground(shapeDrawable2);

        helper.mImage.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (!TextUtils.isEmpty(item.getVisitorId())) {
                    ViewUtils.toPersonal(mContext, item.getVisitorId());
                }

        }
        });

        helper.mRoot.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (!TextUtils.isEmpty(item.getVisitorId())) {
                    Intent intent = new Intent(mContext, HouseHisActivity.class);
                    intent.putExtra("id", item.getVisitorId());
                    mContext.startActivity(intent);
                }
            }
        });


    }

    @Override
    public int getItemType(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_TITLE;
        VisitorsEntity currentItemBean = list.get(position);
        Log.i("asd", "getItemType: " + list);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(currentItemBean.getCreateTime()));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date currentDate = calendar.getTime();

        Calendar lastCalendar = Calendar.getInstance();
        lastCalendar.setTimeInMillis(Long.parseLong(list.get(position - 1).getCreateTime()));
        lastCalendar.set(Calendar.HOUR_OF_DAY, 0);
        lastCalendar.set(Calendar.MINUTE, 0);
        lastCalendar.set(Calendar.SECOND, 0);
        lastCalendar.set(Calendar.MILLISECOND, 0);
        Date lastDate = lastCalendar.getTime();
        if (lastDate.compareTo(currentDate) == 0) {
            return TYPE_CONTENT;
        } else {
            return TYPE_TITLE;
        }
    }
}
