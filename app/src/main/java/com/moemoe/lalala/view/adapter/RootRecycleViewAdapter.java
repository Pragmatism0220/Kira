package com.moemoe.lalala.view.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.view.activity.VisitorsActivity;

import java.text.SimpleDateFormat;

import java.util.List;
import java.util.Locale;


/**
 * Created by zhangyan on 2018/5/31.
 */

public class RootRecycleViewAdapter extends RecyclerView.Adapter<RootRecycleViewAdapter.RootViewHolder> {

    private List<VisitorsActivity.TempEntity> itemBeen;
    private Context mContext;

    public RootRecycleViewAdapter(List<VisitorsActivity.TempEntity> itemBeen, Context mContext) {
        this.itemBeen = itemBeen;
        this.mContext = mContext;
    }

    @Override
    public RootViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RootViewHolder holder = new RootViewHolder(View.inflate(parent.getContext(), R.layout.root_item_visitors, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RootViewHolder holder, int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        holder.mYearTime.setText(sdf.format(itemBeen.get(position).getTitleDate()));

        if (itemBeen.get(position).getItems().isEmpty())
            return;

//        holder.mRv.setLayoutManager(new LinearLayoutManager(holder.mRv.getContext()));
//        VisitorAdapter adapter = new VisitorAdapter(itemBeen.get(position).getItems(), mContext);
//        holder.mRv.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return itemBeen == null ? 0 : itemBeen.size();
    }

    class RootViewHolder extends RecyclerView.ViewHolder {

        TextView mYearTime;
        RecyclerView mRv;

        RootViewHolder(View itemView) {
            super(itemView);
            mYearTime = itemView.findViewById(R.id.root_time);
//            mRv = itemView.findViewById(R.id.branch_rv);
        }
    }
}
