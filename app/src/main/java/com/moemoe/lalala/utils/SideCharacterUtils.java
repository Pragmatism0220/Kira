package com.moemoe.lalala.utils;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.moemoe.lalala.R;
import com.moemoe.lalala.greendao.gen.DeskmateEntilsDao;
import com.moemoe.lalala.model.entity.DeskmateEntils;
import com.moemoe.lalala.view.activity.FeedV3Activity;
import com.moemoe.lalala.view.activity.HouseActivity;
import com.moemoe.lalala.view.activity.MapActivity;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * 扒边小人悬浮框
 * Created by Hygge on 2018/7/9.
 */

@SuppressLint("AppCompatCustomView")
public class SideCharacterUtils {

//
//    private Context mContext;
//    private WindowManager mWindowManager;
//    private WindowManager.LayoutParams wmParams;
//    private int width;
//    private int height;
//    private DeskmateEntils entilsTop;
//    private DeskmateEntils entilsBottom;
//    private DeskmateEntils entilsLeft;
//    private DeskmateEntils entilsRight;
//    private DeskmateEntils entilsDrag;
//    private View inflate;
//    private ImageView imageView;
//    private int functionalX;
//    private int functionalY;
//    private static SideCharacterUtils mInstance;
//
//    public static SideCharacterUtils getInstance() {
//        if (mInstance == null) {
//            mInstance = new SideCharacterUtils();
//        }
//        return mInstance;
//    }
//
//    public void initSideCharacter(Context context) {
//        mContext = context.getApplicationContext();
//        // 获取WindowManager
//        mWindowManager = (WindowManager) mContext
//                .getSystemService(Context.WINDOW_SERVICE);
//        wmParams = new WindowManager.LayoutParams();
//        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//        width = mWindowManager.getDefaultDisplay().getWidth();
//        height = mContext.getResources().getDisplayMetrics().heightPixels;
//        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        //设置图片格式，效果为背景透明  
//        wmParams.format = PixelFormat.RGBA_8888;
//        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
//
//        final DeskmateEntils entity = entilsRight;
////        wmParams.x = width - (int) getResources().getDimension(R.dimen.x280);
////        wmParams.y = height / 2 - (int) getResources().getDimension(R.dimen.x180);
//        wmParams.x = width - (int) mContext.getResources().getDimension(R.dimen.x280);
//        wmParams.y = height / 2 - (int) mContext.getResources().getDimension(R.dimen.x180);
//        inflate = LayoutInflater.from(mContext).inflate(R.layout.ac_window, null);
//        imageView = inflate.findViewById(R.id.imageView);
//        final Drawable drawable;
//        if (!TextUtils.isEmpty(entity.getPath())) {
//            drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entity.getFileName());
//        } else {
//            drawable = ContextCompat.getDrawable(mContext, R.drawable.btn_classmate_len_right);
//        }
//        if (drawable != null) {
//            imageView.setImageDrawable(drawable);
//        } else {
//            FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entity.getFileName());
//        }
//        functionalX = width - (int) mContext.getResources().getDimension(R.dimen.x280);
//        functionalY = height / 2 - (int) mContext.getResources().getDimension(R.dimen.x180);
//        mWindowManager.addView(inflate, wmParams);
//    }
//
//    /**
//     * 数据压制
//     */
//    public int goGreenDao() {
//        ArrayList<DeskmateEntils> entilsDao = (ArrayList<DeskmateEntils>) GreenDaoManager.getInstance().getSession().getDeskmateEntilsDao()
//                .queryBuilder()
//                .where(DeskmateEntilsDao.Properties.Type.eq("HousUser"))
//                .list();
//        if (entilsDao != null && entilsDao.size() > 0) {
//            for (DeskmateEntils entils : entilsDao) {
//                if (entils.getRemark().equals("top")) {
//                    entilsTop = entils;
//                } else if (entils.getRemark().equals("bottom")) {
//                    entilsBottom = entils;
//                } else if (entils.getRemark().equals("left")) {
//                    entilsLeft = entils;
//                } else if (entils.getRemark().equals("right")) {
//                    entilsRight = entils;
//                } else if (entils.getRemark().equals("drag")) {
//                    entilsDrag = entils;
//                }
//            }
//        }
//        return entilsDao.size() > 0 ? entilsDao.size() : 0;
//    }
//
//    public void initDialog(Context context) {
//        View inflate = LayoutInflater.from(mContext).inflate(R.layout.side_character, null);
//        ImageView viewById = (ImageView) inflate.findViewById(R.id.iv_cover);
//    }


//    private int parentHeight;
//    private int parentWidth;
//
//    public SideCharacterUtils(Context context) {
//        super(context);
//    }
//
//    public SideCharacterUtils(Context context, AttributeSet attrs) {
//        super(context, attrs);
//
//    }
//
//    public SideCharacterUtils(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//
//    }
//
//
//    private int lastX;
//    private int lastY;
//
//    private boolean isDrag;
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int rawX = (int) event.getRawX();
//        int rawY = (int) event.getRawY();
//        switch (event.getAction() & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN:
//                setPressed(true);
//                isDrag = false;
//                getParent().requestDisallowInterceptTouchEvent(true);
//                lastX = rawX;
//                lastY = rawY;
//                ViewGroup parent;
//                if (getParent() != null) {
//                    parent = (ViewGroup) getParent();
//                    parentHeight = parent.getHeight();
//                    parentWidth = parent.getWidth();
//                }
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (parentHeight <= 0 || parentWidth == 0) {
//                    isDrag = false;
//                    break;
//                } else {
//                    isDrag = true;
//                }
//                int dx = rawX - lastX;
//                int dy = rawY - lastY;
//                //这里修复一些华为手机无法触发点击事件
//                int distance = (int) Math.sqrt(dx * dx + dy * dy);
//                if (distance == 0) {
//                    isDrag = false;
//                    break;
//                }
//                float x = getX() + dx;
//                float y = getY() + dy;
//                //检测是否到达边缘 左上右下
//                x = x < 0 ? 0 : x > parentWidth - getWidth() ? parentWidth - getWidth() : x;
//                y = getY() < 0 ? 0 : getY() + getHeight() > parentHeight ? parentHeight - getHeight() : y;
//                setX(x);
//                setY(y);
//                lastX = rawX;
//                lastY = rawY;
//                Log.i("aa", "isDrag=" + isDrag + "getX=" + getX() + ";getY=" + getY() + ";parentWidth=" + parentWidth);
//                break;
//            case MotionEvent.ACTION_UP:
//                if (!isNotDrag()) {
//                    //恢复按压效果
//                    setPressed(false);
//                    //Log.i("getX="+getX()+"；screenWidthHalf="+screenWidthHalf);
//                    if (rawX >= parentWidth / 2) {
//                        //靠右吸附
//                        animate().setInterpolator(new DecelerateInterpolator())
//                                .setDuration(500)
//                                .xBy(parentWidth - getWidth() - getX())
//                                .start();
//                    } else {
//                        //靠左吸附
//                        ObjectAnimator oa = ObjectAnimator.ofFloat(this, "x", getX(), 0);
//                        oa.setInterpolator(new DecelerateInterpolator());
//                        oa.setDuration(500);
//                        oa.start();
//                    }
//                }
//                break;
//        }
//        //如果是拖拽则消s耗事件，否则正常传递即可。
//        return !isNotDrag() || super.onTouchEvent(event);
//    }
//
//    private boolean isNotDrag() {
//        return !isDrag && (getX() == 0
//                || (getX() == parentWidth - getWidth()));
//    }

    private Activity context; // 上下文
    private ImageView mImageView; // 可拖动按钮
    private static int mScreenWidth = -1; //屏幕的宽度
    private static int mScreenHeight = -1; //屏幕的高度
    private int relativeMoveX; // 控件相对屏幕左上角移动的位置X
    private int relativeMoveY; // 控件相对屏幕左上角移动的位置Y
    private boolean isIntercept = false; // 是否截断touch事件
    private int startDownX; // 按下时的位置控件相对屏幕左上角的位置X
    private int startDownY; // 按下时的位置控件距离屏幕左上角的位置Y
    private static int[] lastPosition; // 用于记录上一次的位置(坐标0对应x,坐标1对应y)
    private DeskmateEntils entilsTop;
    private DeskmateEntils entilsBottom;
    private DeskmateEntils entilsLeft;
    private DeskmateEntils entilsRight;
    private DeskmateEntils entilsDrag;


    /**
     * @param context        上下文
     * @param mViewContainer 可拖动按钮要存放的对应的Layout
     * @param clickListener  可拖动按钮的点击事件
     */
    public static ImageView addFloatDragView(Activity context, RelativeLayout mViewContainer,
                                             View.OnClickListener clickListener) {

        SideCharacterUtils floatDragView = new SideCharacterUtils(context);
        floatDragView.goGreenDao();
        ImageView imageView = floatDragView.getFloatDragView(clickListener);
        mViewContainer.addView(imageView);
        return imageView;
    }

    // 初始化实例
    private SideCharacterUtils(Activity context) {
        setScreenHW(context);
        this.context = context;
        lastPosition = new int[]{0, 0};
    }

    public double getWidth(int width) {
        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        double v = 750.0 * heightPixels / 1334.0;
        return width * v / 750.0;
    }

    public double getHieght(int height) {
        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        return height * heightPixels / 1334.0;
    }

    // 获取可拖动按钮的实例
    private ImageView getFloatDragView(View.OnClickListener clickListener) {
        if (mImageView != null) {
            return mImageView;
        } else {
            mImageView = new ImageView(context);
            mImageView.setClickable(true);
            mImageView.setFocusable(true);
            final Drawable drawable;
            if (entilsRight != null && !TextUtils.isEmpty(entilsRight.getPath())) {
                drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entilsRight.getFileName());
//                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) getWidth(entilsRight.getW()), (int) getHieght(entilsRight.getH()));

            } else {
                drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_right);
            }
            if (drawable != null) {
                mImageView.setImageDrawable(drawable);
            } else {
                FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entilsRight.getFileName());
            }
//            mImageView.setImageResource(R.drawable.btn_camera_shot_normal);
            setFloatDragViewParams(mImageView);
            mImageView.setOnClickListener(clickListener);
            setFloatDragViewTouch(mImageView);
            return mImageView;
        }

    }

    // 设置可拖动按钮的位置参数
    private void setFloatDragViewParams(View floatDragView) {

        // 记录最后图片在窗体的位置
        int moveX = lastPosition[0];
        int moveY = lastPosition[1];
        if (0 != moveX || 0 != moveY) {// 移动后的位置
            // 每次移动都要设置其layout，不然由于父布局可能嵌套listView，当父布局发生改变冲毁（如下拉刷新时）则移动的view会回到原来的位置
            RelativeLayout.LayoutParams lpFeedback = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lpFeedback.setMargins(moveX, (int) (moveY + context.getResources().getDimension(R.dimen.status_bar_height)), 0, 0);
            floatDragView.setLayoutParams(lpFeedback);
        } else {// 初始位置
            RelativeLayout.LayoutParams lpFeedback = null;
            if (entilsRight == null) {
                lpFeedback = new RelativeLayout.LayoutParams(
                        (int) context.getResources().getDimension(R.dimen.x400), (int) context.getResources().getDimension(R.dimen.y600));
            } else {
//            RelativeLayout.LayoutParams lpFeedback = new RelativeLayout.LayoutParams(
//                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lpFeedback = new RelativeLayout.LayoutParams((int) getWidth(entilsRight.getW() / 2), (int) getHieght(entilsRight.getH() / 2));
            }
            lpFeedback.setMargins(0, 0, 0, 0);
            lpFeedback.addRule(RelativeLayout.CENTER_VERTICAL);
            lpFeedback.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            floatDragView.setLayoutParams(lpFeedback);
        }

    }

    // 可拖动按钮的touch事件
    @SuppressLint("ClickableViewAccessibility")
    private void setFloatDragViewTouch(final ImageView floatDragView) {

        floatDragView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                goGreenDao();
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        isIntercept = false;
                        startDownX = relativeMoveX = (int) event.getRawX();
                        startDownY = relativeMoveY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (context instanceof HouseActivity) {
                            ((HouseActivity) context).GoneSidaMenuOrLine();
                        } else if (context instanceof FeedV3Activity) {
                            ((FeedV3Activity) context).GoneSidaMenuOrLine();
                        } else if (context instanceof MapActivity) {
                            ((MapActivity) context).GoneSidaMenuOrLine();
                        }
                        int dx = (int) event.getRawX() - relativeMoveX;
                        int dy = (int) event.getRawY() - relativeMoveY;
                        int x = Math.abs((int) event.getRawX() - relativeMoveX);
                        int y = Math.abs((int) event.getRawY() - relativeMoveY);
                        if (x < 5 || y < 5) {
                            //为移动  点击事件
                        } else {
                            final Drawable drawable;
                            if (entilsDrag != null && !TextUtils.isEmpty(entilsDrag.getPath())) {
                                drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entilsDrag.getFileName());
//                            RelativeLayout.LayoutParams lpFeedback = new RelativeLayout.LayoutParams((int) getWidth(entilsDrag.getW() / 2), (int) getHieght(entilsDrag.getH() / 2));
//                            v.setLayoutParams(lpFeedback);
                            } else {
                                drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_drop);
                            }
                            if (drawable != null) {
                                mImageView.setImageDrawable(drawable);
                            } else {
                                FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entilsDrag.getFileName());
                            }
                        }
                        int left = v.getLeft() + dx;
                        int top = v.getTop() + dy;
                        int right = v.getRight() + dx;
                        int bottom = v.getBottom() + dy;
                        if (left < 0) {
                            left = 0;
                            right = left + v.getWidth();
                        }
                        if (right > mScreenWidth) {
                            right = mScreenWidth;
                            left = right - v.getWidth();
                        }
                        if (top < 0) {
                            top = 0;
                            bottom = top + v.getHeight();
                        }
                        if (bottom > mScreenHeight) {
                            bottom = mScreenHeight;
                            top = bottom - v.getHeight();
                        }
                        v.layout(left, top, right, bottom);
                        relativeMoveX = (int) event.getRawX();
                        relativeMoveY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        int lastMoveDx = Math.abs((int) event.getRawX() - startDownX);
                        int lastMoveDy = Math.abs((int) event.getRawY() - startDownY);

                        if (5 < lastMoveDx || 5 < lastMoveDy) {// 防止点击的时候稍微有点移动点击事件被拦截了
                            isIntercept = true;
                        } else {
                            isIntercept = false;
                        }
                        // 每次移动都要设置其layout，不然由于父布局可能嵌套listview，
                        // 当父布局发生改变冲毁（如下拉刷新时）则移动的view会回到原来的位置
                        RelativeLayout.LayoutParams lpFeedback = null;
                        if (entilsDrag == null || TextUtils.isEmpty(entilsDrag.getFileName())) {
                            lpFeedback = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        } else {
                            lpFeedback = new RelativeLayout.LayoutParams((int) getWidth(entilsDrag.getW() / 2),
                                    (int) getHieght(entilsDrag.getH() / 2));
//                            lpFeedback = new RelativeLayout.LayoutParams(
//                                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        }

                        lpFeedback.setMargins(v.getLeft(), v.getTop(), 0, 0);

                        v.setLayoutParams(lpFeedback);
                        // preferenceUtil.saveInt("moveX", v.getLeft());
                        // preferenceUtil.saveInt("moveY", v.getTop());
                        // 设置靠近边沿的
                        if (isIntercept) {
                            setImageViewNearEdge(v);
                        }
                        break;
                }
                return isIntercept;
            }
        });
    }

    public int getLift() {
        return mImageView.getLeft();
    }

    public int getRight() {
        return mImageView.getRight();
    }

    public int getTop() {
        return mImageView.getTop();
    }

    public int getBottom() {
        return mImageView.getBottom();
    }

    // 将拖动按钮移动到边沿
    private void setImageViewNearEdge(final View v) {

        if (v.getLeft() < ((NewUtils.getScreenSize(context).x) / 2)) {

            if (v.getTop() > ((NewUtils.getScreenSize(context).y) / 2)) {//左边下半部
                Log.e("getBottom", "-----" + context.getResources().getDisplayMetrics().heightPixels + "--");
                if (v.getLeft() < (NewUtils.getScreenSize(context).y - (v.getTop() + v.getHeight()))) {//更靠左边   left=0;
                    // 设置位移动画 向左移动控件位置
                    final TranslateAnimation animation = new TranslateAnimation(0, -v.getLeft(), 0, 0);
                    animation.setDuration(300);// 设置动画持续时间
                    animation.setRepeatCount(0);// 设置重复次数
                    animation.setFillAfter(true);
                    animation.setRepeatMode(Animation.ABSOLUTE);

                    animation.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation arg0) {
                            // TODO: 2017/3/1  
                        }

                        @Override
                        public void onAnimationRepeat(Animation arg0) {
                            // TODO: 2017/3/1
                        }

                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            v.clearAnimation();
                            final Drawable drawable;
                            RelativeLayout.LayoutParams lpFeedback = null;
                            if (entilsLeft != null && !TextUtils.isEmpty(entilsLeft.getPath())) {
                                drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entilsLeft.getFileName());
                                lpFeedback = new RelativeLayout.LayoutParams((int) getWidth(entilsLeft.getW() / 2),
                                        (int) getHieght(entilsLeft.getH() / 2));
                            } else {
                                drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_left);
                                lpFeedback = new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            }
                            lpFeedback.setMargins(0, v.getTop(), 0, 0);
                            v.setLayoutParams(lpFeedback);
                            if (drawable != null) {
                                mImageView.setImageDrawable(drawable);
                            } else {
                                FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entilsLeft.getFileName());
                            }
                            v.postInvalidateOnAnimation();
                            lastPosition[0] = 0;
                            lastPosition[1] = v.getTop();
                        }
                    });
                    v.startAnimation(animation);
                } else {//更靠下边  bottom=0;
                    // 设置位移动画 向左移动控件位置
                    final TranslateAnimation animation = new TranslateAnimation(0, 0, 0, (NewUtils.getScreenSize(context).y - v.getTop()));
                    animation.setDuration(300);// 设置动画持续时间
                    animation.setRepeatCount(0);// 设置重复次数
                    animation.setFillAfter(true);
                    animation.setRepeatMode(Animation.ABSOLUTE);

                    animation.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation arg0) {
                            // TODO: 2017/3/1  
                        }

                        @Override
                        public void onAnimationRepeat(Animation arg0) {
                            // TODO: 2017/3/1
                        }

                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            v.clearAnimation();
                            final Drawable drawable;
                            RelativeLayout.LayoutParams lpFeedback = null;
                            if (entilsBottom != null && !TextUtils.isEmpty(entilsBottom.getPath())) {
                                drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entilsBottom.getFileName());
                                lpFeedback = new RelativeLayout.LayoutParams((int) getWidth(entilsLeft.getW() / 2),
                                        (int) getHieght(entilsBottom.getH() / 2));
                            } else {
                                drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_down);
                                lpFeedback = new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            }
                            lpFeedback.setMargins(v.getLeft(), NewUtils.getScreenSize(context).y - v.getHeight(), 0, 0);
                            v.setLayoutParams(lpFeedback);
                            if (drawable != null) {
                                mImageView.setImageDrawable(drawable);
                            } else {
                                FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entilsBottom.getFileName());
                            }
                            v.postInvalidateOnAnimation();
                            lastPosition[0] = 0;
                            lastPosition[1] = v.getTop();
                        }
                    });
                    v.startAnimation(animation);
                }
            } else {//左边上半部
                if (v.getLeft() < v.getTop()) {//靠左边 left=0;
                    // 设置位移动画 向左移动控件位置
                    final TranslateAnimation animation = new TranslateAnimation(0, -v.getLeft(), 0, 0);
                    animation.setDuration(300);// 设置动画持续时间
                    animation.setRepeatCount(0);// 设置重复次数
                    animation.setFillAfter(true);
                    animation.setRepeatMode(Animation.ABSOLUTE);

                    animation.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation arg0) {
                            // TODO: 2017/3/1  
                        }

                        @Override
                        public void onAnimationRepeat(Animation arg0) {
                            // TODO: 2017/3/1
                        }

                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            v.clearAnimation();
                            final Drawable drawable;
                            RelativeLayout.LayoutParams lpFeedback = null;
                            if (entilsLeft != null && !TextUtils.isEmpty(entilsLeft.getPath())) {
                                drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entilsLeft.getFileName());
                                lpFeedback = new RelativeLayout.LayoutParams((int) getWidth(entilsLeft.getW() / 2),
                                        (int) getHieght(entilsLeft.getH() / 2));
                            } else {
                                drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_left);
                                lpFeedback = new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            }
                            lpFeedback.setMargins(0, v.getTop(), 0, 0);
                            v.setLayoutParams(lpFeedback);
                            if (drawable != null) {
                                mImageView.setImageDrawable(drawable);
                            } else {
                                FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entilsLeft.getFileName());
                            }
                            v.postInvalidateOnAnimation();
                            lastPosition[0] = 0;
                            lastPosition[1] = v.getTop();
                        }
                    });
                    v.startAnimation(animation);
                } else {//靠上面 top=0;
                    // 设置位移动画 向左移动控件位置
                    final TranslateAnimation animation = new TranslateAnimation(0, 0, 0,
                            -(v.getTop() - context.getResources().getDimension(R.dimen.status_bar_height)));
                    animation.setDuration(300);// 设置动画持续时间
                    animation.setRepeatCount(0);// 设置重复次数
                    animation.setFillAfter(true);
                    animation.setRepeatMode(Animation.ABSOLUTE);

                    animation.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation arg0) {
                            // TODO: 2017/3/1  
                        }

                        @Override
                        public void onAnimationRepeat(Animation arg0) {
                            // TODO: 2017/3/1
                        }

                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            v.clearAnimation();
                            final Drawable drawable;
                            RelativeLayout.LayoutParams lpFeedback = null;
                            if (entilsTop != null && !TextUtils.isEmpty(entilsTop.getPath())) {
                                drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entilsTop.getFileName());
                                lpFeedback = new RelativeLayout.LayoutParams((int) getWidth(entilsTop.getW() / 2),
                                        (int) getHieght(entilsTop.getH() / 2));
                            } else {
                                drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_top);
                                lpFeedback = new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            }
