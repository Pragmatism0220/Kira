package com.moemoe.lalala.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.NewStoryGroupInfo;
import com.moemoe.lalala.model.entity.OnItemListener;
import com.moemoe.lalala.model.api.ApiService;

import java.util.List;

/**
 * Created by Administrator on 2018/5/24.
 */

public class PrAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int ALL_VIEW = -2;//全收集
    private final int HALF_VIEW = 63;//未全收集
    private final int NO_VIEW = -1;//未解锁

    private Context mContext;
    private List<NewStoryGroupInfo> infos;
    private OnItemListener clickListener;

    public PrAdapter(Context mContext, List<NewStoryGroupInfo> infos) {
        this.mContext = mContext;
        this.infos = infos;
    }

    public void addOnItemClickListener(OnItemListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (infos.get(position).isFullCollect()) {
            //未收集
            return HALF_VIEW;
        } else if (!infos.get(position).isFullCollect()) {
            //全收集
            return ALL_VIEW;
        } else if (infos.get(position).isLock()) {
            //未解锁
            return NO_VIEW;
        }
        return super.getItemViewType(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ALL_VIEW) {
            ALLViewHolder holder = new ALLViewHolder(View.inflate(mContext, R.layout.item_principal_line, null));
            return holder;
        } else if (viewType == HALF_VIEW) {
            HalfViewHolder holder = new HalfViewHolder(View.inflate(mContext, R.layout.item_principal_half, null));
            return holder;
        } else if (viewType == NO_VIEW) {
            NoViewHolder holder = new NoViewHolder(View.inflate(mContext, R.layout.item_principal_no, null));
            return holder;
        }
        return new ALLViewHolder(View.inflate(mContext, R.layout.item_principal_line, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemClick(v, position);
                }
            }
        });

        NewStoryGroupInfo data = infos.get(position);
        if (holder instanceof ALLViewHolder) {
            ALLViewHolder allHolder = (ALLViewHolder) holder;
            allHolder.mBottomTitle.setText(data.getContent());
            allHolder.mNum.setText("参与人数：" + data.getJoinNum());
            allHolder.mName.setText(data.getGroupName());
            Glide.with(mContext).load(ApiService.URL_QINIU + data.getImage()).into(allHolder.mPhoto);
            allHolder.mPhoto.setAlpha(1.0f);
        } else if (holder instanceof HalfViewHolder) {
            HalfViewHolder halfHolder = (HalfViewHolder) holder;
            halfHolder.mHalfBottomTitle.setText(data.getContent());
            halfHolder.mHalfBottomText.setText(data.getProgress() + "%");
            halfHolder.mHalfBottomNum.setText("参与人数：" + data.getJoinNum());
            halfHolder.mHalfBottonName.setText(data.getGroupName());
            Glide.with(mContext).load(ApiService.URL_QINIU + data.getImage()).into(halfHolder.mHalfItemPhoto);
            halfHolder.mHalfItemPhoto.setAlpha(1.0f);
        } else if (holder instanceof NoViewHolder) {
            NoViewHolder noHolder = (NoViewHolder) holder;
            noHolder.noBottomTitle.setText(data.getContent());
            noHolder.noBottomNum.setText("参与人数：" + data.getJoinNum());
            noHolder.noBottomName.setText(data.getGroupName());
            Glide.with(mContext).load(ApiService.URL_QINIU + data.getImage()).into(noHolder.mNoPhoto);
            noHolder.mNoPhoto.setAlpha(0.5f);
        }
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    /**
     * 全收集界面viewHolder
     */
    public class ALLViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPhoto;//封面素材
        private ImageView mBottomImage;//封面底部角标
        private TextView mBottomTitle;//封面底部标题

        private TextView mName;//名字
        private TextView mNum;//参与人数

        public ALLViewHolder(View itemView) {
            super(itemView);
            mPhoto = itemView.findViewById(R.id.item_photo);
            mBottomImage = itemView.findViewById(R.id.item_bottom_image);
            mBottomTitle = itemView.findViewById(R.id.item_bottom_title);
            mName = itemView.findViewById(R.id.item_bottom_name);
            mNum = itemView.findViewById(R.id.item_bottom_num);
        }
    }

    /**
     * 没有全部收集完viewholder
     */
    public class HalfViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mHalfItemView;//最外部布局
        private ImageView mHalfItemPhoto;//图片素材  git
        private ImageView mHalfItemBottomImage;//绿色角标
        private TextView mHalfBottomTitle;//绿色角标上标题
        private TextView mHalfBottomText;//绿色角标上进度

        private TextView mHalfBottonName;//名字
        private TextView mHalfBottomNum;//参与人数


        public HalfViewHolder(View itemView) {
            super(itemView);
            mHalfItemView = itemView.findViewById(R.id.half_item_view);
            mHalfItemPhoto = itemView.findViewById(R.id.half_item_photo);
            mHalfItemBottomImage = itemView.findViewById(R.id.half_item_bottom_image);
            mHalfBottomTitle = itemView.findViewById(R.id.half_item_bottom_title);
            mHalfBottomText = itemView.findViewById(R.id.half_item_bottom_text);
            mHalfBottonName = itemView.findViewById(R.id.half_item_bottom_name);
            mHalfBottomNum = itemView.findViewById(R.id.half_item_bottom_num);

        }
    }

    /**
     * 未解锁剧情
     */
    public class NoViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mNoView; //最外部布局
        private ImageView mNoPhoto;//背景素材
        private ImageView mNoBottomIamge;//灰色角标
        private TextView noBottomTitle;//灰色角标标题

        private TextView noBottomName;//名字
        private TextView noBottomNum;//参与人数

        public NoViewHolder(View itemView) {
            super(itemView);
            mNoView = itemView.findViewById(R.id.no_view);
            mNoPhoto = itemView.findViewById(R.id.no_photo);
            mNoBottomIamge = itemView.findViewById(R.id.no_bottom_image);
            noBottomTitle = itemView.findViewById(R.id.no_bottom_title);
            noBottomName = itemView.findViewById(R.id.no_bottom_name);
            noBottomNum = itemView.findViewById(R.id.no_bottom_num);
        }
    }
}
