package com.leaf.magic;

import com.leaf.magic.annotation.Provider;

@Provider(provider = MyTestProvider.class)
public class MyTestProviderImpl implements MyTestProvider {
    private int count = 100;

    @Override
    public String getCount() {
        count += 100;
        return "MyTestProvider count = " + count;
    }
}
