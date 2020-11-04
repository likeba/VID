package com.nomad.data.agent.utils.enums;

import com.nomad.data.agent.utils.EnumUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ReturnCodeType implements EnumValue<Object>  {
    SUCCESS(0),
    FAIL(1);
    private Integer code;

    public Object getValue() {return code; }

    private static final Map<Object, ReturnCodeType> map = EnumUtils.getMap(ReturnCodeType.class);
    public static final ReturnCodeType getType(Object value) {
        return map.get(value);
    }

}
