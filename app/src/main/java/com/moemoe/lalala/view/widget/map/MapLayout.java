package com.moemoe.lalala.view.widget.map;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.AppSetting;
import com.moemoe.lalala.event.HouseLikeEvent;
import com.moemoe.lalala.model.entity.HouseDbEntity;
import com.moemoe.lalala.model.entity.HouseImage;
import com.moemoe.lalala.model.entity.HouseLikeEntity;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.ToastUtils;
import com.moemoe.lalala.view.widget.view.HouseView;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Haru on 2016/7/25 0025.
 */
public class MapLayout extends FrameLayout {
    private TouchImageView touchImageView;
    private List<MapMark> marks = new ArrayList<>();
    private List<HouseView> wuViews = new ArrayList<>();
    private ArrayList<String> mChildView = new ArrayList<>();
    private ArrayList<HouseImage> mCharacter = new ArrayList<>();
    private HashMap<String, HouseImage> mapImage = new HashMap<>();
    private boolean isJump;
    private boolean isHisHouse = false;
    private AnimatorSet animation;
    private ValueAnimator valueAnimator;
    private ValueAnimator valueAnimator1;
    private ValueAnimator valueAnimator3;
    private ValueAnimator valueAnimator4;
    private AnimatorSet s1a;

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

    public void setIsHis(boolean isHis) {
        this.isHisHouse = isHis;
    }

