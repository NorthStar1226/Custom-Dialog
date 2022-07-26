package com.zhangjq.luckydraw;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
        getMmkv().encode(MConstant.mmkv_key, gson.toJson(cookiesLists));
    }

    private LinkedList<CookiesInfo> getCookLists() {
        LinkedList<CookiesInfo> lists = gson.fromJson(getMmkv().decodeString(MConstant.mmkv_key), new TypeToken<LinkedList<CookiesInfo>>() {
        }.getType());
        if (lists != null) {
            lists.forEach(new Consumer<CookiesInfo>() {
                @Override
                public void accept(CookiesInfo cookiesInfo) {
                    Log.d(TAG, "菜名：" + cookiesInfo.getCookiesName());
                }
            });
            return lists;
        }
        lists = new LinkedList<>();
        lists.add(new CookiesInfo("aaa"));
        lists.add(new CookiesInfo("bbb"));
        lists.add(new CookiesInfo("ccc"));
        lists.add(new CookiesInfo("ddd"));
        lists.add(new CookiesInfo("eee"));
        lists.add(new CookiesInfo("fff"));
        lists.add(new CookiesInfo("ggg"));
        lists.add(new CookiesInfo("hhh"));
        lists.add(new CookiesInfo("iii"));
        lists.add(new CookiesInfo("jjj"));
        lists.add(new CookiesInfo("kkk"));
        lists.add(new CookiesInfo("lll"));
        lists.add(new CookiesInfo("mmm"));
        lists.add(new CookiesInfo("nnn"));
        return lists;
    }
}
