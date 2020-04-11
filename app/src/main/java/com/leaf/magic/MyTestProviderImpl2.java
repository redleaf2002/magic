package com.leaf.magic;

import com.leaf.magic.annotation.Provider;

@Provider(provider = MyTestProvider.class, type = 100)
public class MyTestProviderImpl2 implements MyTestProvider {
    private int count = 10000;

    @Override
    public String getCount() {
        return "MyTestProviderImpl2 count = " + count;
    }
}
