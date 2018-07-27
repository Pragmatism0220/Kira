package com.moemoe.lalala.view.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.moemoe.lalala.R;
import com.moemoe.lalala.databinding.AcKissBinding;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.TextAppearOneControl;
import com.moemoe.lalala.view.base.BaseActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Hygge on 2018/7/18.
 */

public class KissActivity extends BaseActivity {

    private AcKissBinding binding;
    private int mCurIndex;
    private ArrayList<Map> arrayList;
    private boolean touch = false;
    private String hair;
    private String forehead;
    private String eye;
    private String ears;
    private String nose;
    private String mouth;
    private String neck;
    private String chest;
    private String shoulder;
    private int goodValue;
    private int hairTouch;
    private int foreheadTouch;
    private int noseTouch;
    private int mouthTouch;
    private int neckrTouch;
    private int eyeTouch;
    private int shoulderTouch;
    private int earsTouch;
    private int chestTouch;
    private boolean bgKissMask;
    private CountDownTimer countDownTimer;
    private int kisslong;
    private int countdownLong;
    private CountDownTimer downTimer;
    private int contdownLoessNum = 0;
    private ObjectAnimator anim;
    private MediaPlayer mBgmPlayer;
    private ScaleAnimation scaleAnimation;
    private AlphaAnimation alphaAnimation;

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.ac_kiss);
        binding.setPresenter(new Presenter());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        TextAppearOneControl.getInstance().release();
        if (mBgmPlayer != null) {
            mBgmPlayer.release();
            mBgmPlayer = null;
        }
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        arrayList = toJson();
        TextAppearOneControl.getInstance().getInstance().setTextView(binding.tvText);
        mCurIndex = 0;
        bgKissMask = false;
        setData();
        countDownTimer = new CountDownTimer(11000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String value = String.valueOf((int) (millisUntilFinished / 1000));
                binding.tvCountdown.setText(value);
            }

            @Override
            public void onFinish() {
                if (kisslong <= 3 && binding.tvCountdown.getVisibility() == View.VISIBLE) {
                    if (contdownLoessNum == 3) {
                        showToast("剧情完成失败～");
                        finish();
                        return;
                    }
                    contdownLoessNum++;
                    binding.ivCgThree.setImageResource(R.drawable.rem_6);
                    ObjectAnimator anim = ObjectAnimator.ofFloat(binding.ivCgFour, "alpha", 1f, 0f);
                    anim.setDuration(1000);// 动画持续时间
                    anim.start();

                    ObjectAnimator animator = ObjectAnimator.ofFloat(binding.ivCgThree, "alpha", 0f, 1f);
                    animator.setDuration(1000);
                    animator.start();
                }
            }
        };
        downTimer = new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownLong = (int) (millisUntilFinished / 1000);
                String value = String.valueOf((int) (millisUntilFinished / 1000));
                binding.tvCountdownTwo.setText(value);
            }

            @Override
            public void onFinish() {
                binding.ivKissLove.clearAnimation();
                binding.ivKissLove.setVisibility(View.GONE);
                binding.tvCountdownTwo.setVisibility(View.GONE);
                if (10 - countdownLong > 5) {
                    contdownLoessNum = 0;
                    showToast("剧情完成");
                    binding.tvCountdown.setVisibility(View.GONE);
                    binding.ivCgThree.setImageResource(R.drawable.rem_7);
                    ObjectAnimator anim = ObjectAnimator.ofFloat(binding.ivCgFour, "alpha", 1f, 0f);
                    anim.setDuration(1000);// 动画持续时间
                    anim.start();

                    ObjectAnimator animator = ObjectAnimator.ofFloat(binding.ivCgThree, "alpha", 0f, 1f);
                    animator.setDuration(1000);
                    animator.start();
                    return;
                }
                if (kisslong <= 3) {

                    if (contdownLoessNum == 3) {
                        showToast("剧情完成失败～");
                        finish();
                        return;
                    }
                    contdownLoessNum++;
                    binding.ivCgThree.setImageResource(R.drawable.rem_6);
                    ObjectAnimator anim = ObjectAnimator.ofFloat(binding.ivCgFour, "alpha", 1f, 0f);
                    anim.setDuration(1000);// 动画持续时间
                    anim.start();

                    ObjectAnimator animator = ObjectAnimator.ofFloat(binding.ivCgThree, "alpha", 0f, 1f);
                    animator.setDuration(1000);
                    animator.start();

                    if (contdownLoessNum == 1) {
                        binding.tvName.setVisibility(View.VISIBLE);
                        binding.tvText.setVisibility(View.VISIBLE);
                        binding.tvName.setText("蕾姆");
                        TextAppearOneControl.getInstance().getInstance().setTextAndStart("蕾姆感觉好舒服……");
                    } else if (contdownLoessNum == 2) {
                        binding.tvName.setVisibility(View.VISIBLE);
                        binding.tvText.setVisibility(View.VISIBLE);
                        binding.tvName.setText("蕾姆");
                        TextAppearOneControl.getInstance().getInstance().setTextAndStart("蕾姆感不讨厌这样呢……");
                    } else if (contdownLoessNum == 3) {
                        binding.tvName.setVisibility(View.VISIBLE);
                        binding.tvText.setVisibility(View.VISIBLE);
                        binding.tvName.setText("蕾姆");
                        TextAppearOneControl.getInstance().getInstance().setTextAndStart("蕾姆好喜欢你……");
                    }
                }
            }
        };


        //爱心动画
        scaleAnimation = new ScaleAnimation(1.0f, 3.0f, 1.0f, 3.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(6000);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setFillBefore(false);
        scaleAnimation.start();
        //bgm
        if (mBgmPlayer != null) {
            if (mBgmPlayer.isPlaying()) {
                mBgmPlayer.release();
            }
            mBgmPlayer = null;
        }
        mBgmPlayer = new MediaPlayer();
        try {
            mBgmPlayer.setDataSource("http://s.moemoe.la/bgm/graduation.mp3");
            mBgmPlayer.prepareAsync();    // 异步准备，不会阻碍主线程
            mBgmPlayer.setLooping(true);
            // 当准备好时
            mBgmPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mBgmPlayer.setLooping(true);
                    mBgmPlayer.start();
                }
            });
            // 当播放完成时
            mBgmPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mBgmPlayer.isLooping()) {
                        mBgmPlayer.setLooping(true);
                        mBgmPlayer.start();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setData() {
        if (mCurIndex != -1) {
            if (arrayList.size() == mCurIndex) {
                mCurIndex = 0;
                return;
            }
            Map map = arrayList.get(mCurIndex);
            double index = (double) map.get("index");
            mCurIndex = (int) index;
            if (!TextUtils.isEmpty((String) map.get("name"))) {
                binding.tvName.setVisibility(View.VISIBLE);
                if (((String) map.get("name")).equals("我")) {
                    binding.tvName.setText(PreferenceUtils.getAuthorInfo().getUserName());
                } else {
                    binding.tvName.setText((String) map.get("name"));
                }
            } else {
                binding.tvName.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty((String) map.get("content"))) {
                binding.tvText.setVisibility(View.VISIBLE);
                TextAppearOneControl.getInstance().setTextAndStart((String) map.get("content"));
            } else {
                binding.tvText.setVisibility(View.GONE);
            }
            ArrayList options = (ArrayList) map.get("options");
            if (options != null && options.size() > 0) {
                binding.rlKissSelect.setVisibility(View.VISIBLE);
            }
            touch = (boolean) map.get("touch");
            if (touch) {
                binding.tvName.setVisibility(View.INVISIBLE);
                binding.tvText.setVisibility(View.INVISIBLE);
                binding.ivTouch.setVisibility(View.VISIBLE);
                setAnimsion();
            }

            Map jsonObject = (Map) map.get("touchPosition");
            if (jsonObject != null) {
                visviblTouch();
                binding.tvName.setVisibility(View.GONE);
                hair = (String) jsonObject.get("hair");
                forehead = (String) jsonObject.get("forehead");
                eye = (String) jsonObject.get("eye");
                ears = (String) jsonObject.get("ears");
                nose = (String) jsonObject.get("nose");
                mouth = (String) jsonObject.get("mouth");
                neck = (String) jsonObject.get("neck");
                chest = (String) jsonObject.get("chest");
                shoulder = (String) jsonObject.get("shoulder");
            }
            bgKissMask = (boolean) map.containsKey("bgKissMask");
            if (bgKissMask) {
                binding.ivCgTwo.setVisibility(View.INVISIBLE);
                binding.ivCgThree.setVisibility(View.VISIBLE);
                binding.ivCgThree.setImageResource(R.drawable.rem_4);
            }
        }
    }

    public void visviblTouch() {
        binding.viewHair.setVisibility(View.VISIBLE);
        binding.viewForehead.setVisibility(View.VISIBLE);
        binding.viewNose.setVisibility(View.VISIBLE);
        binding.viewMouth.setVisibility(View.VISIBLE);
        binding.viewNeck.setVisibility(View.VISIBLE);
        binding.rlKissProgress.setVisibility(View.VISIBLE);
        binding.llEyeEars.setVisibility(View.VISIBLE);
        binding.llShoulder.setVisibility(View.VISIBLE);
        binding.viewChest.setVisibility(View.VISIBLE);
        binding.progressKiss.setProgress(0);
        binding.tvKissProgressbar.setText(goodValue + "/ 10");
        hairTouch = 0;
        foreheadTouch = 0;
        noseTouch = 0;
        mouthTouch = 0;
        neckrTouch = 0;
        eyeTouch = 0;
        earsTouch = 0;
        shoulderTouch = 0;
        chestTouch = 0;

    }

    public void goneTouc() {
        binding.viewHair.setVisibility(View.GONE);
        binding.viewForehead.setVisibility(View.GONE);
        binding.viewNose.setVisibility(View.GONE);
        binding.viewMouth.setVisibility(View.GONE);
        binding.viewNeck.setVisibility(View.GONE);
        binding.rlKissProgress.setVisibility(View.GONE);
        binding.llEyeEars.setVisibility(View.GONE);
        binding.llShoulder.setVisibility(View.GONE);
        binding.viewChest.setVisibility(View.GONE);
        binding.progressKiss.setProgress(0);
        binding.tvKissProgressbar.setText(goodValue + "/ 10");
    }

    /**
     * cg 动画
     */
    public void setAnimsion() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(binding.ivCg, "alpha", 1f, 0f);
        anim.setDuration(1000);// 动画持续时间
        anim.start();

        ObjectAnimator animator = ObjectAnimator.ofFloat(binding.ivCgTwo, "alpha", 0f, 1f);
        animator.setDuration(1000);
        animator.start();

        TranslateAnimation translateAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE, binding.ivTouch.getWidth()
                , TranslateAnimation.ABSOLUTE, -getResources().getDimension(R.dimen.x20), TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 0);
        //设置动画持续时长
        translateAnimation.setDuration(1000);
        translateAnimation.setRepeatCount(ScaleAnimation.INFINITE);
        translateAnimation.setFillAfter(false);
        binding.ivTouch.clearAnimation();
        binding.ivTouch.setAnimation(translateAnimation);


    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        anim = ObjectAnimator.ofFloat(binding.confessionAnimation, "alpha", 1f, 0f);
        anim.setDuration(1500);// 动画持续时间
        anim.setRepeatCount(2);
    }

    @Override
    protected void initListeners() {

        binding.flClick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (countDownTimer != null && downTimer != null && binding.tvCountdown.getVisibility() == View.VISIBLE) {
                            binding.tvCountdown.setVisibility(View.GONE);
                            binding.tvCountdownTwo.setVisibility(View.VISIBLE);
                            binding.ivKissLove.setVisibility(View.VISIBLE);
                            countDownTimer.cancel();
                            downTimer.start();
//                            if (animationSet != null) {
//                                binding.ivKissLove.setVisibility(View.VISIBLE);
//                                binding.ivKissLove.setAlpha(1f);
//                                animationSet.cancel();
//                                binding.ivKissLove.setAnimation(animationSet);
//                            }
                            if (scaleAnimation != null) {
                                binding.tvCountdownTwo.setVisibility(View.GONE);
                                binding.ivKissLove.clearAnimation();
                                scaleAnimation.cancel();
                                scaleAnimation.start();
                                binding.ivKissLove.setAnimation(scaleAnimation);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        if (countDownTimer != null && downTimer != null && binding.ivKissLove.getVisibility() == View.VISIBLE) {
                            countDownTimer.cancel();
                            downTimer.cancel();
                            downTimer.cancel();
                            downTimer.onFinish();
                            if (scaleAnimation != null) {
                                binding.ivKissLove.clearAnimation();
                                binding.ivKissLove.setVisibility(View.GONE);
//                                binding.ivKissLove.setAnimation(alphaAnimation);
                            }

                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void initData() {

    }

    public class Presenter {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_accept:
                    binding.rlKissSelect.setVisibility(View.GONE);
                    binding.tvName.setVisibility(View.INVISIBLE);
                    binding.tvText.setVisibility(View.INVISIBLE);
                    setData();
                    break;
                case R.id.tv_refuse:
                    binding.rlKissSelect.setVisibility(View.GONE);
                    binding.tvName.setVisibility(View.INVISIBLE);
                    binding.tvText.setVisibility(View.INVISIBLE);
                    showToast("剧情结束，好感度降至普通");
                    finish();
                    break;
                case R.id.fl_click:
                    if (anim != null) {
                        anim.clone();
                    }

                    if (!TextAppearOneControl.getInstance().isFinish()) {
                        TextAppearOneControl.getInstance().setShowAll(true);
                        return;
                    }
                    if (binding.ivKissLove.getVisibility() == View.VISIBLE) {
                        return;
                    }
                    if (contdownLoessNum == 3 && kisslong == 3 && binding.tvCountdown.getVisibility() == View.GONE) {
                        contdownLoessNum = 0;

                        return;
                    }
                    if (contdownLoessNum > 0 && contdownLoessNum < 3 && !bgKissMask && binding.maskKissBg.getVisibility() == View.GONE) {
                        bgKissMask = true;
                        return;
                    }
                    if (binding.tvCountdown != null || binding.tvCountdownTwo != null) {
                        if (binding.tvCountdown.getVisibility() == View.VISIBLE || binding.tvCountdownTwo.getVisibility() == View.VISIBLE) {
                            return;
                        }
                    }
                    if (bgKissMask && binding.maskKissBg.getVisibility() == View.GONE) {
                        bgKissMask = false;
                        binding.maskKissBg.setVisibility(View.VISIBLE);
                        return;
                    }
                    if (binding.maskKissBg.getVisibility() == View.VISIBLE) {
                        if (kisslong >= 3) {
                            finish();
                            return;
                        }
                        kisslong++;
                        binding.maskKissBg.setVisibility(View.GONE);
                        binding.tvCountdown.setVisibility(View.VISIBLE);
                        ObjectAnimator anim = ObjectAnimator.ofFloat(binding.ivCgThree, "alpha", 1f, 0f);
                        anim.setDuration(1000);// 动画持续时间
                        anim.start();

                        ObjectAnimator animator = ObjectAnimator.ofFloat(binding.ivCgFour, "alpha", 0f, 1f);
                        animator.setDuration(1000);
                        animator.start();
                        if (countDownTimer != null) {
                            countDownTimer.start();
                        }
                        binding.tvName.setVisibility(View.GONE);
                        binding.tvText.setVisibility(View.GONE);
                        return;
                    }
                    if (goodValue < 10) {
                        if (binding.viewHair != null && binding.viewHair.getVisibility() == View.VISIBLE) {
                            return;
                        }
                        if (binding.rlKissSelect != null && binding.rlKissSelect.getVisibility() == View.VISIBLE) {
                            return;
                        }
                    } else {
                        goneTouc();
                    }
                    if (binding.ivTouch.getAnimation() != null) {
                        binding.ivTouch.clearAnimation();
                        binding.ivTouch.setVisibility(View.GONE);
                    }
                    if (mCurIndex == 0) {
                        finish();
                        return;
                    }
                    setData();
                    break;
                case R.id.view_hair://头发
                    if (goodValue >= 10) {
                        goneTouc();
                        setData();
                        return;
                    } else {
                        binding.confessionAnimation.setX(binding.viewHair.getX());
                        binding.confessionAnimation.setY(binding.viewHair.getY());
                        hairTouch++;
                        goodValue++;
                    }
                    if (hairTouch == 1) {
                        TextAppearOneControl.getInstance().setTextAndStart("你喜欢蕾姆的头发吗?");
                    } else if (hairTouch == 2) {
                        TextAppearOneControl.getInstance().setTextAndStart("蕾姆喜欢被摸头发……");
                    } else {
                        TextAppearOneControl.getInstance().setTextAndStart("客人你洗过手吗？");
                    }
                    binding.tvText.setVisibility(View.VISIBLE);
                    binding.tvName.setVisibility(View.VISIBLE);
                    binding.tvKissProgressbar.setText(goodValue + "/ 10");
                    binding.progressKiss.setProgress(goodValue * 10);
                    binding.confessionAnimation.setVisibility(View.VISIBLE);
                    binding.ivCgTwo.setImageResource(R.drawable.rem_2);
                    if (anim != null) {
                        anim.clone();
                        anim.start();
                    }

                    break;
                case R.id.view_forehead://额头
                    if (goodValue >= 10) {
                        goneTouc();
                        setData();
                        return;
                    } else {
                        binding.confessionAnimation.setX(binding.viewForehead.getX());
                        binding.confessionAnimation.setY(binding.viewForehead.getY());
                        foreheadTouch++;
                        goodValue++;
                    }
                    if (foreheadTouch == 1) {
                        TextAppearOneControl.getInstance().setTextAndStart("露出额头……会害羞的。");
                    } else if (foreheadTouch == 2) {
                        TextAppearOneControl.getInstance().setTextAndStart("头发挡住了你的体温。");
                    } else {
                        TextAppearOneControl.getInstance().setTextAndStart("蕾姆不想额头露出来。");
                    }
                    binding.tvText.setVisibility(View.VISIBLE);
                    binding.tvName.setVisibility(View.VISIBLE);
                    binding.tvKissProgressbar.setText(goodValue + "/ 10");
                    binding.progressKiss.setProgress(goodValue * 10);
                    binding.confessionAnimation.setVisibility(View.VISIBLE);
                    binding.ivCgTwo.setImageResource(R.drawable.rem_2);
                    if (anim != null) {
                        anim.clone();
                        anim.start();
                    }
                    break;
                case R.id.view_eye_left://眼睛左边
                    if (goodValue >= 10) {
                        goneTouc();
                        setData();
                        return;
                    } else {
                        binding.confessionAnimation.setX(binding.viewEyeLeft.getX());
                        binding.confessionAnimation.setY(binding.llEyeEars.getY());
                        eyeTouch++;
                        goodValue++;
                    }
                    if (eyeTouch == 1) {
                        TextAppearOneControl.getInstance().setTextAndStart("蕾姆会不敢看你的。");
                    } else if (eyeTouch == 2) {
                        TextAppearOneControl.getInstance().setTextAndStart("眼睛现在只看得到你呢。");
                    } else {
                        TextAppearOneControl.getInstance().setTextAndStart("别碰红色的眼睛……");
                    }
                    binding.tvText.setVisibility(View.VISIBLE);
                    binding.tvName.setVisibility(View.VISIBLE);
                    binding.tvKissProgressbar.setText(goodValue + "/ 10");
                    binding.progressKiss.setProgress(goodValue * 10);
                    binding.confessionAnimation.setVisibility(View.VISIBLE);
                    binding.ivCgTwo.setImageResource(R.drawable.rem_2);
                    if (anim != null) {
                        anim.clone();
                        anim.start();
                    }
                    break;
                case R.id.view_eye_right://眼睛右边
                    if (goodValue >= 10) {
                        goneTouc();
                        setData();
                        return;
                    } else {
                        binding.confessionAnimation.setX(binding.viewEyeRight.getX());
                        binding.confessionAnimation.setY(binding.llEyeEars.getY());
                        eyeTouch++;
                        goodValue++;
                    }
                    if (eyeTouch == 1) {
                        TextAppearOneControl.getInstance().setTextAndStart("蕾姆会不敢看你的。");
                    } else if (eyeTouch == 2) {
                        TextAppearOneControl.getInstance().setTextAndStart("眼睛现在只看得到你呢。");
                    } else {
                        TextAppearOneControl.getInstance().setTextAndStart("别碰红色的眼睛……");
                    }
                    binding.tvText.setVisibility(View.VISIBLE);
                    binding.tvName.setVisibility(View.VISIBLE);
                    binding.tvKissProgressbar.setText(goodValue + "/ 10");
                    binding.progressKiss.setProgress(goodValue * 10);
                    binding.confessionAnimation.setVisibility(View.VISIBLE);
                    binding.ivCgTwo.setImageResource(R.drawable.rem_2);
                    if (anim != null) {
                        anim.clone();
                        anim.start();
                    }
                    break;
                case R.id.view_ears_left://耳朵左边
                    if (goodValue >= 10) {
                        goneTouc();
                        setData();
                        return;
                    } else {
                        binding.confessionAnimation.setX(binding.viewEarsLeft.getX());
                        binding.confessionAnimation.setY(binding.llEyeEars.getY());
                        earsTouch++;
                        goodValue++;
                    }
                    if (earsTouch == 1) {
                        TextAppearOneControl.getInstance().setTextAndStart("呀！好痒……");
                    } else if (earsTouch == 2) {
                        TextAppearOneControl.getInstance().setTextAndStart("蕾姆的耳朵很奇怪吗？");
                    } else {
                        TextAppearOneControl.getInstance().setTextAndStart("耳朵怎么了吗？");
                    }
                    binding.tvText.setVisibility(View.VISIBLE);
                    binding.tvName.setVisibility(View.VISIBLE);
                    binding.tvKissProgressbar.setText(goodValue + "/ 10");
                    binding.progressKiss.setProgress(goodValue * 10);
                    binding.confessionAnimation.setVisibility(View.VISIBLE);
                    binding.ivCgTwo.setImageResource(R.drawable.rem_2);
                    if (anim != null) {
                        anim.clone();
                        anim.start();
                    }
                    break;
                case R.id.view_ears_right://耳朵右边
                    if (goodValue >= 10) {
                        goneTouc();
                        setData();
                        return;
                    } else {
                        binding.confessionAnimation.setX(binding.viewEarsRight.getX());
                        binding.confessionAnimation.setY(binding.llEyeEars.getY());
                        earsTouch++;
                        goodValue++;
                    }
                    if (earsTouch == 1) {
                        TextAppearOneControl.getInstance().setTextAndStart("呀！好痒……");
                    } else if (earsTouch == 2) {
                        TextAppearOneControl.getInstance().setTextAndStart("蕾姆的耳朵很奇怪吗？");
                    } else {
                        TextAppearOneControl.getInstance().setTextAndStart("耳朵怎么了吗？");
                    }
                    binding.tvText.setVisibility(View.VISIBLE);
                    binding.tvName.setVisibility(View.VISIBLE);
                    binding.tvKissProgressbar.setText(goodValue + "/ 10");
                    binding.progressKiss.setProgress(goodValue * 10);
                    binding.confessionAnimation.setVisibility(View.VISIBLE);
                    binding.ivCgTwo.setImageResource(R.drawable.rem_2);
                    if (anim != null) {
                        anim.clone();
                        anim.start();
                    }
                    break;
                case R.id.view_nose://鼻子
                    if (goodValue >= 10) {
                        goneTouc();
                        setData();
                        return;
                    } else {
                        binding.confessionAnimation.setY(binding.viewNose.getY());
                        binding.confessionAnimation.setX(binding.viewNose.getX());
                        noseTouch++;
                        goodValue++;
                    }
                    if (noseTouch == 1) {
                        TextAppearOneControl.getInstance().setTextAndStart("你喜欢摸这里吗？");
                    } else if (noseTouch == 2) {
                        TextAppearOneControl.getInstance().setTextAndStart("这里……有些敏感。");
                    } else {
                        TextAppearOneControl.getInstance().setTextAndStart("你想对鼻子做什么？");
                    }
                    binding.tvText.setVisibility(View.VISIBLE);
                    binding.tvName.setVisibility(View.VISIBLE);
                    binding.tvKissProgressbar.setText(goodValue + "/ 10");
                    binding.progressKiss.setProgress(goodValue * 10);
                    binding.confessionAnimation.setVisibility(View.VISIBLE);
                    binding.ivCgTwo.setImageResource(R.drawable.rem_2);
                    if (anim != null) {
                        anim.clone();
                        anim.start();
                    }
                    break;
                case R.id.view_mouth://嘴唇
                    if (goodValue >= 10) {
                        goneTouc();
                        setData();
                        return;
                    } else {
                        binding.confessionAnimation.setX(binding.viewMouth.getX());
                        binding.confessionAnimation.setY(binding.viewMouth.getY());
                        mouthTouch++;
                        goodValue++;
                    }
                    if (mouthTouch == 1) {
                        TextAppearOneControl.getInstance().setTextAndStart("你的手指好热呢。");
                    } else if (mouthTouch == 2) {
                        TextAppearOneControl.getInstance().setTextAndStart("很柔软吗？蕾姆不太懂。");
                    } else {
                        TextAppearOneControl.getInstance().setTextAndStart("不要……讨厌。");
                    }
                    binding.tvText.setVisibility(View.VISIBLE);
                    binding.tvName.setVisibility(View.VISIBLE);
                    binding.tvKissProgressbar.setText(goodValue + "/ 10");
                    binding.progressKiss.setProgress(goodValue * 10);
                    binding.confessionAnimation.setVisibility(View.VISIBLE);
                    binding.ivCgTwo.setImageResource(R.drawable.rem_2);
                    if (anim != null) {
                        anim.clone();
                        anim.start();
                    }
                    break;
                case R.id.view_neck://脖子
                    if (goodValue >= 10) {
                        goneTouc();
                        setData();
                        return;
                    } else {
                        binding.confessionAnimation.setX(binding.viewNeck.getX());
                        binding.confessionAnimation.setY(binding.viewNeck.getY());
                        neckrTouch++;
                        goodValue++;
                    }
                    if (mouthTouch == 1) {
                        TextAppearOneControl.getInstance().setTextAndStart("这里……只可以摸一下。");
                    } else if (mouthTouch == 2) {
                        TextAppearOneControl.getInstance().setTextAndStart("摸脖子……感觉好色情呢。");
                    } else {
                        TextAppearOneControl.getInstance().setTextAndStart("你想对蕾姆做什么？");
                    }
                    binding.tvText.setVisibility(View.VISIBLE);
                    binding.tvName.setVisibility(View.VISIBLE);
                    binding.tvKissProgressbar.setText(goodValue + "/ 10");
                    binding.progressKiss.setProgress(goodValue * 10);
                    binding.confessionAnimation.setVisibility(View.VISIBLE);
                    binding.ivCgTwo.setImageResource(R.drawable.rem_2);
                    if (anim != null) {
                        anim.clone();
                        anim.start();
                    }
                    break;
                case R.id.view_shoulder_left://肩膀左边
                    if (goodValue >= 10) {
                        goneTouc();
                        setData();
                        return;
                    } else {
                        binding.confessionAnimation.setX(binding.viewShoulderLeft.getX());
                        binding.confessionAnimation.setY(binding.llShoulder.getY());
                        shoulderTouch++;
                        goodValue++;
                    }
                    if (shoulderTouch == 1) {
                        TextAppearOneControl.getInstance().setTextAndStart("被你碰到的地方好热……");
                    } else if (shoulderTouch == 2) {
                        TextAppearOneControl.getInstance().setTextAndStart("这是要抱住蕾姆的意思吗？");
                    } else {
                        TextAppearOneControl.getInstance().setTextAndStart("肩膀已经受够了..");
                    }
                    binding.tvText.setVisibility(View.VISIBLE);
                    binding.tvName.setVisibility(View.VISIBLE);
                    binding.tvKissProgressbar.setText(goodValue + "/ 10");
                    binding.progressKiss.setProgress(goodValue * 10);
                    binding.confessionAnimation.setVisibility(View.VISIBLE);
                    binding.ivCgTwo.setImageResource(R.drawable.rem_2);
                    if (anim != null) {
                        anim.clone();
                        anim.start();
                    }
                    break;
                case R.id.view_shoulder_right://肩膀右边
                    if (goodValue >= 10) {
                        goneTouc();
                        setData();
                        return;
                    } else {
                        binding.confessionAnimation.setX(binding.viewShoulderRight.getX());
                        binding.confessionAnimation.setY(binding.llShoulder.getY());
                        shoulderTouch++;
                        goodValue++;
                    }
                    if (shoulderTouch == 1) {
                        TextAppearOneControl.getInstance().setTextAndStart("被你碰到的地方好热……");
                    } else if (shoulderTouch == 2) {
                        TextAppearOneControl.getInstance().setTextAndStart("这是要抱住蕾姆的意思吗？");
                    } else {
                        TextAppearOneControl.getInstance().setTextAndStart("肩膀已经受够了..");
                    }
                    binding.tvText.setVisibility(View.VISIBLE);
                    binding.tvName.setVisibility(View.VISIBLE);
                    binding.tvKissProgressbar.setText(goodValue + "/ 10");
                    binding.progressKiss.setProgress(goodValue * 10);
                    binding.confessionAnimation.setVisibility(View.VISIBLE);
                    binding.ivCgTwo.setImageResource(R.drawable.rem_2);
                    if (anim != null) {
                        anim.clone();
                        anim.start();
                    }
                    break;
                case R.id.view_chest://胸部
                    if (goodValue >= 10) {
                        goneTouc();
                        setData();
                        return;
                    } else {
                        if (goodValue != 0) {
                            goodValue = goodValue - 2;
                        }
                    }
                    if (chestTouch == 1) {
                        TextAppearOneControl.getInstance().setTextAndStart("不可以，蕾姆的这里……");
                    } else if (chestTouch == 2) {
                        TextAppearOneControl.getInstance().setTextAndStart("蕾姆还没有准备好！");
                    } else {
                        TextAppearOneControl.getInstance().setTextAndStart("蕾姆讨厌这样的碰触！");
                    }
                    binding.tvText.setVisibility(View.VISIBLE);
                    binding.tvName.setVisibility(View.VISIBLE);
                    binding.tvKissProgressbar.setText(goodValue + "/ 10");
                    binding.progressKiss.setProgress(goodValue * 10);
                    binding.ivCgTwo.setImageResource(R.drawable.rem_3);
//                    binding.confessionAnimation.setVisibility(View.VISIBLE);
                    if (anim != null) {
                        anim.clone();
                    }
                    break;
            }
        }
    }

    public ArrayList<Map> toJson() {
        ArrayList<Map> arrayList = new Gson().fromJson("[\n" +
                "    {\n" +
                "        \"content\":\"我被蕾姆约到了这里。\",\n" +
                "        \"index\":1,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"content\":\"虽然是件开心的事，但眼下的现状却相当微妙。\",\n" +
                "        \"index\":2,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"我\",\n" +
                "        \"content\":\"「我说……蕾姆？」\",\n" +
                "        \"index\":3,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"蕾姆\",\n" +
                "        \"content\":\"「在，请问有什么需要吗？便当和饮料我都带了哦，还有……」\",\n" +
                "        \"index\":4,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"content\":\"她晃了晃手上的东西，我原本以为是套卷子或作业本，现在才发现，那是一封信。\",\n" +
                "        \"index\":5,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"我\",\n" +
                "        \"content\":\"「你叫我出来是来野餐的吗……而且那是什么？催款单？」\",\n" +
                "        \"index\":6,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"蕾姆\",\n" +
                "        \"content\":\"「你就算在今天，也想被蕾姆吐槽吗？」\",\n" +
                "        \"index\":7,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"我\",\n" +
                "        \"content\":\"「今天？今天是什么日子需要你约我出来？」\",\n" +
                "        \"index\":8,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"蕾姆\",\n" +
                "        \"content\":\"「……蕾姆给你造成困扰了？」\",\n" +
                "        \"index\":9,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"content\":\"她答非所问。\",\n" +
                "        \"index\":10,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"我\",\n" +
                "        \"content\":\"「这倒是没有，反正我也没事……也正好想见你。」\",\n" +
                "        \"index\":11,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"content\":\"因为我的话而泛起红霞的脸颊，在我也许过于直接的视线下越发变深。\",\n" +
                "        \"index\":12,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"我\",\n" +
                "        \"content\":\"「你、你别脸红啊！我没有特别的意思！」\",\n" +
                "        \"index\":13,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"content\":\"我怕被她当做是变态，赶紧解释清楚——当然心里却还是那么想的。\",\n" +
                "        \"index\":14,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"蕾姆\",\n" +
                "        \"content\":\"「……只要你现在叫一声蕾姆，蕾姆就会假装没有听到那句话。」\",\n" +
                "        \"index\":15,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"蕾姆\",\n" +
                "        \"content\":\"「但也只是假装，蕾姆不会忘记那句话的。」\",\n" +
                "        \"index\":16,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"我\",\n" +
                "        \"content\":\"「……」\",\n" +
                "        \"index\":17,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"我\",\n" +
                "        \"content\":\"「你这么对我说话，我会以为你有特别的意思。这没关系吗？」\",\n" +
                "        \"index\":18,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"蕾姆\",\n" +
                "        \"content\":\"「蕾姆……蕾姆就是有特别的意思。」\",\n" +
                "        \"index\":19,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"content\":\"她伸出手，将手里的那封信递来。那上面贴着爱心贴纸，不让人想歪是不可能了。\",\n" +
                "        \"index\":20,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"我\",\n" +
                "        \"content\":\"「蕾姆……」\",\n" +
                "        \"index\":21,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"蕾姆\",\n" +
                "        \"content\":\"「你叫了蕾姆的名字，所以蕾姆会假装没听到那句话。」\",\n" +
                "        \"index\":22,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"蕾姆\",\n" +
                "        \"content\":\"「现在，就由蕾姆从零开始说了。」\",\n" +
                "        \"index\":23,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"content\":\"她露出一抹灿烂的笑容，我不由得感觉到一阵心跳——\",\n" +
                "        \"index\":24,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"蕾姆\",\n" +
                "        \"content\":\"「蕾姆想要你听到蕾姆的每句话，记得蕾姆的每句话。蕾姆想要时时刻刻都在你的身边！」\",\n" +
                "        \"index\":25,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"蕾姆\",\n" +
                "        \"content\":\"「蕾姆爱你，蕾姆的心里现在满满的只有你！」\",\n" +
                "        \"index\":26,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"蕾姆\",\n" +
                "        \"content\":\"「这就是蕾姆特别的意思，你是蕾姆特别的那个人！」\",\n" +
                "        \"index\":27,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"content\":\"仿佛是笑着要哭出来似的，和往常相比弱气不少的声音轻轻说着。\",\n" +
                "        \"index\":28,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"蕾姆\",\n" +
                "        \"content\":\"「只要你现在收下这份契约书，蕾姆就会成为你最特别的人……你可不要轻易下决定哦！」\",\n" +
                "        \"index\":29,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"content\":\"因为我的话而泛起红霞的脸颊，在我也许过于直接的视线下不安地左右摆动。\",\n" +
                "        \"index\":30,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"index\":31,\n" +
                "        \"touch\":false,\n" +
                "        \"options\":[\n" +
                "            {\n" +
                "                \"key\":\"yes\",\n" +
                "                \"value\":\"接受\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"key\":\"no\",\n" +
                "                \"value\":\"拒绝\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"蕾姆\",\n" +
                "        \"content\":\"「蕾姆好高兴……」\",\n" +
                "        \"index\":32,\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"index\":33,\n" +
                "        \"name\":\"蕾姆\",\n" +
                "        \"content\":\"「你一定不知道，蕾姆有多高兴。蕾姆明明在你的面前……好想让你知道。」\",\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"index\":34,\n" +
                "        \"content\":\"她忽然靠近我，扬起了通红的脸。\",\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\":\"蕾姆\",\n" +
                "        \"index\":35,\n" +
                "        \"content\":\"「蕾姆现在就想让你感受……」\",\n" +
                "        \"touch\":false\n" +
                "    },\n" +
                "    {\n" +
                "        \"index\":36,\n" +
                "        \"touch\":true\n" +
                "    },\n" +
                "    {\n" +
                "        \"index\":37,\n" +
                "        \"name\":\"类目\",\n" +
                "        \"touch\":false,\n" +
                "        \"touchPosition\":{\n" +
                "            \"hair\":\"头发\",\n" +
                "            \"forehead\":\"额头\",\n" +
                "            \"eye\":\"眼睛\",\n" +
                "            \"ears\":\"耳朵\",\n" +
                "            \"nose\":\"鼻子\",\n" +
                "            \"mouth\":\"嘴唇\",\n" +
                "            \"neck\":\"脖子\",\n" +
                "            \"chest\":\"胸部\",\n" +
                "            \"shoulder\":\"肩膀\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"index\":38,\n" +
                "        \"name\":\"蕾姆\",\n" +
                "        \"touch\":false,\n" +
                "        \"content\":\"「蕾姆已经准备好了……你可以再靠过来点吗？」\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"index\":39,\n" +
                "        \"name\":\"蕾姆\",\n" +
                "        \"bgKissMask\":true,\n" +
                "        \"touch\":false,\n" +
                "        \"content\":\"「蕾姆想知道更多的你……」\"\n" +
                "    }\n" +
                "]", ArrayList.class);
        return arrayList;
    }
}
