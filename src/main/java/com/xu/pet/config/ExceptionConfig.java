package com.xu.pet.config;

import com.xu.pet.core.BusinessException;
import com.xu.pet.enums.EErrorCode;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ControllerAdvice
public class ExceptionConfig {

    //主动抛出异常处理
    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public Map<String, Object> handleException(Exception e) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", EErrorCode.BIZ_DEFAULT.getCode());
        map.put("message", e.getMessage());
        return map;
    }

    //参数校验异常处理
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Map<String, Object> paramExceptionHandler(MethodArgumentNotValidException e) {
        BindingResult exceptions = e.getBindingResult();
        Map<String, Object> map = new HashMap<>();
        // 判断异常中是否有错误信息，如果存在就使用异常中的消息，否则使用默认消息
        if (exceptions.hasErrors()) {
            List<ObjectError> errors = exceptions.getAllErrors();
            if (!errors.isEmpty()) {
                // 这里列出了全部错误参数，按正常逻辑，只需要第一条错误即可
                FieldError fieldError = (FieldError) errors.get(0);
                map.put("code", EErrorCode.PARA_DEFAULT.getCode());
                map.put("message", fieldError.getDefaultMessage());
                return map;
            }
        }
        return map;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map<String, Object> handleAuthorizationException(Exception e) {
        Map<String, Object> map = new HashMap<>();
        if (e instanceof UnauthorizedException) {
            map.put("code", EErrorCode.PERMISSION_ERROR.getCode());
            map.put("message", EErrorCode.PERMISSION_ERROR.getValue());
        }
        return map;
    }


}
