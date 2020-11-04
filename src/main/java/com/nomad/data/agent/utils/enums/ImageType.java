package com.nomad.data.agent.utils.enums;

import com.nomad.data.agent.utils.EnumUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ImageType implements EnumValue<Object> {
    ENVVIRONMENT("ENV", "워크스페이스생성"),
    CUSTOM("CST", "커스텀");

    private String value;
    private String name;

    private static final Map<Object, ImageType> map = EnumUtils.getMap(ImageType.class);

    public static final ImageType getType(Object value) {
        return map.get(value);
    }
}
