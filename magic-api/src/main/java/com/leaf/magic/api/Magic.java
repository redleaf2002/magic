package com.leaf.magic.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.leaf.magic.annotation.ProviderInfo;
import com.leaf.magic.api.template.IProvider;
import com.leaf.magic.api.utils.ClassUtils;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Magic {
    private static Magic mMagic;
    private static Object object = new Object();
    private volatile static boolean initialized = false;
    private static final String MAGIC_PKG_NAME_PROVIDER = "com.leaf.magic.provider";
    private final Map<String, ProviderInfo> mProviderMap = new HashMap<>();
    // save class instance for the method get
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
        initialized = true;
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

    public Object get(Class<?> clazz) {
        if (clazz != null) {
            String key = clazz.getName();
            if (mResultMap.containsKey(key)) {
                return mResultMap.get(key);
            }
            Object object = create(clazz);
            if (object != null) {
                mResultMap.put(key, object);
            }
            return object;
        }
        return null;
    }

    public Object get(Class<?> clazz, int type) {
        if (clazz != null) {
            String key = clazz.getName();
            if (type > 0) {
                key += "." + type;
            }
            if (mResultMap.containsKey(key)) {
                return mResultMap.get(key);
            }
            Object object = create(clazz);
            if (object != null) {
                mResultMap.put(key, object);
            }
            return create(clazz, type);
        }
        return null;
    }

    public Object create(Class<?> clazz) {
        if (clazz != null) {
            String key = clazz.getName();
            return instanceObject(key);
        }
        return null;
    }

    public Object create(Class<?> clazz, int type) {
        if (clazz != null) {
            String key = clazz.getName();
            if (type > 0) {
                key += "." + type;
            }
            return instanceObject(key);
        }
        return null;
    }

    public Object createViewHolder(Class<?> clazz, int type, LayoutInflater layoutInflater, ViewGroup root, boolean attachToRoot) {
        if (clazz != null) {
            String key = clazz.getName();
            if (type > 0) {
                key += "." + type;
            }
            return instanceViewObject(key, layoutInflater, root, attachToRoot);
        }
        return null;
    }

    private Object instanceObject(String key) {
        if (!initialized) {
            throw new IllegalStateException("Magic: you should call init() method firstly");
        }
        if (mProviderMap.containsKey(key)) {
            try {
                Class<?> providerClazz = Class.forName(mProviderMap.get(key).fullName);
                return providerClazz.newInstance();
            } catch (Exception e) {
                Log.e("magic", "magic error=" + e.toString());
            }
        }
        return null;
    }

    private Object instanceViewObject(String key, LayoutInflater layoutInflater, ViewGroup root, boolean attachToRoot) {

        if (!initialized) {
            throw new IllegalStateException("Magic: you should call init() method firstly");
        }
        if (mProviderMap.containsKey(key)) {
            try {
                ProviderInfo providerInfo = mProviderMap.get(key);
                if (providerInfo == null) {
                    return null;
                }
                Class<?> providerClazz = Class.forName(providerInfo.fullName);
                Constructor constructor = providerClazz.getConstructor(LayoutInflater.class, ViewGroup.class, boolean.class);
                if (constructor != null) {
                    Object viewObject = constructor.newInstance(layoutInflater, root, attachToRoot);
                    return viewObject;
                }
            } catch (Exception e) {
                Log.e("magic", "magic error=" + e.toString());
            }
        }
        return null;

    }


}
