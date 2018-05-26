package com.moemoe.lalala.view.widget.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.moemoe.lalala.R;

/**
 * Created by Administrator on 2018/5/17.
 */

public class ImageViewWithNeedle extends RelativeLayout {

    private ImageView mExtralmage;

    public ImageViewWithNeedle(Context context) {
        super(context);
        initView(context);
    }


    public ImageViewWithNeedle(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ImageViewWithNeedle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_image_with_needle, this, false);
        mExtralmage = rootView.findViewById(R.id.image_ext_icon_image);
        addView(rootView);
    }

    public void setExtraImage(Bitmap bitmap) {
        if (mExtralmage != null) {
            mExtralmage.setImageBitmap(bitmap);
        }
    }

}
