package com.leaf.magic;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.leaf.magic.annotation.Provider;

@Provider(provider = BaseViewHolder.class, type = 100, layoutId = R.layout.my_viewholder_layout)
public class MyViewHolder extends BaseViewHolder {
    private TextView titleView;

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
