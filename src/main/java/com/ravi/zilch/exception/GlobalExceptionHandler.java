package com.ravi.zilch.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	
	@ExceptionHandler(ZilchAppException.class)
	public ResponseEntity<ErrorResponse> applicationException(ZilchAppException ex) {
		logger.debug("Inside GlobalExceptionHandler()->applicationException()");
		return new ResponseEntity<ErrorResponse>(ex.getMsg(), ex.getStatus());
	}
	
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		logger.debug("Inside GlobalExceptionHandler()->handleNoHandlerFoundException()");

		ErrorResponse res = new ErrorResponse();
		res.setErrorMessage(ex.getFieldError().getDefaultMessage());
				
		return new ResponseEntity(res, HttpStatus.BAD_REQUEST);
	}


}
