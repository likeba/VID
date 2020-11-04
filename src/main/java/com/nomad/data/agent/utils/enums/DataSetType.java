package com.nomad.data.agent.utils.enums;

import java.util.Map;

import com.nomad.data.agent.utils.EnumUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum DataSetType implements EnumValue<Object> {

    FILE_UPLOAD("FL", "파일업로드"),
    DATABASE("DB", "데이터베이스"),
    APM("APM", "APM");

    private String value;
    private String name;

    private static final Map<Object, DataSetType> map = EnumUtils.getMap(DataSetType.class);

    public static final DataSetType getType(Object value) {
        return map.get(value);
    }
}
