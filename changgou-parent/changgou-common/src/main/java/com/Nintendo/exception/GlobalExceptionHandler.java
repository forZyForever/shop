package com.Nintendo.exception;

import com.Nintendo.entity.Result;
import com.Nintendo.entity.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Package: com.Nintendo.exception
 * Author: ZZM
 * Date: Created in 2019/8/18 11:28
 **/

@ControllerAdvice
//方法被@requestMpping修饰时的方法发生异常时被调用
public class GlobalExceptionHandler {
    private static final Logger log= LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(value=Exception.class)
    @ResponseBody
    public Result handlerException(Exception e){
    log.error("服务器异常",e);
    return new Result(false, StatusCode.ERROR,"服务器异常");
    }
}
