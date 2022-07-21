package com.zhangjq.luckydraw;

import android.app.Application;

import com.tencent.mmkv.MMKV;

/**
 * 创建日期：2022/7/21 14:21
 *
 * @author zhangjq
 * @version 1.0
 * 包名： com.zhangjq.luckydraw
 * 类说明：
 */

public class MyApplication extends Application {
    private static MyApplication myApp;

    private static MMKV mmkv;

    public static MyApplication getMyApp() {
        return myApp;
    }

    public static MMKV getMmkv(){
        return mmkv;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        String rootDir = MMKV.initialize(this);
        System.out.println("mmkv root: " + rootDir);
        mmkv = MMKV.defaultMMKV();
    }
}
