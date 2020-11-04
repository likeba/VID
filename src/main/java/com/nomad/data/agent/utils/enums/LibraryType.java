package com.nomad.data.agent.utils.enums;

import java.util.Map;

import com.nomad.data.agent.utils.EnumUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum LibraryType {

	R("R", "r", "^([^_]*)[_](.*)[.]tar[.]gz$"),	// abbyyR_0.5.1.tar.gz
	PYTHON("PY", "python", "^([^-]*)[-]([^-]+)[-](.*)$");	// numpy-1.13.3-cp35-cp35m-manylinux1_x86_64.whl*
	
	private String value;
	private String name;
	private String patternString;
	
	private static final Map<Object, LibraryType> map = EnumUtils.getMap(LibraryType.class);
	public static final LibraryType getType(Object value) {
		return map.get(value);
	}
	
}
