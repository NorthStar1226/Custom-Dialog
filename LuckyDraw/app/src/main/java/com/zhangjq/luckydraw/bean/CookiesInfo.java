package com.zhangjq.luckydraw.bean;

/**
 * 创建日期：2022/7/25 15:02
 *
 * @author zhangjq
 * @version 1.0
 * 包名： com.zhangjq.luckydraw.bean
 * 类说明：
 */

public class CookiesInfo {
    private boolean isSeclected;

    private String cookiesName;

    public CookiesInfo(String cookiesName) {
        this.cookiesName = cookiesName;
        isSeclected = false;
    }

    public boolean isSeclected() {
        return isSeclected;
    }

    public void setSeclected(boolean isSeclected) {
        this.isSeclected = isSeclected;
    }

    public String getCookiesName() {
        return cookiesName;
    }

    public void setCookiesName(String cookiesName) {
        this.cookiesName = cookiesName;
    }

}
