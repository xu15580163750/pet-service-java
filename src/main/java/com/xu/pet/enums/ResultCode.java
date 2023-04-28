package com.xu.pet.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xuqingf
 * @date 2023/3
 */
public enum ResultCode {

    /*
     *
     * 请求成功
     * */
    SUCCESS(200, "请求成功"),

    /*
     *
     * 失败
     * */
    FAIL(400, "失败"),

    /*
     *
     * 权限不足
     * */
    UNAUTHORIZED(401, "权限不足"),

    /*
     *
     * 未找到
     * */
    NOT_FOUND(404, "未找到"),

    /*
     *
     * 服务器错误
     * */
    INTERNAL_SERVER_ERROR(500, "服务器错误");

    private int code;
    private String value;
    private static final Map<Integer, ResultCode> mapping = (Map) Arrays.stream(values()).collect(Collectors.toMap((x) -> {
        return x.getCode();
    }, (x) -> {
        return x;
    }));

    private ResultCode(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return this.code;
    }

    public static ResultCode get(Integer code) {
        return (ResultCode) mapping.get(code);
    }

    public static String getValue(Integer code) {
        ResultCode e = (ResultCode) mapping.get(code);
        return null != e ? e.getValue() : "";
    }

    public String getValue() {
        return this.value;
    }
}
