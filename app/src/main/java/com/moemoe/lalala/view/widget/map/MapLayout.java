package com.moemoe.lalala.view.widget.map;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.AppSetting;
import com.moemoe.lalala.model.entity.MapDbEntity;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.view.widget.view.HouseView;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramSocketImplFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Haru on 2016/7/25 0025.
 */
public class MapLayout extends FrameLayout {
    private TouchImageView touchImageView;
    private List<MapMark> marks = new ArrayList<>();
    private List<HouseView> wuViews = new ArrayList<>();
    private ArrayList<String> mChildView = new ArrayList<>();

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

    public void addMapMarkView(Drawable markView, float x, float y, double wdp, double hdp, final String schame, String content, final String type, MapMark.RenderDelegate renderDelegate, final MapDbEntity entity) {
        if (markView == null) {
            throw new IllegalArgumentException("View for bubble cannot be null !");
        }
        MapMark mark = new MapMark(getContext());
        mark.setRenderDelegate(renderDelegate);
        mark.setMapX(x);
        mark.setMapY(y);
        mark.setImageDrawable(markView);
        mark.setSchame(schame);
        mark.setContent(content);
        HouseView wuView = new HouseView(getContext());
        wuView.setMapX(x);
        wuView.setMapY(y);
//        wuView.setImageDrawable(markView);
        wuView.setBackground(markView);
        addView(wuView);
        wuViews.add(wuView);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) wuView.getLayoutParams();
        params.width = (int) wdp;
        params.height = (int) hdp;
        wuView.setLayoutParams(params);
        touchImageView.addRenWuMark(wuView);

