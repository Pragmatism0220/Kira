package com.moemoe.lalala.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2018/6/5.
 */

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    @Override
    public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(DiaryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class DiaryViewHolder extends RecyclerView.ViewHolder {
        public DiaryViewHolder(View itemView) {
            super(itemView);
        }
    }
}
