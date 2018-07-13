package com.moemoe.lalala.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.Image;
import com.moemoe.lalala.model.entity.NewStoryInfoEntity;
import com.moemoe.lalala.model.entity.OnImageInfoListener;
import com.moemoe.lalala.model.entity.OnItemListener;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.view.activity.ImageBigSelectActivity;
import com.moemoe.lalala.view.widget.view.ImageViewWithNeedle;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

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
        final NewStoryInfoEntity data = mData.get(position);

        holder.mNumberImage.setEnabled(!data.isLock());
        holder.mTitleText.setText(data.getName());
        holder.mNumberText.setText(data.getSortId());
        if (data.isLock()) {
            holder.mPlayImage.setImageResource(R.drawable.btn_home_plot_memory_play_locked);
        } else {
            holder.mPlayImage.setImageResource(R.drawable.selector_plot_play);
        }
//        holder.mItemOne.removeAllViews();
//        holder.mExtraLayout.removeAllViews();

//        if (data.getImages() != null && data.getImages().size() > 0) {
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mBackImage.getLayoutParams();
//            params.height = (int) mContext.getResources().getDimension(R.dimen.y190);
//            holder.mBackImage.setLayoutParams(params);
//            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) holder.mRlRoot.getLayoutParams();
//            params1.height = (int) mContext.getResources().getDimension(R.dimen.y190);
//            holder.mRlRoot.setLayoutParams(params1);
//            for (int i = 0; i < data.getImages().size(); i++) {
//                ImageViewWithNeedle image = new ImageViewWithNeedle(mContext);
//                image.setExtraImage(data.getImages().get(i));
//                holder.mExtraLayout.addView(image);
//            }
//        }

        if (data.getImages() != null && data.getImages().size() > 0) {
            if (data.getImages().size() == 1) {
                holder.mExtraLayout.setVisibility(View.VISIBLE);
                holder.mItemOne.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(ApiService.URL_QINIU + data.getImages().get(0))
                        .error(R.drawable.shape_gray_e8e8e8_background)
                        .placeholder(R.drawable.shape_gray_e8e8e8_background)
                        .into(holder.mOneInfo);
            } else if (data.getImages().size() == 2) {
                holder.mExtraLayout.setVisibility(View.VISIBLE);
                holder.mItemOne.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(ApiService.URL_QINIU + data.getImages().get(0))
                        .error(R.drawable.shape_gray_e8e8e8_background)
                        .placeholder(R.drawable.shape_gray_e8e8e8_background)
                        .into(holder.mOneInfo);
                holder.mItemTwo.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(ApiService.URL_QINIU + data.getImages().get(1))
                        .error(R.drawable.shape_gray_e8e8e8_background)
                        .placeholder(R.drawable.shape_gray_e8e8e8_background)
                        .into(holder.mTwoInfo);
            } else if (data.getImages().size() == 3) {
                holder.mExtraLayout.setVisibility(View.VISIBLE);

                holder.mItemOne.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(ApiService.URL_QINIU + data.getImages().get(0))
                        .error(R.drawable.shape_gray_e8e8e8_background)
                        .placeholder(R.drawable.shape_gray_e8e8e8_background)
                        .into(holder.mOneInfo);

                holder.mItemTwo.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(ApiService.URL_QINIU + data.getImages().get(1))
                        .error(R.drawable.shape_gray_e8e8e8_background)
                        .placeholder(R.drawable.shape_gray_e8e8e8_background)
                        .into(holder.mTwoInfo);

                holder.mItemThree.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(ApiService.URL_QINIU + data.getImages().get(2))
                        .error(R.drawable.shape_gray_e8e8e8_background)
                        .placeholder(R.drawable.shape_gray_e8e8e8_background)
                        .into(holder.mThreeInfo);

            }
        } else if (data.getImages().size() == 0) {
            holder.mExtraLayout.setVisibility(View.GONE);
        }

        if (mOnItemListener != null) {
            holder.mPlayImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemListener.onItemClick(holder.mPlayImage, position);
                }
            });

            holder.mItemOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnImageListener.onImageListener(holder.mItemOne, position);
                }
            });
//            holder.mItemTwo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnImageListener.onImageListener(holder.mItemOne, position);
//                }
//            });
//            holder.mItemThree.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnImageListener.onImageListener(holder.mItemOne, position);
//                }
//            });

//            holder.mCoverInfoRoot.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    holder.mCoverInfoRoot.setVisibility(View.GONE);
//                }
//            });
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
    private OnImageInfoListener mOnImageListener;

    public void setOnIamgeListener(OnImageInfoListener onIamgeListener) {
        mOnImageListener = onIamgeListener;
    }

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
        private RelativeLayout mCoverInfoRoot;
        private ImageView mCoverInfo;
        private RelativeLayout mItemOne;
        private RelativeLayout mItemTwo;
        private RelativeLayout mItemThree;
        private ImageView mOneInfo;
        private ImageView mTwoInfo;
        private ImageView mThreeInfo;
        private ImageView mImageTitleOne;

        public PrincipalListViewHolder(View itemView) {
            super(itemView);
            mNumberImage = itemView.findViewById(R.id.image_plot_detail_num);
            mBackImage = itemView.findViewById(R.id.image_plot_item_bg);
            mNumberText = itemView.findViewById(R.id.text_plot_detail_num);
            mTitleText = itemView.findViewById(R.id.text_plot_detail_title);
            mPlayImage = itemView.findViewById(R.id.image_plot_detail_play);
            mExtraLayout = itemView.findViewById(R.id.container_plot_extra_image);
            mRlRoot = itemView.findViewById(R.id.rl_root);
//            mCoverInfoRoot = itemView.findViewById(R.id.rl_cover_info);
//            mCoverInfo = itemView.findViewById(R.id.cover_info);
            mItemOne = itemView.findViewById(R.id.cg_item_one);
            mItemTwo = itemView.findViewById(R.id.cg_item_two);
            mItemThree = itemView.findViewById(R.id.cg_item_three);

            mOneInfo = itemView.findViewById(R.id.image_ext_icon_image_one);
            mTwoInfo = itemView.findViewById(R.id.image_ext_icon_image_two);
            mThreeInfo = itemView.findViewById(R.id.image_ext_icon_image_three);

            mImageTitleOne = itemView.findViewById(R.id.cg_item_one_info);

        }
    }
}

