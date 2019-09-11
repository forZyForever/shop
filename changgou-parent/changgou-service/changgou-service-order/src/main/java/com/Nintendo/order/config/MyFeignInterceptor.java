package com.Nintendo.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @Package: com.Nintendo.order.config
 * @Author: ZZM
 * @Date: Created in 2019/9/1 20:45
 * @Address:CN.SZ
 **/
@Component
public class MyFeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        //获取请求对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != requestAttributes) {
            HttpServletRequest request = requestAttributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (null != headerNames) {
                //2.获取请求对象中的所有的头信息(网关传递过来的
                while (headerNames.hasMoreElements()) {
                    //头名称
                    String name = headerNames.nextElement();
                    //头的值
                    String value = request.getHeader(name);
                    //头信息转发给feign
                    requestTemplate.header(name, value);
                }
            }
        }
    }
}
