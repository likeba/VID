package com.nomad.data.agent.config.exception;

import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nomad.data.agent.utils.enums.ErrorCodeType;

import lombok.Getter;

@Getter
public class CustomException extends NestedRuntimeException {

	private static final long serialVersionUID = -8362599436867362096L;
	private String message;
	private HttpStatus httpStatus;
	private String code;
	
	public CustomException(String message, HttpStatus httpStatus, String code) {
		super(message);
		this.message = message;
		this.httpStatus = httpStatus;
		this.code = code;
	}
	
	public CustomException(String customMessage, ErrorCodeType errorCodeType) {
		this(customMessage, errorCodeType.getHttpStatus(), errorCodeType.getCode());
	}
	
	public CustomException(ErrorCodeType errorCodeType) {
		this(errorCodeType.getMessage(), errorCodeType);
	}
	
	public RestError toRestError() {
		return new RestError(this.code, this.message, this.httpStatus);
	}
	
	public ResponseEntity<RestError> toResponseEntity() {
		return new ResponseEntity<>(this.toRestError(), this.httpStatus);
	}
	
}
