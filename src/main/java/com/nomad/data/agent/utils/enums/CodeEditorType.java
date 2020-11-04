package com.nomad.data.agent.utils.enums;

import com.nomad.data.agent.utils.EnumUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum CodeEditorType implements EnumValue<Object> {

    JUPYTERLAB("JL", "jupyterlab"),
    ZEPPLIN("ZP", "zepplin"),
    RSTUDIO("RS", "rstudio"),
    ETC("ETC", "etc"),
    CST("CST", "custom")
    ;

    private String value;
    private String name;

    private static final Map<Object, CodeEditorType> map = EnumUtils.getMap(CodeEditorType.class);

    public static final CodeEditorType getType(Object value) {
        return map.get(value);
    }
}
