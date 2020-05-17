package com.leaf.magic.annotation;

public class ProviderInfo {
    public int type = 0;
    public int layoutId = -1;
    public String fullName;

    public ProviderInfo(int type, int layoutId, String fullName) {
        this.type = type;
        this.layoutId = layoutId;
        this.fullName = fullName;

    }
}
