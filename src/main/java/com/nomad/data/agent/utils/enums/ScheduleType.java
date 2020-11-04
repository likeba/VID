package com.nomad.data.agent.utils.enums;

import java.util.Map;

import com.nomad.data.agent.utils.EnumUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ScheduleType implements EnumValue<Object> {
	
	DATASET("DATASET", "데이터셋 추출");

    private String value;
    private String name;

    private static final Map<Object, ScheduleType> map = EnumUtils.getMap(ScheduleType.class);

    public static final ScheduleType getType(Object value) {
        return map.get(value);
    }
}
