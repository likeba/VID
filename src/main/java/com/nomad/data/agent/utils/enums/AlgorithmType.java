package com.nomad.data.agent.utils.enums;

import java.util.Map;

import com.nomad.data.agent.utils.EnumUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum AlgorithmType implements EnumValue<Object> {

    BASE("BASE", "기본제공"),
    USER("USER", "사용자생성");

    private String value;
    private String name;

    private static final Map<Object, AlgorithmType> map = EnumUtils.getMap(AlgorithmType.class);

    public static final AlgorithmType getType(Object value) {
        return map.get(value);
    }
}
