package com.leaf.magic;

import android.support.v7.widget.RecyclerView;
import android.view.View;


public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindView(Object baseData, int position);

}
