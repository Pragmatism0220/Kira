package com.moemoe.lalala.model.entity.tag;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.utils.BaseUrlSpan;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.view.activity.PersonalV2Activity;

/**
 *
 * Created by yi on 2017/9/19.
 */

public class FontUrlSpan extends BaseUrlSpan {

    public FontUrlSpan(Context context, BaseTag tag){
        super(context,tag);
    }

    public FontUrlSpan(Context context, String url, BaseTag tag) {
        super(context, url,tag);
    }

    @Override
    public void onClick(View widget) {

    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(StringUtils.readColorStr(mUrl,ContextCompat.getColor(mContext, R.color.main_cyan)));
        ds.setUnderlineText(false);
    }
}
