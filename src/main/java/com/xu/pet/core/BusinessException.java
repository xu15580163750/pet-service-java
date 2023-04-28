package com.xu.pet.core;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 自定义业务异常类
 *
 * @author xuqingf
 * @date 2023/2/20
 */
@Data
@Accessors(chain = true)
@Builder(toBuilder = true)
public class BusinessException extends RuntimeException {
    private Integer errorCode;
    private String errorMessage;

    public BusinessException(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
