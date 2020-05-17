package com.leaf.magic;


import com.leaf.magic.annotation.Provider;

@Provider(provider = MyTestProvider.class,type = 100)
public class MyTestProviderImpl1 implements MyTestProvider {
    private int count = 100;

    @Override
    public String getCount() {
        count += 100;
        return "is MyTestProviderImpl1 count = " + count;
    }
}
