package com.moemoe.lalala.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.event.StorageDefaultDataEvent;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.AllFurnitureInfo;
import com.moemoe.lalala.model.entity.PropInfoEntity;
import com.moemoe.lalala.view.activity.StorageActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by zhangyan on 2018/5/23.
 */

public class FurnitureInfoAdapter extends RecyclerView.Adapter<FurnitureInfoAdapter.FurnitureHolder> {


    private Context mContext;
    private List<AllFurnitureInfo> infos;
    private int position;
    private int mSelectedPos = -1;//保存当前选中的position 重点！

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

    public void unRegister() {
        EventBus.getDefault().unregister(this);
        StorageActivity.sIsRegist = false;
    }

    public List<AllFurnitureInfo> getData() {
        return infos;
    }


    public void setList(List<AllFurnitureInfo> mData) {
        this.infos = mData;
    }

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


        //如果是套装
        if (data.getType().equals("套装")) {
            //套装是否拥有
            if (!data.isUserSuitFurnitureHad()) {
                holder.mFphoto.setAlpha(0.5f);//未拥有
            } else {
                holder.mFphoto.setAlpha(1.0f);//拥有
            }
        } else {
            //判断家具是否拥有
            if (!data.isUserFurnitureHad()) {
                holder.mFphoto.setAlpha(0.5f);
            } else {
                holder.mFphoto.setAlpha(1.0f);
            }
        }

        if (data.getType().equals("套装")) {
            //套装是否（使用）放入宅屋
            if (data.isSuitPutInHouse()) {
                holder.mFusing.setVisibility(View.VISIBLE);//放入
            } else {
                holder.mFusing.setVisibility(View.GONE);//未放入
            }
        } else {
            //家具是否（使用）放入宅屋，
            if (data.isPutInHouse()) {
                holder.mFusing.setVisibility(View.VISIBLE);//放入
            } else {
                holder.mFusing.setVisibility(View.GONE);
            }
        }
        if (data.isShowNews() == true) {
            holder.mFurnitureNews.setVisibility(View.VISIBLE);
        }else {
            holder.mFurnitureNews.setVisibility(View.GONE);
        }


        if (data.getType().equals("套装")) {
            holder.mFName.setText(data.getSuitTypeName());
            Glide.with(mContext).load(ApiService.URL_QINIU + data.getSuitTypeDetailIcon()).
                    bitmapTransform(new RoundedCornersTransformation(mContext, 12, 0)).crossFade(1000)
                    .into(holder.mFphoto);
        } else {
            holder.mFName.setText(data.getName());
            Glide.with(mContext).load(ApiService.URL_QINIU + data.getDetailIcon()).
                    bitmapTransform(new RoundedCornersTransformation(mContext, 12, 0)).crossFade(1000)
                    .into(holder.mFphoto);
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
        private ImageView mFurnitureNews;

        public FurnitureHolder(View itemView) {
            super(itemView);
            mFll = itemView.findViewById(R.id.item_furniture_ll);
            mFTop = itemView.findViewById(R.id.item_furniture_top);
            mFCheck = itemView.findViewById(R.id.item_furniture_check_btn);
            mFphoto = itemView.findViewById(R.id.item_furniture_image);
            mFusing = itemView.findViewById(R.id.item_furniture_using);
            mFName = itemView.findViewById(R.id.item_furniture_name);
            mFStyle = itemView.findViewById(R.id.item_furniture_style);
            mFurnitureNews = itemView.findViewById(R.id.furniture_news);
        }
    }
}
