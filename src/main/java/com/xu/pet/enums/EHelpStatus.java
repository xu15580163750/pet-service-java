package com.xu.pet.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/*
 *
 *救助状态:等待救援中 1,正在救援中 2,救援成功 3 ,救援失败4 ,未知 0
 */
public enum EHelpStatus {

    UNKONW(0, "未知"),
    HELPWAIT(1, "等待救援中"),
    HELPING(2, "正在救援中"),
    HELPSUCCESS(3, "救援成功"),
    HELPFAIL(4, "救援失败");

    private Integer code;
    private String value;
    private static final Map<Integer, EResponse> mapping = (Map) Arrays.stream(values()).collect(Collectors.toMap((x) -> {
        return x.getCode();
    }, (x) -> {
        return x;
    }));

    private EHelpStatus(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
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
