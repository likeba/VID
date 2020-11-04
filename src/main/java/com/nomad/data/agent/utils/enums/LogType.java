package com.nomad.data.agent.utils.enums;

import java.util.Map;

import com.nomad.data.agent.utils.EnumUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum LogType implements EnumValue<Object> {
    ALGORITHM("AGR", "알고리즘"),
    ALGORITHMLOC("AGR-LOC", "로컬 알고리즘"),
    BOARD("BOARD", "게시판"),
    COMMON("COMM", "공통코드"),
    DATASET("DATA", "데이터셋"),
    SQL("SQL", "쿼리"),
    DATASETLOC("DATA-LOC", "로컬 데이터셋"),
    DOCKER("DOCK", "도커이미지"),
    DOCKERLIB("DOCK-LIB", "도커이미지 라이브러리"),
    ETC("ETC", "기타"),
    FUNCTION("FUNCTION", "기능"),
    EXPERIMENT("EXPR", "실험"),
    EXPERIMENTLRN("LEARN", "실험 학습"),
    LIBRARYLARG("LIB-LRG", "라이브러리 대분류"),
    LIBRARYSML("LIB-SML", "라이브러리 소분류"),
    PACKAGE("PKG", "패키지"),
    RELEASE("RLS", "배포"),
    SERVER("SERV", "서버"),
    USER("USER", "사용자"),
    WORKSPACE("WKSPC", "워크스페이스"),
    WORKSPACEGPU("WKSPC-GPU", "워크스페이스 GPU할당"),
    WORKSPACESHA("WKSPC-SHARE", "워크스페이스 공유"),
    GROUP("GROUP", "그룹"),
    MONITOR("MONITOR", "모니터링"),
    SCHEDULER("SCHEDULER", "스케쥴러"),
    GITLAB("GITLAB", "깃랩"), 
    DATASOURCE("DATA-SOURCE", "데이터 소스"), 
    TEST("TEST", "테스트"), 
    SCHEDULE_DATASET("SCHEDULE-DATASET", "스케줄-데이터");

    private String value;
    private String name;

    private static final Map<Object, LogType> map = EnumUtils.getMap(LogType.class);

    public static final LogType getType(Object value) {
        return map.get(value);
    }
}
