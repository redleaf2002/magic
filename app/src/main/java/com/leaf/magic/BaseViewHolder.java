package com.leaf.magic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public BaseViewHolder(LayoutInflater layoutInflater, ViewGroup root, boolean attachToRoot) {
        super(null);
    }

    public abstract void bindView(Object baseData, int position);

}
