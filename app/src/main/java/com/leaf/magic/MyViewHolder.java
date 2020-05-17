package com.leaf.magic;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leaf.magic.annotation.Provider;

@Provider(provider = BaseViewHolder.class, type = 100)
public class MyViewHolder extends BaseViewHolder {
    private TextView titleView;

    public MyViewHolder(LayoutInflater layoutInflater, ViewGroup root, boolean attachToRoot) {
        this(layoutInflater.inflate(R.layout.my_viewholder_layout, root, attachToRoot));
    }

    public MyViewHolder(View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.title);
    }

    @Override
    public void bindView(Object baseData, int position) {
        if (baseData instanceof String) {
            String title = (String) baseData;
            Log.i("magic", "bindView " + title);
            titleView.setText(title);
        }
    }
}
