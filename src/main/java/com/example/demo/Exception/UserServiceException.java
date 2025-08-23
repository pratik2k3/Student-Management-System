package com.example.demo.Exception;

import org.springframework.http.HttpStatus;

public class UserServiceException extends RuntimeException {

	private String message;
	private HttpStatus httpsStatus;
	
	 public UserServiceException(String message , HttpStatus httpStatus  ) {
	        this.httpsStatus =    httpStatus;
	        this.message = message;
	    }

	public String getMessage() {
		return message;
	}

	public HttpStatus getHttpsStatus() {
		return httpsStatus;
	}

	
	 
	 
	 
	 
	 
	 
}
