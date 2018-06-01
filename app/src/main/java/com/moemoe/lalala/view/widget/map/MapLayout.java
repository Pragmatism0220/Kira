package com.moemoe.lalala.view.widget.map;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.moemoe.lalala.presenter.DormitoryContract;
import com.moemoe.lalala.utils.DensityUtil;
import com.moemoe.lalala.utils.NoDoubleClickListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Haru on 2016/7/25 0025.
 */
public class MapLayout extends FrameLayout {
    private TouchImageView touchImageView;
    private List<MapMark> marks = new ArrayList<>();

    public MapLayout(Context context) {
        this(context, null);
    }

    public MapLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialImageView(context);
    }

    public MapLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialImageView(context);
    }

    private void initialImageView(Context context) {
        touchImageView = new TouchImageView(context);
        touchImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(touchImageView, params);
    }

    public void setOnImageClickLietener(OnClickListener lietener) {
        touchImageView.setOnClickListener(lietener);
    }

    public void addMapMarkView(Drawable markView, float x, float y, int wdp, int hdp, String schame, String content, String type, MapMark.RenderDelegate renderDelegate) {
        if (markView == null) {
            throw new IllegalArgumentException("View for bubble cannot be null !");
        }
        MapMark mark = new MapMark(getContext());
        mark.setRenderDelegate(renderDelegate);
        mark.setMapX(x);
        mark.setMapY(y);
        mark.setImageDrawable(markView);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mark.getLayoutParams();
        float density = getContext().getResources().getDisplayMetrics().density;

        layoutParams.width = (int) (wdp / density + 0.5f);
        layoutParams.height = (int) (hdp / density + 0.5f);
        mark.setLayoutParams(layoutParams);
//        mark.setImageResource(markView);
        mark.setSchame(schame);
        mark.setContent(content);
        // mark.setShowTime(showTime);
//        mark.setStartTime(start);
//        mark.setStartTime1(start1); 
//        mark.setEndTime(end);
//        mark.setEndTime1(end1);
        marks.add(mark);
        addView(mark);
        touchImageView.addMapMark(mark);
    }

    public void setTouchImageViewWidthOrHeight(int width, int height) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) touchImageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        touchImageView.setLayoutParams(layoutParams);
    }

    public int getViewHeight() {
        return touchImageView.getViewHeight();
    }

    public void removeAllMarkView(boolean isChange) {
        removeViews(1, marks.size());
        if (isChange) {
            marks.clear();
        }
        touchImageView.removeAllMark(isChange);
    }

    public void rebuildMarks() {
        for (MapMark mapMark : marks) {
            addView(mapMark);
        }
        // touchImageView.postInvalidate();
    }

    public void setMapBitmap(Bitmap bitmap) {
        touchImageView.setImageBitmap(bitmap);
    }

    public void setMapResource(int bitmap) {
        touchImageView.setImageResource(bitmap);
    }

    public void setImageDrawable(Drawable drawable) {
        touchImageView.setImageDrawable(drawable);
    }

    public void setMapUrl(String name) {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try {
            InputStream is = am.open(name);
            image = BitmapFactory.decodeStream(is);
            touchImageView.setImageBitmap(image);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setIsDialogCause(boolean isDialogCause) {
        touchImageView.setIsDialogCause(isDialogCause);
    }
}