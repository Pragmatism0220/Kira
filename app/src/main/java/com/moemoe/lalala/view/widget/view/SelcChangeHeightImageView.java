package com.moemoe.lalala.view.widget.view;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;

/**
 * Created by Administrator on 2018/5/17.
 */

@SuppressLint("AppCompatCustomView")
public class SelcChangeHeightImageView extends ImageView {
    /**
     * 选中和未选中时的高度最大最小值
     */
    private int maxHeight = 37;
    private int minHeight = 34;

    private Context mContext;

    public SelcChangeHeightImageView(Context context) {
        super(context);
        initView(context);
    }

    public SelcChangeHeightImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SelcChangeHeightImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        mContext = context;
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            getLayoutParams().height = formatDipToPx(mContext, maxHeight);
        } else {
            getLayoutParams().height = formatDipToPx(mContext,minHeight);
        }
    }

    /**
     * 把dip单位转成px单位
     *
     * @param context
     *            context对象
     * @param dip
     *            dip数值
     * @return
     */
    public static int formatDipToPx(Context context, int dip) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        return (int) Math.ceil(dip * dm.density);
    }

}
