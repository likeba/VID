package com.nomad.data.agent.config.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RestError {
	private String code;
	private String message;
	private HttpStatus status;
}
