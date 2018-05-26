package com.moemoe.lalala.view.widget.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.moemoe.lalala.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hygge on 2018/5/24.
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
//        LayoutParams params1 = new LayoutParams(LayoutParams.MATCH_PARENT, Layo utParams.MATCH_PARENT);
//        ImageView imageView = new ImageView(context);
//        imageView.setImageResource(R.drawable.bg_cardbg_nopic);
//        addView(imageView,params1);
    }

    public void setOnImageClickLietener(OnClickListener lietener) {
        touchImageView.setOnClickListener(lietener);
    }

    public void addMapMarkView(int markView, float x, float y, String schame, String content, String start, String end, String start1, String end1, MapMark.RenderDelegate renderDelegate) {
        if (markView <= 0) {
            throw new IllegalArgumentException("View for bubble cannot be null !");
        }
        MapMark mark = new MapMark(getContext());
        mark.setRenderDelegate(renderDelegate);
        mark.setMapX(x);
        mark.setMapY(y);
        mark.setImageResource(markView);
        mark.setSchame(schame);
        mark.setContent(content);
        // mark.setShowTime(showTime);
        mark.setStartTime(start);
        mark.setStartTime1(start1);
        mark.setEndTime(end);
        mark.setEndTime1(end1);
        marks.add(mark);
        addView(mark);
        touchImageView.addMapMark(mark);
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
        touchImageView.postInvalidate();
    }

    public void setMapBitmap(Bitmap bitmap) {
        touchImageView.setImageBitmap(bitmap);
    }

    public void setMapResource(int bitmap) {
        touchImageView.setImageResource(bitmap);
    }

    public void setImageURI(Uri uri) {
        touchImageView.setImageURI(uri);
    }

    public void setIsDialogCause(boolean isDialogCause) {
        touchImageView.setIsDialogCause(isDialogCause);
    }
}