        wuView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onHouseClick(view, schame, type, entity.getId());
            }
        });
        mChildView.add(entity.getId());

        if (entity.getType().equals("2") && entity.getImage_w() > 0 && entity.getImage_h() > 0) {

            HouseView houseView = new HouseView(getContext());
            houseView.setBackgroundResource(R.drawable.bg_ic_home_heart_selector);
//            houseView.setImageResource(R.drawable.bg_ic_home_heart_selector);
            float likeX = (float) (((wdp - getContext().getResources().getDimension(R.dimen.x150)) / 2) + x);
            float likeY = (float) (y - getContext().getResources().getDimension(R.dimen.x150) - getContext().getResources().getDimension(R.dimen.y24));
            houseView.setMapX(likeX);
            houseView.setMapY(likeY);
            addView(houseView);
            wuViews.add(houseView);
            touchImageView.addRenWuMark(houseView);
            LayoutParams layoutParams = (LayoutParams) houseView.getLayoutParams();
            layoutParams.width = (int) getContext().getResources().getDimension(R.dimen.x150);
            layoutParams.height = (int) getContext().getResources().getDimension(R.dimen.x150);
            houseView.setLayoutParams(layoutParams);
            mChildView.add("like" + entity.getId());
        }

    }

    /**
     * 插入图片的点击事件
     *
     * @param view
     * @param schame
     * @param type
     */
    public void onHouseClick(View view, String schame, String type, String id) {
        if (StringUtils.isKillEvent() && !AppSetting.isEnterEventToday) {
            return;
        }
        if (type.equals("2")) {//角色
            float x = view.getX();
            float y = view.getY();
            int widthPixels = getContext().getResources().getDisplayMetrics().widthPixels;
            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();
            if (widthPixels - x < getContext().getResources().getDimension(R.dimen.x24)) {
                startRightAnimatorPath(view, x, y);
            } else {
                startLeftAnimatorPath(view, x, y, id);
            }
        } else if (type.equals("3")) {//垃圾
            removeView(view);
            wuViews.remove(view);
        } else {

        }
    }

    /**
     * 动画效果
     *
     * @param view
     * @param x
     * @param y
     */
    public void startLeftAnimatorPath(final View view, float x, float y, String id) {
        int widthPixels = getContext().getResources().getDisplayMetrics().widthPixels;
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        boolean isJump = true;
        float rightX = 0;
        float leftX = 0;
        if ((widthPixels - (x + measuredWidth)) <= getContext().getResources().getDimension(R.dimen.x24)) {
            isJump = false;
            rightX = x - 200;
        } else if (x < getContext().getResources().getDimension(R.dimen.x24)) {
            isJump = true;
            leftX = x + 200;
        } else {
            if (!isJump) {
                if (x < 200) {
                    if (x < 24) {
                        isJump = true;
                        leftX = x + 200;
                    } else {
                        isJump = false;

                    }
                } else {
                    isJump = false;
                    rightX = x - 200;
                }
            } else {
                if (widthPixels - (x + measuredWidth) < 24) {
                    isJump = false;
                    rightX = x - 200;
                } else {
                    isJump = true;
                    leftX = x + 200;
                }
            }
        }
        int child = 0;
        for (int i = 0; i < mChildView.size(); i++) {
            if (id.equals(mChildView.get(i))) {
                child = i + 2;
            }
        }
        final View childAt = getChildAt(child);
        float chlidX = childAt.getX();
        float childAtY = childAt.getY();
        ObjectAnimator translateAnimation = null;
        ObjectAnimator translateAnimation1 = null;
        if (isJump) {
            translateAnimation = ObjectAnimator.ofFloat(view, "X", x, leftX);
            translateAnimation1 = ObjectAnimator.ofFloat(childAt, "X", chlidX, chlidX + 200);
        } else {
            translateAnimation = ObjectAnimator.ofFloat(view, "X", x, rightX);
            translateAnimation1 = ObjectAnimator.ofFloat(childAt, "X", chlidX, chlidX - 200);
        }
        translateAnimation.setInterpolator(new LinearInterpolator());
        translateAnimation.setDuration(710);
        translateAnimation1.setInterpolator(new LinearInterpolator());
        translateAnimation1.setDuration(710);

        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view,
                "Y", y, y - 150)
                .setDuration(300);
        anim1.setInterpolator(new DecelerateInterpolator());
        anim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.postInvalidate();
                view.invalidate();
            }
        });
        ObjectAnimator anim1a = ObjectAnimator.ofFloat(childAt,
                "Y", childAtY, childAtY - 150)
                .setDuration(300);
        anim1a.setInterpolator(new DecelerateInterpolator());
        anim1a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                childAt.postInvalidate();
                childAt.invalidate();
            }
        });


        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view,
                "Y", y - 150, y)
                .setDuration(200);
        anim2.setInterpolator(new AccelerateInterpolator());
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.postInvalidate();
                view.invalidate();
            }
        });

        ObjectAnimator anim2a = ObjectAnimator.ofFloat(childAt,
                "Y", childAtY - 150, childAtY)
                .setDuration(200);
        anim2a.setInterpolator(new AccelerateInterpolator());
        anim2a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                childAt.postInvalidate();
                childAt.invalidate();
            }
        });


        ObjectAnimator anim3 = ObjectAnimator.ofFloat(view,
                "Y", y, y - 50)

                .setDuration(100);
        anim3.setInterpolator(new DecelerateInterpolator());
        anim3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.postInvalidate();
                view.invalidate();
            }
        });
        ObjectAnimator anim3a = ObjectAnimator.ofFloat(childAt,
                "Y", childAtY, childAtY - 50)

                .setDuration(100);
        anim3a.setInterpolator(new DecelerateInterpolator());
        anim3a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                childAt.postInvalidate();
                childAt.invalidate();
            }
        });


        ObjectAnimator anim4 = ObjectAnimator.ofFloat(view,
                "Y", y - 50, y)
                .setDuration(50);
        anim4.setInterpolator(new AccelerateInterpolator());
        anim4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.postInvalidate();
                view.invalidate();
            }
        });
        ObjectAnimator anim4a = ObjectAnimator.ofFloat(childAt,
                "Y", childAtY - 50, childAtY)
                .setDuration(50);
        anim4a.setInterpolator(new AccelerateInterpolator());
        anim4a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                childAt.postInvalidate();
                childAt.invalidate();
            }
        });


        ObjectAnimator anim5 = ObjectAnimator.ofFloat(view,
                "Y", y, y - 20)
                .setDuration(40);
        anim5.setInterpolator(new DecelerateInterpolator());
        anim5.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.postInvalidate();
                view.invalidate();
            }
        });
        ObjectAnimator anim5a = ObjectAnimator.ofFloat(childAt,
                "Y", childAtY, childAtY - 20)
                .setDuration(40);
        anim5a.setInterpolator(new DecelerateInterpolator());
        anim5a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                childAt.postInvalidate();
                childAt.invalidate();
            }
        });


        ObjectAnimator anim6 = ObjectAnimator.ofFloat(view,
                "Y", y - 20, y)
                .setDuration(20);
        anim6.setInterpolator(new AccelerateInterpolator());
        anim6.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.postInvalidate();
                view.invalidate();
            }
        });
        ObjectAnimator anim6a = ObjectAnimator.ofFloat(childAt,
                "Y", childAtY - 20, childAtY)
                .setDuration(20);
        anim6a.setInterpolator(new AccelerateInterpolator());
        anim6a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                childAt.postInvalidate();
                childAt.invalidate();
            }
        });


        AnimatorSet s1 = new AnimatorSet();
        s1.playSequentially(anim1, anim2, anim3, anim4, anim5, anim6);// 顺序播放效果，参数个数可变  
        AnimatorSet s1a = new AnimatorSet();
        s1a.playSequentially(anim1a, anim2a, anim3a, anim4a, anim5a, anim6a);// 顺序播放效果，参数个数可变  


        AnimatorSet animation = new AnimatorSet();
        animation.playTogether(translateAnimation, translateAnimation1, s1, s1a);// 并行  
        animation.start();
    }

    public void startRightAnimatorPath(final View view, float x, float y) {

        ObjectAnimator translateAnimation = ObjectAnimator.ofFloat(view, "X", x, x - 200);
        translateAnimation.setInterpolator(new LinearInterpolator());
        translateAnimation.setDuration(710);
        translateAnimation.start();
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view,
                "Y", y, y - 150)
                .setDuration(300);
        anim1.setInterpolator(new DecelerateInterpolator());
        anim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.postInvalidate();
                view.invalidate();
            }
        });

        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view,
                "Y", y - 150, y)
                .setDuration(200);
        anim2.setInterpolator(new AccelerateInterpolator());
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.postInvalidate();
                view.invalidate();
            }
        });

        ObjectAnimator anim3 = ObjectAnimator.ofFloat(view,
                "Y", y, y - 50)

                .setDuration(100);
        anim3.setInterpolator(new DecelerateInterpolator());
        anim3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.postInvalidate();
                view.invalidate();
            }
        });

        ObjectAnimator anim4 = ObjectAnimator.ofFloat(view,
                "Y", y - 50, y)
                .setDuration(50);
        anim4.setInterpolator(new AccelerateInterpolator());
        anim4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.postInvalidate();
                view.invalidate();
            }
        });

        ObjectAnimator anim5 = ObjectAnimator.ofFloat(view,
                "Y", y, y - 20)
                .setDuration(40);
        anim5.setInterpolator(new DecelerateInterpolator());
        anim5.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.postInvalidate();
                view.invalidate();
            }
        });


        ObjectAnimator anim6 = ObjectAnimator.ofFloat(view,
                "Y", y - 20, y)
                .setDuration(20);
        anim6.setInterpolator(new AccelerateInterpolator());
        anim6.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.postInvalidate();
                view.invalidate();
            }
        });

        AnimatorSet s1 = new AnimatorSet();
        s1.playSequentially(anim1, anim2, anim3, anim4, anim5, anim6);// 顺序播放效果，参数个数可变  


        AnimatorSet animation = new AnimatorSet();
        animation.playTogether(translateAnimation, s1);// 并行  
        animation.start();
    }

    public void addMapMarkView(int markView, float x, float y, double wdp, double hdp, String schame, String content, String type, MapMark.RenderDelegate renderDelegate, String id) {
        if (markView >= 0) {
            throw new IllegalArgumentException("View for bubble cannot be null !");
        }
        MapMark mark = new MapMark(getContext());
        mark.setRenderDelegate(renderDelegate);
        mark.setMapX(x);
        mark.setMapY(y);
        mark.setImageResource(markView);
        mark.setSchame(schame);
        mark.setContent(content);

        HouseView wuView = new HouseView(getContext());
        wuView.setMapX(x);
        wuView.setMapY(y);
//        wuView.setImageResource(markView);
        wuView.setBackgroundResource(markView);
        addView(wuView);
        wuViews.add(wuView);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) wuView.getLayoutParams();
        params.width = (int) wdp;
        params.height = (int) hdp;
        wuView.setLayoutParams(params);
        touchImageView.addRenWuMark(wuView);
        mChildView.add(id);
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