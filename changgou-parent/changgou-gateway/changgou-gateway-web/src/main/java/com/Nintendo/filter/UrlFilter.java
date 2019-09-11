package com.Nintendo.filter;

/**
 * @Package: com.Nintendo.filter
 * @Author: ZZM
 * @Date: Created in 2019/9/2 19:39
 * @Address:CN.SZ
 **/
public class UrlFilter {
    private static final String NoInterceptorUrl = "/api/user/login,/api/user/add";
    public static boolean hasAutorize(String url) {
        String[] split = NoInterceptorUrl.split(",");
        for (String s : split) {
            if (s.equals(url)) {
                return true;
            }
        }
        return false;
    }
}
