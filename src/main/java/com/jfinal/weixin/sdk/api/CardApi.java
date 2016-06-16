package com.jfinal.weixin.sdk.api;

import com.jfinal.weixin.sdk.utils.HttpUtils;

/**
 * 会员卡相关接口
 * Created by L.cm on 2016/6/16.
 */
public class CardApi {
    private static String cardCreateUrl = "https://api.weixin.qq.com/card/create?access_token=";

    /**
     * 创建会员卡接口
     * @param jsonStr JSON数据
     * @return {ApiResult}
     */
    public static ApiResult create(String jsonStr) {
        String jsonResult = HttpUtils.post(cardCreateUrl + AccessTokenApi.getAccessTokenStr(), jsonStr);
        return new ApiResult(jsonResult);
    }

}
