package com.moemoe.lalala.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moemoe.lalala.R;
import com.moemoe.lalala.view.base.EveryDayBean;
import com.moemoe.lalala.view.base.PrincipalListBean;
import com.moemoe.lalala.view.widget.view.ImageViewWithNeedle;

import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 */

public class ActivityEveryDayInfoAdapter extends RecyclerView.Adapter<ActivityEveryDayInfoAdapter.ActivityEveryDayInfoViewHolder> {

    private Context mContext;
    private List<PrincipalListBean> mData;

    public ActivityEveryDayInfoAdapter(Context mContext, List<PrincipalListBean> lists) {
        this.mContext = mContext;
        this.mData = lists;
    }

    @Override
    public ActivityEveryDayInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ActivityEveryDayInfoViewHolder holder = new ActivityEveryDayInfoViewHolder(View.inflate(mContext, R.layout.item_principal_list, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(ActivityEveryDayInfoViewHolder holder, int position) {
        PrincipalListBean data = mData.get(position);
        holder.mNumberImage.setEnabled(!data.isLock());
        holder.mPlayImage.setEnabled(!data.isLock());
        holder.mTitleText.setText(data.getTitle());
        holder.mNumberText.setText(data.getNumber());

        if (data.getExtraImages() != null && data.getExtraImages().size() > 0) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mBackImage.getLayoutParams();
            params.height = formatDipToPx(mContext, 95);
            holder.mBackImage.setLayoutParams(params);
//            for (Bitmap bitmap : data.getExtraImages()) {
//                ImageViewWithNeedle image = new ImageViewWithNeedle(mContext);
//                image.setExtraImage(bitmap);
//                holder.mExtraLayout.addView(image);
//            }

        }


//        holder.mTitle.setText(new EveryDayBean().getTitleName());
//        holder.mNum.setText(new EveryDayBean().getNumber() + "");

//        if (holder.mrecall.isChecked()) {
//            Toast.makeText(mContext, "回忆事件", Toast.LENGTH_SHORT).show();
//            holder.mNum.setClickable(true);
//        } else {
//            Toast.makeText(mContext, "没有回忆", Toast.LENGTH_SHORT).show();
//        }
    }

    /**
     * 把dip单位转成px单位
     *
     * @param context context对象
     * @param dip     dip数值
     * @return
     */
    public static int formatDipToPx(Context context, int dip) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        return (int) Math.ceil(dip * dm.density);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    class ActivityEveryDayInfoViewHolder extends RecyclerView.ViewHolder {

        private ImageView mNumberImage;
        private ImageView mBackImage;
        private TextView mNumberText;
        private TextView mTitleText;
        private ImageView mPlayImage;
        private LinearLayout mExtraLayout;

        public ActivityEveryDayInfoViewHolder(View itemView) {
            super(itemView);
            mNumberImage = itemView.findViewById(R.id.image_plot_detail_num);
            mBackImage = itemView.findViewById(R.id.image_plot_item_bg);
            mNumberText = itemView.findViewById(R.id.text_plot_detail_num);
            mTitleText = itemView.findViewById(R.id.text_plot_detail_title);
            mPlayImage = itemView.findViewById(R.id.image_plot_detail_play);
            mExtraLayout = itemView.findViewById(R.id.container_plot_extra_image);
        }
    }
}
