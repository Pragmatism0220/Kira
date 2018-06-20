package com.moemoe.lalala.view.adapter;

import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.VisitorsEntity;
import com.moemoe.lalala.utils.LevelSpan;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.view.activity.NewVisitorActivity;
import com.moemoe.lalala.view.widget.adapter.ClickableViewHolder;

import java.text.SimpleDateFormat;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Administrator on 2018/6/4.
 */

public class NewVisitorHolder extends ClickableViewHolder {

    TextView mTitleTime;

    TextView mTime;
    ImageView mImage;
    TextView mName;
    ImageView mSex;
    TextView mLevel;
    TextView mInfo;

    RelativeLayout mRoot;


    int viewType;


    public NewVisitorHolder(View itemView, int viewType) {
        super(itemView);
        if (itemView == null) {
            return;
        }
        this.viewType = viewType;
        if (viewType == NewVisitorAdapter.TYPE_TITLE) {
            mTitleTime = $(R.id.root_time);
        }
        mName = $(R.id.visitor_name);
        mTime = $(R.id.visitor_time);
        mInfo = $(R.id.visitor_info);
        mImage = $(R.id.visitor_image);
        mSex = $(R.id.visitor_sex);
        mLevel = $(R.id.visitor_level);
        mRoot = $(R.id.visitor_root);
    }

}
