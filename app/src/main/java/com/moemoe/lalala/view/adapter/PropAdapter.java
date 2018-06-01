package com.moemoe.lalala.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.event.OnItemListener;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.PropInfoEntity;
import com.moemoe.lalala.view.base.PropInfo;

import java.util.List;

/**
 * Created by zhangyan on 2018/5/21.
 */

public class PropAdapter extends RecyclerView.Adapter<PropAdapter.PropViewHolder> {


    private Context mContext;
    private List<PropInfoEntity> infos;

    private int mSelectedPos = -1;//保存当前选中的position 重点！


    public PropAdapter(Context mContext, List<PropInfoEntity> infos) {
        this.mContext = mContext;
        this.infos = infos;
    }

    @Override
    public PropViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PropViewHolder holder = new PropViewHolder(View.inflate(mContext, R.layout.item_storage_info, null));
        return holder;
    }


    @Override
    public void onBindViewHolder(final PropViewHolder holder, final int position) {
        final PropInfoEntity data = infos.get(position);
        Log.i("PropFragment", "onBindViewHolder: " + data);
        if (data.isUserHadTool()) {
            Glide.with(mContext).load(ApiService.URL_QINIU + data.getImage()).into(holder.mView);
            holder.mName.setText(data.getName());
            holder.mNum.setText("数量" + data.getToolCount());
        } else {
            holder.mName.setText(data.getName());
            holder.mNum.setText("数量" + data.getToolCount());
            Glide.with(mContext).load(ApiService.URL_QINIU + data.getImage()).into(holder.mView);
            holder.mView.setAlpha(0.5f);
        }


        holder.mBg.setChecked(mSelectedPos == position);
        if (mOnItemListener != null) {
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemListener.onItemClick(holder.mView, position);
                    if (mSelectedPos != position) {
                        holder.mBg.setChecked(true);
                        if (mSelectedPos != -1) {
                            notifyItemChanged(mSelectedPos, 0);
                        }
                        mSelectedPos = position;
                    }
                }
            });
        }
//        holder.mBg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    String info = holder.mBg.getText().toString();
//                    Log.i("asd", "onCheckedChanged: "+info);
//                }
//            }
//        });

    }

    public void setData(List<PropInfoEntity> data) {

        this.infos = data;
        notifyDataSetChanged();
    }

    public void setRestore(List<PropInfoEntity> data) {
        this.infos = data;
        notifyDataSetChanged();
    }

    public int getSelectedPos() {
        return mSelectedPos;
    }

    @Override
    public int getItemCount() {
        return infos != null ? infos.size() : 0;
    }

    private OnItemListener mOnItemListener;

    public void setOnItemClickListener(OnItemListener onItemClickListener) {
        mOnItemListener = onItemClickListener;
    }

    class PropViewHolder extends RecyclerView.ViewHolder {
        private CheckBox mBg;
        private ImageView mView;//道具图片
        private TextView mName;//道具名称
        private TextView mNum;//道具数量
        private FrameLayout mTop;
        private LinearLayout mL;

        public PropViewHolder(View itemView) {
            super(itemView);
            mBg = itemView.findViewById(R.id.item_commodity_check_btn);
            mView = itemView.findViewById(R.id.item_commodity_image);
            mName = itemView.findViewById(R.id.item_commodity_name);
            mNum = itemView.findViewById(R.id.item_commodity_num);
            mTop = itemView.findViewById(R.id.top);
            mL = itemView.findViewById(R.id.ll);
        }
    }
}
