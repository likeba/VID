package com.nomad.data.agent.utils.enums;

import java.util.Map;

import com.nomad.data.agent.utils.EnumUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum DataSourceType implements EnumValue<Object> {

	DATABASE("DB", "데이터베이스"),
    SFPT("SFTP", "SFTP");

    private String value;
    private String name;

    private static final Map<Object, DataSourceType> map = EnumUtils.getMap(DataSourceType.class);

    public static final DataSourceType getType(Object value) {
        return map.get(value);
    }
	
}