    public void clearAllView() {
        marks.clear();
        wuViews.clear();
        mChildView.clear();
        touchImageView.removeAllMark(true);
        if (touchImageView != null) {
            touchImageView = null;
        }
        removeAllViews();
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

    public void addMapMarkView(Drawable markView, final float x, final float y, final double wdp, final double hdp, final String schame, String content, final String type, MapMark.RenderDelegate renderDelegate, final HouseDbEntity entity) {
        if (markView == null) {
            throw new IllegalArgumentException("View for bubble cannot be null !");
        }

        int heightPixels = getContext().getResources().getDisplayMetrics().heightPixels;
        final double v = 3600.0 * heightPixels / 1920.0;
        double wight = 1080.0 * v / 3600.0;

        if (entity.getType().equals("2") && entity.getImage_w() > 0 && entity.getImage_h() > 0) {
            HouseView shadowView = new HouseView(getContext());
            shadowView.setBackgroundResource(R.drawable.bg_home_roles_shadow);
            float shadowX = (float) (((wdp - getContext().getResources().getDimension(R.dimen.x88)) / 2) + x);
            float shadowY = (float) (y + hdp - ((int) getContext().getResources().getDimension(R.dimen.y15)));
            shadowView.setMapX(shadowX);
            shadowView.setMapY(shadowY);
            addView(shadowView);
            wuViews.add(shadowView);
            touchImageView.addRenWuMark(shadowView);
            mChildView.add("shadow" + entity.getId());
            FrameLayout.LayoutParams sleepParams = (FrameLayout.LayoutParams) shadowView.getLayoutParams();
            sleepParams.width = (int) getContext().getResources().getDimension(R.dimen.x88);
            sleepParams.height = (int) getContext().getResources().getDimension(R.dimen.y20);
            shadowView.setLayoutParams(sleepParams);
        }
        final HouseView wuView = new HouseView(getContext());
        wuView.setMapX(x);
        wuView.setMapY(y);
        wuView.setBackground(markView);
        addView(wuView);
        wuViews.add(wuView);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) wuView.getLayoutParams();
        params.width = (int) wdp;
        params.height = (int) hdp;
        wuView.setLayoutParams(params);
        touchImageView.addRenWuMark(wuView);
        if (type.equals("1")) {
            if (entity.getId().equals("65ac0b01-907f-440e-89e2-40a136053cc5")) {
                wuView.setTouch(false);
            } else {
                wuView.setTouch(false);
            }
        }
        isJump = true;
        if (entity.getPointX() > wight) {
            isJump = true;
        } else {
            isJump = false;
        }
        wuView.setOnClickListener(new NoDoubleClickListener(700) {
            @Override
            public void onNoDoubleClick(View view) {
                if (type.equals("2")) {
                    if (entity.isTimerIsSleep()) {
                        ToastUtils.showShortToast(getContext(), "睡觉中~~~");
                    } else {
                        onHouseClick(view, schame, type, entity.getId(), isJump);
                        isJump = !isJump;
                    }
                } else if (type.equals("3")) {
                    EventBus.getDefault().post(new HouseLikeEvent("", 3));
                    wuView.setVisibility(GONE);
                } else if (entity.getId().equals("2b262ee5-930a-4a2e-8b13-fb40189cc00d")) {
                    //从左向右
                    Animation rotateAnim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnim.setDuration(100);
                    rotateAnim.setRepeatCount(10);
                    rotateAnim.setInterpolator(new LinearInterpolator());
                    view.startAnimation(rotateAnim);
                }

            }
        });

        mChildView.add(entity.getId());

        if (entity.getType().equals("2") && entity.getImage_w() > 0 && entity.getImage_h() > 0) {
            HouseImage image = new HouseImage();
            image.setH(hdp);
            image.setW(wdp);
            image.setId(entity.getId());
            image.setSleep(entity.isTimerIsSleep());
            if (entity.getTimerRoleId() != null && entity.getTimerRoleId().equals(entity.getId())) {
                final HouseView houseView = new HouseView(getContext());
                houseView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                float likeX = (float) (((wdp - getContext().getResources().getDimension(R.dimen.x136)) / 2) + x);
                float likeY = (float) (y - getContext().getResources().getDimension(R.dimen.y80));
                houseView.setMapX(likeX);
                houseView.setMapY(likeY);
                houseView.setTextSize(getContext().getResources().getDimension(R.dimen.x10));
                houseView.setGravity(Gravity.CENTER_HORIZONTAL);
                houseView.setPadding(0, (int) getContext().getResources().getDimension(R.dimen.y30), 0, 0);
                addView(houseView);
                wuViews.add(houseView);
                touchImageView.addRenWuMark(houseView);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) houseView.getLayoutParams();
                layoutParams.width = (int) getContext().getResources().getDimension(R.dimen.x136);
                layoutParams.height = (int) getContext().getResources().getDimension(R.dimen.y118);
                houseView.setLayoutParams(layoutParams);
                mChildView.add("like" + entity.getId());
                if (isHisHouse) {
                    houseView.setBackgroundResource(R.drawable.bg_ic_home_heart_break_ture);
                    houseView.setText("");
                } else {
                    houseView.setBackgroundResource(R.drawable.bg_ic_home_heart_selector);
                    CountDownTimer countDownTimer = new CountDownTimer(entity.getTimerRemainTime(), 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            String value = StringUtils.getTimeFromMillisecond(millisUntilFinished);
                            houseView.setText(value);
                        }

                        @Override
                        public void onFinish() {
                            entity.setTimerIsCollectable(true);
                            houseView.setBackgroundResource(R.drawable.bg_ic_home_heart_selector_ture);
                            houseView.setText("");
                        }
                    }.start();
                }
                houseView.setOnClickListener(new NoDoubleClickListener(700) {
                    @Override
                    public void onNoDoubleClick(View view) {
                        if (isHisHouse) {
                            EventBus.getDefault().post(new HouseLikeEvent(entity.getTimerRoleId(), 3));
                            mapImage.get(entity.getId()).setRoleTimer(false);
                            houseView.setVisibility(GONE);
                            setLikeAnimtion(view);
                        } else {
                            if (entity.isTimerIsSleep()) {
                                ToastUtils.showShortToast(getContext(), "睡觉中~~~");
                            } else {
                                if (entity.getTimerIsCollectable()) {
                                    entity.setTimerIsCollectable(false);
                                    EventBus.getDefault().post(new HouseLikeEvent(entity.getTimerRoleId(), 0));
                                    setLikeAnimtion(view);
                                } else {
                                    //从左向右
                                    Animation rotateAnim = new RotateAnimation(-45f, 45f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                    rotateAnim.setDuration(100);
                                    rotateAnim.setRepeatCount(10);
                                    rotateAnim.setInterpolator(new LinearInterpolator());
                                    view.startAnimation(rotateAnim);
                                }
                            }
                        }
                    }
                });
                if (entity.isTimerIsSleep()) {
                    HouseView sleepView = new HouseView(getContext());
                    sleepView.setBackgroundResource(R.drawable.ic_home_roles_sleep);
                    float sleepX = (float) (x - ((getContext().getResources().getDimension(R.dimen.x132)) / 2));
                    float sleepY = (float) (y - getContext().getResources().getDimension(R.dimen.y80));
                    sleepView.setMapX(sleepX);
                    sleepView.setMapY(sleepY);
                    addView(sleepView);
                    wuViews.add(sleepView);
                    touchImageView.addRenWuMark(sleepView);
                    mChildView.add("sleep" + entity.getId());
                    FrameLayout.LayoutParams sleepParams = (FrameLayout.LayoutParams) sleepView.getLayoutParams();
                    sleepParams.width = (int) getContext().getResources().getDimension(R.dimen.x132);
                    sleepParams.height = (int) getContext().getResources().getDimension(R.dimen.y132);
                    sleepView.setLayoutParams(sleepParams);
                }
                image.setRoleTimer(true);
            } else {
                image.setRoleTimer(false);
            }
            mCharacter.add(image);
            mapImage.put(entity.getId(), image);
        }
    }

    /**
     * 循环 人物 呼吸动画
     *
     * @param wdp
     * @param hdp
     * @param wuView
     */
    private void goAnimator(final double wdp, double hdp, final View wuView, final View childView, final View shadowView, boolean isSleep, String id) {

//        CancelValueAnimator();

        valueAnimator = ValueAnimator.ofInt((int) hdp, (int) (hdp - getResources().getDimension(R.dimen.y8)), (int) hdp);
        valueAnimator.setRepeatCount(Animation.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                LayoutParams layoutParams = (LayoutParams) wuView.getLayoutParams();
                layoutParams.width = (int) wdp;
                layoutParams.height = (int) valueAnimator.getAnimatedValue();
                wuView.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.setDuration(3500);
        float wuViewY = wuView.getY();
        valueAnimator1 = ValueAnimator.ofFloat(wuViewY, wuViewY + getResources().getDimension(R.dimen.y8), wuViewY);
        valueAnimator1.setRepeatCount(Animation.INFINITE);
        valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                wuView.setY((Float) valueAnimator.getAnimatedValue());
            }
        });
        valueAnimator1.setDuration(3500);
        valueAnimator.start();
        valueAnimator1.start();
        if (mapImage.get(id).isRoleTimer()) {
            float childAtY = childView.getY();
            valueAnimator3 = ValueAnimator.ofFloat(childAtY, childAtY + getResources().getDimension(R.dimen.y6), childAtY);
            valueAnimator3.setRepeatCount(Animation.INFINITE);
            valueAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    childView.setY((Float) valueAnimator.getAnimatedValue());
                }
            });
            valueAnimator3.setDuration(3500);
            valueAnimator3.start();
        }

        if (isSleep) {
            float shadowAtY = shadowView.getY();
            valueAnimator4 = ValueAnimator.ofFloat(shadowAtY, shadowAtY + getResources().getDimension(R.dimen.y6), shadowAtY);
            valueAnimator4.setRepeatCount(Animation.INFINITE);
            valueAnimator4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    shadowView.setY((Float) valueAnimator.getAnimatedValue());
                }
            });
            valueAnimator4.setDuration(3500);
            valueAnimator4.start();
        }

    }

    /**
     * 关闭动画
     */
    private void CancelValueAnimator() {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
            valueAnimator = null;
        }

        if (valueAnimator1 != null && valueAnimator1.isRunning()) {
            valueAnimator1.cancel();
            valueAnimator1 = null;
        }
        if (valueAnimator3 != null && valueAnimator3.isRunning()) {
            valueAnimator3.cancel();
            valueAnimator3 = null;
        }
        if (valueAnimator4 != null && valueAnimator4.isRunning()) {
            valueAnimator4.cancel();
            valueAnimator4 = null;
        }
    }

    /**
     * 心动点击事件的回调
     *
     * @param entity
     */
    public void setTimerLikeView(HouseLikeEntity entity) {
        if (entity != null) {
            int child = 0;
            for (int i = 0; i < mChildView.size(); i++) {
                if (entity.getRoleId().equals(mChildView.get(i))) {
                    child = i + 2;
                }
            }
            final HouseView childAt = (HouseView) getChildAt(child);
            if (isHisHouse) {
                childAt.setBackgroundResource(R.drawable.bg_ic_home_heart_break_ture);
                childAt.setText("");
            } else {
                childAt.setBackgroundResource(R.drawable.bg_ic_home_heart_selector);
            }
            new CountDownTimer(entity.getRemainTime(), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    String value = StringUtils.getTimeFromMillisecond(millisUntilFinished);
                    childAt.setText(value);
                }

                @Override
                public void onFinish() {
                    childAt.setBackgroundResource(R.drawable.bg_ic_home_heart_selector_ture);
                    childAt.setText("");
                }
            }.start();
        }
    }

    /**
     * 缩放心动
     *
     * @param view
     */
    public void setLikeAnimtion(View view) {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.8f, 1.0f, 1.5f, 1.0f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.8f, 1.0f, 1.5f, 1.0f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(700);
        set.playTogether(animatorX, animatorY);
        set.start();
    }

    /**
     * 重新添加滑动image
     *
     * @param context
     */
    public void addTouchView(Context context) {
        if (touchImageView == null) {
            touchImageView = new TouchImageView(context);
            touchImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            addView(touchImageView, params);
        }
    }

    /**
     * 插入图片的点击事件
     *
     * @param view
     * @param schame
     * @param type
     */
    public void onHouseClick(View view, String schame, String type, String id, boolean isJump) {
        if (StringUtils.isKillEvent() && !AppSetting.isEnterEventToday) {
            return;
        }
        if (type.equals("2")) {//角色
            float x = view.getX();
            float y = view.getY();
            startAnimatorPath(view, x, y, id, isJump);
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
    public void startAnimatorPath(final View view, float x, float y, final String id, boolean isJump) {
        touchImageView.setIsTouch(true);
        int widthPixels = getContext().getResources().getDisplayMetrics().widthPixels;
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        float rightX = 0;
        float leftX = 0;
        if (x > (widthPixels - measuredWidth) / 2) {
            if (isJump) {
                leftX = x + getContext().getResources().getDimension(R.dimen.x100);
            } else {
                rightX = x - getContext().getResources().getDimension(R.dimen.x100);
            }
        } else {
            if (isJump) {
                leftX = x + getContext().getResources().getDimension(R.dimen.x100);
            } else {
                rightX = x - getContext().getResources().getDimension(R.dimen.x100);
            }
        }
        int child = 0;
        int shadow = 0;
        int sleep = 0;
        for (int i = 0; i < mChildView.size(); i++) {
            if (id.equals(mChildView.get(i))) {
                child = i + 2;
                shadow = i;
                sleep = i + 3;
            }
        }
        HouseImage houseImage = mapImage.get(id);

        final View childAt = getChildAt(child);
        float chlidX = childAt.getX();
        float childAtY = childAt.getY();
        View shadowAt = getChildAt(shadow);
        float shadowAtX = shadowAt.getX();
        float shadowAtY = shadowAt.getY();
        final View sleepAt = getChildAt(sleep);

        ObjectAnimator translateAnimation = null;
        ObjectAnimator translateAnimation1 = null;
        ObjectAnimator translateAnimation2 = null;
        if (isJump) {
            translateAnimation = ObjectAnimator.ofFloat(view, "X", x, leftX);
            translateAnimation1 = ObjectAnimator.ofFloat(childAt, "X", chlidX, chlidX + getContext().getResources().getDimension(R.dimen.x100));
            translateAnimation2 = ObjectAnimator.ofFloat(shadowAt, "X", shadowAtX, shadowAtX + getContext().getResources().getDimension(R.dimen.x100));
        } else {
            translateAnimation = ObjectAnimator.ofFloat(view, "X", x, rightX);
            translateAnimation1 = ObjectAnimator.ofFloat(childAt, "X", chlidX, chlidX - getContext().getResources().getDimension(R.dimen.x100));
            translateAnimation2 = ObjectAnimator.ofFloat(shadowAt, "X", shadowAtX, shadowAtX - getContext().getResources().getDimension(R.dimen.x100));
        }
        translateAnimation.setInterpolator(new LinearInterpolator());
        translateAnimation.setDuration(710);

        translateAnimation2.setInterpolator(new LinearInterpolator());
        translateAnimation2.setDuration(710);

        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view,
                "Y", y, y - 80)
                .setDuration(250);
        anim1.setInterpolator(new DecelerateInterpolator());
        anim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.postInvalidate();
                view.invalidate();
            }
        });


        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view,
                "Y", y - 80, y)
                .setDuration(150);
        anim2.setInterpolator(new AccelerateInterpolator());
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.postInvalidate();
                view.invalidate();
            }
        });


        ObjectAnimator anim3 = ObjectAnimator.ofFloat(view,
                "Y", y, y - 40)

                .setDuration(70);
        anim3.setInterpolator(new DecelerateInterpolator());
        anim3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.postInvalidate();
                view.invalidate();
            }
        });


        ObjectAnimator anim4 = ObjectAnimator.ofFloat(view,
                "Y", y - 40, y)
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


        animation = new AnimatorSet();

        if (houseImage.isRoleTimer()) {
            ObjectAnimator anim1a = ObjectAnimator.ofFloat(childAt,
                    "Y", childAtY, childAtY - 80)
                    .setDuration(250);
            anim1a.setInterpolator(new DecelerateInterpolator());
            anim1a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    childAt.postInvalidate();
                    childAt.invalidate();
                }
            });

            ObjectAnimator anim2a = ObjectAnimator.ofFloat(childAt,
                    "Y", childAtY - 80, childAtY)
                    .setDuration(150);
            anim2a.setInterpolator(new AccelerateInterpolator());
            anim2a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    childAt.postInvalidate();
                    childAt.invalidate();
                }
            });
            ObjectAnimator anim3a = ObjectAnimator.ofFloat(childAt,
                    "Y", childAtY, childAtY - 40)

                    .setDuration(70);
            anim3a.setInterpolator(new DecelerateInterpolator());
            anim3a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    childAt.postInvalidate();
                    childAt.invalidate();
                }
            });
            ObjectAnimator anim4a = ObjectAnimator.ofFloat(childAt,
                    "Y", childAtY - 40, childAtY)
                    .setDuration(50);
            anim4a.setInterpolator(new AccelerateInterpolator());
            anim4a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    childAt.postInvalidate();
                    childAt.invalidate();
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

            translateAnimation1.setInterpolator(new LinearInterpolator());
            translateAnimation1.setDuration(710);

            s1a = new AnimatorSet();
            s1a.playSequentially(anim1a, anim2a, anim3a, anim4a, anim5a, anim6a);// 顺序播放效果，参数个数可变  
            animation.playTogether(translateAnimation, translateAnimation1, translateAnimation2, s1, s1a);// 并行  
        } else {
            animation.playTogether(translateAnimation, translateAnimation1, translateAnimation2, s1);// 并行  
        }

        animation.start();

        animation.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                touchImageView.setIsTouch(false);
            }
        });
    }

    public int getViewHeight() {
        return touchImageView.getViewHeight();
    }

    public void removeAllMarkView(boolean isChange) {
        if (isChange) {
            marks.clear();
            wuViews.clear();
            mChildView.clear();
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

        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                for (int i = 0; i < mChildView.size(); i++) {
                    HouseImage houseImage = mapImage.get(mChildView.get(i));
                    if (houseImage != null && houseImage.getId().equals(mChildView.get(i))) {
                        goAnimator(houseImage.getW(), houseImage.getH(), getChildAt(i + 1), getChildAt(i + 2), getChildAt(i + 3), houseImage.isSleep(), houseImage.getId());
                    }
                }
            }
        }.start();
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