package com.Nintendo.filter;


import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Package: com.Nintendo.filter
 * @Author: ZZM
 * @Date: Created in 2019/8/30 10:50
 * @Address:CN.SZ
 **/

/**
 * 自定义全局过滤器::用于鉴权(获取令牌 解析 判断)
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    //令牌头名字
    private static final String AUTHORIZE_TOKEN = "Authorization";
    //用户登录地址
    private static final String USER_LOGIN_URL = "http://localhost:9001/oauth/login";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //判断是否是登录路径,如果是放行,不是则走下面进行鉴权
        if (UrlFilter.hasAutorize(request.getURI().toString())) {
            return chain.filter(exchange);
        }
        //先从头信息中获取令牌
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
        if (StringUtils.isEmpty(token)) {
            //从cookie中获取
            HttpCookie first = request.getCookies().getFirst(AUTHORIZE_TOKEN);
            if (null != first) {
                token = first.getValue();
            }
        }
        //cookie中没有则去请求参数中获取
        if (StringUtils.isEmpty(token)) {
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        }
        if (StringUtils.isEmpty(token)) {

            //未携带令牌的情况下去登陆页面
            //登录成功跳转到之前要访问的页面
            return needAuthorization(USER_LOGIN_URL + "?FROM=" + request.getURI(), exchange);
            //设置为没有权限
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            return response.setComplete();
        }
        //解析令牌数据
//        try {
//            //Claims claims = JwtUtil.parseJWT(token);
//            //把令牌设置到头信息中去
        request.mutate().header(AUTHORIZE_TOKEN, "Bearer " + token);
//        } catch (Exception e) {
//            e.printStackTrace();
//            //解析失败
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            return response.setComplete();
//        }
        return chain.filter(exchange);
    }   //设置登陆头

    public Mono<Void> needAuthorization(String url, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().set("Location", url);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
