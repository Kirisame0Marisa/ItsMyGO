package com.mygo.bangmall.product.exception;
import com.mygo.common.exception.BaziCodeEnume;
import com.mygo.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "com.mygo.bangmall.product.controller")
public class BangmallExceptionControllerAdvice {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        log.error("数据校验出现问题{},异常类型: {}",e.getMessage(),e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errorMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach((fieldError -> {
            errorMap.put(fieldError.getField(),fieldError.getDefaultMessage());
        }));
        return R.error(BaziCodeEnume.VALID_EXCEPTION.getCode(),BaziCodeEnume.VALID_EXCEPTION.getMsg()).put("data",errorMap);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable) {
        log.error("错误:", throwable);
        return R.error(BaziCodeEnume.UNKNOWN_EXCEPTION.getCode(),BaziCodeEnume.UNKNOWN_EXCEPTION.getMsg());
    }

}
