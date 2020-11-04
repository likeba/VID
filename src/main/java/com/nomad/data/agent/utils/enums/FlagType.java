package com.nomad.data.agent.utils.enums;

import java.util.Map;

import com.nomad.data.agent.utils.EnumUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum FlagType implements EnumValue<Object> {
    TRUE("Y", "예", Boolean.TRUE),
    FALSE("N", "아니오", Boolean.FALSE),
    PENDING("P", "대기", Boolean.FALSE);

    private String value;
    private String name;
    private Boolean flag;

    private static final Map<Object, FlagType> map = EnumUtils.getMap(FlagType.class);

    public static final FlagType getType(Object value) {
        return map.get(value);
    }
}
