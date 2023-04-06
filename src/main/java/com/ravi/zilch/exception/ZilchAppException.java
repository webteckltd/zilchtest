package com.ravi.zilch.exception;

import org.springframework.http.HttpStatus;


public class ZilchAppException extends RuntimeException{
	
	private ErrorResponse msg;
	private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

	public ZilchAppException(ErrorResponse message , HttpStatus status ) {
		super();
		this.msg = message;
		this.status = status;
	}

	public ErrorResponse getMsg() {
		return msg;
	}

	public void setMsg(ErrorResponse msg) {
		this.msg = msg;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	
}