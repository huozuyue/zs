package com.hzy.serviceimpl;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by hzy on 2018/6/5.
 */

public  class SharedPreferencesHelper  {
    private static final String SHARED_PATH = "com.example.hzy.lovenum.SharedPreferencesone";
    // 引导界面KEY
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";
    private static final String GUIDE_PATH = "guide_shared";
    private static SharedPreferencesHelper instance;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private SharedPreferencesHelper(Context context) {
        sp = context.getSharedPreferences(SHARED_PATH, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static SharedPreferencesHelper getInstance(Context context) {
        if (instance == null && context != null) {
            instance = new SharedPreferencesHelper(context);
        }
        return instance;
    }

    /**
     * 判断activity是否引导�?	 *
     *
     * @param context
     * @return 是否已经引导�?true引导过了 false未引�?
     */
    public static boolean activityIsGuided(Context context, String className) {
        if (context == null || className == null
                || "".equalsIgnoreCase(className))
            return false;
        String[] classNames = context
                .getSharedPreferences(GUIDE_PATH, Context.MODE_WORLD_READABLE)
                .getString(KEY_GUIDE_ACTIVITY, "").split("\\|");// 取得�?��类名 �?
        // com.my.MainFrameActivity
        for (String string : classNames) {
            if (className.equalsIgnoreCase(string)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置该activity被引导过了�? 将类名已 |a|b|c这种形式保存为value，因为偏好中只能保存键�?�?	 *
     *
     * @param context
     * @param className
     */
    public static void setIsGuided(Context context, String className) {
        if (context == null || className == null
                || "".equalsIgnoreCase(className))
            return;
        String classNames = context.getSharedPreferences(GUIDE_PATH,
                Context.MODE_WORLD_READABLE).getString(KEY_GUIDE_ACTIVITY, "");
        StringBuilder sb = new StringBuilder(classNames).append("|").append(
                className);// 添加�?		context.getSharedPreferences(GUIDE_PATH, Context
        // .MODE_WORLD_READABLE)// 保存修改后的�?				.edit().putString(KEY_GUIDE_ACTIVITY, sb
        // .toString()).commit();
    }

    public long getLongValue(String key) {
        if (key != null && !key.equals("")) {
            return sp.getLong(key, 0);
        }
        return 0;
    }

    public String getStringValue(String key) {
        if (key != null && !key.equals("")) {
            return sp.getString(key, "");
        }
        return null;
    }

    public int getIntValue(String key) {
        if (key != null && !key.equals("")) {
            return sp.getInt(key, 0);
        }
        return 0;
    }

    public int getIntValueByDefault(String key) {
        if (key != null && !key.equals("")) {
            return sp.getInt(key, 18);
        }
        return 18;
    }

    public boolean getBooleanValue(String key) {
        if (key != null && !key.equals("")) {
            return sp.getBoolean(key, false);
        }
        return true;
    }

    public float getFloatValue(String key) {
        if (key != null && !key.equals("")) {
            return sp.getFloat(key, 0);
        }
        return 0;
    }

    public double getDoubleValue(String key) {
        if (key != null && !key.equals("")) {
            return Double.longBitsToDouble(sp.getLong(key, Double.doubleToLongBits(0)));
           // return sp.getFloat(key, 0);
        }
        return 0;
    }

    public void putStringValue(String key, String value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public void putIntValue(String key, int value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }

    public void putBooleanValue(String key, boolean value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putBoolean(key, value);
            editor.commit();
        }
    }

    public void putLongValue(String key, long value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putLong(key, value);
            editor.commit();
        }
    }

    public void putDoubbleValue(String key, double value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putLong(key, Double.doubleToRawLongBits(value));
            editor.commit();




        }
    }

    public void putFloatValue(String key, Float value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putFloat(key, value);
            editor.commit();
        }
    }

    /**
     * 进行获取存入和取出的时间间隔�?	 *
     *
     * @return
     */
    public long getDurtionTime(String tag) {
        long durtion = System.currentTimeMillis()
                - sp.getLong(tag, System.currentTimeMillis());
        putLongValue(tag, System.currentTimeMillis());
        return durtion;
    }

    /**
     * 删除SharedPreferences里指定key对应的数据项
     *
     * @param key
     */
    public void removeStringValue(String key) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.remove(key);
            editor.commit();
        }
    }

    public void clear() {
        Log.d("sp", "清除sp");
        editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
