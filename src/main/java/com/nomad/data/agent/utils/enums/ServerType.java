package com.nomad.data.agent.utils.enums;

import com.nomad.data.agent.utils.EnumUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ServerType implements EnumValue<Object> {
    WORKER("WK", "학습"),
    MANAGER("MG", "매니저"),
    DATA("DT", "데이터"),
    MODEL("MD", "운영");

    private String value;
    private String name;

    private static final Map<Object, ServerType> map = EnumUtils.getMap(ServerType.class);

    public static final ServerType getType(Object value) {
        return map.get(value);
    }
}
