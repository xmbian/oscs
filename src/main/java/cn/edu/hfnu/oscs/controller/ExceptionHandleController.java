package cn.edu.hfnu.oscs.controller;

import cn.edu.hfnu.oscs.enums.ResponseStatus;
import cn.edu.hfnu.oscs.exception.ZbException;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @version V1.0
 * @date 2018年7月13日
 * @author superzheng
 */

/*
 通过@ControllerAdvice注解可以将对于控制器的全局配置放在同一个位置。
 注解了@ControllerAdvice的类的方法可以使用@ExceptionHandler、@InitBinder、@ModelAttribute注解到方法上。
     @ExceptionHandler：用于全局处理控制器里的异常。
     @InitBinder：用来设置WebDataBinder，用于自动绑定前台请求参数到Model中。
     @ModelAttribute：本来作用是绑定键值对到Model中，此处让全局的@RequestMapping都能获得在此处设置的键值对
 @ControllerAdvice注解将作用在所有注解了@RequestMapping的控制器的方法上。
 */
@ControllerAdvice
public class ExceptionHandleController {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandleController.class);

    @ExceptionHandler(ZbException.class)
    public String handleZb(Exception e, HttpServletRequest request) {
        request.setAttribute("javax.servlet.error.status_code",ResponseStatus.ERROR.getCode());
        Map<String,Object> map = new HashMap<>(2);
        map.put("status", ResponseStatus.ERROR.getCode());
        map.put("msg", StringUtils.isNotBlank(e.getMessage())? e.getMessage() : ResponseStatus.ERROR.getMessage());
        logger.error(e.getMessage());
        request.setAttribute("ext",map);
        return "forward:/error";
    }
    
    @ExceptionHandler(AuthorizationException.class)
    public String handleAuth(HttpServletRequest request) {
        request.setAttribute("javax.servlet.error.status_code",ResponseStatus.FORBIDDEN.getCode());
        return "forward:/error";
    }


}
