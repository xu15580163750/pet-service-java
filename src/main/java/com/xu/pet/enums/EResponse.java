package com.xu.pet.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * 返回结果枚举
 * */
public enum EResponse {

    SUCCESS("200", "success"),
    ERROR("-1", "error");


    private String code;
    private String value;
    private static final Map<Integer, EResponse> mapping = (Map) Arrays.stream(values()).collect(Collectors.toMap((x) -> {
        return x.getCode();
    }, (x) -> {
        return x;
    }));

    private EResponse(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return this.code;
    }

    public static EResponse get(Integer code) {
        return (EResponse) mapping.get(code);
    }

    public static String getValue(Integer code) {
        EResponse e = (EResponse) mapping.get(code);
        return null != e ? e.getValue() : "";
    }

    public String getValue() {
        return this.value;
    }
}
