package com.nomad.data.agent.config.exception;

import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;

import com.nomad.data.agent.utils.enums.ErrorCodeType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@RestController
public class CustomExceptionHandler {

    @ExceptionHandler({MultipartException.class})
    public ResponseEntity handleMultipartException(MultipartException e) {
        CustomException customException = null;
        try {
            FileUploadBase.SizeLimitExceededException ex = (FileUploadBase.SizeLimitExceededException) e.getCause().getCause();
            Long permittedSize = ex.getPermittedSize();
            Long actualSize = ex.getActualSize();

            String message = String.format(ErrorCodeType.FILE_LIMIT_SIZE.getMessage(), actualSize, permittedSize);
            customException = new CustomException(message, ErrorCodeType.FILE_LIMIT_SIZE);
        } catch (Exception e1) {
            log.error(">>>>> handle multipart error", e);
            customException = new CustomException(ErrorCodeType.FILE_PROCESS_HANDLE);
        }
        log.error("> {}:{}", e.getClass().getSimpleName(), customException.toResponseEntity());
        return customException.toResponseEntity();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public void handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = String.format("%s [%s]", bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getField());
        CustomException customException = new CustomException(message, ErrorCodeType.COMMON_BAD_REQUEST);
        throw customException;
    }

    @ExceptionHandler({BindException.class})
    public void handleBindingException(BindException e) {
        String message = String.format("%s [%s]", e.getFieldError().getDefaultMessage(), e.getFieldError().getField());
        CustomException customException = new CustomException(message, ErrorCodeType.COMMON_BAD_REQUEST);
        throw customException;
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity handleException(Exception e) {
        CustomException customException = new CustomException(e.getMessage(), ErrorCodeType.COMMON_INTERNAL_SERVER_ERROR);
        throw customException;
    }

    @ExceptionHandler({CustomException.class})
    public ResponseEntity handleCustomException(CustomException e) {
        log.error("> {}:{}", e.getClass().getSimpleName(), e.toRestError());
        return e.toResponseEntity();
    }


}
