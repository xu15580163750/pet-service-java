package com.xu.pet.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum EErrorCode {
    SUCCESS(0, "成功"),
    FAILURE(1, "失败"),
    SERVER_ERROR(100000, "服务异常"),
    SERVER_CALL_ERROR(100001, "服务调用异常"),
    SERVER_TRY_ERROR(100002, "服务重试异常"),
    DATABASE_ERROR(20000, "数据库异常"),
    TOKEN_ERROR(300000, "认证信息错误"),
    TOKEN_EXPIRE(300001, "认证信息过期"),
    TOKEN_EMPTY(300002, "认证信息缺失"),
    PERMISSION_ERROR(300003, "权限不足"),
    VERIFY_PASSWORD_ERROR(300004, "密码验证错误"),
    VERIFY_OVERDUE_ERROR(300006, "密码验证过期"),
    VERIFY_SCOPE_ERROR(300009, "操作不允许"),
    PARA_DEFAULT(400000, "通用入参异常"),
    E400001(400001, "数字型入参"),
    BIZ_DEFAULT(500000, "通用业务处理异常"),
    E500001(500001, "数据字典%s不存在"),
    E500002(500002, "您无权使用此业务功能"),
    E500003(500003, "ID的记录不存在"),
    E500004(500004, "系统参数%s类型不匹配"),
    OTHER_ERROR(900000, "其他错误");

    private Integer code;
    private String value;
    private static final Map<Integer, EErrorCode> mapping = (Map)Arrays.stream(values()).collect(Collectors.toMap((x) -> {
        return x.getCode();
    }, (x) -> {
        return x;
    }));

    private EErrorCode(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return this.code;
    }

    public static EErrorCode get(Integer code) {
        return (EErrorCode)mapping.get(code);
    }

    public static String getValue(Integer code) {
        EErrorCode e = (EErrorCode)mapping.get(code);
        return null != e ? e.getValue() : "";
    }

    public String getValue() {
        return this.value;
    }
}
