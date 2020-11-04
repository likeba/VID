package com.nomad.data.agent.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class JsonUtils {

    public static <T> T toObj(String src, Class<T> valueType) {
        if (StringUtils.isEmpty(src)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            return mapper.readValue(src, valueType);
        } catch (IOException e) {
            log.error(">>>>> json to Obj error ", e);
            return null;
        }
    }

    public static String toString(Object src) {
        if (ObjectUtils.isEmpty(src)) {
            return null;
        }
        if (src instanceof String) {
            return String.valueOf(src);
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(src);
        } catch (JsonProcessingException e) {
            log.error(">>>>> json to string error ", e);
            return null;
        }
    }

    public static <T> T toObjFromSrc(Object src, Class<T> valueType) {
        return toObj(toString(src), valueType);
    }


    @SuppressWarnings("rawtypes")
    public static Map toMap(Object src) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(toString(src), Map.class);
        } catch (IOException e) {
            log.error(">>>>> json to map error ", e);
        }
        return null;
    }

    public static MultiValueMap toMultiValueMap(Object src) {
        Map map = toMap(src);
        if (ObjectUtils.isEmpty(map)) {
            return null;
        }
        MultiValueMap multiValueMap = new LinkedMultiValueMap();
        Set<Map.Entry> set = map.entrySet();
        for (Map.Entry entry : set) {
            if (!ObjectUtils.isEmpty(entry.getValue())) {
                Object value = entry.getValue();
                if (value instanceof ArrayList) {
                    List<Object> valueList = (ArrayList) value;
                    for (Object obj : valueList) {
                        multiValueMap.add(entry.getKey(), obj.toString());
                    }
                } else {
                    multiValueMap.add(entry.getKey(), entry.getValue().toString());
                }
            }
        }
        return multiValueMap;
    }
}
