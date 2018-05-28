package com.moemoe.lalala.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.event.OnItemListener;
import com.moemoe.lalala.view.base.FurnitureInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/5/23.
 */

public class FurnitureInfoAdapter extends RecyclerView.Adapter<FurnitureInfoAdapter.FurnitureHolder> {

    private Context mContext;
    private List<FurnitureInfo> infos;

    private int mSelectedPos = -1;//保存当前选中的position 重点！

    public FurnitureInfoAdapter(Context mContext, List<FurnitureInfo> infos) {
        this.mContext = mContext;
        this.infos = infos;
    }

    @Override

    public FurnitureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FurnitureHolder holder = new FurnitureHolder(View.inflate(mContext, R.layout.item_furniture_info, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(final FurnitureHolder holder, final int position) {
        FurnitureInfo data = infos.get(position);
        if (!data.isUse()) {
            holder.mFusing.setVisibility(View.VISIBLE);
        } else {
            holder.mFusing.setVisibility(View.GONE);
        }
        holder.mFName.setText(data.getName());
        holder.mFStyle.setText(data.getStyle());
        holder.mFCheck.setChecked(mSelectedPos == position);


        if (mOnItemListener != null) {
            holder.mFphoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemListener.onItemClick(holder.mFphoto, position);
                    if (mSelectedPos != position) {
                        holder.mFCheck.setChecked(true);
                        if (mSelectedPos != -1) {
                            notifyItemChanged(mSelectedPos, 0);
                        }
                        mSelectedPos = position;
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return infos != null ? infos.size() : 0;
    }

    public int getSelectedPos() {
        return mSelectedPos;
    }

    private OnItemListener mOnItemListener;

    public void setOnItemClickListener(OnItemListener onItemClickListener) {
        mOnItemListener = onItemClickListener;
    }

    class FurnitureHolder extends RecyclerView.ViewHolder {

        private LinearLayout mFll;
        private FrameLayout mFTop;
        private CheckBox mFCheck;
        private ImageView mFphoto;
        private ImageView mFusing;
        private TextView mFName;
        private TextView mFStyle;

        public FurnitureHolder(View itemView) {
            super(itemView);
            mFll = itemView.findViewById(R.id.item_furniture_ll);
            mFTop = itemView.findViewById(R.id.item_furniture_top);
            mFCheck = itemView.findViewById(R.id.item_furniture_check_btn);
            mFphoto = itemView.findViewById(R.id.item_furniture_image);
            mFusing = itemView.findViewById(R.id.item_furniture_using);
            mFName = itemView.findViewById(R.id.item_furniture_name);
            mFStyle = itemView.findViewById(R.id.item_furniture_style);
        }
    }
}
