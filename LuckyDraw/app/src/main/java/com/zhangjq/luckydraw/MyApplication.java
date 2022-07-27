package com.zhangjq.luckydraw;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.mmkv.MMKV;
import com.zhangjq.luckydraw.bean.CookiesInfo;
import com.zhangjq.luckydraw.util.MConstant;

import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * 创建日期：2022/7/21 14:21
 *
 * @author zhangjq
 * @version 1.0
 * 包名： com.zhangjq.luckydraw
 * 类说明：
 */

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private static MyApplication myApp;

    private static MMKV mmkv;

    public static MyApplication getMyApp() {
        return myApp;
    }

    public static MMKV getMmkv(){
        return mmkv;
    }

    private static LinkedList<CookiesInfo> cookiesLists;

    private static Gson gson = new Gson();

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        myApp = this;
        String rootDir = MMKV.initialize(this);
        System.out.println("mmkv root: " + rootDir);
        mmkv = MMKV.defaultMMKV();
        cookiesLists = getCookLists();
    }

    public static LinkedList<CookiesInfo> getCookiesLists() {
        return cookiesLists;
    }

    public static void setCookiesLists(LinkedList<CookiesInfo> cookiesLists) {
        MyApplication.cookiesLists = cookiesLists;
        MyApplication.cookiesLists.forEach(new Consumer<CookiesInfo>() {
            @Override
            public void accept(CookiesInfo cookiesInfo) {
                Log.d(TAG, "菜名：" + cookiesInfo.getCookiesName());
            }
        });
        getMmkv().encode(MConstant.mmkv_key, gson.toJson(cookiesLists));
    }

    private LinkedList<CookiesInfo> getCookLists() {
        LinkedList<CookiesInfo> lists = gson.fromJson(getMmkv().decodeString(MConstant.mmkv_key), new TypeToken<LinkedList<CookiesInfo>>() {
        }.getType());
        if (lists != null) {
            lists.forEach(new Consumer<CookiesInfo>() {
                @Override
                public void accept(CookiesInfo cookiesInfo) {
//                    Log.d(TAG, "菜名：" + cookiesInfo.getCookiesName());
                }
            });
            return lists;
        }
        lists = new LinkedList<>();
        lists.add(new CookiesInfo("馄饨"));
        lists.add(new CookiesInfo("米线"));
        lists.add(new CookiesInfo("酸辣粉"));
        lists.add(new CookiesInfo("螺狮粉"));
        lists.add(new CookiesInfo("麻辣烫"));
        lists.add(new CookiesInfo("炒饭"));
        lists.add(new CookiesInfo("卤肉饭"));
        lists.add(new CookiesInfo("烤肉饭"));
        lists.add(new CookiesInfo("黄焖鸡米饭"));
        lists.add(new CookiesInfo("酸菜鱼"));
        lists.add(new CookiesInfo("烤鸭"));
        return lists;
    }
}
