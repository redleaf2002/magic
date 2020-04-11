package com.leaf.magic;

import android.app.Application;

import com.leaf.magic.api.Magic;

public class LeafApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Magic.getInstance().init(getApplicationContext());
    }
}
