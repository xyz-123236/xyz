package cn.xyz.config;


import cn.xyz.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    //private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(value = CustomException.class)
    @ResponseBody
    public Result customException(HttpServletRequest req, CustomException e){
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest req, Exception e){
        log.error("程序异常：" + e.getMessage());
        e.printStackTrace();
        return Result.error("程序异常，按F12 -> Console查看详情", e);
    }
}
