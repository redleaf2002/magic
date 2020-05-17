package com.leaf.magic.api.template;


import com.leaf.magic.annotation.ProviderInfo;

import java.util.Map;

/**
 * Created by suhong01 on 2018/8/3.
 */

public interface IProvider {
    void loadInfo(Map<String, ProviderInfo> infoMap);
}
