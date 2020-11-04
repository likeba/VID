package com.nomad.data.agent.utils.enums;

import java.util.Map;

import org.springframework.http.HttpStatus;

import com.nomad.data.agent.utils.EnumUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ErrorCodeType implements EnumValue<Object> {

    //Common 00
    COMMON_BAD_REQUEST("0000", HttpStatus.BAD_REQUEST, "잘못된 요청"),
    COMMON_NOT_FOUND_ID("0001", HttpStatus.BAD_REQUEST, "존재 하지 않는 ID입니다."),
    COMMON_ALREADY_EXIST("0002", HttpStatus.BAD_REQUEST, "이미 존재합니다."),
    COMMON_DUPLICATED_NAME("0003", HttpStatus.BAD_REQUEST, "중복된 이름입니다."),
    COMMON_ALREADY_DELETED("0004", HttpStatus.BAD_REQUEST, "이미 삭제되었습니다."),
    COMMON_INTERNAL_SERVER_ERROR("0005", HttpStatus.INTERNAL_SERVER_ERROR, "내부 시스템 에러가 발생하였습니다."),
    COMMON_COMMAND_RUN_FAIL("0006", HttpStatus.INTERNAL_SERVER_ERROR, "내부 명령어 실행하는 과정에서 문제가 발생했습니다."),
    COMMON_SCHEDULE_FAIL("0007", HttpStatus.INTERNAL_SERVER_ERROR, "스케줄 동작이 실패했습니다."),
    
    //DB 01
    DB_PROCESS_HANDLE("0100", HttpStatus.INTERNAL_SERVER_ERROR, "DB 데이터 처리에 문제가 발생하였습니다."),
    DB_INSERT_HANDLE("0101", HttpStatus.INTERNAL_SERVER_ERROR, "DB 데이터 생성에 문제가 발생하였습니다."),
    DB_UPDATE_HANDLE("0102", HttpStatus.INTERNAL_SERVER_ERROR, "DB 데이터 수정에 문제가 발생하였습니다."),
    DB_DELETE_HANDLE("0103", HttpStatus.INTERNAL_SERVER_ERROR, "DB 데이터 삭제에 문제가 발생하였습니다."),
    DB_FOREIGN_KEY_HANDLE("0104", HttpStatus.INTERNAL_SERVER_ERROR, "FOREIGN KEY 제약 조건에 위배 됩니다."),
    DB_INVALID_CONNECTION_INFO("0105", HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 정보가 올바르지 않습니다."),

    //File 02
    FILE_EMPTY("0200", HttpStatus.BAD_REQUEST, "파일이 비어 있습니다."),
    FILE_NOT_FOUND("0201", HttpStatus.NOT_FOUND, "파일이 존재하지 않습니다."),
    FILE_UNSUPPORTED_TYPE("0202", HttpStatus.BAD_REQUEST, "지원하는 파일 형식이 아닙니다."),
    FILE_LIMIT_SIZE("0203", HttpStatus.BAD_REQUEST, "파일 사이즈(%d)가 허용 사이즈(%d)를 초과하였습니다."),
    FILE_INVALID_TYPE("0204", HttpStatus.BAD_REQUEST, "잘못된 파일 형식입니다."),
    FILE_PROCESS_HANDLE("0205", HttpStatus.INTERNAL_SERVER_ERROR, "파일 처리에 문제가 발생하였습니다."),
    FILE_UPLOAD_HANDLE("0206", HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 과정에서 문제가 발생하였습니다."),
    FILE_DOWNLOAD_HANDLE("0207", HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장 과정에서 문제가 발생하였습니다."),
    FILE_DELETE_ERROR("0208", HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제 과정에서 문제가 발생하였습니다."),

    //User 03
    USER_SESSION_EXPIRED("0300", HttpStatus.UNAUTHORIZED, "세션이 만료되었습니다."),
    USER_INVALID_TOKEN("0301", HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
    USER_INVALID_PASSWORD("0302", HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다."),
    USER_ACCOUNT_DISABLED("0303", HttpStatus.UNAUTHORIZED, "계정이 비활성화 되었습니다."),
    USER_NO_PERMISSION("0304", HttpStatus.FORBIDDEN, "권한이 없습니다."),
    USER_WAITING_ACCOUNT("0305", HttpStatus.UNAUTHORIZED, "사용 대기 중인 계정입니다."),

    //Workspace 04
    WORKSPACE_INVALID("0400", HttpStatus.BAD_REQUEST, "잘못된 워크스페이스입니다."),
    WORKSPACE_LIMIT_STORAGE("0401", HttpStatus.BAD_REQUEST, "디스크 공간이 부족합니다."),
    WORKSPACE_LIMIT_RESOURCE("0402", HttpStatus.BAD_REQUEST, "워크스페이스 생성이 가능한 서버 리소스가 부족합니다."),
    WORKSPACE_EXIST("0403", HttpStatus.FORBIDDEN, "사용중인 워크스페이스가 있어서 삭제할 수 없습니다."),

    //DataSet 05
    VIEW_DATASET_QUERY_ERROR("0500", HttpStatus.BAD_REQUEST, "잘못된 쿼리입니다."),
    DATASET_APM_MOVE_ERROR("0501", HttpStatus.INTERNAL_SERVER_ERROR, "APM 파일 이동 과정에서 문제가 발생하였습니다."),

    //Algorithm 06
    ALGORITHM_UPLOAD_ERROR("0600", HttpStatus.INTERNAL_SERVER_ERROR, "알고리즘을 등록하는 과정에서 문제가 발생하였습니다."),

    //Experiment 07
    EXPERIMENT_ALREADY_TRAINING("0700", HttpStatus.BAD_REQUEST, "이미 학습이 실행 중입니다."),
    EXPERIMENT_INVALID_TRAIN("0701", HttpStatus.BAD_REQUEST, "잘못된 학습입니다."),
    EXPERIMENT_RUN_FAILED("0702", HttpStatus.BAD_REQUEST, "실행 처리가 실패 했습니다."),

    //Deployment 08
    DEPLOYMENT_ALREADY_DOING("0800", HttpStatus.BAD_REQUEST, "이미 배포중입니다."),
    DEPLOYMENT_NOT_RUNNING("0801", HttpStatus.BAD_REQUEST, "배포 실행 중이 아닙니다."),

    //Model 09


    //Docker 10
    DOCKER_API_EXCEPTION("1000", HttpStatus.INTERNAL_SERVER_ERROR, "도커 동작이 실패하였습니다."),
    DOCKER_IMAGE_NOT_FOUND("1001", HttpStatus.NOT_FOUND, "해당하는 도커이미지를 찾을 수 없습니다."),
    DOCKER_IMAGE_REGISTER_ERROR("1002", HttpStatus.INTERNAL_SERVER_ERROR, "도커 이미지 등록하는 과정에서 오류가 발생했습니다."),
    DOCKER_IMAGE_DELETE_ERROR("1003", HttpStatus.BAD_REQUEST, "도커이미지 삭제하는 과정에서 오류가 발생했습니다."),
    DOCKER_IMAGE_INVALID_ERROR("1004", HttpStatus.BAD_REQUEST, "잘못된 도커이미지 입니다."),
    DOCKER_IMAGE_DUPLICATE_ERROR("1005", HttpStatus.BAD_REQUEST, "중복된 도커이미지 입니다."),
    
    //외부 연동(python) 20
    API_CALL_ERROR_WRONG("2000", HttpStatus.BAD_GATEWAY, "API 호출에서 오류가 발생하였습니다."),
    API_MAKE_URL_ERROR("2001", HttpStatus.BAD_GATEWAY, "API 호출 주소 생성 과정에서 문제가 발생했습니다."),

    //DW 연동 30
    DW_SELECT_HANDLE("3000", HttpStatus.INTERNAL_SERVER_ERROR, "DW 연동에 문제가 발생하였습니다."),
    DW_QUERY_LIMIT_SIZE("3001", HttpStatus.BAD_REQUEST, "쿼리 사이즈가 허용사이즈(4000KB)를 초과하였습니다."), 
    
    //파일 업로드
    UPLOAD_FILE_ERROR("4000", HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 오류가 발생했습니다."), 
    FILE_WRONG_ACCESS("4001", HttpStatus.BAD_REQUEST, "잘못된 경로의 접근입니다."),
    
    //데이터 소스
    CONNECTION_TEST_FAIL("5001", HttpStatus.INTERNAL_SERVER_ERROR, "데이터 소스 연결을 실패했습니다."),
    DATA_EXTRACT_FAIL("5002", HttpStatus.INTERNAL_SERVER_ERROR, "데이터 추출을 실패했습니다."),
    
    //Git
    GIT_CLONE_ERROR("6000", HttpStatus.INTERNAL_SERVER_ERROR, "클론하는 과정에서 문제가 발생하였습니다."),
    GIT_COMMIT_ERROR("6001", HttpStatus.INTERNAL_SERVER_ERROR, "커밋하는 과정에서 문제가 발생하였습니다."),
    GIT_ADD_ERROR("6002", HttpStatus.INTERNAL_SERVER_ERROR, "변경사항 추가하는 과정에서 문제가 발생하였습니다."),
    GIT_PUSH_ERROR("6003", HttpStatus.INTERNAL_SERVER_ERROR, "푸쉬하는 과정에서 문제가 발생하였습니다."),
    GIT_INIT_ERROR("6004", HttpStatus.INTERNAL_SERVER_ERROR, "깃 초기화 하는 과정에서 문제가 발생하였습니다."),
    
    //Gitlab
    GITLAB_CREATE_REPOSITORY_ERROR("7001", HttpStatus.INTERNAL_SERVER_ERROR, "깃랩저장소 생성에 문제가 발생하였습니다."),
    GITLAB_GET_PROJECT_FAILED("7002", HttpStatus.INTERNAL_SERVER_ERROR, "깃랩저장소 가져오는 과정에서 오류가 발생하였습니다."),
    GITLAB_DELETE_PROJECT_FAILED("7003", HttpStatus.INTERNAL_SERVER_ERROR, "깃랩저장소 삭제 과정에서 오류가 발생하였습니다."),
    
    //SystemLog
    SYSTEM_LOG_GET_GET_FILES_ERROR("0801", HttpStatus.INTERNAL_SERVER_ERROR, "로그파일목록을 조회하는 과정에서 오류가 발생했습니다."),
    SYSTEM_LOG_VIEW_ERROR("0802", HttpStatus.INTERNAL_SERVER_ERROR, "로그내용을 조회하는 과정에서 오류가 발생했습니다."),

    ;

    private String code;
    private HttpStatus httpStatus;
    private String message;

    public String getValue() {
        return code;
    }

    private static final Map<Object, ErrorCodeType> map = EnumUtils.getMap(ErrorCodeType.class);
    public static final ErrorCodeType getType(Object value) {
        return map.get(value);
    }
}
