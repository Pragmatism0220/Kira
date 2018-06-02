package com.moemoe.lalala.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.event.OnItemListener;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.AllFurnitureInfo;
import com.moemoe.lalala.model.entity.FurnitureInfoEntity;
import com.moemoe.lalala.model.entity.SuitFurnituresInfo;
import com.moemoe.lalala.view.base.FurnitureInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2018/5/23.
 */

public class FurnitureInfoAdapter extends RecyclerView.Adapter<FurnitureInfoAdapter.FurnitureHolder> {

    private Context mContext;
    private List<AllFurnitureInfo> infos;

    private int mSelectedPos = -1;//保存当前选中的position 重点！

    public FurnitureInfoAdapter(Context mContext) {
        this.mContext = mContext;
        this.infos = new ArrayList<>();
    }

    public void setList(List<AllFurnitureInfo> mData) {
        this.infos = mData;
        notifyDataSetChanged();
    }

    @Override
    public FurnitureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FurnitureHolder holder = new FurnitureHolder(View.inflate(mContext, R.layout.item_furniture_info, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(final FurnitureHolder holder, final int position) {
        AllFurnitureInfo data = infos.get(position);
        
        if (!data.isPutInHouse()) {
            holder.mFusing.setVisibility(View.VISIBLE);
        } else {
            holder.mFusing.setVisibility(View.GONE);
        }
        
        if (data.getType().equals("套装")){
            holder.mFName.setText(data.getSuitTypeName());
            holder.mFCheck.setChecked(mSelectedPos == position);
            Glide.with(mContext).load(ApiService.URL_QINIU + data.getSuitTypeImage()).into(holder.mFphoto);
//            holder.mFStyle.setText(data.getSuitTypeDescribe());
        }else {
            holder.mFName.setText(data.getName());
            holder.mFCheck.setChecked(mSelectedPos == position);
            Glide.with(mContext).load(ApiService.URL_QINIU + data.getImage()).into(holder.mFphoto);
            holder.mFStyle.setText(data.getSuitTypeName());
        }

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
