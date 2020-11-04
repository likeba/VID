package com.nomad.data.agent.utils.enums;

import java.util.Map;

import com.nomad.data.agent.utils.EnumUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum LogMessageType implements EnumValue<Object> {
    OK_LIST("목록조회"),
    OK_READ("조회"),
    OK_DB_INSERT("등록 성공"),
    OK_DB_UPDATE("수정 성공"),
    OK_DB_DELETE("삭제 성공"),
    OK_RUN("실행 성공"),
    OK_UPDATE_TOKEN("토큰 수정 성공"),
    OK_UPDATE_PASSWORD("비밀번호 수정 성공"),
    OK_INIT_SSO("통합인증정보 갱신 성공"),
    OK_DOWNLOAD("다운로드 성공"),
    OK_MONITOR("모니터링 조회"),
    OK_BACKUP("비활성화 성공"),
    OK_RESTORE("활성화 성공"),
    OK_BACKUP_EXPIRED_WORKSPACE("만료 워크스페이스 백업 성공"),
    OK_UPDATE_DEL_FL("삭제 여부 업데이트 성공"),
    OK_DELETE_EXPIRED_DATASET("만료 데이터셋 삭제 성공"),
    OK_CREATE_GITLAB_REPOSITORY("깃랩저장소 생성 성공"),
    OK_SAVE_CONTENTS_OF_WORKSPACE("워크스페이스 내부 파일 백업 성공"),
    OK_CONNECTION_TEST("연결 테스트 성공"),
    OK_SCHEDULE("스케줄 동작 성공"),
    OK_FILE_UPLOAD("파일 업로드 성공"),
    OK_FILE_MOVE("파일 이동 성공"),

    ERROR_RUN("실행 실패"),
    ERROR_CONNECTION_TEST("연결 테스트 실패"),
    ERROR_COMMON_NOT_FOUND_ID(ErrorCodeType.COMMON_NOT_FOUND_ID.getMessage()),
    ERROR_COMMON_ALREADY_EXIST(ErrorCodeType.COMMON_ALREADY_EXIST.getMessage()),
    ERROR_COMMON_ALREADY_DELETED(ErrorCodeType.COMMON_ALREADY_DELETED.getMessage()),
    ERROR_DB_PROCESS_HANDLE(ErrorCodeType.DB_PROCESS_HANDLE.getMessage()),
    ERROR_DB_INSERT_HANDLE(ErrorCodeType.DB_INSERT_HANDLE.getMessage()),
    ERROR_DB_UPDATE_HANDLE(ErrorCodeType.DB_UPDATE_HANDLE.getMessage()),
    ERROR_DB_DELETE_HANDLE(ErrorCodeType.DB_DELETE_HANDLE.getMessage()),
    ERROR_USER_NO_PERMISSION(ErrorCodeType.USER_NO_PERMISSION.getMessage()),
    ERROR_FOREIGN_KEY(ErrorCodeType.DB_FOREIGN_KEY_HANDLE.getMessage()),
    ERROR_USER_EXIST("사용자 존재"),
    ERROR_COMMON_DUPLICATED_NAME(ErrorCodeType.COMMON_DUPLICATED_NAME.getMessage()),
    ERROR_COMMON_SCHEDULE_FAIL(ErrorCodeType.COMMON_SCHEDULE_FAIL.getMessage()),
    ERROR_FILE_UPLOAD_HANDLE(ErrorCodeType.FILE_UPLOAD_HANDLE.getMessage()),
    ERROR_DATASET_APM_MOVE(ErrorCodeType.DATASET_APM_MOVE_ERROR.getMessage()),
    ;

    private String value;

    private static final Map<Object, LogMessageType> map = EnumUtils.getMap(LogMessageType.class);

    public static final LogMessageType getType(Object value) {
        return map.get(value);
    }
}
