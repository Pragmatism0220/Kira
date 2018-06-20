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

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.event.OnItemListener;
import com.moemoe.lalala.event.StorageDefaultDataEvent;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.AllFurnitureInfo;
import com.moemoe.lalala.view.activity.StorageActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/23.
 */

public class FurnitureInfoAdapter extends RecyclerView.Adapter<FurnitureInfoAdapter.FurnitureHolder> {


    private Context mContext;
    private List<AllFurnitureInfo> infos;
    private int position;
    private int mSelectedPos = -1;//保存当前选中的position 重点！

//    private boolean isHava;
    private View mDefaultedData;

    private PropAdapter.RoleItemClickListener listener;

    public FurnitureInfoAdapter(Context mContext) {
        this.mContext = mContext;
        this.infos = new ArrayList<>();
        if (!StorageActivity.sIsRegist) {
            EventBus.getDefault().register(this);
            StorageActivity.sIsRegist = true;
        }
    }

    //这里就是监听注册啊 先看这个吧，你泡一下泡谁？ 还是炮？


    public void unRegister() {
        EventBus.getDefault().unregister(this);
        StorageActivity.sIsRegist = false;
    }


    public void setList(List<AllFurnitureInfo> mData) {
        this.infos = mData;
        notifyDataSetChanged();
    }

//    public void setHava(boolean hava) {
//        isHava = hava;
//    }

    public List<AllFurnitureInfo> getList() {
        return infos;
    }

    @Override
    public FurnitureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FurnitureHolder holder = new FurnitureHolder(View.inflate(mContext, R.layout.item_furniture_info, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(final FurnitureHolder holder, final int position) {
        this.position = position;
        AllFurnitureInfo data = infos.get(position);
        if (data.isSuitPutInHouse()) {
            holder.mFusing.setVisibility(View.VISIBLE);
        } else {
            holder.mFusing.setVisibility(View.GONE);
        }
        if (data.isPutInHouse()) {
            holder.mFusing.setVisibility(View.VISIBLE);
        } else {
            holder.mFusing.setVisibility(View.GONE);
        }
        //套装是否拥有
        if (!data.isUserSuitFurnitureHad() ) {
            holder.mFphoto.setAlpha(0.5f);
        } else {
            holder.mFphoto.setAlpha(1.0f);
        }

        //判断家具是否拥有
        if (!data.isUserFurnitureHad() ) {
            holder.mFphoto.setAlpha(0.5f);
        } else {
            holder.mFphoto.setAlpha(1.0f);
        }


        if (data.getType().equals("套装")) {
            holder.mFName.setText(data.getSuitTypeName());
            Glide.with(mContext).load(ApiService.URL_QINIU + data.getSuitTypeDetailIcon()).into(holder.mFphoto);
        } else {
            holder.mFName.setText(data.getName());
            Glide.with(mContext).load(ApiService.URL_QINIU + data.getDetailIcon()).into(holder.mFphoto);
            holder.mFStyle.setText(data.getSuitTypeName());
        }

        holder.mFCheck.setSelected(data.isSelected());

        if (listener != null) {
            holder.mFCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(holder.mFCheck, position, holder.getAdapterPosition());
                }
            });
            if (position == 0) {
                mDefaultedData = holder.mFCheck;
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(StorageDefaultDataEvent e) {
        if (e != null && e.isFurniture()) {
            mDefaultedData.performClick();
        }
    }

    @Override
    public int getItemCount() {
        return infos != null ? infos.size() : 0;
    }

    public int getSelectedPos() {
        return mSelectedPos;
    }

    /**
     * 是声明点击事件接口
     */
    public interface RoleItemClickListener {
        void onClick(View v, int position, int which);
    }

    public void setOnItemClickListener(PropAdapter.RoleItemClickListener listener) {
        this.listener = listener;
    }

    class FurnitureHolder extends RecyclerView.ViewHolder {

        private LinearLayout mFll;
        private FrameLayout mFTop;
        private ImageView mFCheck;
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