//                            lpFeedback.setMargins(0, v.getTop(), 0, 0);
                            lpFeedback.setMargins(v.getLeft(), (int) context.getResources().getDimension(R.dimen.status_bar_height), 0, 0);
                            v.setLayoutParams(lpFeedback);
                            if (drawable != null) {
                                mImageView.setImageDrawable(drawable);
                            } else {
                                FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entilsTop.getFileName());
                            }
                            v.postInvalidateOnAnimation();
                            lastPosition[0] = 0;
                            lastPosition[1] = v.getTop();
                        }
                    });
                    v.startAnimation(animation);
                }
            }
        } else {
            if (v.getTop() > ((NewUtils.getScreenSize(context).y) / 2)) {//右边下半部
                if ((NewUtils.getScreenSize(context).x - v.getLeft() - v.getWidth()) < (NewUtils.getScreenSize(context).y - v.getTop() - v.getHeight())) {//靠右 left=w；
                    final TranslateAnimation animation = new TranslateAnimation(0, (NewUtils.getScreenSize(context).x
                            - v.getLeft() - v.getWidth()), 0, 0);
                    animation.setDuration(300);// 设置动画持续时间
                    animation.setRepeatCount(0);// 设置重复次数
                    animation.setRepeatMode(Animation.ABSOLUTE);
                    animation.setFillAfter(true);
                    animation.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation arg0) {
                            // TODO: 2017/3/1
                        }

                        @Override
                        public void onAnimationRepeat(Animation arg0) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            v.clearAnimation();
                            RelativeLayout.LayoutParams lpFeedback = null;
                            final Drawable drawable;
                            if (entilsRight != null && !TextUtils.isEmpty(entilsRight.getPath())) {
                                drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entilsRight.getFileName());
                                lpFeedback = new RelativeLayout.LayoutParams((int) getWidth(entilsRight.getW() / 2),
                                        (int) getHieght(entilsRight.getH() / 2));
                            } else {
                                drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_right);
                                lpFeedback = new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            }
                            lpFeedback.setMargins(NewUtils.getScreenSize(context).x - v.getWidth(), v.getTop(), 0, 0);
                            v.setLayoutParams(lpFeedback);
                            if (drawable != null) {
                                mImageView.setImageDrawable(drawable);
                            } else {
                                FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entilsRight.getFileName());
                            }

                            v.postInvalidateOnAnimation();
                            lastPosition[0] = NewUtils.getScreenSize(context).x - v.getWidth();
                            lastPosition[1] = v.getTop();
                        }
                    });
                    v.startAnimation(animation);
                } else {//靠下bottom=0;
                    final TranslateAnimation animation = new TranslateAnimation(0, 0, 0,
                            (NewUtils.getScreenSize(context).y - v.getTop()));
                    animation.setDuration(300);// 设置动画持续时间
                    animation.setRepeatCount(0);// 设置重复次数
                    animation.setRepeatMode(Animation.ABSOLUTE);
                    animation.setFillAfter(true);
                    animation.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation arg0) {
                            // TODO: 2017/3/1
                        }

                        @Override
                        public void onAnimationRepeat(Animation arg0) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            v.clearAnimation();
                            RelativeLayout.LayoutParams lpFeedback = null;
                            final Drawable drawable;
                            if (entilsBottom != null && !TextUtils.isEmpty(entilsBottom.getPath())) {
                                drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entilsBottom.getFileName());
                                lpFeedback = new RelativeLayout.LayoutParams((int) getWidth(entilsBottom.getW() / 2),
                                        (int) getHieght(entilsBottom.getH() / 2));
                            } else {
                                drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_down);
                                lpFeedback = new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            }
                            lpFeedback.setMargins(NewUtils.getScreenSize(context).x - v.getWidth(), v.getTop(), 0, 0);
                            v.setLayoutParams(lpFeedback);
                            if (drawable != null) {
                                mImageView.setImageDrawable(drawable);
                            } else {
                                FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entilsBottom.getFileName());
                            }

                            v.postInvalidateOnAnimation();
                            lastPosition[0] = NewUtils.getScreenSize(context).x - v.getWidth();
                            lastPosition[1] = v.getTop();
                        }
                    });
                    v.startAnimation(animation);
                }
            } else {//右边上半部
                if ((NewUtils.getScreenSize(context).x - v.getLeft() - v.getWidth()) < v.getTop()) {//靠右 right=0;
                    final TranslateAnimation animation = new TranslateAnimation(0, (NewUtils.getScreenSize(context).x
                            - v.getLeft() - v.getWidth()), 0, 0);
                    animation.setDuration(300);// 设置动画持续时间
                    animation.setRepeatCount(0);// 设置重复次数
                    animation.setRepeatMode(Animation.ABSOLUTE);
                    animation.setFillAfter(true);
                    animation.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation arg0) {
                            // TODO: 2017/3/1
                        }

                        @Override
                        public void onAnimationRepeat(Animation arg0) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            v.clearAnimation();
                            RelativeLayout.LayoutParams lpFeedback = null;
                            final Drawable drawable;
                            if (entilsRight != null && !TextUtils.isEmpty(entilsRight.getPath())) {
                                drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entilsRight.getFileName());
                                lpFeedback = new RelativeLayout.LayoutParams((int) getWidth(entilsRight.getW() / 2),
                                        (int) getHieght(entilsRight.getH() / 2));
                            } else {
                                drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_right);
                                lpFeedback = new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            }
                            lpFeedback.setMargins(NewUtils.getScreenSize(context).x - v.getWidth(), v.getTop(), 0, 0);
                            v.setLayoutParams(lpFeedback);
                            if (drawable != null) {
                                mImageView.setImageDrawable(drawable);
                            } else {
                                FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entilsRight.getFileName());
                            }

                            v.postInvalidateOnAnimation();
                            lastPosition[0] = NewUtils.getScreenSize(context).x - v.getWidth();
                            lastPosition[1] = v.getTop();
                        }
                    });
                    v.startAnimation(animation);
                } else {//靠上 top= 0;
                    final TranslateAnimation animation = new TranslateAnimation(0, 0, 0,
                            -(v.getTop() - context.getResources().getDimension(R.dimen.status_bar_height)));
                    animation.setDuration(300);// 设置动画持续时间
                    animation.setRepeatCount(0);// 设置重复次数
                    animation.setRepeatMode(Animation.ABSOLUTE);
                    animation.setFillAfter(true);
                    animation.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation arg0) {
                            // TODO: 2017/3/1
                        }

                        @Override
                        public void onAnimationRepeat(Animation arg0) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            v.clearAnimation();
                            RelativeLayout.LayoutParams lpFeedback = null;
                            final Drawable drawable;
                            if (entilsTop != null && !TextUtils.isEmpty(entilsTop.getPath())) {
                                drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entilsTop.getFileName());
                                lpFeedback = new RelativeLayout.LayoutParams((int) getWidth(entilsTop.getW() / 2),
                                        (int) getHieght(entilsTop.getH() / 2));
                            } else {
                                drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_top);
                                lpFeedback = new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            }
                            lpFeedback.setMargins(v.getLeft(), (int) context.getResources().getDimension(R.dimen.status_bar_height), 0, 0);
                            v.setLayoutParams(lpFeedback);
                            if (drawable != null) {
                                mImageView.setImageDrawable(drawable);
                            } else {
                                FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entilsTop.getFileName());
                            }

                            v.postInvalidateOnAnimation();
                            lastPosition[0] = NewUtils.getScreenSize(context).x - v.getWidth();
                            lastPosition[1] = v.getTop();
                        }
                    });
                    v.startAnimation(animation);
                }
            }

        }
    }

    // 计算屏幕的实际高宽
    private void setScreenHW(Activity context) {
        if (mScreenHeight < 0) {
            // 减去状态栏高度，否则挨着底部移动，导致图标变小
            Point screen = NewUtils.getScreenSize(context);
            mScreenWidth = screen.x;
//            mScreenHeight = screen.y - NewUtils.getStatusBarHeight(context);
            mScreenHeight = screen.y;
        }
    }

    /**
     * //     * 数据压制
     * //
     */
    public int goGreenDao() {
        ArrayList<DeskmateEntils> entilsDao = (ArrayList<DeskmateEntils>) GreenDaoManager.getInstance().getSession().getDeskmateEntilsDao()
                .queryBuilder()
                .where(DeskmateEntilsDao.Properties.Type.eq("HousUser"))
                .list();
        if (entilsDao != null && entilsDao.size() > 0) {
            for (DeskmateEntils entils : entilsDao) {
                if (entils.getRemark().equals("top")) {
                    entilsTop = entils;
                } else if (entils.getRemark().equals("bottom")) {
                    entilsBottom = entils;
                } else if (entils.getRemark().equals("left")) {
                    entilsLeft = entils;
                } else if (entils.getRemark().equals("right")) {
                    entilsRight = entils;
                } else if (entils.getRemark().equals("drag")) {
                    entilsDrag = entils;
                }
            }
        }
        return entilsDao.size() > 0 ? entilsDao.size() : 0;
    }

}
