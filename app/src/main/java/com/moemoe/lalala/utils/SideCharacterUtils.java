package com.moemoe.lalala.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.greendao.gen.DeskmateEntilsDao;
import com.moemoe.lalala.model.entity.DeskmateEntils;

import java.util.ArrayList;

/**
 * 扒边小人悬浮框
 * Created by Hygge on 2018/7/9.
 */

public class SideCharacterUtils {


    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams wmParams;
    private int width;
    private int height;
    private DeskmateEntils entilsTop;
    private DeskmateEntils entilsBottom;
    private DeskmateEntils entilsLeft;
    private DeskmateEntils entilsRight;
    private DeskmateEntils entilsDrag;
    private View inflate;
    private ImageView imageView;
    private int functionalX;
    private int functionalY;
    private static SideCharacterUtils mInstance;

    public static SideCharacterUtils getInstance() {
        if (mInstance == null) {
            mInstance = new SideCharacterUtils();
        }
        return mInstance;
    }

    public void initSideCharacter(Context context) {
        mContext = context.getApplicationContext();
        // 获取WindowManager
        mWindowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        width = mWindowManager.getDefaultDisplay().getWidth();
        height = mContext.getResources().getDisplayMetrics().heightPixels;
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置图片格式，效果为背景透明  
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;

        final DeskmateEntils entity = entilsRight;
//        wmParams.x = width - (int) getResources().getDimension(R.dimen.x280);
//        wmParams.y = height / 2 - (int) getResources().getDimension(R.dimen.x180);
        wmParams.x = width - (int) mContext.getResources().getDimension(R.dimen.x280);
        wmParams.y = height / 2 - (int) mContext.getResources().getDimension(R.dimen.x180);
        inflate = LayoutInflater.from(mContext).inflate(R.layout.ac_window, null);
        imageView = inflate.findViewById(R.id.imageView);
        final Drawable drawable;
        if (!TextUtils.isEmpty(entity.getPath())) {
            drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entity.getFileName());
        } else {
            drawable = ContextCompat.getDrawable(mContext, R.drawable.btn_classmate_len_right);
        }
        if (drawable != null) {
            imageView.setImageDrawable(drawable);
        } else {
            FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entity.getFileName());
        }
        functionalX = width - (int) mContext.getResources().getDimension(R.dimen.x280);
        functionalY = height / 2 - (int) mContext.getResources().getDimension(R.dimen.x180);
        mWindowManager.addView(inflate, wmParams);
    }

    /**
     * 数据压制
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
