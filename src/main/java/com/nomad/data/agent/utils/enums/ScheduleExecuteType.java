package com.nomad.data.agent.utils.enums;

import java.util.Map;

import com.nomad.data.agent.utils.EnumUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ScheduleExecuteType implements EnumValue<Object> {
    
	NEXT_DAY("0", "다음날"),
	EVERY_DAY("1", "매 일"),
	EVERY_WEEK("2", "매 주"),
	EVERY_MONTH("3", "매 월");

    private String value;
    private String name;

    private static final Map<Object, ScheduleExecuteType> map = EnumUtils.getMap(ScheduleExecuteType.class);

    public static final ScheduleExecuteType getType(Object value) {
        return map.get(value);
    }
}
