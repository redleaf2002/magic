package com.leaf.magic.api;

import android.content.Context;
import android.text.TextUtils;

import com.leaf.magic.api.template.IProvider;
import com.leaf.magic.api.utils.ClassUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Magic {
    private static final String TAG = "Magic";
    private static Magic mMagic;
    private static Object object = new Object();
    private volatile static boolean isinitialized = false;
    private static final String MAGIC_PKG_NAME_PROVIDER = "com.leaf.magic.provider";
    private final Map<String, String> mProviderMap = new HashMap<>();
    private ConcurrentHashMap<String, Object> mResultMap = new ConcurrentHashMap<>();

    private Magic() {
    }

    public static Magic getInstance() {
        if (mMagic == null) {
            synchronized (object) {
                if (mMagic == null) {
                    mMagic = new Magic();
                }
            }
        }
        return mMagic;
    }

    public void init(Context context) {
        isinitialized = true;
        initRouterModule(context);
    }

    private void initRouterModule(Context context) {
        Set<String> classSet = null;
        try {
            classSet = ClassUtils.getFileNameByPackageName(context.getApplicationContext(), MAGIC_PKG_NAME_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (classSet != null) {
            Iterator<String> iterable = classSet.iterator();
            while (iterable.hasNext()) {
                String routerClass = iterable.next();
                try {
                    if (!TextUtils.isEmpty(routerClass)) {
                        Class<?> clazz = Class.forName(routerClass);
                        IProvider iProvider = (IProvider) clazz.newInstance();
                        if (iProvider != null) {
                            iProvider.loadInfo(mProviderMap);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Object getServiceInstance(Class<?> clazz) {
        if (clazz != null) {
            String key = clazz.getName();
            if (mResultMap.containsKey(key)) {
                return mResultMap.get(key);
            }
            Object object = generateServiceInstance(clazz);
            if (object != null) {
                mResultMap.put(key, object);
            }
            return object;
        }
        return null;
    }

    public Object getServiceInstance(Class<?> clazz, int type) {
        if (clazz != null) {
            String key = clazz.getName();
            if (type > 0) {
                key += "." + type;
            }
            if (mResultMap.containsKey(key)) {
                return mResultMap.get(key);
            }
            Object object = generateServiceInstance(clazz);
            if (object != null) {
                mResultMap.put(key, object);
            }
            return generateServiceInstance(clazz, type);
        }
        return null;
    }

    public Object generateServiceInstance(Class<?> clazz) {
        if (clazz != null) {
            String key = clazz.getName();
            return instanceObject(key);
        }
        return null;
    }

    public Object generateServiceInstance(Class<?> clazz, int type) {
        if (clazz != null) {
            String key = clazz.getName();
            if (type > 0) {
                key += "." + type;
            }
            return instanceObject(key);
        }
        return null;
    }

    private Object instanceObject(String key) {
        if (!isinitialized) {
            throw new IllegalStateException("Magic: you should call init() method firstly");
        }
        if (mProviderMap.containsKey(key)) {
            try {
                Class<?> providerClazz = Class.forName(mProviderMap.get(key));
                return providerClazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
