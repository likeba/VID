package com.nomad.data.agent.dataset.dto.info;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AipDataObjectInfo {

	private List<Map<String, Object>> data;
	private int size;
	
}
