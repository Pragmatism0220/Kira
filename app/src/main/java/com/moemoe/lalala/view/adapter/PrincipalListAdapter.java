package com.moemoe.lalala.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.NewStoryInfoEntity;
import com.moemoe.lalala.model.entity.OnItemListener;
import com.moemoe.lalala.view.widget.view.ImageViewWithNeedle;

import java.util.List;

/**
 * Created by zhangyan on 2018/5/16.
 * 主线剧情列表式详情
 */

public class PrincipalListAdapter extends RecyclerView.Adapter<PrincipalListAdapter.PrincipalListViewHolder> {

    private Context mContext;
    private List<NewStoryInfoEntity> mData;

    public PrincipalListAdapter(Context mContext, List<NewStoryInfoEntity> lists) {
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
        NewStoryInfoEntity data = mData.get(position);

        holder.mNumberImage.setEnabled(!data.isLock());
        holder.mPlayImage.setEnabled(!data.isLock());
        holder.mTitleText.setText(data.getName());
        holder.mNumberText.setText(data.getSortId());


        holder.mExtraLayout.removeAllViews();
        if (data.getImages() != null && data.getImages().size() > 0) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mBackImage.getLayoutParams();
            params.height = (int) mContext.getResources().getDimension(R.dimen.y190);
            holder.mBackImage.setLayoutParams(params);
            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) holder.mRlRoot.getLayoutParams();
            params1.height = (int) mContext.getResources().getDimension(R.dimen.y190);
            holder.mRlRoot.setLayoutParams(params1);
            for (int i = 0; i < data.getImages().size(); i++) {
                ImageViewWithNeedle image = new ImageViewWithNeedle(mContext);
                image.setExtraImage(data.getImages().get(i));
                holder.mExtraLayout.addView(image);
            }
        }


        if (mOnItemListener != null) {
            holder.mBackImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemListener.onItemClick(holder.mBackImage, position);
                }
            });
        }
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
        RelativeLayout mRlRoot;

        public PrincipalListViewHolder(View itemView) {
            super(itemView);
            mNumberImage = itemView.findViewById(R.id.image_plot_detail_num);
            mBackImage = itemView.findViewById(R.id.image_plot_item_bg);
            mNumberText = itemView.findViewById(R.id.text_plot_detail_num);
            mTitleText = itemView.findViewById(R.id.text_plot_detail_title);
            mPlayImage = itemView.findViewById(R.id.image_plot_detail_play);
            mExtraLayout = itemView.findViewById(R.id.container_plot_extra_image);
            mRlRoot = itemView.findViewById(R.id.rl_root);
        }
    }
}
