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

import com.moemoe.lalala.R;
import com.moemoe.lalala.event.OnItemListener;
import com.moemoe.lalala.view.base.PrincipalListBean;
import com.moemoe.lalala.view.widget.view.ImageViewWithNeedle;

import java.util.List;

/**
 * Created by zhangyan on 2018/5/16.
 * 主线剧情列表式详情
 */

public class PrincipalListAdapter extends RecyclerView.Adapter<PrincipalListAdapter.PrincipalListViewHolder> {

    private Context mContext;
    private List<PrincipalListBean> mData;

    public PrincipalListAdapter(Context mContext, List<PrincipalListBean> lists) {
        this.mContext = mContext;
        this.mData = lists;
    }

    @Override
    public PrincipalListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PrincipalListViewHolder holder = new PrincipalListViewHolder(View.inflate(mContext, R.layout.item_principal_list, null));
        return holder;
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
    public void onBindViewHolder(final PrincipalListViewHolder holder, final int position) {
        PrincipalListBean data = mData.get(position);
        holder.mNumberImage.setEnabled(!data.isLock());
        holder.mPlayImage.setEnabled(!data.isLock());
        holder.mTitleText.setText(data.getTitle());
        holder.mNumberText.setText(data.getNumber());

        if (data.getExtraImages() != null && data.getExtraImages().size() > 0) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mBackImage.getLayoutParams();
            params.height = formatDipToPx(mContext, 95);
            holder.mBackImage.setLayoutParams(params);
            for (Bitmap bitmap : data.getExtraImages()) {
                ImageViewWithNeedle image = new ImageViewWithNeedle(mContext);
                image.setExtraImage(bitmap);
                holder.mExtraLayout.addView(image);
            }
        }

//        if (mOnItemListener != null) {
//            holder.mTitle.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnItemListener.onItemClick(holder.mTitle, position);
//                }
//            });
//        }
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }


    /**
     * 点击事件
     */
    private OnItemListener mOnItemListener;

    public void setOnItemClickListener(OnItemListener onItemClickListener) {
        mOnItemListener = onItemClickListener;
    }

    class PrincipalListViewHolder extends RecyclerView.ViewHolder {
        private ImageView mNumberImage;
        private ImageView mBackImage;
        private TextView mNumberText;
        private TextView mTitleText;
        private ImageView mPlayImage;
        private LinearLayout mExtraLayout;

        public PrincipalListViewHolder(View itemView) {
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
